package com.portfolio.blog.post.application.service;

import com.portfolio.blog.post.infra.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @PersistenceContext
    EntityManager em;
    // TEST 수정 필요 - 210801
    /*@Test
    public void 포스트_등록_서비스() throws Exception {
        //given
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "super",
                        "1234",
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_SUPER"))
                ));
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("서비스_등록_테스트")
                .contents("서비스_등록_테스트")
                .attachmentsList(new ArrayList<>())
                .build();
        MockMultipartFile file1
                = new MockMultipartFile(
                "file1",
                "hello1.jpeg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Hello, World!".getBytes()
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "file2",
                "hello2.jpeg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Hello, World!".getBytes()
        );
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);
        postRequestDto.setAttachmentsList(fileList);

        //when
        Long result = postService.savePost(postRequestDto);

        //then
        assertNotNull(result, "포스트 등록 실패");
    }

    *//**
     * Null 값 인서트 관련 테스트는 repository 테스트 담당
     *
     * @throws Exception
     *//*
    @Test
    public void 포스트_등록_서비스_예외처리() throws Exception {
        //then
        assertThrows(InternalAuthenticationServiceException.class, () -> {
            //given
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            "empty",
                            "1234",
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    ));
            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("포스트_서비스_등록_테스트")
                    .contents("포스트_서비스_등록_테스트")
                    .build();

            //when
            postService.savePost(postRequestDto);
        });
    }

    @Test
    public void 포스트_수정_서비스() throws Exception {
        //given
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "super",
                        "1234",
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_SUPER"))
                ));
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_서비스_등록_테스트")
                .contents("포스트_서비스_등록_테스트")
                .build();
        Long findId = postService.savePost(postRequestDto);

        postRequestDto.setTitle("포스트_서비스_수정_테스트");
        postRequestDto.setContents("포스트_서비스_수정_테스트");

        //when
        postService.updatePost(postRequestDto, findId);
        Post findPost = postRepository.findById(findId).get();

        //then
        assertEquals(findPost.getTitle(), postRequestDto.getTitle(), "포스트 수정 후, 제목 불일치");
    }

    @Test
    public void 포스트_단건조회_서비스() throws Exception {
        //given
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "super",
                        "1234",
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_SUPER"))
                ));

        List<MultipartFile> attachmentsList = new ArrayList<>();
        MockMultipartFile file1 = new MockMultipartFile("file1","test1.txt" , "text/plain" , "hello file".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2","test2.txt" , "text/plain" , "hello file".getBytes());
        attachmentsList.add(file1);
        attachmentsList.add(file2);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_서비스_등록_테스트")
                .contents("포스트_서비스_등록_테스트")
                .attachmentsList(attachmentsList)
                .build();
        Long findId = postService.savePost(postRequestDto);

        //when
        PostResponseDto findDto = postService.findPost(findId);

        //then
        assertEquals(findDto.getId(), findId, "검색 객체 불일치");
        assertEquals(findDto.getAttachmentsList().size(), postRequestDto.getAttachmentsList().size(), "첨부파일 갯수 불일치");
    }

    @Test
    public void 포스트_조회_서비스() throws Exception {
        //given
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "admin",
                        "1234",
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                ));
        for (int i=0; i<20; i++) {
            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("서비스_등록_테스트_"+i)
                    .contents("서비스_등록_테스트_"+i)
                    .build();
            postService.savePost(postRequestDto);
        }

        PostSearchDto titleSearchDto = PostSearchDto.builder()
                .title("서비스_등록_테스트")
                .build();
        PostSearchDto oneSearchDto = PostSearchDto.builder()
                .title("서비스_등록_테스트_11")
                .build();
        PostSearchDto authorSearchDto = PostSearchDto.builder()
                .author("admin")
                .build();
        PostSearchDto regDateSearchDto = PostSearchDto.builder()
                .regDateDesc(LocalDateTime.now().toString())
                .build();
        PostSearchDto emptySearchDto = PostSearchDto.builder().build();

        //when
        int size = 5;
        Page<PostResponseDto> findSimpleResult_1 = postService.findPosts(titleSearchDto, PageRequest.of(1, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        Page<PostResponseDto> findSimpleResult_2 = postService.findPosts(oneSearchDto, PageRequest.of(0, 5 , Sort.Direction.DESC, "CREATED_DATE"));
        Page<PostResponseDto> findEmptySimpleResult = postService.findPosts(emptySearchDto, PageRequest.of(1, 5 , Sort.Direction.DESC, "CREATED_DATE"));

        //then
        assertEquals(findSimpleResult_1.getSize(), size, "요청과 사이즈가 불일치");
        assertEquals(findSimpleResult_2.getTotalElements(), 1, "1개만 검색되었을때 총 데이터 수 오류");
        assertEquals(findSimpleResult_2.getTotalPages(), 1, "1개만 검색되었을때 페이지 수 오류");
    }

    @Test
    public void 포스트_삭제_서비스() throws Exception {
        //given
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "super",
                        "1234",
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_SUPER"))
                ));
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("포스트_서비스_등록_테스트")
                .contents("포스트_서비스_등록_테스트")
                .build();
        MockMultipartFile file1
                = new MockMultipartFile(
                "file1",
                "hello1.jpeg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Hello, World!".getBytes()
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "file2",
                "hello2.jpeg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Hello, World!".getBytes()
        );
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);
        postRequestDto.setAttachmentsList(fileList);
        Long findId = postService.savePost(postRequestDto);

        //when
        String result = postService.deletePost(findId);
        em.flush();
        em.clear();

        //then
        assertEquals(result, "success", "포스트 삭제 실패");
    }*/
}