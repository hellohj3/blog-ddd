package com.portfolio.blog.post.application.service;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.common.util.handler.AttachmentsHandler;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.infra.PostRepository;
import com.portfolio.blog.post.infra.PostRepositoryImpl;
import com.portfolio.blog.post.ui.dto.PostRequestDto;
import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final AttachmentsHandler attachmentsHandler;

    // 포스트 저장
    public Long savePost(PostRequestDto postRequestDto) throws Exception {
        // 추 후, 세션벨류로 대체
        Account account = accountRepository.findById("admin").get();
        List<Attachments> attachmentsList = attachmentsHandler.parseAttachments(postRequestDto.getAttachmentsList());
        Post savePost = postRepository.save(Post.createPost(postRequestDto, account, attachmentsList));

        return (ObjectUtils.isEmpty(savePost)) ? 0L : savePost.getId();
    }

    // 포스트 수정
    public Long updatePost(PostRequestDto postRequestDto, Long post_id) throws Exception {
        Long getId = 0L;
        Optional<Post> findByOptional = postRepository.findById(post_id);
        List<Attachments> attachmentsList = attachmentsHandler.parseAttachments(postRequestDto.getAttachmentsList());

        if (findByOptional.isPresent()) {
            Post findPost = findByOptional.get();
            findPost.updatePost(postRequestDto, attachmentsList);
        }

        return getId;
    }

    // 포스트 리스트 - 페이징
    public Page<PostResponseDto> findPosts(PostSearchDto postSearchDto, Pageable pageable) {
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
