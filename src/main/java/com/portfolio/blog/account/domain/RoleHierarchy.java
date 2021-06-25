package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class RoleHierarchy {

    @Id @Column(name = "hierarchy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String childName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name")
    private RoleHierarchy parentName;

    @OneToMany(mappedBy = "parentName")
    @Column(nullable = false)
    private Set<RoleHierarchy> roleHierarchy = new HashSet<RoleHierarchy>();

    public void changeParentRoleName(RoleHierarchy parentName) {
        this.parentName = parentName;
    }
}
