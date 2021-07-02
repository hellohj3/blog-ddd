package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.domain.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {
}
