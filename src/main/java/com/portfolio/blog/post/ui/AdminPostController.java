package com.portfolio.blog.post.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @GetMapping("/admin/posts")
    public String list(PostSearchDto postSearchDto, Pageable pageable, Model model) {
        Page<PostResponseDto> posts = postService.findPosts(postSearchDto, pageable);
        model.addAttribute("posts", posts);
        return "admin/post/list";
    }

}
