package org.lightnsalt.hikingdom.service.member.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberTokenRes {
	private String accessToken;
	private String refreshToken;
}
