package com.portfolio.blog.account.infra.query;

import com.portfolio.blog.account.domain.Account;

import java.util.List;

public interface AccountRepositoryQuery {
    List<Account> findAccountAndRolesByAccountId(String accountId);
}
