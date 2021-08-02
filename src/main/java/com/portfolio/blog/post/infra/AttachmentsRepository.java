package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.domain.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {
    List<Attachments> findByNameIn(List<String> names);
}
