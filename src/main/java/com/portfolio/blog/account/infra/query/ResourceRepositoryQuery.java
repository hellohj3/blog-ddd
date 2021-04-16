package com.portfolio.blog.account.infra.query;

import com.portfolio.blog.account.application.dto.RoleResourceDto;

import java.util.List;

public interface ResourceRepositoryQuery {
    List<RoleResourceDto> findAllResourceByType(String type);
}
