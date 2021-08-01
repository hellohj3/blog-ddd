package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class AttachmentsDto {
    private Long id;
    private String path;
    private String origin;
    private String name;
    private String extension;
    private Long size;

    @Builder
    @QueryProjection
    public AttachmentsDto(Long id, String path, String origin, String name, String extension, Long size) {
        this.id = id;
        this.path = path;
        this.origin = origin;
        this.name = name;
        this.extension = extension;
        this.size = size;
    }
}
