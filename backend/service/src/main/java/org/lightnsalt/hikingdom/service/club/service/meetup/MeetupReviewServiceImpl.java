package org.lightnsalt.hikingdom.service.club.service.meetup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.lightnsalt.hikingdom.common.error.ErrorCode;
import org.lightnsalt.hikingdom.common.error.GlobalException;
import org.lightnsalt.hikingdom.service.club.dto.request.MeetupReviewReq;
import org.lightnsalt.hikingdom.service.club.dto.response.meetup.MeetupReviewRes;
import org.lightnsalt.hikingdom.domain.entity.club.Club;
import org.lightnsalt.hikingdom.domain.entity.club.meetup.Meetup;
import org.lightnsalt.hikingdom.domain.entity.club.meetup.MeetupReview;
import org.lightnsalt.hikingdom.service.club.repository.ClubMemberRepository;
import org.lightnsalt.hikingdom.service.club.repository.ClubRepository;
import org.lightnsalt.hikingdom.service.club.repository.meetup.MeetupMemberRepository;
import org.lightnsalt.hikingdom.service.club.repository.meetup.MeetupRepository;
import org.lightnsalt.hikingdom.service.club.repository.meetup.MeetupReviewRepository;
import org.lightnsalt.hikingdom.domain.entity.member.Member;
import org.lightnsalt.hikingdom.service.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetupReviewServiceImpl implements MeetupReviewService {
	private final ClubRepository clubRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final MeetupRepository meetupRepository;
	private final MeetupMemberRepository meetupMemberRepository;
	private final MeetupReviewRepository meetupReviewRepository;
	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public Long addMeetupReview(String email, Long clubId, Long meetupId, MeetupReviewReq meetupReviewReq) {
		final Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED));
		final Club club = clubRepository.findById(clubId)
			.orElseThrow(() -> new GlobalException(ErrorCode.CLUB_NOT_FOUND));
		final Meetup meetup = meetupRepository.findById(meetupId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEETUP_NOT_FOUND));

		// 일정 참여 여부 확인
		if (!meetupMemberRepository.existsByMeetupIdAndMemberId(meetupId, member.getId()))
			throw new GlobalException(ErrorCode.MEETUP_MEMBER_UNAUTHORIZED);

		MeetupReview meetupReview = MeetupReview.builder()
			.club(club)
			.meetup(meetup)
			.member(member)
			.content(meetupReviewReq.getContent())
			.build();

		meetupReviewRepository.save(meetupReview);

		return meetupReview.getId();
	}

	@Transactional
	@Override
	public void removeMeetupReview(String email, Long clubId, Long meetupId, Long reviewId) {
		final Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED));
		final MeetupReview meetupReview = meetupReviewRepository.findById(reviewId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEETUP_REVIEW_NOT_FOUND));

		// 일정 후기 작성자 여부 확인
		if (!meetupReview.getMember().equals(member))
			throw new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED);

		// 정상 삭제 처리 여부 확인
		if (!deleteMeetupReview(reviewId))
			throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	@Transactional
	@Override
	public List<MeetupReviewRes> findMeetupReviewList(String email, Long clubId, Long meetupId) {
		final Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED));

		// 소모임 가입 여부 확인
		if (clubMemberRepository.findByClubIdAndMemberId(clubId, member.getId()).isEmpty())
			throw new GlobalException(ErrorCode.CLUB_MEMBER_UNAUTHORIZED);

		return meetupReviewRepository.findByMeetupId(meetupId)
			.stream()
			.map(MeetupReviewRes::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public boolean deleteMeetupReview(Long reviewId) {
		return meetupReviewRepository.updateMeetupReviewIsDeletedById(reviewId, true, LocalDateTime.now()) > 0;
	}
}
