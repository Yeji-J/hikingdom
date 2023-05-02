package org.lightnsalt.hikingdom.domain.club.repository;

import org.lightnsalt.hikingdom.domain.club.entity.meetup.MeetupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupMemberRepository extends JpaRepository<MeetupMember, Long> {
	int countByMeetupId(Long id);
}
