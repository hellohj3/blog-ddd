package com.portfolio.blog.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RoleResource {

    @Id @Column(name = "role_resources_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @Builder
    public RoleResource(Long id, Role role, Resource resource) {
        this.id = id;
        this.role = role;
        this.resource = resource;
    }

    public static RoleResource createRoleResource(Role role, Resource resource) {
        return RoleResource.builder()
                .role(role)
                .resource(resource)
                .build();
    }

    public void changeResource(Resource resource) {
        this.resource = resource;
    }

}
