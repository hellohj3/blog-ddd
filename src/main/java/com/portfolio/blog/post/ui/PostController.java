package com.portfolio.blog.post.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.ui.dto.PostDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 포스트 리스트
    @GetMapping("/posts")
    public String list(PostSearchDto postSearchDto, Pageable pageable, Model model) {
        Page<PostDto> posts = postService.findPosts(postSearchDto, pageable);
        model.addAttribute("posts", posts);
        return "front/post/list";
    }

}
