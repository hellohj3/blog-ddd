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

    /**
     * ID 에 따른 계정 및 권한정보 조회쿼리
     * fetchOne 의 경우 결과가 여러개일경우 NonUniqueResultException
     * 을 발생시키지만 Account 테이블에서 accountId 는 PK 이므로
     * fetchOne 을 사용해도 예외가 터지지 않을것 같아서 사용
     *
     * @param accountId
     * @return null : 계정 없음 / Account : 계정정보
     */
    @Override
    public Account findAccountAndRolesByAccountId(String accountId) {
        return queryFactory
                .selectFrom(account).distinct()
                .join(account.role, role).fetchJoin()
                .where(account.accountId.eq(accountId))
                .fetchOne();
    }
}
