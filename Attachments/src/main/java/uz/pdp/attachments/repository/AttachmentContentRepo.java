package uz.pdp.attachments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.attachments.entity.AttachmentContent;

public interface AttachmentContentRepo extends JpaRepository<AttachmentContent, Long> {

    AttachmentContent findByAttachmentId(Long attachmentId);
}
