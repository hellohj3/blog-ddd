package com.portfolio.blog.post.infra;

import com.portfolio.blog.account.infra.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AttachmentsRepository attachmentsRepository;
    @PersistenceContext
    EntityManager em;
    // TEST 수정 필요 - 210801
    /*@Test
    public void 포스트_생성() throws Exception {
        //given
        // 1. 포스트 생성
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_01")
                .contents("포스트_내용_01")
                .build();
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(1234123L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(1111L)
                .build());
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);
        Post post = Post.createPost(postRequestDto, account, attachmentsList);

        //when
        // 2. 저장
        Post savePost = postRepository.save(post);

        //then
        // 3. 비교
        assertEquals(post.getId(), savePost.getId(), "포스트 ID 다름");
        assertEquals(post, savePost, "포스트 객체 다름");
    }

    @Test
    public void 포스트_NULL_생성불가() throws Exception {
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            //given
            PostRequestDto emptyPostRequestDto = PostRequestDto.builder().build();
            Account emptyAccount = Account.builder().build();
            List<Attachments> emptyAttachmentsList = new ArrayList<>();
            Post emptyPost = Post.createPost(emptyPostRequestDto, emptyAccount, emptyAttachmentsList);

            //when
            postRepository.save(emptyPost);
        });
    }

    @Test
    public void 포스트_수정() throws Exception {
        //given
        PostRequestDto createPostRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_01")
                .contents("포스트_내용_01")
                .build();
        PostRequestDto updatePostRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_02")
                .contents("포스트_내용_02")
                .build();
        Account account = accountRepository.findById("admin").get();
        List<Attachments> beforeAttachmentsList = new ArrayList<>();
        List<Attachments> afterAttachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12120L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11110L)
                .build());
        Attachments attachments_3 = attachmentsRepository.save(Attachments.builder()
                .path("경로_3")
                .origin("첨부파일명_오리진_3")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(10010L)
                .build());
        beforeAttachmentsList.add(attachments_1);
        beforeAttachmentsList.add(attachments_2);
        afterAttachmentsList.add(attachments_1);
        afterAttachmentsList.add(attachments_3);
        Post post = Post.createPost(createPostRequestDto, account, beforeAttachmentsList);
        postRepository.save(post);

        //when
        post.updatePost(updatePostRequestDto, afterAttachmentsList);

        //then
        assertNotEquals(post.getTitle(), "포스트_타이틀_01", "포스트 타이틀 불일치");
        assertEquals(post.getTitle(), updatePostRequestDto.getTitle(), "포스트 타이틀 불일치");
        assertNotEquals(post.getContents(), "포스트_내용_01", "포스트 내용 불일치");
        assertEquals(post.getContents(), updatePostRequestDto.getContents(), "포스트 내용 불일치");
        assertFalse(post.getAttachmentsList().contains(attachments_2), "첨부파일 리스트에 attachments_2 객체 있음");
        assertTrue(post.getAttachmentsList().contains(attachments_3), "첨부파일 리스트에 attachments_3 객체 업음");
    }

    @Test
    public void 포스트_읽기() throws Exception {
        //given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_01")
                .contents("포스트_내용_01")
                .build();
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12121L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11112L)
                .build());
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);
        Post post = Post.createPost(postRequestDto, account, attachmentsList);
        Post savePost = postRepository.save(post);

        //when
        Post findPost = postRepository.findById(savePost.getId()).get();

        //then
        assertEquals(savePost, findPost, "포스트 객체 다름");
    }

    @Test
    public void 포스트_삭제() throws Exception {
        //given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_01")
                .contents("포스트_내용_01")
                .build();
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12123L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11113L)
                .build());
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);
        Post post = Post.createPost(postRequestDto, account, attachmentsList);
        postRepository.save(post);

        //when
        postRepository.delete(post);
        Optional<Post> findId = postRepository.findById(post.getId());

        //then
        assertTrue(findId.isEmpty(), "포스트 객체 존재함");
    }

    @Test
    public void 커스텀_조회쿼리() throws Exception {
        //given
        PostRequestDto firstPostRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_01")
                .contents("포스트_내용_01")
                .build();
        Account account = accountRepository.findById("admin").get();
        List<Attachments> firstAttachmentsList = new ArrayList<>();
        Attachments firstAttachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12121L)
                .build());
        Attachments firstAttachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11112L)
                .build());
        firstAttachmentsList.add(firstAttachments_1);
        firstAttachmentsList.add(firstAttachments_2);
        Post firstPost = Post.createPost(firstPostRequestDto, account, firstAttachmentsList);
        Post firstSavePost = postRepository.save(firstPost);

        PostRequestDto secondPostRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_02")
                .contents("포스트_내용_02")
                .build();
        List<Attachments> secondAttachmentsList = new ArrayList<>();
        Attachments secondAttachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12121L)
                .build());
        Attachments secondAttachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11112L)
                .build());
        secondAttachmentsList.add(secondAttachments_1);
        secondAttachmentsList.add(secondAttachments_2);
        Post secondPost = Post.createPost(secondPostRequestDto, account, secondAttachmentsList);
        Post secondSavePost = postRepository.save(secondPost);

        PostSearchDto titleSearchDto = PostSearchDto.builder()
                .title("포스트_타이틀")
                .build();
        PostSearchDto authorSearchDto = PostSearchDto.builder()
                .author("admin")
                .build();
        PostSearchDto regDateSearchDto = PostSearchDto.builder()
                .regDateDesc("2021-07-01")
                .build();
        //when
        List<PostResponseDto> findTitleList = postRepository.search(titleSearchDto);
        List<PostResponseDto> findAuthorList = postRepository.search(authorSearchDto);
        List<PostResponseDto> findRegDateList = postRepository.search(regDateSearchDto);

        //then
        assertEquals(findTitleList.size(), 2, "검색된 리스트 개수 불일치");
        assertEquals(findAuthorList.size(), 2, "검색된 리스트 개수 불일치");
        assertEquals(findRegDateList.size(), 2, "검색된 리스트 개수 불일치");
        assertEquals(findRegDateList.get(1).getTitle(), secondPostRequestDto.getTitle(), "검색된 리스트 정렬순서 기대값 불일치");
    }

    @Test
    public void 커스텀_조회쿼리_카운트동시() throws Exception {
        //given
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12121L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11112L)
                .build());
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);
        for (int i=0; i<20; i++) {
            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("포스트_타이틀_"+i)
                    .contents("포스트_내용_"+i)
                    .build();
            Post post = Post.createPost(postRequestDto, account, attachmentsList);
            postRepository.save(post);
        }

        PostSearchDto titleSearchDto = PostSearchDto.builder()
                .title("포스트_타이틀")
                .build();
        PostSearchDto oneSearchDto = PostSearchDto.builder()
                .title("포스트_타이틀_11")
                .build();
        PostSearchDto authorSearchDto = PostSearchDto.builder()
                .author("admin")
                .build();
        PostSearchDto regDateSearchDto = PostSearchDto.builder()
                .regDateDesc("2021-07-01")
                .build();
        PostSearchDto emptySearchDto = PostSearchDto.builder().build();

        //when
        int size = 5;
        Page<PostResponseDto> findSimpleResult_1 = postRepository.searchPaginationSimple(titleSearchDto, PageRequest.of(1, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        Page<PostResponseDto> findSimpleResult_2 = postRepository.searchPaginationSimple(oneSearchDto, PageRequest.of(0, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        Page<PostResponseDto> findEmptySimpleResult = postRepository.searchPaginationSimple(emptySearchDto, PageRequest.of(1, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        List<Post> findAll = postRepository.findAll();

        //then
        assertEquals(findSimpleResult_1.getSize(), size, "요청과 사이즈가 불일치");
        assertEquals(findEmptySimpleResult.getTotalElements(), findAll.size(), "데이터 토탈 사이즈가 불일치");
        assertEquals(findEmptySimpleResult.getTotalPages(), findAll.size()/5, "페이지 사이즈 불일치 1");
        assertEquals(findSimpleResult_1.getTotalPages(), findAll.size()/5, "페이지 사이즈 불일치 2");
        assertEquals(findSimpleResult_2.getTotalElements(), 1, "1개만 검색되었을때 총 데이터 수 오류");
        assertEquals(findSimpleResult_2.getTotalPages(), 1, "1개만 검색되었을때 페이지 수 오류");
    }

    @Test
    public void 커스텀_조회쿼리_카운트별도() {
        //given
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12121L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11112L)
                .build());
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);
        for (int i=0; i<20; i++) {
            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("포스트_타이틀_"+i)
                    .contents("포스트_내용_"+i)
                    .build();
            Post post = Post.createPost(postRequestDto, account, attachmentsList);
            postRepository.save(post);
        }

        PostSearchDto titleSearchDto = PostSearchDto.builder()
                .title("포스트_타이틀")
                .build();
        PostSearchDto oneSearchDto = PostSearchDto.builder()
                .title("포스트_타이틀_11")
                .build();
        PostSearchDto authorSearchDto = PostSearchDto.builder()
                .author("admin")
                .build();
        PostSearchDto regDateSearchDto = PostSearchDto.builder()
                .regDateDesc("2021-07-01")
                .build();
        PostSearchDto emptySearchDto = PostSearchDto.builder().build();

        //when
        int size = 5;
        Page<PostResponseDto> findSimpleResult_1 = postRepository.searchPaginationComplex(titleSearchDto, PageRequest.of(1, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        Page<PostResponseDto> findSimpleResult_2 = postRepository.searchPaginationComplex(oneSearchDto, PageRequest.of(0, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        Page<PostResponseDto> findEmptySimpleResult = postRepository.searchPaginationComplex(emptySearchDto, PageRequest.of(1, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        List<Post> findAll = postRepository.findAll();

        //then
        assertEquals(findSimpleResult_1.getSize(), size, "요청과 사이즈가 불일치");
        assertEquals(findEmptySimpleResult.getTotalElements(), findAll.size(), "데이터 토탈 사이즈가 불일치");
        assertEquals(findEmptySimpleResult.getTotalPages(), findAll.size()/5, "페이지 사이즈 불일치 1");
        assertEquals(findSimpleResult_1.getTotalPages(), findAll.size()/5, "페이지 사이즈 불일치 2");
        assertEquals(findSimpleResult_2.getTotalElements(), 1, "1개만 검색되었을때 총 데이터 수 오류");
        assertEquals(findSimpleResult_2.getTotalPages(), 1, "1개만 검색되었을때 페이지 수 오류");
    }

    @Test
    public void 포스트_단건_조회() throws Exception {
        //given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_타이틀_01")
                .contents("포스트_내용_01")
                .build();
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = new ArrayList<>();
        Attachments attachments_1 = attachmentsRepository.save(Attachments.builder()
                .path("경로_1")
                .origin("첨부파일명_오리진_1")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(12121L)
                .build());
        Attachments attachments_2 = attachmentsRepository.save(Attachments.builder()
                .path("경로_2")
                .origin("첨부파일명_오리진_2")
                .name(String.valueOf(System.currentTimeMillis()))
                .size(11112L)
                .build());
        attachmentsList.add(attachments_1);
        attachmentsList.add(attachments_2);
        Post post = Post.createPost(postRequestDto, account, attachmentsList);
        Post savePost = postRepository.save(post);

        //when
        PostResponseDto findDto = postRepository.findByIdToDto(savePost.getId());

        //then
        assertEquals(savePost.getId(), findDto.getId(), "포스트 객체 다름");
        assertEquals(savePost.getAttachmentsList().size(), findDto.getAttachmentsList().size(), "포스트 내의 첨부파일 갯수 불일치");
    }*/
}