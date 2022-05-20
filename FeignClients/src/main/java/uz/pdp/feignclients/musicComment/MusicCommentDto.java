package uz.pdp.feignclients.musicComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@PackagePrivate
public class MusicCommentDto {

    private Integer id;
    private Integer musicId;
    private Integer userId;
    private String commentBody;
    private Integer rate;
    private Timestamp createdAt;

}
