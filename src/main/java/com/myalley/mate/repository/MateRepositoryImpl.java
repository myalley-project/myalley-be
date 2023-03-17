package com.myalley.mate.repository;

import com.myalley.mate.domain.Mate;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.myalley.mate.domain.QMate.mate;

@RequiredArgsConstructor
public class MateRepositoryImpl implements MateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Mate> findMates(String status, String title, Pageable pageable) {
        List<Mate> mates = findMateResponses(status, title, pageable);
        Long count = getCount(status, title);

        return new PageImpl<>(mates, pageable, count);
    }

    List<Mate> findMateResponses(String status, String title, Pageable pageable) {
        List<Mate> mates = queryFactory
                .selectFrom(mate)
                .where(
                        eqStatus(status),
                        eqTitle(title)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, mate.id))
                .fetch();

        return mates;
    }

    private Long getCount(String status, String title) {
        Long count = queryFactory
                .select(mate.count())
                .from(mate)
                .where(
                        eqStatus(status),
                        eqTitle(title)
                )
                .fetchOne();
        return count;
    }

    private BooleanExpression eqStatus(String searchStatus) {
        return searchStatus == null ? null : mate.status.contains(searchStatus);
    }

    private BooleanExpression eqTitle(String searchTitle) {
        return searchTitle == null ? null : mate.title.contains(searchTitle);
    }
}
