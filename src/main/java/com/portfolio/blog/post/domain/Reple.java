package com.portfolio.blog.post.domain;

import com.portfolio.blog.account.domain.Account;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Reple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reple_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reple")
    private Reple parentReple;

    @OneToMany(mappedBy = "parentReple")
    @Column
    private List<Reple> repleList = new ArrayList<>();

}
