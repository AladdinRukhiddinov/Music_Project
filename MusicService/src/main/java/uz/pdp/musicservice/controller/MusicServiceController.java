package uz.pdp.musicservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.musicservice.payload.ApiResponse;
import uz.pdp.musicservice.payload.dto.MusicDto;
import uz.pdp.musicservice.service.MusicService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/MusicService")
public class MusicServiceController {


    @Autowired
    MusicService musicService;

    @GetMapping("/view")
    public HttpEntity<?> getAllMusics(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String search) {
        List<Map<String, Object>> musicMap = musicService.getAllMusics(page, size, search);
        return ResponseEntity.status(200).body(musicMap);
    }

    @GetMapping("/view/{musicId}")
    public HttpEntity<?> getMusicById(@PathVariable Integer musicId) {
        ApiResponse apiResponse = musicService.getMusicById(musicId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse.isSuccess() ? apiResponse.getObject() : null);
    }

    @GetMapping("/user-collection/{userId}")
    public HttpEntity<?> getUserMusicCollection(@PathVariable Integer userId,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size) {
        ApiResponse apiResponse = musicService.getUserMusicCollection(userId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse.getObject());
    }

    @PostMapping("/add-music-to-collection/{musicId}")
    public HttpEntity<?> addMusicToCollection(@PathVariable Integer musicId) {
        ApiResponse apiResponse = musicService.addMusicToCollection(musicId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

    @PostMapping()
    public HttpEntity<?> addMusic(@RequestBody MusicDto music) {
        ApiResponse apiResponse = musicService.saveNewMusic(music);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }


}
