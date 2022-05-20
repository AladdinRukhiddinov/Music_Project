package uz.pdp.musicservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.musicservice.entity.Music;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Integer> {

    @Query(nativeQuery = true, value = "select b.* from musics m where lower(m.title) like lower(concat('%', :search, '%'))")
    List<Music> getAllMusics(Pageable pageable, String search);

    boolean existsByTitle(String title);
}
