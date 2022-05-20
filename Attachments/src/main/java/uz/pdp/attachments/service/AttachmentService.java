package uz.pdp.attachments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.attachments.common.ApiResponse;
import uz.pdp.attachments.entity.Attachment;
import uz.pdp.attachments.entity.AttachmentContent;
import uz.pdp.attachments.repository.AttachmentContentRepo;
import uz.pdp.attachments.repository.AttachmentRepo;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentService {

    private final AttachmentRepo attachmentRepo;
    private final AttachmentContentRepo contentRepo;

    public ApiResponse attachmentById(Long attachmentId) {
        Optional<Attachment> byId = attachmentRepo.findById(attachmentId);
        return byId.map(attachment -> new ApiResponse("Success!", true, attachment))
                .orElseGet(() -> new ApiResponse("Attachment not found!", false));
    }

    public ApiResponse allAttachmentsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Attachment> attachmentPage = attachmentRepo.findAll(pageable);
        if (attachmentPage.isEmpty()) {
            return new ApiResponse("Attachments not found!", false);
        }
        return new ApiResponse("Success!", true, attachmentPage);
    }

    public ApiResponse fileUpload(MultipartFile file) throws IOException {
        Attachment attachment = attachmentRepo.save(new Attachment(file.getOriginalFilename(), file.getContentType(), file.getSize()));
        contentRepo.save(new AttachmentContent(file.getBytes(), attachment));
        return new ApiResponse("Success!", true);
    }

    public ResponseEntity<ByteArrayResource> fileDownload(Long attachmentId) {
        AttachmentContent byAttachmentId = contentRepo.getById(attachmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(byAttachmentId.getAttachment().getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + byAttachmentId.getAttachment().getOriginalName() + "\"")
                .body(new ByteArrayResource(byAttachmentId.getBytes()));
    }

    public ApiResponse editFile(Long attachmentId, MultipartFile file) {
        Optional<Attachment> optionalAttachment = attachmentRepo.findById(attachmentId);
        if (!optionalAttachment.isPresent()) {
            return new ApiResponse("Attachment Not Found!", false);
        }
        try {
            Attachment editAttachment = optionalAttachment.get();
            editAttachment.setOriginalName(file.getOriginalFilename());
            editAttachment.setContentType(file.getContentType());
            editAttachment.setSize(file.getSize());
            Attachment attachment = attachmentRepo.save(editAttachment);

            AttachmentContent editAttachmentContent = contentRepo.getById(attachmentId);
            editAttachmentContent.setAttachment(attachment);
            editAttachmentContent.setBytes(file.getBytes());
            contentRepo.save(editAttachmentContent);
            return new ApiResponse("Success!", true);
        } catch (IOException e) {
            return new ApiResponse("Error!", false);
        }
    }

    public ApiResponse deleteFile(Long attachmentId) {
        Optional<Attachment> optionalAttachment = attachmentRepo.findById(attachmentId);
        if (!optionalAttachment.isPresent()) {
            return new ApiResponse("Attachment not Found", false);
        }
        try {
            AttachmentContent attachmentContent = contentRepo.getById(attachmentId);
            contentRepo.delete(attachmentContent);
            attachmentRepo.delete(optionalAttachment.get());
            return new ApiResponse("Success!", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }
}
