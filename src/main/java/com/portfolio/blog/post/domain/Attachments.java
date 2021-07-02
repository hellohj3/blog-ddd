package com.portfolio.blog.post.domain;

import com.portfolio.blog.common.domain.BaseTimeEntity;
import com.portfolio.blog.post.ui.dto.AttachmentsResponseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Attachments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachments_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String path;
    private String origin;
    private String name;
    private Long size;

    // 포스트 연결 메소드
    public void connectPost(Post post) {
        this.post = post;
    }

    // 포스트 연결 해제
    public void disconnectPost() {
        this.post = null;
    }

    // AttachmentsResponseDto 로 변환
    public AttachmentsResponseDto parseResponseDto(Attachments attachments) {
        return AttachmentsResponseDto.builder()
                .id(attachments.getId())
                .path(attachments.getPath())
                .name(attachments.getName())
                .build();
    }
}
