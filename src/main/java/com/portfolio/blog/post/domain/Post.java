package com.portfolio.blog.post.domain;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.common.domain.BaseTimeEntity;
import com.portfolio.blog.post.ui.dto.AttachmentsDto;
import com.portfolio.blog.post.ui.dto.PostDto;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "post")
    private List<Attachments> attachmentsList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Reple> reples = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Column(nullable = false)
    private Integer viewCount;

    // 포스트 수정 메소드
    public List<String> updatePost(PostDto postDto) {
        List<String> deleteAttachmentsIds = new ArrayList<>();
        this.title = StringUtils.hasText(postDto.getTitle()) ? postDto.getTitle() : this.title;
        this.contents = StringUtils.hasText(postDto.getContents()) ? postDto.getContents() : this.contents;

        if (postDto.getAttachmentsList() != null) {
            List<String> names = postDto.getAttachmentsList().stream()
                    .map(AttachmentsDto::getName).collect(Collectors.toList());

            for (int i=0; i<this.attachmentsList.size(); i++) {
                if (!names.contains(attachmentsList.get(i).getName())) {
                    deleteAttachmentsIds.add(attachmentsList.get(i).getName());
                    this.removeAttachments(attachmentsList.get(i));
                }
            }

            this.attachmentsList = postDto.getAttachmentsList().stream()
                    .map(AttachmentsDto::createEntity).collect(Collectors.toList());
        }
        this.updateDate();

        return deleteAttachmentsIds;
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

    // 댓글 추가 메소드
    public void addReple(Reple reple) {
        this.reples.add(reple);
        reple.connectPost(this);
    }

    // 첨부파일 삭제 메소드
    public void removeAttachments(Attachments attachments) {
        this.attachmentsList.remove(attachments);
        attachments.disconnectPost();
    }

    // PostDto 로 변환
    public PostDto parseDto() {
        return PostDto.builder()
                .id(this.id)
                .author(this.account.getAccountId())
                .title(this.title)
                .contents(this.contents)
                .viewCount(this.viewCount)
                .createdDate(this.getCreatedDate())
                .build();
    }
}
