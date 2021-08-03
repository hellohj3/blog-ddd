package com.portfolio.blog.post.infra.query;

import com.portfolio.blog.post.ui.dto.PostDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryQuery {
    // 리스트 조회 (검색)
    List<PostDto> search(PostSearchDto postSearchDto);

    // 리스트 페이징 (카운트 동시)
    Page<PostDto> searchPaginationSimple(PostSearchDto postSearchDto, Pageable pageable);

    // 리스트 페이징 (카운트 별도)
    Page<PostDto> searchPaginationComplex(PostSearchDto postSearchDto, Pageable pageable);

    // 단건조회
    PostDto findByIdToDto(Long id);
}
