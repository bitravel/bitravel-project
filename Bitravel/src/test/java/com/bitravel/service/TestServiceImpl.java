package com.bitravel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.entity.Test;
import com.bitravel.data.repository.TestRepository;
 
@Service
public class TestServiceImpl implements TestService {
 
    @Autowired
    TestRepository testRepository;
     
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<Test> selectTestList() {
        return (List<Test>) testRepository.findAll();
    }
 
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Optional<Test> selectTest(Long uid) {
        return testRepository.findById(uid);
    }
 
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void insertTest(Test test) {
        testRepository.save(test);
    }
 
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateTest(Test test) {
        testRepository.save(test);
    }
 
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteTest(Long uid) {
        testRepository.deleteById(uid);
 
    }
}