package uz.pdp.musicservice.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.musicservice.entity.Genre;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MusicDto {

    private String title;
    private Integer attachmentId;
    private Integer userId;
    private List<Integer> genres;
    private List<Integer> authors;

}
