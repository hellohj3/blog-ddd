package com.portfolio.blog.post.ui;

import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.post.infra.AttachmentsRepository;
import com.portfolio.blog.post.infra.PostRepository;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
class PostControllerTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    AttachmentsRepository attachmentsRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Mock
    Model model;
    // TEST 수정 필요 - 210801
    /*@BeforeEach
    public void setUp() {
        Account account = accountRepository.findById("super").get();
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
    }

    @Test
    public void 포스트_생성_컨트롤() throws Exception {
        //given
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "super",
                        "1234",
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_SUPER"))
                ));
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("컨트롤러_등록_테스트")
                .contents("컨트롤러_등록_테스트")
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
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MultiValueMap<String, String> paraMap =new LinkedMultiValueMap<>();
        paraMap.add("title", postRequestDto.getTitle());
        paraMap.add("contents", postRequestDto.getContents());

        //then
        mockMvc.perform(
                multipart("/post")
                .file(file1)
                .file(file2)
                .params(paraMap))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }*/

}