package com.bitravel.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.TravelCountGender;

// Entity가 하나 추가될 때마다 별개의 Repository class가 필요함
public interface TravelCountGenderRepository extends JpaRepository<TravelCountGender, Long>{
}
