package com.portfolio.blog.account.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Resource 테이블 정보
 * resource_id : bigint(20), resource PK
 * name : text, 리소스 표현식(url 경로, method 경로, ...)
 * type : 리소스 타입(url, method, ...)
 * order_num : 적용 순서(Security는 적용 순서가 중료함)
 *
 * @author 박상재
 * @version 1.0
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Resource {

    @Id @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer orderNumber;

    @OneToMany(mappedBy = "resource")
    private List<RoleResource> roleResources;

    public void addRoleResource(RoleResource roleResource) {
        roleResources.add(roleResource);
        roleResource.changeResource(this);
    }

}
