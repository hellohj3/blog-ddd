package com.portfolio.blog.post.ui;

import com.portfolio.blog.post.application.service.PostService;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.ui.dto.PostRequestDto;
import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.reflections.Reflections.log;

@Controller
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @GetMapping("/admin/posts")
    public String postList(PostSearchDto postSearchDto, Model model,
                       @PageableDefault(page = 0, size = 5, sort = "CREATED_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.findPosts(postSearchDto, pageable);
        model.addAttribute("posts", posts);

        return "admin/post/list";
    }

    @GetMapping("/admin/post")
    public String postForm(Model model) {
        model.addAttribute("post"
                , PostRequestDto.builder()
                .title("")
                .contents("")
                .attachmentsList(new ArrayList<>())
                .build());

        return "admin/post/create";
    }

    @PostMapping("/admin/post")
    public String postCreate(Model model, @Valid PostRequestDto postRequestDto) {


        return "redirect:/admin/posts";
    }

    @PostMapping("/admin/attachment")
    public List<String> attachmentCreate(
            @RequestParam(value = "objectType", defaultValue = "-1", required = false) Integer objectType,
            @RequestParam(value = "objectId", defaultValue = "-1", required = false) Long objectId,
            @RequestParam(value = "attachmentsId", defaultValue = "0", required = false) Long attachmentsId,
            @RequestHeader MultiValueMap<String, String> header, MultipartHttpServletRequest request) throws Exception {
        List<String> attachmentList = new ArrayList<String>();
        Iterator<String> names = request.getFileNames();

        while (names.hasNext()) {
            String fileName = names.next();
            MultipartFile file = request.getFile(fileName);
            Attachments attachments = upload(objectType, objectId, attachmentsId, file);

            attachmentList.add(attachments.getPath()+attachments.getName());
        }
        
        return attachmentList;
    }

    private Attachments upload(Integer objectType, Long objectId, Long attachmentsId, MultipartFile file) {
        Attachments attachments = Attachments.builder().build();
        
        if (attachmentsId > 0) {
            a
        }
    }
}
