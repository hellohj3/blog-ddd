package com.portfolio.blog.account.infra;

import com.portfolio.blog.account.domain.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {
    Optional<RoleHierarchy> findByChildName(String childName);
}
