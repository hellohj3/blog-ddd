package com.portfolio.blog.post.domain;

import com.portfolio.blog.common.domain.BaseTimeEntity;
import com.portfolio.blog.post.ui.dto.AttachmentsDto;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Attachments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachments_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long size;

    // 첨부파일 수정 메소드
    public String updateAttachments(AttachmentsDto attachmentsDto) {
        if (StringUtils.hasText(attachmentsDto.getName())) {
            if (!attachmentsDto.getName().equals(this.origin)) {
                String beforeFileName = this.name;
                this.origin = attachmentsDto.getName();
                this.name = String.valueOf(System.currentTimeMillis()) + attachmentsDto.getExtension();
                this.size = attachmentsDto.getSize();

                return beforeFileName;
            } else {
                return "fail";
            }
        } else {
            return "fail";
        }
    }

    // 포스트 연결 메소드
    public void connectPost(Post post) {
        this.post = post;
    }

    // 포스트 연결 해제
    public void disconnectPost() {
        this.post = null;
    }

    // AttachmentsResponseDto 로 변환
    public AttachmentsDto parseDto() {
        return AttachmentsDto.builder()
                .id(this.getId())
                .path(this.getPath())
                .name(this.getName())
                .build();
    }

}
