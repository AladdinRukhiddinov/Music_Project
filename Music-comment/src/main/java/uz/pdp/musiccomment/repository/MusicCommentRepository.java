package uz.pdp.musiccomment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.musiccomment.entity.MusicComment;
import uz.pdp.musiccomment.entity.enums.Status;

import java.util.List;

public interface MusicCommentRepository extends JpaRepository<MusicComment, Integer> {

    @Query(nativeQuery = true, value = "select m.id,\n" +
            "       m.music_id,\n" +
            "       m.user_id,\n" +
            "       m.comment_body,\n" +
            "       m.rate,\n" +
            "       m.created_at, \n" +
            "       m.status\n" +
            "from music_comments m\n" +
            "where music_Id = :musicId\n" +
            "and m.status = 'ACCEPTED'")
    List<MusicComment> getByMusicId(Integer musicId);

    List<MusicComment> findAllByMusicIdAndStatus(Integer musicId, Status status);

    @Query(nativeQuery = true, value = "select\n" +
            "       avg(m.rate) as rate\n" +
            "from music_comments m\n" +
            "where music_id = :musicId")
    Double getAverage(Integer musicId);
}
