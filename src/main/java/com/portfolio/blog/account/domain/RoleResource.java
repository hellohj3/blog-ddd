package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
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
