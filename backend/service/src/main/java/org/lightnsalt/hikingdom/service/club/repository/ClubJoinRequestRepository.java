package org.lightnsalt.hikingdom.service.club.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.lightnsalt.hikingdom.domain.common.enumType.JoinRequestStatusType;
import org.lightnsalt.hikingdom.domain.entity.club.Club;
import org.lightnsalt.hikingdom.domain.entity.club.ClubJoinRequest;
import org.lightnsalt.hikingdom.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubJoinRequestRepository extends JpaRepository<ClubJoinRequest, Long> {
	Optional<ClubJoinRequest> findByMemberIdAndClubIdAndStatus(@Param("memberId") Long memberId,
		@Param("clubId") Long clubId, @Param("status") JoinRequestStatusType status);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE ClubJoinRequest c SET c.status = :status, c.modifiedAt = :now "
		+ "WHERE c.member = :member AND  c.status = 'PENDING'")
	void updatePendingJoinRequestByMember(@Param("member") Member member, @Param("status") JoinRequestStatusType status,
		@Param("now") LocalDateTime now);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE ClubJoinRequest c SET c.status = :status, c.modifiedAt = :now "
		+ "WHERE c.member = :member AND c.club = :club AND  c.status = 'PENDING'")
	int updatePendingJoinRequestByMemberAndClub(@Param("member") Member member, @Param("club") Club club,
		@Param("status") JoinRequestStatusType status, @Param("now") LocalDateTime now);

	List<ClubJoinRequest> findByClubIdAndStatus(Long clubId, JoinRequestStatusType status);

	List<ClubJoinRequest> findByMemberIdAndStatus(Long memberId, JoinRequestStatusType status);

}
