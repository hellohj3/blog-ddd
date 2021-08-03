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

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RepleService {

    private final PostRepository postRepository;
    private final RepleRepository repleRepository;
    private final AccountRepository accountRepository;

    // 리플 리스트 (페이징)
    public Page<RepleDto> findReples(RepleSearchDto repleSearchDto, Pageable pageable) {
        return repleRepository.searchPaginationSimple(repleSearchDto, pageable);
    }

    // 리플 저장
    @Transactional
    public void saveReple(RepleDto repleDto) {
        Account account =
                accountRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(() -> new InternalAuthenticationServiceException("계정정보 없음"));
        Optional<Post> findByOptional = postRepository.findById(repleDto.getPostId());
        Post post = findByOptional.orElseThrow(() -> new IllegalArgumentException("포스트 정보 없음"));

        Reple reple = Reple.createEntity(repleDto);
        reple.connectPost(post);
        reple.connectAccount(account);

        repleRepository.save(reple);
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
