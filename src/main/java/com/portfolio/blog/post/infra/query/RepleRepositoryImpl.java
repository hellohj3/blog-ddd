package com.portfolio.blog.post.infra.query;

import com.portfolio.blog.account.domain.QAccount;
import com.portfolio.blog.post.domain.QPost;
import com.portfolio.blog.post.domain.Reple;
import com.portfolio.blog.post.domain.QReple;
import com.portfolio.blog.post.ui.dto.RepleDto;
import com.portfolio.blog.post.ui.dto.RepleSearchDto;
import com.portfolio.blog.post.ui.dto.QRepleDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.portfolio.blog.account.domain.QAccount.account;
import static com.portfolio.blog.post.domain.QAttachments.attachments;
import static com.portfolio.blog.post.domain.QPost.post;
import static com.portfolio.blog.post.domain.QReple.*;
import static com.portfolio.blog.post.domain.QReple.reple;
import static org.springframework.util.StringUtils.hasText;

public class RepleRepositoryImpl implements RepleRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public RepleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 리스트 조회 (검색)
    @Override
    public List<RepleDto> search(RepleSearchDto repleSearchDto) {
        return null;
    }

    // 리스트 페이징 (카운트 동시)
    @Override
    public Page<RepleDto> searchPaginationSimple(RepleSearchDto repleSearchDto, Pageable pageable) {
        QueryResults<RepleDto> results = queryFactory
                .select(new QRepleDto(
                        reple.id,
                        reple.post.id,
                        reple.parentReple.id,
                        reple.account.accountId,
                        reple.repleContents,
                        reple.post.title,
                        reple.createdDate,
                        reple.modifiedDate
                ))
                .from(reple)
                .leftJoin(reple.account, account)
                .leftJoin(reple.post, post)
                .where(
                        authorEq(repleSearchDto.getAuthor()),
                        repleContentContains(repleSearchDto.getRepleContent()),
                        reple.parentReple.isNull())
                .orderBy(sortingType(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<RepleDto> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }
    // 포스트 연관관계 리플 리스트 페이징 (카운트 동시)
    @Override
    public Page<RepleDto> searchPaginationSimple(Long postId, Pageable pageable) {
        QueryResults<RepleDto> results = queryFactory
                .select(new QRepleDto(
                        reple.id,
                        reple.post.id,
                        reple.parentReple.id,
                        reple.account.accountId,
                        reple.repleContents,
                        reple.post.title,
                        reple.createdDate,
                        reple.modifiedDate
                ))
                .from(reple)
                .leftJoin(reple.account, account)
                .leftJoin(reple.post, post)
                .where(
                        postIdEq(postId),
                        reple.parentReple.isNull())
                .orderBy(sortingType(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<RepleDto> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }

    // 리스트 (답글)
    @Override
    public List<RepleDto> searchParent(List<Long> ids) {
        return queryFactory
                .select(new QRepleDto(
                        reple.id,
                        reple.post.id,
                        reple.parentReple.id,
                        reple.account.accountId,
                        reple.repleContents,
                        reple.post.title,
                        reple.createdDate,
                        reple.modifiedDate
                ))
                .from(reple)
                .leftJoin(reple.account, account)
                .leftJoin(reple.post, post)
                .where(
                        reple.parentReple.isNotNull(),
                        reple.parentReple.id.in(ids))
                .fetch();
    }

    // 리스트 페이징 (카운트 별도)
    @Override
    public Page<RepleDto> searchPaginationComplex(RepleSearchDto repleSearchDto, Pageable pageable) {
        return null;
    }
    // 단건조회

    @Override
    public RepleDto findByIdToDto(Long id) {
        return queryFactory
                .select(new QRepleDto(
                        reple.id,
                        reple.post.id,
                        reple.parentReple.id,
                        reple.account.accountId,
                        reple.repleContents,
                        reple.post.title,
                        reple.createdDate,
                        reple.modifiedDate
                ))
                .from(reple)
                .leftJoin(reple.account, account)
                .leftJoin(reple.post, post)
                .where(reple.id.eq(id))
                .fetchOne();
    }

    // 동적 쿼리 (작성자)
    private BooleanExpression authorEq(String author) {
        return hasText(author) ? reple.account.accountId.eq(author) : null;
    }

    // 동적 쿼리 (내용)
    private BooleanExpression repleContentContains(String repleContent) {
        return hasText(repleContent) ? reple.repleContents.contains(repleContent) : null;
    }

    // 동적 쿼리 (포스트 아이디)
    private BooleanExpression postIdEq(Long postId) {
        return postId != null ? reple.post.id.eq(postId) : null;
    }

    // 동적 쿼리 (정렬)
    private List<OrderSpecifier> sortingType(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "CREATED_DATE":
                        OrderSpecifier<?> orderId = getSortedColumn(direction, reple.createdDate, "createdDate");
                        orders.add(orderId);
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }

    // 정렬 컬럼 선택
    private OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

}
