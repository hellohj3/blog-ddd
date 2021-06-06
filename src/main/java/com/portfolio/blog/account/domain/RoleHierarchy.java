package com.portfolio.blog.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
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

    @Builder
    public RoleHierarchy(Long id, String childName) {
        this.id = id;
        this.childName = childName;
    }

    public void changeParentRoleName(RoleHierarchy parentName) {
        this.parentName = parentName;
    }
}
