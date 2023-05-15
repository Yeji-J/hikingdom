package org.lightnsalt.hikingdom.chat.controller;

import org.lightnsalt.hikingdom.chat.dto.request.ChatReq;
import org.lightnsalt.hikingdom.chat.dto.response.message.MessageRes;
import org.lightnsalt.hikingdom.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketController {
	private final SimpMessagingTemplate template;
	private final ChatService chatService;

	@MessageMapping("/clubs/{clubId}")
	public void messageSave(@DestinationVariable Long clubId, ChatReq chatReq) {
		log.info("clubId {} ", clubId);
		log.info("chatReq {} ", chatReq);
		MessageRes message = chatService.saveMessage(chatReq);
		log.info("send chat : {} ", message);
		template.convertAndSend("/sub/clubs/" + clubId, message);
	}
}
