package com.myalley.exhibition.repository;

import com.myalley.exhibition.domain.Exhibition;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.myalley.exhibition.domain.QExhibition.exhibition;

@RequiredArgsConstructor
public class ExhibitionRepositoryImpl implements ExhibitionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Exhibition> findPagedExhibitions(String status, String type, String title, Pageable pageable) {
        List<Exhibition> exhibitions = getExhibitionResponses(status, type, title, pageable);
        Long count = getCount(status, type, title);

        return new PageImpl<>(exhibitions, pageable, count);
    }

    private Long getCount(String status, String type, String title) {
        Long count = queryFactory
                .select(exhibition.count())
                .from(exhibition)
                .where(
                        eqStatus(status),
                        eqType(type),
                        eqTitle(title)
                )
                .fetchOne();

        return count;
    }

    private List<Exhibition> getExhibitionResponses(String status, String type, String title, Pageable pageable) {
        List<Exhibition> exhibitions = queryFactory
                .selectFrom(exhibition)
                .where(
                        eqStatus(status),
                        eqType(type),
                        eqTitle(title)
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
