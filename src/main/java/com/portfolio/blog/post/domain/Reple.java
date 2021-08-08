package com.portfolio.blog.post.domain;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.common.domain.BaseTimeEntity;
import com.portfolio.blog.post.ui.dto.RepleDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Reple extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reple_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String repleContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reple")
    private Reple parentReple;

    @OneToMany(mappedBy = "parentReple")
    @Column
    private List<Reple> repleList = new ArrayList<>();

    // 댓글 수정
    public void updateReple(RepleDto repleDto) {
        this.repleContents = repleDto.getRepleContents();
        this.updateDate();
    }

    // 포스트 연결 메소드
    public void connectPost(Post post) {
        this.post = post;
    }

    // 포스트 연관관계 해제 메소드
    public void disConnectPost() {
        this.post = null;
    }

    // 계정 연결 메소드
    public void connectAccount(Account account) {
        this.account = account;
    }

    // 계정 연관관계 해제 메소드
    public void disConnectAccount() {
        this.account = null;
    }

    // 리플 연결 메소드
    public void connectParentReple(Reple parentReple) {
        this.parentReple = parentReple;
    }

    // 리플 연관관계 해제 메소드
    public void disConnectParentReple() {
        this.parentReple = null;
    }

    // RepleDto 로 변환
    public RepleDto parseDto() {
        return RepleDto.builder()
                .id(this.id)
                .postId(this.post.getId())
                .parentId(this.parentReple.id)
                .author(this.account.getAccountId())
                .repleContents(this.repleContents)
                .postTitle(this.post.getTitle())
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

}
