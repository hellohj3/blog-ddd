package com.portfolio.blog.post.ui.dto;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.post.domain.Post;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostDto {

    private String author;
    private String title;
    private String contents;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .account(Account.builder()
                        .accountId(this.author)
                        .build())
                .build();
    }

}
