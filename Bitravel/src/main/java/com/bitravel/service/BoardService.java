package com.bitravel.service;

import java.util.List;
import java.util.Optional;

import com.bitravel.data.entity.Board;
 
public interface BoardService {
    /**
     * 글 목록 조회
     * @return
     */
    public List<Board> selectBoardList();
     
    /**
     * 글 읽기
     * @param bid
     * @return
     */
    public Optional<Board> selectBoard(Long bid);
     
    /**
     * 글 작성
     * @param board
     */
    public void insertBoard(Board board);
     
    /**
     * 글 수정
     * @param board
     */
    public void updateBoard(Board board);
     
    /**
     * 글 삭제
     * @param bid
     */
    public void deleteBoard(Long bid);
}