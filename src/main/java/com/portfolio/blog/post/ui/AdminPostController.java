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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminPostController {

    @Value("${env.imageLoadSrc}")
    String imageLoadSrc;
    private final PostService postService;
    private final AttachmentsService attachmentsService;

    @GetMapping("/admin/posts")
    public String listPost(PostSearchDto postSearchDto, Model model,
                       @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.findPosts(postSearchDto, pageable);
        CriteriaDto pagination = (posts.isEmpty()) ? CriteriaDto.builder().build()
                : CriteriaDto.builder().page(posts).build();

        model.addAttribute("list", posts.getContent());
        model.addAttribute("pagination", pagination);

        return "admin/post/list";
    }

    @GetMapping("/admin/post")
    public String formPost(Model model) {
        model.addAttribute("post"
                , PostDto.builder()
                .title("")
                .contents("")
                .build());

        return "admin/post/create";
    }

    @PostMapping("/admin/post")
    public String createPost(Model model, @Valid PostDto postDto,
                             @RequestParam("attachmentsIds") String attachmentsIds) throws Exception {
        String[] splitAttachmentsId = attachmentsIds.split(",");
        LinkedList<AttachmentsDto> attachmentsDtos = new LinkedList<>();

        for (String attachmentsId : splitAttachmentsId) {
            attachmentsDtos.add(AttachmentsDto.builder()
                    .id(Long.parseLong(attachmentsId))
                    .build());
        }
        postDto.setAttachmentsList(attachmentsDtos);

        Long savePostId = postService.savePost(postDto);

        return "redirect:/admin/posts";
    }

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
            attachmentsDto.setPath(imageLoadSrc);
            attachmentList.add(attachmentsDto);
        }
        
        return attachmentList;
    }

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
