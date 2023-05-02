package org.lightnsalt.hikingdom.domain.info.repository;

import static org.lightnsalt.hikingdom.domain.info.entity.QBaseAddressInfo.*;

import java.util.List;

import org.lightnsalt.hikingdom.domain.info.entity.BaseAddressInfo;
import org.lightnsalt.hikingdom.domain.info.entity.QBaseAddressInfo;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BaseAddressInfoRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	public List<BaseAddressInfo> selectGugunList(String dongCode) {
		QBaseAddressInfo qBaseAddressInfo = baseAddressInfo;
		BooleanExpression predicate;

		if (dongCode.equals("3611000000")) {
			predicate = qBaseAddressInfo.dongCode.eq(dongCode);
		} else {
			String likeExpression = String.format("%s___00000", dongCode.substring(0, 2));
			predicate = qBaseAddressInfo.dongCode.like(likeExpression)
				.and(qBaseAddressInfo.dongCode.notLike("__00000000"));
		}

		return jpaQueryFactory.selectFrom(baseAddressInfo)
			.where(predicate)
			.orderBy(baseAddressInfo.gugunName.asc())
			.fetch();
	}
}