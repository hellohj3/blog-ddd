package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Role {

    @Id @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String roleDesc;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "role")
    private Account account;

    @OneToMany(mappedBy = "role")
    private List<RoleResource> roleResources = new ArrayList<>();

}
