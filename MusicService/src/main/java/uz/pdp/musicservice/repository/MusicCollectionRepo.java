package uz.pdp.musicservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.musicservice.entity.Music;
import uz.pdp.musicservice.entity.MusicCollection;

import java.util.List;

public interface MusicCollectionRepo extends JpaRepository<MusicCollection, Integer> {

    @Query(nativeQuery = true, value = "select m.id as id,\n" +
            "       m.title as title,\n" +
            "       m.user_id as userId,\n" +
            "       m.attachment_id as attachmentId\n" +
            "from music_collection mc\n" +
            "         join musics m on m.id = mc.music_id\n" +
            "where mc.user_id =:userId limit :size offset :page")
    List<Music> getUserMusicCollection(Integer userId, Integer size, Integer page);

    List<MusicCollection> findAllByUserId(Pageable pageable, Integer userId);

    boolean existsByUserIdAndMusicId(Integer userId, Integer music_id);
}
