package org.lightnsalt.hikingdom.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lightnsalt.hikingdom.chat.dto.CustomPage;
import org.lightnsalt.hikingdom.chat.dto.request.ChatReq;
import org.lightnsalt.hikingdom.chat.dto.response.ChatInfoRes;
import org.lightnsalt.hikingdom.chat.dto.response.message.ChatListMessageRes;
import org.lightnsalt.hikingdom.chat.dto.response.MemberInfoRes;
import org.lightnsalt.hikingdom.chat.dto.response.message.ChatMessageRes;
import org.lightnsalt.hikingdom.chat.dto.response.message.MemberListMessageRes;
import org.lightnsalt.hikingdom.chat.dto.response.message.MessageRes;
import org.lightnsalt.hikingdom.chat.entity.Chat;
import org.lightnsalt.hikingdom.chat.repository.mongo.ChatRepository;
import org.lightnsalt.hikingdom.chat.repository.mysql.ClubMemberRepository;
import org.lightnsalt.hikingdom.chat.repository.mysql.ClubRepository;
import org.lightnsalt.hikingdom.common.error.ErrorCode;
import org.lightnsalt.hikingdom.common.error.GlobalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatRepository chatRepository;
	private final ClubRepository clubRepository;
	private final ClubMemberRepository clubMemberRepository;

	@Override
	public MessageRes addChat(ChatReq chatReq) {
		if (!clubRepository.existsById(chatReq.getClubId()))
			throw new GlobalException(ErrorCode.CLUB_NOT_FOUND);

		Chat chat = Chat.builder()
			.memberId(chatReq.getMemberId())
			.clubId(chatReq.getClubId())
			.content(chatReq.getContent())
			.sendAt(LocalDateTime.now())
			.build();

		Chat savedChat = chatRepository.save(chat);
		ChatInfoRes chatInfoRes = new ChatInfoRes(savedChat);

		return new ChatMessageRes(chatInfoRes);
	}

	@Override
	public MessageRes findPrevChat(Long clubId, String chatId, Integer size) {
		if (!clubRepository.existsById(clubId))
			throw new GlobalException(ErrorCode.CLUB_NOT_FOUND);

		Pageable pageable = PageRequest.of(0, size, Sort.by("sendAt").descending());

		Page<Chat> chatPage;
		if (chatId == null) {
			chatPage = chatRepository.findByClubIdOrderBySendAtDesc(clubId, pageable);
		} else {
			Chat chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new GlobalException(ErrorCode.CHAT_NOT_FOUND));
			chatPage = chatRepository.findByClubIdAndSendAtBeforeOrderBySendAtDesc(clubId, chat.getSendAt(),
				pageable);
		}

		CustomPage<ChatInfoRes> chats = new CustomPage<>(
			chatPage.getContent().stream().map(ChatInfoRes::new).collect(Collectors.toList()),
			chatPage.getNumber(), chatPage.getSize(), chatPage.getTotalElements(), chatPage.hasNext());

		return new ChatListMessageRes(chats);
	}

	@Override
	public MessageRes findMember(Long clubId) {
		if (!clubRepository.existsById(clubId))
			throw new GlobalException(ErrorCode.CLUB_NOT_FOUND);

		Map<Long, MemberInfoRes> members = clubMemberRepository.findByClubId(clubId).stream()
			.collect(Collectors.toMap(MemberInfoRes::getMemberId, Function.identity()));

		return new MemberListMessageRes(members);
	}

	@Override
	public MessageRes convertMemberResToMessageRes(List<MemberInfoRes> memberInfoResList) {
		Map<Long, MemberInfoRes> members = memberInfoResList.stream()
			.collect(Collectors.toMap(MemberInfoRes::getMemberId, Function.identity()));

		return new MemberListMessageRes(members);
	}
}
