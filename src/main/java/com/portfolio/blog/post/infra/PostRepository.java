package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.infra.query.PostRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuery {
}
