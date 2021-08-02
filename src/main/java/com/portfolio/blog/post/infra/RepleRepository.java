package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.domain.Reple;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepleRepository extends JpaRepository<Reple, Long> {
}
