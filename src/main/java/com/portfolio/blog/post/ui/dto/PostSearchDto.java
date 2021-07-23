package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PostSearchDto {
    private String title;
    private String author;
    private String regDateDesc;
    private Integer limit;

    @Builder
    @QueryProjection
    public PostSearchDto(String title, String author, String regDateDesc, Integer limit) {
        this.title = title;
        this.author = author;
        this.regDateDesc = regDateDesc;
        this.limit = limit;
    }
}
