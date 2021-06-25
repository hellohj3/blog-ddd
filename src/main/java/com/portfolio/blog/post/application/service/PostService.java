package com.portfolio.blog.post.application.service;

import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.infra.PostRepository;
import com.portfolio.blog.post.ui.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public Long savePost(PostDto postDto) {
        return postRepository.save(postDto.toEntity()).getId();
    }
}
