package com.portfolio.blog.post.ui.dto;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.domain.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Page<RepleDto> reples;
    private Integer viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    @QueryProjection
    public PostDto(Long id, String author, String title, String contents,
                   Integer viewCount, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // Dto to Entity
    public Post createEntity(Account account, List<Attachments> attachmentsList) {
        Post post = Post.builder()
                .id(this.id != null ? this.id : null)
                .title(this.title)
                .contents(this.contents)
                .account(account)
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();

        for (Attachments attachments : attachmentsList) {
            post.addAttachments(attachments);
        }

        return post;
    }

}
