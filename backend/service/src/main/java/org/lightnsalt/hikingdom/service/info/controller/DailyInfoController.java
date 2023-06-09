package org.lightnsalt.hikingdom.service.info.controller;

import java.util.List;

import org.lightnsalt.hikingdom.common.dto.BaseResponseBody;
import org.lightnsalt.hikingdom.common.dto.CustomResponseBody;
import org.lightnsalt.hikingdom.service.info.dto.response.ClubDailyRes;
import org.lightnsalt.hikingdom.service.info.dto.response.MountainDailyRes;
import org.lightnsalt.hikingdom.service.info.service.DailyInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/info/today")
@RequiredArgsConstructor
public class DailyInfoController {
	private final DailyInfoService dailyInfoService;

	@GetMapping("/mountain")
	public ResponseEntity<CustomResponseBody> dailyMountainDetails() {

		List<MountainDailyRes> result = dailyInfoService.findDailyMountainList();
		return new ResponseEntity<>(BaseResponseBody.of("오늘의 산 조회에 성공했습니다", result), HttpStatus.OK);
	}

	@GetMapping("/club")
	public ResponseEntity<CustomResponseBody> dailyClubDetails() {

		ClubDailyRes result = dailyInfoService.findDailyClub();
		return new ResponseEntity<>(BaseResponseBody.of("오늘의 소모임 조회에 성공했습니다", result), HttpStatus.OK);
	}

}
