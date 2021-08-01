package com.portfolio.blog.post.domain;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.common.domain.BaseTimeEntity;
import com.portfolio.blog.post.ui.dto.PostDto;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Attachments> attachmentsList = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Integer viewCount;

    // 포스트 생성 메소드
    public static Post createPost(PostDto postDto, Account account, LinkedList<Attachments> attachmentsList) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .contents(postDto.getContents())
                .account(account)
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();

        for (Attachments attachments : attachmentsList) {
            post.addAttachments(attachments);
        }

        return post;
    }

    // 포스트 수정 메소드
    public void updatePost(PostDto postDto) {

        this.title = StringUtils.hasText(postDto.getTitle()) ? postDto.getTitle() : this.title;
        this.contents = StringUtils.hasText(postDto.getContents()) ? postDto.getContents() : this.contents;
        this.updateDate();
    }

    // 뷰카운트 증가 메소드
    public void addViewCount() {
        this.viewCount++;
    }

    // 첨부파일 추가 메소드
    public void addAttachments(Attachments attachments) {
        this.attachmentsList.add(attachments);
        attachments.connectPost(this);
    }

    // 첨부파일 삭제 메소드
    public void removeAttachments(Attachments attachments) {
        this.attachmentsList.remove(attachments);
        attachments.disconnectPost();
    }

    // PostResponseDto 로 변환
    public PostDto parseDto() {
        return PostDto.builder()
                .id(this.getId())
                .author(this.account.getAccountId())
                .title(this.getTitle())
                .contents(this.getContents())
                .build();
    }
}
