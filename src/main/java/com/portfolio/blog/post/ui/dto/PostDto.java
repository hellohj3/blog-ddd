package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.LinkedList;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PostDto {

    private Long id;
    private String author;
    private String title;
    private String contents;
    private LinkedList<AttachmentsDto> attachmentsList;

    @Builder
    @QueryProjection
    public PostDto(Long id, String author, String title, String contents) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.contents = contents;
    }
}
