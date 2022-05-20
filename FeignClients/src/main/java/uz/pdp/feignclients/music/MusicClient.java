package uz.pdp.feignclients.music;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient("MusicService")
public interface MusicClient {
    @GetMapping("/api/MusicService/view/{musicId}")
    Map<String, Object> getMusicInfo(@PathVariable Integer musicId);
}
