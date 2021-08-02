package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PostDto {

    private Long id;
    private String author;
    private String title;
    private String contents;
    private List<AttachmentsDto> attachmentsList;
    private Integer viewCount;
    private LocalDateTime createdDate;

    @Builder
    @QueryProjection
    public PostDto(Long id, String author, String title, String contents, Integer viewCount, LocalDateTime createdDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
    }
}
