package com.portfolio.blog.common.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.ui.dto.PostDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin/posts";
    }

}
