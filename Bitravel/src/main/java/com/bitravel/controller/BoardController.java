package com.bitravel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bitravel.data.entity.Board;
import com.bitravel.model.ApiResponseMessage;
import com.bitravel.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(value = "/board")
@Api(value = "BoardController")
public class BoardController {
 
    BoardService boardService;
    
    // DB 연결 테스트용 링크 -> 포트번호만 상황에 따라 바꾸어서 사용하세요. http://localhost:8080/swagger-ui/
     
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "글 목록 조회", notes = "글 목록을 조회하는 API.")
    public List<Board> getBoardList(){
        return boardService.selectBoardList();
    }
 
    @RequestMapping(value = "/view/{bid}", method = RequestMethod.GET)
    @ApiOperation(value = "글 내용 조회", notes = "개별 글의 정보를 조회하는 API. Board entity 클래스의 bid값을 기준으로 데이터를 가져온다.")
    public Optional<Board> getBoard( @PathVariable("bid") Long bid ){
        return boardService.selectBoard(bid);
    }
    
    // 다른 method들도 이후 view(template)가 완성되고 나면 return을 string으로 바꾸어 준 뒤, Session에 생성한 객체를 담는 방향으로 모두 변경 예정
    @RequestMapping(value = "/writeView", method = RequestMethod.GET)
    public String showWriteView() {
    	return "WriteView";
    }
    // 글쓰기 페이지에서 user attribute를 session에서 꺼내오면 됨
    
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    @ApiOperation(value = "글 작성", notes = "글 내용을 저장하는 API. Board entity 클래스로 데이터를 저장한다.")
    public ResponseEntity<ApiResponseMessage> insertBoard( Board board ){
        ApiResponseMessage message = new ApiResponseMessage("Success", "등록되었습니다.", "", "");
        ResponseEntity<ApiResponseMessage> response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         
        try {
            boardService.insertBoard(board);
        } catch(Exception ex){
        	log.error("error: "+ex);
            message = new ApiResponseMessage("Failed", "게시물 등록에 실패하였습니다.", "ERROR00001", "Fail to registration for board information.");
            response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;
    }
     
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    @ApiOperation(value = "글 수정", notes = "글 내용을 수정하는 API. Board entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 bid 값을 함깨 보내줘야한다.")
    public ResponseEntity<ApiResponseMessage> updateBoard( Board board ){
        ApiResponseMessage message = new ApiResponseMessage("Success", "등록되었습니다.", "", "");
        ResponseEntity<ApiResponseMessage> response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         
        try {
            boardService.updateBoard(board);
        } catch(Exception ex){
            message = new ApiResponseMessage("Failed", "글 수정에 실패하였습니다.", "ERROR00002", "Fail to update for board information.");
            response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
        return response;
    }
    
    @RequestMapping(value = "/modifyView", method = RequestMethod.GET)
    public String showModifyView() {
    	return "modifyView";
    }
    
    @RequestMapping(value = "/delete/{bid}", method = RequestMethod.DELETE)
    @ApiOperation(value = "글 삭제", notes = "글 내용을 삭제하는 API. Board entity 클래스의 bid 값으로 데이터를 삭제한다.")
    public ResponseEntity<ApiResponseMessage> deleteBoard( @PathVariable("bid") Long bid ){
        ApiResponseMessage message = new ApiResponseMessage("Success", "등록되었습니다.", "", "");
        ResponseEntity<ApiResponseMessage> response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         
        try {
            boardService.deleteBoard(bid);
        } catch(Exception ex){
            message = new ApiResponseMessage("Failed", "글 삭제에 실패하였습니다.", "ERROR00003", "Fail to remove for board information.");
            response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
        return response;
    }
}