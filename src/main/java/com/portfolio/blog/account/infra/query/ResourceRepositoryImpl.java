package com.portfolio.blog.account.infra.query;

import com.portfolio.blog.account.domain.Resource;
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
    public List<Resource> findAllResourceByType(String type) {
        return queryFactory
                .selectFrom(resource)
                .join(resource.roleResources, roleResource).fetchJoin()
                .join(roleResource.role, role).fetchJoin()
                .where(resource.type.eq(type))
                .fetch();
    }
}
