package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class AttachmentsResponseDto {
    private Long id;
    private String path;
    private String name;

    @Builder
    @QueryProjection
    public AttachmentsResponseDto(Long id, String path, String name) {
        this.id = id;
        this.path = path;
        this.name = name;
    }
}
