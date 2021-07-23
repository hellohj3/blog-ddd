package com.portfolio.blog.post.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.ui.dto.PostRequestDto;
import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @GetMapping("/admin/posts")
    public String list(PostSearchDto postSearchDto, Model model,
                       @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.findPosts(postSearchDto, pageable);
        model.addAttribute("posts", posts);

        return "admin/post/list";
    }

    @GetMapping("/admin/post")
    public String form(Model model) {
        model.addAttribute("post"
                , PostRequestDto.builder()
                .title("")
                .contents("")
                .attachmentsList(new ArrayList<>())
                .build());

        return "admin/post/create";
    }
}
