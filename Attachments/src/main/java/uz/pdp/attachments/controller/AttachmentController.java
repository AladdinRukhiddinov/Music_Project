package uz.pdp.attachments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.attachments.common.ApiResponse;
import uz.pdp.attachments.service.AttachmentService;
import uz.pdp.attachments.util.Constants;

import java.io.IOException;

@RestController
@RequestMapping("/api/attachment-service")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/byId/{attachmentId}")
    public HttpEntity<?> getAttachmentById(@PathVariable Long attachmentId) {
        ApiResponse apiResponse = attachmentService.attachmentById(attachmentId);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }

    @GetMapping()
    public HttpEntity<?> getAllAttachmentById(@RequestParam(name = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                              @RequestParam(name = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        ApiResponse apiResponse = attachmentService.allAttachmentsByPage(page, size);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }

    @PostMapping("/upload")
    public HttpEntity<?> fileUpload(@RequestParam MultipartFile file) throws IOException {
        ApiResponse apiResponse = attachmentService.fileUpload(file);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/download/{attachmentId}")
    public HttpEntity<?> downloadFile(@PathVariable Long attachmentId) {
        return attachmentService.fileDownload(attachmentId);
    }

    @DeleteMapping("delete/{attachmentId}")
    public HttpEntity<?> deleteFile(@PathVariable Long attachmentId) {
        ApiResponse apiResponse = attachmentService.deleteFile(attachmentId);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }

    @PutMapping("edit-file/{attachmentId}")
    public HttpEntity<?> editFile(@PathVariable Long attachmentId, @RequestParam("file") MultipartFile file) {
        ApiResponse apiResponse = attachmentService.editFile(attachmentId, file);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 404).body(apiResponse);
    }
}
