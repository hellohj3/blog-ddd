package com.portfolio.blog.post.infra.query;

import com.portfolio.blog.post.ui.dto.PostDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryQuery {
    List<PostDto> search(PostSearchDto postSearchDto);
    Page<PostDto> searchPaginationSimple(PostSearchDto postSearchDto, Pageable pageable);
    Page<PostDto> searchPaginationComplex(PostSearchDto postSearchDto, Pageable pageable);
    PostDto findByIdToDto(Long id);
}
