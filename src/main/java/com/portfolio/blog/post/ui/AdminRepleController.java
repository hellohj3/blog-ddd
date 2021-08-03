package com.portfolio.blog.post.ui;

import com.portfolio.blog.common.ui.dto.CriteriaDto;
import com.portfolio.blog.post.application.service.RepleService;
import com.portfolio.blog.post.ui.dto.RepleDto;
import com.portfolio.blog.post.ui.dto.RepleSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AdminRepleController {

    private final RepleService repleService;

    // 리스트
    @GetMapping("/admin/reples")
    public String listReple(RepleSearchDto repleSearchDto, Model model,
                            @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<RepleDto> reples = repleService.findReples(repleSearchDto, pageable);
        CriteriaDto pagination = CriteriaDto.builder().page(reples).build();

        model.addAttribute("list", reples.getContent());
        model.addAttribute("pagination", pagination);
        model.addAttribute("reple", RepleDto.builder().build());

        return "admin/reple/list";
    }

    // 등록
    @PostMapping("/admin/reple")
    public String createReple(Model model, @Valid RepleDto repleDto) throws Exception {
        repleService.saveReple(repleDto);

        return "redirect:/admin/reples";
    }

    // 삭제
    @DeleteMapping("/admin/reple/{id}")
    @ResponseBody
    public String deleteReple(@PathVariable("id") Long id) throws Exception {
        String result = repleService.deleteReple(id);

        return result;
    }
}
