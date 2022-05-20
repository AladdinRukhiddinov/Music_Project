package uz.pdp.feignclients.musicComment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("musicComment")
public interface MusicCommentClient {

    @GetMapping("/api/musicComment/{musicId}")
    List<MusicCommentDto> getMusicComments(@PathVariable Integer musicId);

    @GetMapping("/api/musicComment/average-rating/{musicId}")
    Double getAverageRating(@PathVariable Integer musicId);

}
