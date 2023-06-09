package org.lightnsalt.hikingdom.service.club.repository.record;

import static org.lightnsalt.hikingdom.domain.entity.club.QClub.*;
import static org.lightnsalt.hikingdom.domain.entity.club.record.QClubRanking.*;

import java.time.LocalDate;
import java.util.List;

import org.lightnsalt.hikingdom.service.club.dto.response.ClubSearchRes;
import org.lightnsalt.hikingdom.service.club.repository.ClubSearchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClubRankingRepositoryCustom extends ClubSearchRepository {
	private final JPAQueryFactory queryFactory;

	private Slice<ClubSearchRes> selectClubRankingBySort(LocalDate date, OrderSpecifier<?> orderSpecifier, Long clubId,
		Pageable pageable) {
		List<ClubSearchRes> result = queryFactory
			.select(Projections.constructor(ClubSearchRes.class, club, clubRanking.ranking))
			.from(club)
			.leftJoin(clubRanking).on(club.id.eq(clubRanking.club.id))
			.where(clubRanking.setDate.eq(date),
				club.isDeleted.eq(false),
				isLast(clubId))
			.orderBy(orderSpecifier,
				clubRanking.ranking.asc())
			.limit(pageable.getPageSize() + 1L)
			.fetch();

		return checkLastPage(pageable, result);
	}

	public Slice<ClubSearchRes> sortClubRankingByRanking(LocalDate date, Long clubId, Pageable pageable) {
		return selectClubRankingBySort(date, clubRanking.ranking.asc(), clubId, pageable);
	}

	public Slice<ClubSearchRes> sortClubRankingByMountainCount(LocalDate date, Long clubId, Pageable pageable) {
		return selectClubRankingBySort(date, clubRanking.totalMountainCount.desc(), clubId, pageable);
	}

	public Slice<ClubSearchRes> sortClubRankingByAssetCount(LocalDate date, Long clubId, Pageable pageable) {
		return selectClubRankingBySort(date, clubRanking.totalAssetCount.desc(), clubId, pageable);
	}

	public Slice<ClubSearchRes> sortClubRankingByMemberCount(LocalDate date, Long clubId, Pageable pageable) {
		return selectClubRankingBySort(date, clubRanking.totalMemberCount.desc(), clubId, pageable);
	}
}
