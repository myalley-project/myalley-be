package com.myalley.mate.repository;

import com.myalley.mate.dto.response.MyMateBookmarkResponse;
import com.myalley.mate.dto.response.QMyMateBookmarkResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.myalley.mate.domain.QMateBookmark.mateBookmark;
import static com.myalley.exhibition.domain.QExhibition.exhibition;
import static com.myalley.mate.domain.QMate.mate;

@RequiredArgsConstructor
public class MateBookmarkRepositoryImpl implements MateBookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MyMateBookmarkResponse> findMateBookmarkByMember(Long memberId, Pageable pageable) {
        List<MyMateBookmarkResponse> mates = findByMember(memberId, pageable);

        JPAQuery<Long> countQuery = queryFactory
                .select(mateBookmark.count())
                .from(mateBookmark)
                .innerJoin(mateBookmark.mate, mate)
                .where(
                        mateBookmark.member.memberId.eq(memberId),
                        mateBookmark.isBookmarked.eq(true),
                        mateBookmark.mate.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(mates, pageable, countQuery::fetchOne);
    }

    List<MyMateBookmarkResponse> findByMember(Long memberId, Pageable pageable) {
        List<MyMateBookmarkResponse> mates = queryFactory
                .select(new QMyMateBookmarkResponse(
                        mateBookmark.mate.id,
                        mateBookmark.mate.title,
                        mateBookmark.mate.status,
                        mateBookmark.mate.mateGender,
                        mateBookmark.mate.mateAge,
                        mateBookmark.mate.availableDate,
                        mateBookmark.mate.viewCount,
                        mateBookmark.mate.createdAt,
                        mateBookmark.mate.exhibition.id.as("exhibition_id"),
                        mateBookmark.mate.exhibition.title,
                        mateBookmark.mate.exhibition.posterUrl,
                        mateBookmark.mate.exhibition.space,
                        mateBookmark.mate.exhibition.status
                ))
                .from(mateBookmark)
                .innerJoin(mateBookmark.mate.exhibition, exhibition)
                .where(
                        mateBookmark.member.memberId.eq(memberId),
                        mateBookmark.isBookmarked.eq(true),
                        mateBookmark.mate.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, mateBookmark.mate.id))
                .fetch();

        return mates;
    }
}
