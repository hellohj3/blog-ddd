package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class RepleDto {

    private Long id;
    private Long postId;
    private Long parentId;
    private String author;
    private String contents;
    private String postTitle;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    @QueryProjection
    public RepleDto(Long id, Long postId, Long parentId, String author, String contents, String postTitle,
                    LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.postId = postId;
        this.parentId = parentId;
        this.author = author;
        this.contents = contents;
        this.postTitle = postTitle;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}
