package com.portfolio.blog.account.infra.query;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.domain.QAccount;
import com.portfolio.blog.account.domain.QRole;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.portfolio.blog.account.domain.QAccount.account;
import static com.portfolio.blog.account.domain.QRole.role;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Account> findAccountAndRolesByAccountId(String accountId) {
        return queryFactory
                .selectFrom(account).distinct()
                .join(account.role, role).fetchJoin()
                .where(account.accountId.eq(accountId))
                .fetch();
    }
}
