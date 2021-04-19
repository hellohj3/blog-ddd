package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Role 테이블 정보
 * role_id : bigint(20), role PK
 * name : varchar(255), 권한명
 * desc : varchar(255), 권한설명
 *
 * @author 박상재
 * @version 1.0
 */
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
    private String desc;

    @OneToMany(mappedBy = "role")
    private List<Account> accounts;

    @OneToMany(mappedBy = "role")
    private List<RoleResource> roleResources;

    @Builder
    public Role(Long id, String name, String desc, List<Account> accounts, List<RoleResource> roleResources) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.accounts = accounts;
        this.roleResources = roleResources;
    }

}
