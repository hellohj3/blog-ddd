package com.portfolio.blog.account.infra;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.query.AccountRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String>, AccountRepositoryQuery {
}
