package com.portfolio.blog.account.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class RoleResourceDto {

    private Integer orderNumber;
    private String roleName;
    private String resourceName;

}
