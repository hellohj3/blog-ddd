package com.portfolio.blog.post.ui;

import com.google.gson.Gson;
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
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Long> ids = reples.getContent().stream().map(r -> r.getId()).collect(Collectors.toList());
        List<RepleDto> parents = repleService.findParent(ids);
        List<Long> parentIds = parents.stream().map(p -> p.getParentId()).distinct().collect(Collectors.toList());

        model.addAttribute("list", reples.getContent());
        model.addAttribute("parents", parents);
        model.addAttribute("parentIds", parentIds);
        model.addAttribute("pagination", pagination);
        model.addAttribute("reple", RepleDto.builder().build());

        return "admin/reple/list";
    }

    // 단건조회
    @GetMapping("/admin/reple/{id}")
    @ResponseBody
    public String viewReple(@PathVariable("id") Long id) {
        RepleDto repleDto = repleService.findReple(id);
        HashMap<String, Object> result = new HashMap<>();

        result.put("parentReple", repleDto);

        Gson gson = new Gson();

        return gson.toJson(result);
    }

    // 등록
    @PostMapping("/admin/reple")
    @ResponseBody
    public String createReple(@RequestBody RepleDto repleDto) throws Exception {
        repleService.saveReple(repleDto);

        return "redirect:/admin/reples";
    }

    // 수정
    @PutMapping("/admin/reple")
    @ResponseBody
    public String updateReple(@RequestBody RepleDto repleDto) throws Exception {
        repleService.updateReple(repleDto);

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
