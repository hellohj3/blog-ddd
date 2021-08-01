package com.portfolio.blog.post.domain;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class AttachmentsTest {

    @PersistenceContext
    EntityManager em;

    /*@Test
    public void 첨부파일_객체_생성_조회() throws Exception {
        //given
        Attachments attachments_1 = Attachments.builder()
                .path("경로_1")
                .origin("원본파일명_1")
                .name("객체테스트_1")
                .size(10000L)
                .build();
        Attachments attachments_2 = Attachments.builder()
                .path("경로_2")
                .origin("원본파일명_2")
                .name("객체테스트_2")
                .size(20000L)
                .build();

        //when
        em.persist(attachments_1);
        em.persist(attachments_2);
        List<Attachments> resultList_1 =
                em.createQuery("select a from Attachments a where a.name = '객체테스트_1'",
                        Attachments.class).getResultList();
        Attachments findAttachments_1 = resultList_1.get(0);
        List<Attachments> resultList_2 =
                em.createQuery("select a from Attachments a where a.name = '객체테스트_2'",
                        Attachments.class).getResultList();
        Attachments findAttachments_2 = resultList_2.get(0);

        //then
        assertEquals(resultList_1.size(), 1, "첨부파일 객체 인서트 후, 검색 사이즈가 다름");
        assertEquals(findAttachments_1, attachments_1, "첨부파일 객체 불일치");
    }

    @Test
    public void 첨부파일_객체_수정() throws Exception {
        //given
        Post post = Post.builder()
                .title("포스트 타이틀")
                .contents("포스트 내용")
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();
        em.persist(post);

        Attachments attachments_1 = Attachments.builder()
                .path("경로_1")
                .origin("원본파일명_1")
                .name("객체테스트_1")
                .size(10000L)
                .build();
        Attachments attachments_2 = Attachments.builder()
                .path("경로_2")
                .post(post)
                .origin("원본파일명_2")
                .name("객체테스트_2")
                .size(20000L)
                .build();

        em.persist(attachments_1);
        em.persist(attachments_2);

        List<Attachments> resultList_1 =
                em.createQuery("select a from Attachments a where a.name = '객체테스트_1'",
                        Attachments.class).getResultList();
        Attachments findAttachments_1 = resultList_1.get(0);
        List<Attachments> resultList_2 =
                em.createQuery("select a from Attachments a where a.name = '객체테스트_2'",
                        Attachments.class).getResultList();
        Attachments findAttachments_2 = resultList_2.get(0);

        //when - then
        findAttachments_1.connectPost(post);
        findAttachments_2.disconnectPost();

        em.flush();
        em.clear();

        //then
        assertNotEquals(findAttachments_1.getPost(), null, "첨부파일 객체에 포스트 연결 없음");
        assertEquals(findAttachments_1.getPost().getTitle(), post.getTitle(), "첨부파일 연관관계 값 불일치");
        assertEquals(findAttachments_2.getPost(), null, "첨부파일 객체에 포스트 연결 있음");
    }

    @Test
    public void 첨부파일_객체_삭제() throws Exception {
        //given
        Post post = Post.builder()
                .title("포스트 타이틀")
                .contents("포스트 내용")
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();
        em.persist(post);

        Attachments attachments_1 = Attachments.builder()
                .path("경로_1")
                .origin("원본파일명_1")
                .name("객체테스트_1")
                .size(10000L)
                .build();
        Attachments attachments_2 = Attachments.builder()
                .path("경로_2")
                .post(post)
                .origin("원본파일명_2")
                .name("객체테스트_2")
                .size(20000L)
                .build();

        em.persist(attachments_1);
        em.persist(attachments_2);

        List<Attachments> resultList_1 =
                em.createQuery("select a from Attachments a where a.name = '객체테스트_1'",
                        Attachments.class).getResultList();
        Attachments findAttachments_1 = resultList_1.get(0);
        List<Attachments> resultList_2 =
                em.createQuery("select a from Attachments a where a.name = '객체테스트_2'",
                        Attachments.class).getResultList();
        Attachments findAttachments_2 = resultList_2.get(0);

        //when
        Long id_1 = findAttachments_1.getId();
        Long id_2 = findAttachments_2.getId();

        em.remove(findAttachments_1);
        em.remove(findAttachments_2);

        em.flush();
        em.clear();

        Attachments findRemoveAttachments_1 = em.find(Attachments.class, id_1);
        Attachments findRemoveAttachments_2 = em.find(Attachments.class, id_2);

        //then
        assertEquals(findRemoveAttachments_1, null, "첨부파일 객체_1 삭제 안됬음");
        assertEquals(findRemoveAttachments_2, null, "첨부파일 객체_2 삭제 안됬음");
    }

    @Test
    public void Attachments_의_DTO_변환() throws Exception {
        //given
        Attachments attachments_1 = Attachments.builder()
                .path("경로_1")
                .origin("원본파일명_1")
                .name("객체테스트_1")
                .size(10000L)
                .build();

        //when

        //then
        assertEquals(attachments_1.parseResponseDto().getClass(), AttachmentsResponseDto.class, "DTO 변환 리턴값 불일치");
    }*/

}