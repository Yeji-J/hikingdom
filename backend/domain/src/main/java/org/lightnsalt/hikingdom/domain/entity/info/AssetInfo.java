package org.lightnsalt.hikingdom.domain.entity.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "asset_info")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssetInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@ToString.Exclude
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mountain_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private MountainInfo mountain;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(name = "asset_url", nullable = false, length = 512)
	private String assetUrl;

	@Column(name = "get_condition", nullable = false, length = 512)
	private String getCondition;

	@Column(name = "score", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
	private Integer score;

	@Builder
	public AssetInfo(MountainInfo mountain, String name, String assetUrl, String getCondition, Integer score) {
		this.mountain = mountain;
		this.name = name;
		this.assetUrl = assetUrl;
		this.getCondition = getCondition;
		this.score = score;
	}
}
