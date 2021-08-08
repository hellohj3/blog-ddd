package com.portfolio.blog.post.ui;

import com.portfolio.blog.common.ui.dto.CriteriaDto;
import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.application.service.RepleService;
import com.portfolio.blog.post.ui.dto.PostDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import com.portfolio.blog.post.ui.dto.RepleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final RepleService repleService;

    // 리스트
    @GetMapping("/")
    public String listPost(PostSearchDto postSearchDto, Model model,
                           @PageableDefault(page = 0, size = 100, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.findPosts(postSearchDto, pageable);
        CriteriaDto pagination = CriteriaDto.builder().page(posts).build();

        model.addAttribute("list", posts.getContent());
        model.addAttribute("pagination", pagination);

        return "front/post/list";
    }

    // 조회
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model,
                           @PageableDefault(page = 0, size = 100, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        PostDto postDto = postService.findPost(id);
        postDto.setReples(repleService.findReples(id, pageable));

        model.addAttribute("post", postDto);

        return "front/post/view";
    }

}
