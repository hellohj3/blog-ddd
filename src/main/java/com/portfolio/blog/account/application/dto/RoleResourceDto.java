package com.portfolio.blog.account.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class RoleResourceDto {

    private Integer desc;
    private String roleName;
    private String resourceName;

    @QueryProjection
    public RoleResourceDto(Integer desc, String roleName, String resourceName) {
        this.desc = desc;
        this.roleName = roleName;
        this.resourceName = resourceName;
    }

}
