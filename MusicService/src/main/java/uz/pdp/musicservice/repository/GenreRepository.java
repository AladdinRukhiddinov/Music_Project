package uz.pdp.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.musicservice.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre,Integer> {

}
