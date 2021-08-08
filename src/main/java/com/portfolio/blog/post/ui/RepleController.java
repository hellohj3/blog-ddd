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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class RepleController {

    private final RepleService repleService;

    // 리스트
    @GetMapping("/reples/{postId}")
    @ResponseBody
    public String listReple(@PathVariable("postId") Long postId,
                            @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        return getPagination(postId, pageable);
    }

    // 등록
    @PostMapping("/reple")
    @ResponseBody
    public String createReple(@RequestBody RepleDto repleDto,
                              @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        Long postId = repleDto.getPostId();

        repleService.saveReple(repleDto);

        return getPagination(postId, pageable);
    }

    // 수정
    @PutMapping("/reple")
    @ResponseBody
    public String updateReple(@RequestBody RepleDto repleDto,
                              @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        if (!checkAuthor(repleDto.getId()))
            throw new AccessDeniedException("해당 권한이 없습니다.");

        Long postId = repleDto.getPostId();
        repleService.updateReple(repleDto);

        return getPagination(postId, pageable);
    }

    // 삭제
    @DeleteMapping("/reple")
    @ResponseBody
    public String deleteReple(@RequestBody RepleDto repleDto,
                              @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        if (!checkAuthor(repleDto.getId()))
            throw new AccessDeniedException("해당 권한이 없습니다.");

        Long postId = repleDto.getPostId();
        repleService.deleteReple(repleDto.getId());

        return getPagination(postId, pageable);
    }

    public boolean checkAuthor(Long id) {
        RepleDto dto = repleService.findReple(id);
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();

        return accountId.equals(dto.getAuthor());
    }

    public String getPagination(Long postId, Pageable pageable) {
        HashMap<String, Object> result = new HashMap<>();
        Page<RepleDto> reples = repleService.findReples(postId, pageable);
        List<RepleDto> contents = reples.getContent();
        CriteriaDto pagination = CriteriaDto.builder().page(reples).build();
        List<Long> ids = reples.getContent().stream().map(r -> r.getId()).collect(Collectors.toList());
        List<RepleDto> parents = repleService.findParent(ids);
        List<Long> parentIds = parents.stream().map(p -> p.getParentId()).distinct().collect(Collectors.toList());

        result.put("repleList", contents);
        result.put("parents", parents);
        result.put("parentIds", parentIds);
        result.put("pagination", pagination);
        result.put("reple", RepleDto.builder().build());

        Gson gson = new Gson();

        return gson.toJson(result);
    }

}
