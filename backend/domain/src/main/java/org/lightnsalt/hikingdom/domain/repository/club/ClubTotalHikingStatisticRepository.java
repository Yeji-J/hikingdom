package org.lightnsalt.hikingdom.domain.repository.club;

import java.util.Optional;

import org.lightnsalt.hikingdom.domain.entity.club.record.ClubTotalHikingStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubTotalHikingStatisticRepository extends JpaRepository<ClubTotalHikingStatistic, Long> {
	Optional<ClubTotalHikingStatistic> findById(Long clubId);
}
