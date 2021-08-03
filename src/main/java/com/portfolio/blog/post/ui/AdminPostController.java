package com.portfolio.blog.post.ui;

import com.portfolio.blog.common.ui.dto.CriteriaDto;
import com.portfolio.blog.post.application.service.AttachmentsService;
import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.ui.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminPostController {

    @Value("${env.imageLoadBufferSrc}")
    String loadBufferPath;
    @Value("${env.imageLoadSrc}")
    String loadPath;
    private final PostService postService;
    private final AttachmentsService attachmentsService;

    // 리스트
    @GetMapping("/admin/posts")
    public String listPost(PostSearchDto postSearchDto, Model model,
                       @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.findPosts(postSearchDto, pageable);
        CriteriaDto pagination = CriteriaDto.builder().page(posts).build();

        model.addAttribute("list", posts.getContent());
        model.addAttribute("pagination", pagination);

        return "admin/post/list";
    }

    // 등록 폼
    @GetMapping("/admin/post")
    public String formPost(Model model) {
        model.addAttribute("post"
                , PostDto.builder()
                .title("")
                .contents("")
                .build());

        return "admin/post/create";
    }

    // 등록
    @PostMapping("/admin/post")
    public String createPost(Model model, @Valid PostDto postDto,
                             @RequestParam("attachmentsSrcs") String attachmentsSrcs) throws Exception {
        List<String> names = Arrays.asList(attachmentsSrcs.replace(loadBufferPath + "/", "").split(","));
        List<AttachmentsDto> attachmentsDtos = attachmentsService.findAttachmentsByNames(names);
        postDto.setAttachmentsList(attachmentsDtos);
        postDto.setContents(postDto.getContents().replace(loadBufferPath, loadPath));
        postService.savePost(postDto);

        return "redirect:/admin/posts";
    }

    // 수정 폼
    @GetMapping("/admin/post/{id}")
    public String updateFormPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", postService.findPost(id));

        return "admin/post/update";
    }

    // 수정
    @PutMapping("/admin/post/{id}")
    @ResponseBody
    public String updatePost(@PathVariable("id") Long id, @Valid PostDto postDto,
                             @RequestParam("attachmentsSrcs") String attachmentsSrcs) throws Exception {
        List<String> names = Arrays.asList(attachmentsSrcs.replace(loadPath + "/", "").split(","));
        List<AttachmentsDto> findDtos = attachmentsService.findAttachmentsByNames(names);
        postDto.setAttachmentsList(findDtos);
        postDto.setId(id);
        postService.updatePost(postDto);

        return "/admin/posts";
    }

    // 삭제
    @DeleteMapping("/admin/post/{id}")
    @ResponseBody
    public String deletePost(@PathVariable("id") Long id) throws Exception {
        String result = postService.deletePost(id);

        return result;
    }

    // 첨부파일 등록
    @PostMapping("/admin/attachments")
    @ResponseBody
    public List<AttachmentsDto> createAttachments(
            @RequestHeader MultiValueMap<String, String> header, MultipartHttpServletRequest request) throws Exception {
        List<AttachmentsDto> attachmentList = new ArrayList<AttachmentsDto>();
        Iterator<String> names = request.getFileNames();

        while (names.hasNext()) {
            String fileName = names.next();
            MultipartFile file = request.getFile(fileName);
            AttachmentsDto attachmentsDto = upload(file);
            attachmentsDto.setPath(loadBufferPath);     // 에디터에 임시로드 되어야 하므로 버퍼 경로 사용
            attachmentList.add(attachmentsDto);
        }
        
        return attachmentList;
    }

    // 첨부파일 업로드 메소드
    private AttachmentsDto upload(MultipartFile multipartFile) throws Exception {
        String extension = "";

        if (multipartFile.getContentType().contains("image/jpeg"))
            extension = ".jpg";
        else if (multipartFile.getContentType().contains("image/png"))
            extension = ".png";
        else
            throw new IllegalArgumentException("지원하지 않는 확장자");

        return attachmentsService.saveAttachments(multipartFile);
    }

}
