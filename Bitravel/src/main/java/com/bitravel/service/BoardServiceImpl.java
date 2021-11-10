package com.bitravel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bitravel.data.entity.Board;
import com.bitravel.data.repository.BoardRepository;
 
@Service
public class BoardServiceImpl implements BoardService {
 
    @Autowired
    BoardRepository boardRepository;
     
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<Board> selectBoardList() {
        return (List<Board>) boardRepository.findAll();
    }
 
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Optional<Board> selectBoard(Long bid) {
        return boardRepository.findById(bid);
    }
 
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void insertBoard(Board board) {
        boardRepository.save(board);
    }
 
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateBoard(Board board) {
        boardRepository.save(board);
    }
 
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteBoard(Long bid) {
        boardRepository.deleteById(bid);
 
    }
}