package com.portfolio.blog.post.ui.dto;

import com.portfolio.blog.post.domain.Reple;
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
    private String repleContents;
    private String postTitle;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    @QueryProjection
    public RepleDto(Long id, Long postId, Long parentId, String author, String repleContents, String postTitle,
                    LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.postId = postId;
        this.parentId = parentId;
        this.author = author;
        this.repleContents = repleContents;
        this.postTitle = postTitle;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // Dto to Entity
    public Reple createEntity() {
        return Reple.builder()
                .id(this.id != null ? this.id : null)
                .repleContents(this.repleContents)
                .build();
    }

}
