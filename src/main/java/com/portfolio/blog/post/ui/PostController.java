package com.portfolio.blog.post.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.ui.dto.PostRequestDto;
import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 포스트 리스트
    @GetMapping("/posts")
    public String list(PostSearchDto postSearchDto, Pageable pageable, Model model) {
        Page<PostResponseDto> posts = postService.findPosts(postSearchDto, pageable);
        model.addAttribute("posts", posts);
        return "front/post/list";
    }

    // 포스트 생성
    @PostMapping("/post")
    public String create(PostRequestDto postRequestDto, Model model) throws Exception {
        Long result = postService.savePost(postRequestDto);
        boolean resultFlag = !ObjectUtils.isEmpty(result);

        model.addAttribute("result", (resultFlag) ?
                "fail" : "success");
        model.addAttribute("alertMsg", (resultFlag) ?
                "포스트 등록에 실패하였습니다" : "성공적으로 포스팅 하였습니다.");

        return "redirect:/posts";
    }

}
