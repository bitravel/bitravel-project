package com.bitravel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bitravel.data.entity.User;
import com.bitravel.model.ApiResponseMessage;
import com.bitravel.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@Api(value = "UserController")
public class UserController {
 
    @Autowired
    UserService userService;
    
    // DB 연결 테스트용 링크 -> 포트번호만 상황에 따라 바꾸어서 사용하세요. http://localhost:8080/swagger-ui/
     
 
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "사용자 목록 조회", notes = "사용자 목록을 조회하는 API.")
    public List<User> getUserList(){
        return userService.selectUserList();
    }
 
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ApiOperation(value = "사용자 정보 조회", notes = "사용자의 정보를 조회하는 API. User entity 클래스의 uid값을 기준으로 데이터를 가져온다.")
    public Optional<User> getUser( @PathVariable("uid") String uid ){
        return userService.selectUser(uid);
    }
 
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "사용자 정보 등록", notes = "사용자 정보를 저장하는 API. User entity 클래스로 데이터를 저장한다.")
    public ResponseEntity<ApiResponseMessage> insertUser( User user ){
        ApiResponseMessage message = new ApiResponseMessage("Success", "등록되었습니다.", "", "");
        ResponseEntity<ApiResponseMessage> response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         
        try {
            userService.insertUser(user);
        } catch(Exception ex){
            message = new ApiResponseMessage("Failed", "사용자 등록에 실패하였습니다.", "ERROR00001", "Fail to registration for user information.");
            log.error("error: "+ex);
            response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
        return response;
    }
     
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보를 수정하는 API. User entity 클래스로 데이터를 수정한다.<br>이때엔 정보를 등록할 때와는 다르게 uid 값을 함깨 보내줘야한다.")
    public ResponseEntity<ApiResponseMessage> updateUser( User user ){
        ApiResponseMessage message = new ApiResponseMessage("Success", "등록되었습니다.", "", "");
        ResponseEntity<ApiResponseMessage> response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         
        try {
            userService.updateUser(user);
        } catch(Exception ex){
            message = new ApiResponseMessage("Failed", "사용자 정보 수정에 실패하였습니다.", "ERROR00002", "Fail to update for user information.");
            response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
        return response;
    }
 
    @RequestMapping(value = "/{uid}", method = RequestMethod.DELETE)
    @ApiOperation(value = "사용자 정보 삭제", notes = "사용자 정보를 삭제하는 API. User entity 클래스의 uid 값으로 데이터를 삭제한다.")
    public ResponseEntity<ApiResponseMessage> deleteUser( @PathVariable("uid") String uid ){
        ApiResponseMessage message = new ApiResponseMessage("Success", "등록되었습니다.", "", "");
        ResponseEntity<ApiResponseMessage> response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
         
        try {
            userService.deleteUser(uid);
        } catch(Exception ex){
            message = new ApiResponseMessage("Failed", "사용자 정보 삭제에 실패하였습니다.", "ERROR00003", "Fail to remove for user information.");
            response = new ResponseEntity<ApiResponseMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
        return response;
    }
    
    @PostMapping("/signIn")
    public String signIn(String id, String pwd) {
    	if(userService.validationLogin(id, pwd)) {
    		return "OK";
    	}
    	return "Fail";
    }
}