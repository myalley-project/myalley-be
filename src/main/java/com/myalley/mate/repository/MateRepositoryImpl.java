package com.myalley.mate.repository;

import com.myalley.mate.dto.response.MateMyResponse;
import com.myalley.mate.dto.response.MateSimpleResponse;
import com.myalley.mate.dto.response.QMateMyResponse;
import com.myalley.mate.dto.response.QMateSimpleResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.myalley.mate.domain.QMate.mate;
import static com.myalley.exhibition.domain.QExhibition.exhibition;
import static com.myalley.member.domain.QMember.member;

@RequiredArgsConstructor
public class MateRepositoryImpl implements MateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MateSimpleResponse> findMatesByStatusAndTitle(String status, String title, Pageable pageable) {
        List<MateSimpleResponse> mates = findMateResponses(status, title, pageable);

        JPAQuery<Long> countQuery = queryFactory
                .select(mate.count())
                .from(mate)
                .where(
                        eqStatus(status),
                        eqTitle(title),
                        mate.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(mates, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<MateSimpleResponse> findMatesByExhibitionId(Long exhibitionId, Pageable pageable) {
        List<MateSimpleResponse> mates = findMatesByExhibition(exhibitionId, pageable);

        JPAQuery<Long> countQuery = queryFactory
                .select(mate.count())
                .from(mate)
                .where(
                        mate.exhibition.id.eq(exhibitionId),
                        mate.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(mates, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<MateMyResponse> findMatesByMember(Long memberId, Pageable pageable) {
        List<MateMyResponse> mates = findMatesByMemberId(memberId, pageable);

        JPAQuery<Long> countQuery = queryFactory
                .select(mate.count())
                .from(mate)
                .where(
                        mate.member.memberId.eq(memberId),
                        mate.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(mates, pageable, countQuery::fetchOne);
    }

    List<MateMyResponse> findMatesByMemberId(Long memberId, Pageable pageable) {
        List<MateMyResponse> mates = queryFactory
                .select(new QMateMyResponse(
                        mate.id,
                        mate.title,
                        mate.status,
                        mate.mateGender,
                        mate.mateAge,
                        mate.availableDate,
                        mate.viewCount,
                        mate.createdAt,
                        mate.exhibition.id.as("exhibition_id"),
                        mate.exhibition.title,
                        mate.exhibition.posterUrl,
                        mate.exhibition.space,
                        mate.exhibition.status
                ))
                .from(mate)
                .innerJoin(mate.exhibition, exhibition)
                .where(
                        mate.member.memberId.eq(memberId),
                        mate.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, mate.id))
                .fetch();

        return mates;
    }

    List<MateSimpleResponse> findMatesByExhibition(Long exhibitionId, Pageable pageable) {
        List<MateSimpleResponse> mates = queryFactory
                .select(new QMateSimpleResponse(
                        mate.id,
                        mate.title,
                        mate.status,
                        mate.mateGender,
                        mate.mateAge,
                        mate.availableDate,
                        mate.viewCount,
                        mate.createdAt,
                        mate.exhibition.id.as("exhibition_id"),
                        mate.exhibition.title,
                        mate.exhibition.posterUrl,
                        mate.exhibition.space,
                        mate.exhibition.status,
                        mate.member.memberId.as("member_id"),
                        mate.member.nickname
                ))
                .from(mate)
                .innerJoin(mate.exhibition, exhibition)
                .innerJoin(mate.member, member)
                .where(
                        mate.exhibition.id.eq(exhibitionId),
                        mate.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, mate.id))
                .fetch();

        return mates;
    }

    List<MateSimpleResponse> findMateResponses(String status, String title, Pageable pageable) {
        List<MateSimpleResponse> mates = queryFactory
                .select(new QMateSimpleResponse(
                        mate.id,
                        mate.title,
                        mate.status,
                        mate.mateGender,
                        mate.mateAge,
                        mate.availableDate,
                        mate.viewCount,
                        mate.createdAt,
                        mate.exhibition.id.as("exhibition_id"),
                        mate.exhibition.title,
                        mate.exhibition.posterUrl,
                        mate.exhibition.space,
                        mate.exhibition.status,
                        mate.member.memberId.as("member_id"),
                        mate.member.nickname
                ))
                .from(mate)
                .innerJoin(mate.exhibition, exhibition)
                .innerJoin(mate.member, member)
                .where(
                        eqStatus(status),
                        eqTitle(title),
                        mate.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, mate.id))
                .fetch();

        return mates;
    }

    private BooleanExpression eqStatus(String searchStatus) {
        return searchStatus == null ? null : mate.status.contains(searchStatus);
    }

    private BooleanExpression eqTitle(String searchTitle) {
        return searchTitle == null ? null : mate.title.contains(searchTitle);
    }
}
