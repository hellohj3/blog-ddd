package com.portfolio.blog.account.infra;

import com.portfolio.blog.account.domain.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {
}
