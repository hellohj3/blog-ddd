package com.portfolio.blog.post.application.service;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.domain.Reple;
import com.portfolio.blog.post.infra.PostRepository;
import com.portfolio.blog.post.infra.RepleRepository;
import com.portfolio.blog.post.ui.dto.RepleDto;
import com.portfolio.blog.post.ui.dto.RepleSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RepleService {

    private final PostRepository postRepository;
    private final RepleRepository repleRepository;
    private final AccountRepository accountRepository;

    // 답글 리스트
    public List<RepleDto> findParent(List<Long> ids) {
        return repleRepository.searchParent(ids);
    }

    // 리플 리스트 (페이징)
    public Page<RepleDto> findReples(RepleSearchDto repleSearchDto, Pageable pageable) {
        return repleRepository.searchPaginationSimple(repleSearchDto, pageable);
    }

    // 포스트 연관관계 리플 리스트 (페이징)
    public Page<RepleDto> findReples(Long postId, Pageable pageable) {
        return repleRepository.searchPaginationSimple(postId, pageable);
    }

    // 리플 단건조회
    public RepleDto findReple(Long id) {
        return repleRepository.findByIdToDto(id);
    }

    // 리플 저장
    @Transactional
    public void saveReple(RepleDto repleDto) {
        Account account =
                accountRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(() -> new InternalAuthenticationServiceException("계정정보 없음"));
        Optional<Post> findByOptional = postRepository.findById(repleDto.getPostId());
        Post post = findByOptional.orElseThrow(() -> new IllegalArgumentException("포스트 정보 없음"));

        Reple reple = repleDto.createEntity();
        reple.connectPost(post);
        reple.connectAccount(account);

        if (repleDto.getParentId() != null) {
            Reple parentReple = repleRepository.findById(repleDto.getParentId()).orElseThrow(() -> new IllegalArgumentException("댓글 정보 없음"));
            reple.connectParentReple(parentReple);
        }

        repleRepository.save(reple);
    }

    // 리플 수정
    @Transactional
    public void updateReple(RepleDto repleDto) {
        Reple reple = repleRepository.findById(repleDto.getId()).orElseThrow(() -> new IllegalArgumentException("댓글 정보 없음"));

        reple.updateReple(repleDto);
    }

    // 리플 삭제

    @Transactional
    public String deleteReple(Long id) {
        Optional<Reple> findByOptional = repleRepository.findById(id);

        if (findByOptional.isPresent()) {
            Reple reple = findByOptional.get();

            reple.disConnectPost();
            reple.disConnectAccount();

            repleRepository.delete(reple);
            return "success";
        } else {
            return "fail";
        }
    }
}
