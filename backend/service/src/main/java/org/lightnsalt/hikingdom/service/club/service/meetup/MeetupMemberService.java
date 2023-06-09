package org.lightnsalt.hikingdom.service.club.service.meetup;

import java.util.List;

import org.lightnsalt.hikingdom.service.club.dto.response.meetup.MeetupMemberDetailListRes;
import org.lightnsalt.hikingdom.service.club.dto.response.meetup.MeetupMemberListRes;

public interface MeetupMemberService {
	void addJoinMeetup(String email, Long clubId, Long meetupId);

	void removeJoinMeetup(String email, Long clubId, Long meetupId);

	List<MeetupMemberDetailListRes> findMeetupMemberDetail(Long clubId, Long meetupId);

	MeetupMemberListRes findMeetupMember(String email, Long clubId, Long meetupId);
}
