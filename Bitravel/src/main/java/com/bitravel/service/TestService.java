package com.bitravel.service;

import java.util.List;
import java.util.Optional;

import com.bitravel.data.entity.Test;
 
public interface TestService {
    /**
     * 사용자 목록 조회
     * @return
     */
    public List<Test> selectTestList();
     
    /**
     * 사용자 조회
     * @param uid
     * @return
     */
    public Optional<Test> selectTest(Long uid);
     
    /**
     * 사용자 등록
     * @param test
     */
    public void insertTest(Test test);
     
    /**
     * 사용자 정보 수정
     * @param test
     */
    public void updateTest(Test test);
     
    /**
     * 사용자 삭제
     * @param uid
     */
    public void deleteTest(Long uid);
}