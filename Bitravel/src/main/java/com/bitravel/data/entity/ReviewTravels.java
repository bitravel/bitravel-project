package com.bitravel.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Id;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
	private Long reviewTravelId;
	
	@ManyToOne
    @JoinColumn(name="travel_id")
    private Travel travel;
	
	@ManyToOne
    @JoinColumn(name="review_id")
    private Review review;
		
}
