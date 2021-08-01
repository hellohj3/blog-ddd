package com.portfolio.blog.post.application.service;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final AttachmentsRepository attachmentsRepository;

    // 포스트 저장
    @Transactional
    public Long savePost(PostDto postDto) throws Exception {
        Account account =
                accountRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(() -> new InternalAuthenticationServiceException("계정정보 없음"));
        LinkedList<AttachmentsDto> attachmentsDtos = postDto.getAttachmentsList();
        LinkedList<Attachments> attachmentsList = new LinkedList<>();

        for (AttachmentsDto attachmentsDto : attachmentsDtos) {
            Optional<Attachments> findByOptional = attachmentsRepository.findById(attachmentsDto.getId());
            Attachments findByAttachments = findByOptional.orElseThrow(() -> new IllegalArgumentException("첨부파일 존재하지 않음"));
            attachmentsList.add(findByAttachments);
        }
        Post savePost = postRepository.save(Post.createPost(postDto, account, attachmentsList));

        return (ObjectUtils.isEmpty(savePost)) ? 0L : savePost.getId();
    }

    // 포스트 수정
    @Transactional
    public void updatePost(PostDto postDto, Long post_id) throws Exception {
        Optional<Post> findByOptional = postRepository.findById(post_id);

        if (findByOptional.isPresent()) {
            Post findPost = findByOptional.get();
            findPost.updatePost(postDto);
        }
    }

    // 포스트 단건조회
    public PostDto findPost(Long id) {
        return postRepository.findByIdToDto(id);
    }

    // 포스트 리스트 - 페이징
    public Page<PostDto> findPosts(PostSearchDto postSearchDto, Pageable pageable) {
        return postRepository.searchPaginationSimple(postSearchDto, pageable);
    }

    // 포스트 삭제
    public String deletePost(Long id) {
        Optional<Post> findByOptional = postRepository.findById(id);

        if (findByOptional.isPresent()) {
            postRepository.delete(findByOptional.get());
            return "success";
        } else {
            return "fail";
        }
    }
}
