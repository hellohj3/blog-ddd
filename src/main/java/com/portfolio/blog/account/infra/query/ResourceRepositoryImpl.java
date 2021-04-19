package com.portfolio.blog.account.infra.query;

import com.portfolio.blog.account.application.dto.QRoleResourceDto;
import com.portfolio.blog.account.application.dto.RoleResourceDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.portfolio.blog.account.domain.QResource.resource;
import static com.portfolio.blog.account.domain.QRole.role;
import static com.portfolio.blog.account.domain.QRoleResource.roleResource;

@RequiredArgsConstructor
public class ResourceRepositoryImpl implements ResourceRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoleResourceDto> findAllResourceByType(String type) {
        return queryFactory
                .select(new QRoleResourceDto(
                        resource.orderNumber.as("orderNumber"),
                        role.name.as("roleName"),
                        resource.name.as("resourceName")
                ))
                .from(resource)
                .join(resource.roleResources, roleResource)
                .join(roleResource.role, role)
                .where(resource.type.eq(type))
                .orderBy(resource.orderNumber.asc())
                .fetch();
    }
}
