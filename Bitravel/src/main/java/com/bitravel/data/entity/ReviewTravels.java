package com.bitravel.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviewTravels")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewTravels {
	
	// 후기와 여행지 관계 Entity
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_travel_id")
	private Long reviewTravelId;
	
	@ManyToOne
    @JoinColumn(name="travel_id")
    private Travel travel;
	
	@ManyToOne
    @JoinColumn(name="review_id")
    private Review review;
	
	private String travelName;
	private String latitude;
	private String longitude;
	
	@Builder
	public ReviewTravels(Travel travel, Review review, String travelName, String latitude, String longitude) {
		this.travel = travel;
		this.review = review;
		this.travelName = travelName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public void update(Travel travel, Review review) {
    	this.travel = travel;
    	this.review = review;
    }

}
