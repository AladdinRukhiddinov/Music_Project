package uz.pdp.musiccomment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.feignclients.musicComment.MusicCommentDto;
import uz.pdp.musiccomment.common.ApiResponse;
import uz.pdp.musiccomment.entity.MusicComment;
import uz.pdp.musiccomment.service.MusicCommentService;

import java.util.List;

@RestController
@RequestMapping("/api/musicComment")
public class MusicCommentController {

    @Autowired
    MusicCommentService musicCommentService;


    @GetMapping
    public ResponseEntity<?> getAllMusicComment() {
        return musicCommentService.getAllMusicComment();
    }

    @GetMapping("/{musicId}")
    public List<MusicCommentDto> getMusicCommentsByMusicId(@PathVariable Integer musicId) {
        return musicCommentService.getMusicCommentByMusicId(musicId);
    }

    @PostMapping
    public ResponseEntity<?> saveMusicComment(@RequestBody MusicComment musicComment) {
        ApiResponse apiResponse = musicCommentService.saveMusicComment(musicComment);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse.getObject());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return musicCommentService.delete(id);
    }

    @GetMapping("/average-rating/{musicId}")
    public ResponseEntity<?> getAverageRate(@PathVariable Integer musicId) {
        return musicCommentService.getAverageRate(musicId);
    }

    @GetMapping("/comment")
    public ResponseEntity<?> setCommentStatus(@RequestParam(name = "isAccepted") boolean isAccepted,
                                             @RequestParam(name = "commentId") Integer commentId) {
        ApiResponse apiResponse = musicCommentService.setCommentStatus(isAccepted, commentId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

}
