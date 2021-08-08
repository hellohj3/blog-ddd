package com.portfolio.blog.post.infra.query;

import com.portfolio.blog.post.ui.dto.RepleDto;
import com.portfolio.blog.post.ui.dto.RepleSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RepleRepositoryQuery {
    // 리스트 조회 (검색)
    List<RepleDto> search(RepleSearchDto repleSearchDto);

    // 리스트 조회 (답글)
    List<RepleDto> searchParent(List<Long> ids);

    // 리스트 페이징 (카운트 동시)
    Page<RepleDto> searchPaginationSimple(RepleSearchDto repleSearchDto, Pageable pageable);

    // 리스트 페이징 (카운트 동시)
    Page<RepleDto> searchPaginationSimple(Long postId, Pageable pageable);

    // 리스트 페이징 (카운트 별도)
    Page<RepleDto> searchPaginationComplex(RepleSearchDto repleSearchDto, Pageable pageable);

    // 단건조회
    RepleDto findByIdToDto(Long id);
}
