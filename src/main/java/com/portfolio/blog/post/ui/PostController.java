package com.portfolio.blog.post.ui;

import com.portfolio.blog.common.ui.dto.CriteriaDto;
import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.ui.dto.PostDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 리스트
    @GetMapping("/")
    public String listPost(PostSearchDto postSearchDto, Model model,
                           @PageableDefault(page = 0, size = 100, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.findPosts(postSearchDto, pageable);
        CriteriaDto pagination = CriteriaDto.builder().page(posts).build();

        model.addAttribute("list", posts.getContent());
        model.addAttribute("pagination", pagination);

        return "front/main/list";
    }

}
