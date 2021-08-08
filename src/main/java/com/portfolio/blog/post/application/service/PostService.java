package com.portfolio.blog.post.application.service;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.common.util.handler.AttachmentsHandler;
import com.portfolio.blog.common.util.handler.CustomFilter;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.infra.AttachmentsRepository;
import com.portfolio.blog.post.infra.PostRepository;
import com.portfolio.blog.post.ui.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final AttachmentsHandler attachmentsHandler;
    private final AttachmentsRepository attachmentsRepository;

    // 포스트 리스트 (페이징)
    public Page<PostDto> findPosts(PostSearchDto postSearchDto, Pageable pageable) {
        return postRepository.searchPaginationSimple(postSearchDto, pageable);
    }

    // 포스트 단건조회
    public PostDto findPost(Long id) {
        return postRepository.findByIdToDto(id);
    }

    // 포스트 저장
    @Transactional
    public void savePost(PostDto postDto) throws Exception {
        if (CustomFilter.xssFilter(postDto.getContents())) {
            throw new IllegalArgumentException("XSS 필터링");
        }

        Account account =
                accountRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(() -> new InternalAuthenticationServiceException("계정정보 없음"));
        List<AttachmentsDto> attachmentsDtos = postDto.getAttachmentsList();
        List<Attachments> attachmentsList = new ArrayList<>();

        for (AttachmentsDto attachmentsDto : attachmentsDtos) {
            Optional<Attachments> findByOptional = attachmentsRepository.findById(attachmentsDto.getId());
            Attachments findByAttachments = findByOptional.orElseThrow(() -> new IllegalArgumentException("첨부파일 존재하지 않음"));
            attachmentsList.add(findByAttachments);
        }
        attachmentsHandler.copyFile(attachmentsDtos.stream().map(AttachmentsDto::getName).collect(Collectors.toList()));
        postRepository.save(postDto.createEntity(account, attachmentsList));
    }

    // 포스트 수정
    @Transactional
    public void updatePost(PostDto postDto) throws Exception {
        if (CustomFilter.xssFilter(postDto.getContents())) {
            throw new IllegalArgumentException("XSS 필터링");
        }
        
        Optional<Post> findByOptional = postRepository.findById(postDto.getId());

        if (findByOptional.isPresent()) {
            Post findPost = findByOptional.get();
            List<String> deleteAttachmentsIds = findPost.updatePost(postDto);
            attachmentsHandler.deleteRealFileBulk(deleteAttachmentsIds);
        }
    }

    // 포스트 삭제
    @Transactional
    public String deletePost(Long id) throws Exception {
        Optional<Post> findByOptional = postRepository.findById(id);

        if (findByOptional.isPresent()) {
            Post findPost = findByOptional.get();

            if (findPost.getAttachmentsList().size() > 0) {
                List<Attachments> attachmentsList = findPost.getAttachmentsList();

                for (int i=0; i<attachmentsList.size(); i++) {
                    attachmentsHandler.deleteRealFileSingle(attachmentsList.get(i).getName());
                    attachmentsRepository.delete(attachmentsList.get(i));
                }
            }

            postRepository.delete(findPost);
            return "success";
        } else {
            return "fail";
        }
    }

}
