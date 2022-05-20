package uz.pdp.attachments.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private boolean isStatus;
    private Object object;

    public ApiResponse(String message, boolean isStatus) {
        this.message = message;
        this.isStatus = isStatus;
    }
}
