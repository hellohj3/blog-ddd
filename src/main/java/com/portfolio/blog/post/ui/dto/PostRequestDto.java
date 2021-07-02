package com.portfolio.blog.post.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String contents;
    private List<MultipartFile> attachmentsList;

    @Builder
    @QueryProjection
    public PostRequestDto(String title, String contents, List<MultipartFile> attachmentsList) {
        this.title = title;
        this.contents = contents;
        this.attachmentsList = attachmentsList;
    }
}
