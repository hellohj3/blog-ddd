package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class RepleSearchDto {
    private String author;
    private String contents;

    @Builder
    @QueryProjection
    public RepleSearchDto(String author, String contents) {
        this.author = author;
        this.contents = contents;
    }

}
