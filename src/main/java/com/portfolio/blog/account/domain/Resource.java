package com.portfolio.blog.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Resource {

    @Id @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer desc;

    @OneToMany(mappedBy = "resource")
    private List<RoleResource> roleResources;

    @Builder
    public Resource(Long id, String name, String type, Integer desc) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.desc = desc;
    }

}
