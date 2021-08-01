package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.domain.Attachments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AttachmentsRepositoryTest {

    @Autowired
    AttachmentsRepository attachmentsRepository;
    @PersistenceContext
    EntityManager em;

    /*@Test
    public void 첨부파일_생성() throws Exception {
        //given
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(1234123L)
                .build());

        //when
        Attachments saveAttachments = attachmentsRepository.save(attachments_1);

        //then
        assertEquals(saveAttachments, attachments_1, "첨부파일 객체 불일치");
    }

    @Test
    public void 첨부파일_NULL_생성불가() throws Exception {
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            //given
            Attachments attachments = Attachments.builder().build();

            //when
            attachmentsRepository.save(attachments);
        });
    }

    @Test
    public void 첨부파일_수정() throws Exception {
        //given
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(1234123L)
                .build());

        //when
        attachmentsRepository.save(attachments_1);
        attachments_1.disconnectPost();

        //then
        assertTrue(attachments_1.getPost() == null, "첨부파일과 포스트 연결 안끊김");
    }

    @Test
    public void 첨부파일_읽기() throws Exception {
        //given
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(1234123L)
                .build());

        //when
        Attachments saveEntity = attachmentsRepository.save(attachments_1);
        Attachments findEntity = attachmentsRepository.findById(saveEntity.getId()).get();

        //then
        assertEquals(saveEntity, findEntity, "첨부파일 객체 불일치");
    }

    @Test
    public void 첨부파일_삭제() throws Exception {
        //given
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(1234123L)
                .build());

        attachmentsRepository.save(attachments_1);
        attachmentsRepository.delete(attachments_1);

        //when
        Optional<Attachments> findEntity = attachmentsRepository.findById(attachments_1.getId());

        //then
        assertTrue(findEntity.isEmpty(), "첨부파일 객체 삭제 실패");
    }*/

}