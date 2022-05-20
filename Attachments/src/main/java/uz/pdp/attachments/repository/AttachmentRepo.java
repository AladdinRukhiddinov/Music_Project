package uz.pdp.attachments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.attachments.entity.Attachment;

public interface AttachmentRepo extends JpaRepository<Attachment, Long> {
}
