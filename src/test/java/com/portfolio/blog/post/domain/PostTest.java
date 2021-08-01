package com.portfolio.blog.post.domain;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class PostTest {

    @PersistenceContext
    EntityManager em;

    /*@Test
    public void 포스트_객체_생성_조회() throws Exception {
        //given
        Post post = Post.builder()
                .title("포스트 타이틀")
                .contents("포스트 내용")
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();

        //when
        em.persist(post);
        List<Post> resultList =
                em.createQuery("select p from Post p where p.title = '포스트 타이틀'",
                        Post.class).getResultList();
        Post findPost = resultList.get(0);

        //then
        assertEquals(resultList.size(), 1, "포스트 객체 인서트 후, 검색 사이즈가 다름");
        assertEquals(findPost, post, "포스트 객체 불일치");
    }
    
    @Test
    public void 포스트_객체_수정() throws Exception {
        // TEST 수정 필요 - 210801
        //given
        *//*Post post = Post.builder()
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
                .origin("원본파일명_2")
                .name("객체테스트_2")
                .size(20000L)
                .build();
        em.persist(attachments_1);
        em.persist(attachments_2);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트 타이틀만 변경")
                .build();
        List<Attachments> attachmentsList = new ArrayList<>();
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);

        //when
        post.updatePost(postRequestDto, attachmentsList);
        post.addViewCount();

        em.flush();
        em.clear();

        //then
        assertEquals(post.getTitle(), postRequestDto.getTitle(), "포스트 객체 타이틀 변경 안됨");
        assertEquals(post.getAttachmentsList().get(0), attachments_1, "포스트에 첨부파일 추가 안됨");
        assertEquals(post.getViewCount(), 1, "포스트 조회수 증가 실패");*//*
    }

    @Test
    public void 포스트_객체_삭제() throws Exception {
        //given
        Post post_1 = Post.builder()
                .title("포스트 타이틀_1")
                .contents("포스트 내용_1")
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();
        Post post_2 = Post.builder()
                .title("포스트 타이틀_2")
                .contents("포스트 내용_2")
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();
        em.persist(post_1);
        em.persist(post_2);

        Attachments attachments_1 = Attachments.builder()
                .path("경로_1")
                .origin("원본파일명_1")
                .name("객체테스트_1")
                .size(10000L)
                .build();
        em.persist(attachments_1);

        post_2.addAttachments(attachments_1);

        //when
        Long id_1 = post_1.getId();
        Long id_2 = post_2.getId();

        em.remove(post_1);
        em.remove(post_2);

        em.flush();
        em.clear();

        Post findRemovePost_1 = em.find(Post.class, id_1);
        Post findRemovePost_2 = em.find(Post.class, id_2);

        //then
        assertEquals(findRemovePost_1, null, "포스트 객체_1 삭제 안됬음");
        assertEquals(findRemovePost_2, null, "포스트 객체_2 삭제 안됬음");
    }

    @Test
    public void Post_의_DTO_변환() throws Exception {
        //given
        Post post = Post.builder()
                .title("포스트 타이틀_1")
                .contents("포스트 내용_1")
                .account(em.find(Account.class, "admin"))
                .attachmentsList(new ArrayList<>())
                .viewCount(0)
                .build();

        //when

        //then
        assertEquals(post.parseResponseDto().getClass(), PostResponseDto.class, "DTO 변환 리턴값 불일치");
    }*/

}