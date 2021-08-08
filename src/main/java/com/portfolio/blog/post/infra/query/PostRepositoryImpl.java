package com.portfolio.blog.post.infra.query;

import com.nimbusds.jose.util.IntegerUtils;
import com.portfolio.blog.post.domain.Post;
import com.portfolio.blog.post.domain.QPost;
import com.portfolio.blog.post.ui.dto.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.portfolio.blog.account.domain.QAccount.account;
import static com.portfolio.blog.post.domain.QAttachments.attachments;
import static com.portfolio.blog.post.domain.QPost.*;
import static com.portfolio.blog.post.domain.QPost.post;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;

public class PostRepositoryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 리스트 조회 (검색)
    @Override
    public List<PostDto> search(PostSearchDto postSearchDto) {
        return queryFactory
                .select(new QPostDto(
                        post.id,
                        post.account.accountId,
                        post.title,
                        post.contents,
                        post.viewCount,
                        post.createdDate,
                        post.modifiedDate
                ))
                .from(post)
                .limit(postSearchDto.getLimit())
                .leftJoin(post.account, account)
                .fetch();
    }

    // 리스트 페이징 (카운트 동시)
    @Override
    public Page<PostDto> searchPaginationSimple(PostSearchDto postSearchDto, Pageable pageable) {
        QueryResults<PostDto> results = queryFactory
                .select(new QPostDto(
                        post.id,
                        post.account.accountId,
                        post.title,
                        post.contents,
                        post.viewCount,
                        post.createdDate,
                        post.modifiedDate
                ))
                .from(post)
                .leftJoin(post.account, account)
                .where(
                        titleContains(postSearchDto.getTitle()),
                        authorEq(postSearchDto.getAuthor()))
                .orderBy(sortingType(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<PostDto> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }

    // 리스트 페이징 (카운트 별도)
    @Override
    public Page<PostDto> searchPaginationComplex(PostSearchDto postSearchDto, Pageable pageable) {
        List<PostDto> contents = queryFactory
                .select(new QPostDto(
                        post.id,
                        post.account.accountId,
                        post.title,
                        post.contents,
                        post.viewCount,
                        post.createdDate,
                        post.modifiedDate
                ))
                .from(post)
                .leftJoin(post.account, account)
                .where(
                        titleContains(postSearchDto.getTitle()),
                        authorEq(postSearchDto.getAuthor())
                )
                .orderBy(sortingType(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        titleContains(postSearchDto.getTitle()),
                        authorEq(postSearchDto.getAuthor()));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    // 단건조회
    @Override
    public PostDto findByIdToDto(Long id) {
        Post post = queryFactory
                .selectFrom(QPost.post)
                .leftJoin(QPost.post.account, account)
                .leftJoin(QPost.post.attachmentsList, attachments)
                .fetchJoin()
                .where(QPost.post.id.eq(id))
                .fetchOne();

        PostDto postDto = post.parseDto();
        postDto.setAttachmentsList(
                post.getAttachmentsList().stream().map(a -> a.parseDto()).collect(Collectors.toList())
        );

        return postDto;
    }

    // 동적 쿼리 (제목)
    private BooleanExpression titleContains(String title) {
        return hasText(title) ? post.title.contains(title) : null;
    }

    // 동적 쿼리 (작성자)
    private BooleanExpression authorEq(String author) {
        return hasText(author) ? post.account.accountId.eq(author) : null;
    }

    // 동적 쿼리 (정렬)
    private List<OrderSpecifier> sortingType(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "CREATED_DATE":
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

    // 정렬 컬럼 선택
    private OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }
}
