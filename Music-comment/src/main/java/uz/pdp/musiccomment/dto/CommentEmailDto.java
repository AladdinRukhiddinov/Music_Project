package uz.pdp.musiccomment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEmailDto {
    private String receiverEmail;
    private String commentAuthorName;
    private String subject;
    private String musicTitle;
    private String commentBody;
    private String acceptUrl;
    private String rejectUrl;

}
