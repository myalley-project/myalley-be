package com.myalley.exhibition.repository;

import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.QExhibitionBasicResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.myalley.exhibition.domain.QExhibition.exhibition;
import static com.myalley.exhibition.domain.QExhibitionBookmark.exhibitionBookmark;

@RequiredArgsConstructor
public class ExhibitionBookmarkRepositoryImpl implements ExhibitionBookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ExhibitionBasicResponse> findExhibitionBookmarkByMember(Long memberId, Pageable pageable) {
        List<ExhibitionBasicResponse> exhibitions = findByMember(memberId, pageable);

        JPAQuery<Long> countQuery = queryFactory
                .select(exhibitionBookmark.count())
                .from(exhibitionBookmark)
                .innerJoin(exhibitionBookmark.exhibition, exhibition)
                .where(
                        exhibitionBookmark.member.memberId.eq(memberId),
                        exhibitionBookmark.isBookmarked.eq(true),
                        exhibitionBookmark.exhibition.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(exhibitions, pageable, countQuery::fetchOne);
    }

    List<ExhibitionBasicResponse> findByMember(Long memberId, Pageable pageable) {
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
                .from(exhibitionBookmark)
                .innerJoin(exhibitionBookmark.exhibition, exhibition)
                .where(
                        exhibitionBookmark.member.memberId.eq(memberId),
                        exhibitionBookmark.isBookmarked.eq(true),
                        exhibitionBookmark.exhibition.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier<>(Order.DESC, exhibition.id))
                .fetch();

        return exhibitions;
    }

}
