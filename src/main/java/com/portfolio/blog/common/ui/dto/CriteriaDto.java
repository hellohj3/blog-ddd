package com.portfolio.blog.common.ui.dto;

import lombok.*;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CriteriaDto {
    int currentPage;
    int pageSize;
    int totalPage;
    int startPage;
    int endPage;
    boolean first;
    boolean last;

    @Builder
    public CriteriaDto(Page<?> page) {
        this.currentPage = page.getPageable().getPageNumber();
        this.pageSize = page.getPageable().getPageSize();
        this.totalPage = page.getTotalPages();
        this.startPage = (int) Math.floor(currentPage / pageSize * pageSize + 1);
        this.endPage = ((startPage + pageSize -1) > totalPage) ? totalPage : (startPage + pageSize -1);
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
