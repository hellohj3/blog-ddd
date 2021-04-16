package com.portfolio.blog.account.infra;

import com.portfolio.blog.account.domain.Resource;
import com.portfolio.blog.account.infra.query.ResourceRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long>, ResourceRepositoryQuery {
}
