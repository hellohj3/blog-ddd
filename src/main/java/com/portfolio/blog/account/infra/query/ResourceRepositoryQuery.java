package com.portfolio.blog.account.infra.query;

import com.portfolio.blog.account.domain.Resource;

import java.util.List;

public interface ResourceRepositoryQuery {
    List<Resource> findAllResourceByType(String type);
}
