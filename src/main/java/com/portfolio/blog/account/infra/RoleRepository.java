package com.portfolio.blog.account.infra;

import com.portfolio.blog.account.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String Name);
}
