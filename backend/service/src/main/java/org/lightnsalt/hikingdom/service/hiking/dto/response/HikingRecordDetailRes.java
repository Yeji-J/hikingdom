package org.lightnsalt.hikingdom.service.hiking.dto.response;

import java.time.LocalDateTime;

import org.lightnsalt.hikingdom.domain.entity.hiking.MemberHiking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HikingRecordDetailRes {
	private String gpsRoute;
	private String mountainName;
	private LocalDateTime startAt;
	private int totalDistance;
	private double maxAlt;
	private int totalDuration;

	public HikingRecordDetailRes(MemberHiking memberHiking) {
		this.gpsRoute = memberHiking.getGpsRoute().getGpsRoute().toString();
		this.mountainName = memberHiking.getMountain().getName();
		this.startAt = memberHiking.getStartAt();
		this.totalDistance = Math.round(memberHiking.getTotalDistance());
		this.maxAlt = memberHiking.getTotalAlt();
		this.totalDuration = Math.round(memberHiking.getTotalDuration());
	}
}