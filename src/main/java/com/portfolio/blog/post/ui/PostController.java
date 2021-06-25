package com.portfolio.blog.post.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.ui.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String list(Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts", posts);
        return "main";
    }

    @PostMapping("/post")
    public String create(PostDto postDto) {
        postService.savePost(postDto);
        return "";
    }

}
