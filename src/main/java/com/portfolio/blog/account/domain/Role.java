package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Role {

    @Id @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer desc;

    @OneToMany(mappedBy = "role")
    private List<Account> accounts;

    @OneToMany(mappedBy = "role")
    private List<RoleResource> roleResources;

    @Builder
    public Role(Long id, String name, Integer desc, List<Account> accounts, List<RoleResource> roleResources) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.accounts = accounts;
        this.roleResources = roleResources;
    }

}
