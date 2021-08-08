package com.portfolio.blog.account.domain;

import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.domain.Reple;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Account {

    @Id
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer age;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Reple> reples = new ArrayList<>();

}
