package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostResponseDto> search(PostSearchDto postSearchDto);
    Page<PostResponseDto> searchPaginationSimple(PostSearchDto postSearchDto, Pageable pageable);
    Page<PostResponseDto> searchPaginationComplex(PostSearchDto postSearchDto, Pageable pageable);
}
