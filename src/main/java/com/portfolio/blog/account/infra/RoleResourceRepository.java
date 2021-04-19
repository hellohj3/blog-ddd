package com.portfolio.blog.account.infra;

import com.portfolio.blog.account.domain.Resource;
import com.portfolio.blog.account.domain.Role;
import com.portfolio.blog.account.domain.RoleResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleResourceRepository extends JpaRepository<RoleResource, Long> {
    Optional<RoleResource> findByRoleAndResource(Role role, Resource resource);
}
