package com.myalley.exhibition.repository;

import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.QExhibitionBasicResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.myalley.exhibition.domain.QExhibition.exhibition;

@RequiredArgsConstructor
public class ExhibitionRepositoryImpl implements ExhibitionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ExhibitionBasicResponse> findPagedExhibitions(String status, String type, String title, Pageable pageable) {
        List<ExhibitionBasicResponse> exhibitions = getExhibitionResponses(status, type, title, pageable);

        JPAQuery<Long> countQuery = queryFactory
                .select(exhibition.count())
                .from(exhibition)
                .where(
                        eqStatus(status),
                        eqType(type),
                        eqTitle(title),
                        exhibition.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(exhibitions, pageable, countQuery::fetchOne);
    }

    private List<ExhibitionBasicResponse> getExhibitionResponses(String status, String type, String title, Pageable pageable) {
        List<ExhibitionBasicResponse> exhibitions = queryFactory
                .select(new QExhibitionBasicResponse(
                        exhibition.id,
                        exhibition.title,
                        exhibition.space,
                        exhibition.posterUrl,
                        exhibition.duration,
                        exhibition.type,
                        exhibition.status,
                        exhibition.viewCount
                ))
                .from(exhibition)
                .where(
                        eqStatus(status),
                        eqType(type),
                        eqTitle(title),
                        exhibition.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortExhibitionList(pageable))
                .fetch();

        return exhibitions;
    }

    private BooleanExpression eqTitle(String searchTitle) {
        return searchTitle == null ? null : exhibition.title.contains(searchTitle);
    }

    private BooleanExpression eqStatus(String searchStatus) {
        return searchStatus == null ? null : exhibition.status.contains(searchStatus);
    }

    private BooleanExpression eqType(String searchType) {
        return searchType == null ? null : exhibition.type.contains(searchType);
    }

    private OrderSpecifier<?> sortExhibitionList(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "viewCount" :
                        return new OrderSpecifier<>(direction, exhibition.viewCount);
                    case "id" :
                        return new OrderSpecifier<>(direction, exhibition.id);
                }
            }
        }
        return null;
    }

}
