package com.portfolio.blog.post.ui;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.infra.AttachmentsRepository;
import com.portfolio.blog.post.infra.PostRepository;
import com.portfolio.blog.post.ui.dto.PostRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @BeforeEach
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
        Model model;

        //when
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //then
        mockMvc.perform(post("/posts").params())
    }
}