package uz.pdp.musiccomment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.musiccomment.entity.enums.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "music_comments")
public class MusicComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @Column(nullable = false)
    private Integer musicId;

    @Column(nullable = false)
    private Integer userId;

    private String commentBody;

    private Integer rate = 0;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = " timestamp default now() ")
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());


}
