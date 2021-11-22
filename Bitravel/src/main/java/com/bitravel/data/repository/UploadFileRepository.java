package com.bitravel.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitravel.data.entity.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>{
}
