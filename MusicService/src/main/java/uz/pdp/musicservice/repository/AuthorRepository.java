package uz.pdp.musicservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.musicservice.entity.Author;
import uz.pdp.musicservice.entity.Music;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,Integer> {


}
