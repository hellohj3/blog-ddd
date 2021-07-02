package com.portfolio.blog.post.infra;

import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.domain.QAttachments;
import com.portfolio.blog.post.ui.dto.PostResponseDto;
import com.portfolio.blog.post.ui.dto.PostSearchDto;
import com.portfolio.blog.post.ui.dto.QPostResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.portfolio.blog.account.domain.QAccount.account;
import static com.portfolio.blog.post.domain.QAttachments.*;
import static com.portfolio.blog.post.domain.QPost.post;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 페이징 없는 리스트 조회
    @Override
    public List<PostResponseDto> search(PostSearchDto postSearchDto) {
        return queryFactory
                .select(new QPostResponseDto(
                        post.id,
                        post.account.accountId,
                        post.title,
                        post.contents
                ))
                .from(post)
                .leftJoin(post.account, account)
                .fetch();
    }

    // 페이징 및 카운트를 동시에 날리는 리스트 조회
    @Override
    public Page<PostResponseDto> searchPaginationSimple(PostSearchDto postSearchDto, Pageable pageable) {
        QueryResults<PostResponseDto> results = queryFactory
                .select(new QPostResponseDto(
                        post.id,
                        post.account.accountId,
                        post.title,
                        post.contents
                ))
                .from(post)
                .leftJoin(post.account, account)
                .where(
                        titleContains(postSearchDto.getTitle()),
                        authorEq(postSearchDto.getAuthor()))
                .orderBy(sortingType(pageable).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<PostResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    // 페이징 및 카운트를 별도로 날리는 리스트 조회
    @Override
    public Page<PostResponseDto> searchPaginationComplex(PostSearchDto postSearchDto, Pageable pageable) {
        List<PostResponseDto> content = queryFactory
                .select(new QPostResponseDto(
                        post.id,
                        post.account.accountId,
                        post.title,
                        post.contents
                ))
                .from(post)
                .leftJoin(post.account, account)
                .where(
                        titleContains(postSearchDto.getTitle()),
                        authorEq(postSearchDto.getAuthor())
                )
                .orderBy(sortingType(pageable).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        titleContains(postSearchDto.getTitle()),
                        authorEq(postSearchDto.getAuthor()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    // 포스트 제목 검색 동적 쿼리
    private BooleanExpression titleContains(String title) {
        return hasText(title) ? post.title.contains(title) : null;
    }

    // 작성자 검색 동적 쿼리
    private BooleanExpression authorEq(String author) {
        return hasText(author) ? post.account.accountId.eq(author) : null;
    }

    // 정렬 선택
    private List<OrderSpecifier> sortingType(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "createdDate":
                        OrderSpecifier<?> orderId = getSortedColumn(direction, post.createdDate, "createdDate");
                        orders.add(orderId);
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }

    private OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }
}
