package uz.pdp.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.feignclients.musicComment.MusicCommentClient;
import uz.pdp.feignclients.musicComment.MusicCommentDto;
import uz.pdp.musicservice.entity.Author;
import uz.pdp.musicservice.entity.Genre;
import uz.pdp.musicservice.entity.Music;
import uz.pdp.musicservice.entity.MusicCollection;
import uz.pdp.musicservice.payload.ApiResponse;
import uz.pdp.musicservice.payload.dto.MusicDto;
import uz.pdp.musicservice.repository.AuthorRepository;
import uz.pdp.musicservice.repository.GenreRepository;
import uz.pdp.musicservice.repository.MusicCollectionRepo;
import uz.pdp.musicservice.repository.MusicRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final MusicCollectionRepo musicCollectionRepository;
    private final MusicCommentClient musicCommentClient;


    public List<Map<String, Object>> getAllMusics(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        List<Music> allMusics = musicRepository.getAllMusics(pageable, search);

        List<Map<String, Object>> musicList = new ArrayList<>();
        for (Music music : allMusics) {
            Map<String, Object> musicMap = new HashMap<>();
            musicMap.put("musicId", music.getId());
            musicMap.put("musicTitle", music.getTitle());
            musicMap.put("genre", music.getGenres());
            musicMap.put("attachmentId", music.getAttachmentId());

            Double averageRating = musicCommentClient.getAverageRating(music.getId());
            musicMap.put("averageRating", averageRating);
            musicList.add(musicMap);
        }

        return musicList;
    }

    public ApiResponse getMusicById(Integer musicId) {
        Optional<Music> optionalMusic = musicRepository.findById(musicId);
        if (optionalMusic.isEmpty()) return new ApiResponse("Music not found", false);
        Music music = optionalMusic.get();

        Map<String, Object> musicMap = new HashMap<>();
        musicMap.put("musicId", music.getId());
        musicMap.put("musicTitle", music.getTitle());
        musicMap.put("attachmentId", music.getAttachmentId());
        musicMap.put("createdBy", music.getUserId());
        musicMap.put("genre", music.getGenres());
        musicMap.put("authors", music.getAuthors());


        List<MusicCommentDto> musicReviews = musicCommentClient.getMusicComments(musicId);
        musicMap.put("reviews", musicReviews);

        return new ApiResponse("Ok", true, musicMap);
    }


    public ApiResponse getUserMusicCollection(Integer userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        List<MusicCollection> musicCollections = musicCollectionRepository.findAllByUserId(pageable, userId);

        List<Map<String, Object>> musicList = new ArrayList<>();
        for (MusicCollection music : musicCollections) {
            Map<String, Object> musicMap = new HashMap<>();
            musicMap.put("musicId", music.getMusic().getId());
            musicMap.put("musicTitle", music.getMusic().getTitle());
            musicMap.put("attachmentId", music.getMusic().getAttachmentId());
            musicList.add(musicMap);
        }

        return new ApiResponse("Ok", true, musicList);
    }

    public ApiResponse addMusicToCollection(Integer musicId) {
        Optional<Music> optionalMusic = musicRepository.findById(musicId);
        if (optionalMusic.isEmpty()) return new ApiResponse("Music not found", false);
        Music music = optionalMusic.get();

        boolean existsInCollection = musicCollectionRepository.existsByUserIdAndMusicId(1, musicId);
        if (existsInCollection) return new ApiResponse("This music is already exists in your collection", false);

        MusicCollection musicCollection = new MusicCollection(null, 1, music);
        musicCollectionRepository.save(musicCollection);
        return new ApiResponse("Successfully added to music Collection", true);
    }

    public ApiResponse saveNewMusic(MusicDto musicDto) {
        boolean existsByTitle = musicRepository.existsByTitle(musicDto.getTitle());
        if (existsByTitle) return new ApiResponse("This music already exists", true);

        List<Author> authors = new ArrayList<>();

        for (Integer authorId : musicDto.getAuthors()) {
            authorRepository.findById(authorId).ifPresent(authors::add);
        }
        if (authors.isEmpty()) return new ApiResponse("Authors not found!", false);

        List<Genre> genreList = new ArrayList<>();

        for (Integer genreId : musicDto.getGenres()) {
            genreRepository.findById(genreId).ifPresent(genreList::add);
        }
        if (genreList.isEmpty()) return new ApiResponse("Genres not found!", false);

        Music music = new Music(null, musicDto.getTitle(), musicDto.getUserId(), musicDto.getAttachmentId(), genreList, authors);
        musicRepository.save(music);
        return new ApiResponse("Successfully saved", true, music);
    }

}
