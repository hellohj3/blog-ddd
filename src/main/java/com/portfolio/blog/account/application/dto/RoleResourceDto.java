package com.portfolio.blog.account.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class RoleResourceDto {

    private Integer orderNumber;
    private String roleName;
    private String resourceName;

    @QueryProjection
    public RoleResourceDto(Integer orderNumber, String roleName, String resourceName) {
        this.orderNumber = orderNumber;
        this.roleName = roleName;
        this.resourceName = resourceName;
    }

}
