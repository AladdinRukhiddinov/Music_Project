package uz.pdp.musiccomment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.feignclients.music.MusicClient;
import uz.pdp.feignclients.musicComment.MusicCommentDto;
import uz.pdp.musiccomment.common.ApiResponse;
import uz.pdp.musiccomment.dto.CommentEmailDto;
import uz.pdp.musiccomment.entity.MusicComment;
import uz.pdp.musiccomment.entity.enums.Status;
import uz.pdp.musiccomment.repository.MusicCommentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicCommentService {
    private final MusicCommentRepository musicCommentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MusicClient musicClient;

    @Value("${spring.rabbitmq.exchange}")
    String exchange;
    @Value("${spring.rabbitmq.routingkey}")
    String routingKey;

    public ResponseEntity<?> getAllMusicComment() {
        List<MusicComment> all = musicCommentRepository.findAll();
        return ResponseEntity.ok(all);
    }

    public List<MusicCommentDto> getMusicCommentByMusicId(Integer musicId) {
        List<MusicComment> byMusicId = musicCommentRepository.findAllByMusicIdAndStatus(musicId, Status.ACCEPTED);

        List<MusicCommentDto> musicCommentDtoArrayList = new ArrayList<>();
        for (MusicComment musicComment : byMusicId) {
            MusicCommentDto musicCommentDto = new MusicCommentDto(musicComment.getId(), musicComment.getMusicId(), musicComment.getUserId(),
                    musicComment.getCommentBody(), musicComment.getRate(), musicComment.getCreatedAt());
            musicCommentDtoArrayList.add(musicCommentDto);
        }
        return musicCommentDtoArrayList;
    }

    public ApiResponse saveMusicComment(MusicComment musicComment) {
        musicComment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        musicComment.setStatus(Status.NEW);
        musicComment.setUserId(1);
        MusicComment savedMusicComment = musicCommentRepository.save(musicComment);

        Map<String, Object> musicMap = musicClient.getMusicInfo(musicComment.getMusicId());


        CommentEmailDto commentEmailDto = new CommentEmailDto();
        commentEmailDto.setMusicTitle((String) musicMap.get("musicTitle"));
        commentEmailDto.setCommentBody(musicComment.getCommentBody());
        commentEmailDto.setCommentAuthorName("Nodirbek Nurqulov");
        commentEmailDto.setReceiverEmail("abdulaziz2000go@gmail.com");
        commentEmailDto.setAcceptUrl("http://localhost:8082/api/musicComment/comment?isAccepted=true&commentId=" + savedMusicComment.getId());
        commentEmailDto.setRejectUrl("http://localhost:8082/api/musicComment/comment?isAccepted=false&commentId=" + savedMusicComment.getId());
        commentEmailDto.setSubject("New music comment for your Music");
        sendEmailToMusicCreator(commentEmailDto);

        return new ApiResponse("Comment successfully saved", true, musicComment);
    }

    public void sendEmailToMusicCreator(CommentEmailDto commentEmailDto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, commentEmailDto);
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            musicCommentRepository.deleteById(id);
        } catch (Exception ignored) {
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAverageRate(Integer musicId) {
        Double average = musicCommentRepository.getAverage(musicId);
        return ResponseEntity.ok(average);

    }

    public ApiResponse setCommentStatus(boolean isAccepted, Integer commentId) {
        Optional<MusicComment> optionalMusicComment = musicCommentRepository.findById(commentId);
        if (optionalMusicComment.isEmpty()) return new ApiResponse("Not found", false);
        MusicComment musicComment = optionalMusicComment.get();

        if (isAccepted) {
            musicComment.setStatus(Status.ACCEPTED);
        } else {
            musicComment.setStatus(Status.REJECTED);
        }

        musicCommentRepository.save(musicComment);
        return new ApiResponse(isAccepted ? "Comment is Accepted" : "Comment is rejected", true);
    }
}
