package com.portfolio.blog.post.infra;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    AccountRepository accountRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void 포스트_CRUD_테스트() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("포스트_제목_001")
                .contents("포스트_내용_001")
                .viewCount(0)
                .build();
        Post post2 = Post.builder()
                .title("포스트_제목_002")
                .contents("포스트_내용_002")
                .viewCount(0)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);

        //when
        Post findPost1 = postRepository.findById(post1.getId()).get();
        Post findPost2 = postRepository.findById(post2.getId()).get();
        List<Post> all = postRepository.findAll();
        long count = postRepository.count();
        postRepository.delete(post1);
        postRepository.delete(post2);
        long deleteCount = postRepository.count();

        //then
        assertThat(findPost1).isEqualTo(post1);
        assertThat(findPost2).isEqualTo(post2);
        assertThat(all.size()).isEqualTo(2);
        assertThat(count).isEqualTo(2);
        assertThat(deleteCount).isEqualTo(0);
    }

}