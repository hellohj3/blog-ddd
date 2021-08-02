package com.portfolio.blog.post.ui.dto;

import com.portfolio.blog.post.domain.Attachments;
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

    public Attachments parseEntity() {
        return Attachments.builder()
                .id(this.id)
                .path(this.path)
                .origin(this.origin)
                .name(this.name)
                .size(this.size)
                .build();
    }
}