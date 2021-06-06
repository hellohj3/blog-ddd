package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Account {

    @Id
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Builder
    public Account(String accountId, String password, String name, Integer age, String email, Role role) {
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }
}
