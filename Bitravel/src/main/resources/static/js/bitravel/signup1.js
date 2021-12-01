/**
 * 
 */
 	$(document).ready(function() {	
	
	const largeSelect = document.getElementById('nowLarge');
	
	/**
     * 광역지자체 선택이 안되있을 것이므로 기초지자체는 선택 불가능하게 설정
     */
	if(largeSelect.value=="") {
		document.getElementById('nowSmall').disabled = true;
		document.getElementById('nowSmall').disabled = 'disabled';
	}
	
	/**
     * 광역지자체 리스트 가져오기
     */
    fetch(`/api/regions/list`).then(response => {
    	
    	if (!response.ok) {
			throw new Error('Request failed...');
	    }
    	return response.json();
    	
   	}).then(json => {
   		// 리스트 형태이므로 대괄호를 중괄호로 바꾸어야 올바른 JSON 형식이 됨
   		json = JSON.stringify(json);
   		json.replace('[', '{').replace(']', '}');
   		const newList = JSON.parse(json);
   		var tag = "";
   		newList.forEach(function (item, index, array) {
   			tag += "<option value='"+item+"'>"+item+"</option>\n";
   		});
   		largeSelect.innerHTML += tag;
   		
   	}).catch(error => {
    	alert('리스트 불러오기에 실패했습니다.');
    	
   	});
    
	/**
     * 광역지자체 값이 바뀔 때마다 기초자치단체 리스트 가져오기
     */
   		$('#nowLarge').on("change", function(){
   			
   			const smallSelect = document.getElementById('nowSmall');
   			
	    	if(largeSelect.value == "") {
	    		smallSelect.disabled = true;
	    		smallSelect.disabled = 'disabled';
	    	} else {
	    		smallSelect.disabled = '';
	    		smallSelect.disabled = false;
	    		
	    		var url = '/api/regions/list/' + largeSelect.value;
	    		fetch(url).then(response => {
	    	    	
	    	    	if (!response.ok) {
	    				throw new Error('Request failed...');
	    		    }
	    	    	return response.json();
	    	    	
	    	   	}).then(json => {
	    	   		console.table(json);
	    	   		json = JSON.stringify(json);
	    	   		json.replace('[', '{').replace(']', '}');
	    	   		const newList = JSON.parse(json);
	    	   		var tag = "";
	    	   		newList.forEach(function (item, index, array) {
	    	   			tag += "<option value='"+item+"'>"+item+"</option>\n";
	    	   		});
	    	   		smallSelect.innerHTML = tag;
	    	   		
	    	   	}).catch(error => {
	    	    	alert('리스트 불러오기에 실패했습니다.');
	    	   	});	
	    	}
	    });
	});
	
		/**
         * Gender radio button의 선택 여부에 따라 label 색 바꿔줌
         */
		$('input[name="gender"]').change(function() {
			$('input[name="gender"]').each(function() {
				var value = $(this).val();
				var checked = $(this).prop('checked');
				var $label = $(this).next();

				if (checked)
					$label.prop('checked', true);
				else
					$label.prop('cheched', false);
			});
		});
		
		/**
         * 닉네임 입력값이 변경될 때마다 nickname validation 값을 unchecked로 변경
         */
		$('input[name="nickname"]').change(function() {
			var now = $('input[name="nicknameValidation"]');
			now.prop('value', 'unchecked');
			console.log("닉네임 재검증 필요");
		});
		
		/**
         * 닉네임 유효성 검사 후 nickname validation 값을 checked로 변경
         */
		function isValidNickname() {
			const form = document.getElementById('signup-form');
        	if (!form.signUpNickname.value.trim()) {
        		alert('닉네임을 입력해 주세요.');
        		form.signUpNickname.value = '';
        		form.signUpNickname.focus();
        		return false;
        	} else if (form.signUpNickname.value.trim().length<2) {
        		alert('닉네임이 너무 짧습니다. 닉네임은 2자 이상이어야 합니다.');
        		form.signUpNickname.focus();
        		return false;
        	} else if (form.signUpNickname.value.trim().length>8) {
        		alert('닉네임이 너무 깁니다. 닉네임은 8자 이하여야 합니다.');
        		form.signUpNickname.focus();
        		return false;
        	} else if (form.signUpNickname.value.trim().indexOf(" ") != -1) {
           		alert('닉네임에 공백을 넣을 수 없습니다.');
           		form.signUpNickname.value = '';
        		form.signUpNickname.focus();
        		return false;
        	}
        	
        	var url = '/api/signup?nickname='+form.signUpNickname.value.trim();
            
            	fetch(url).then(response => {
            		if (response.ok) {
            			alert("이미 사용 중인 닉네임입니다.");
            			return false;
            		} else {
            			if(response.status==500) {
            				form.nicknameValidation.value = 'checked';
	            			console.log("닉네임 검증 확인");
	            			alert("사용 가능한 닉네임입니다.");
	            			return true;
            			}
            			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
            		}
            		
            	}).catch(error => {
            		alert('오류가 발생하였습니다. \n'+error);
            	});
		}
		
		/**
         * 입력 정보 유효성 검사
         */
        function isValid() { 
          	const form = document.getElementById('signup-form');         	
        	if (!form.signUpMail.value.trim()) {
        		alert('이메일을 입력해 주세요.');
        		form.signUpMail.value = '';
        		form.signUpMail.focus();
        		return false;
        	} else if ( form.signUpMail.value.trim().length<5
        			|| !form.signUpMail.value.trim().includes("@") 
        			|| form.signUpMail.value.trim().endsWith("@")
        			|| !/^[a-z0-9()]+$/i.test(form.signUpMail.value.trim().slice(-2,-1))) {
        		alert('올바른 이메일 형식이 아닙니다.');	
        		form.signUpMail.focus();
        		return false;
        	}
            
        	if (!form.signUpPassword.value.trim()) {
        		alert('비밀번호를 입력해 주세요.');
        		form.signUpPassword.value = '';
        		form.signUpPassword.focus();
        		return false;
        	} else if(form.signUpConfirmPassword.value.trim()!=form.signUpPassword.value.trim()) {
        		alert('입력된 두 비밀번호가 다릅니다.')
        		form.signUpPassword.value = '';
        		form.signUpConfirmPassword.value = '';
        		form.signUpPassword.focus();
        		return false;
        	} else if (form.signUpPassword.value.trim().length<6) {
        		alert('비밀번호가 너무 짧습니다. 6자 이상으로 입력해 주세요.');
        		form.signUpPassword.value = '';
        		form.signUpConfirmPassword.value = '';
        		form.signUpPassword.focus();
        		return false;
        	}
        	
        	if (!form.signUpRealName.value.trim()) {
        		alert('이름을 입력해 주세요.');
        		form.signUpRealName.value = '';
        		form.signUpRealName.focus();
        		return false;
        	} else if (form.signUpRealName.value.trim().length<2) {
        		alert('이름이 너무 짧습니다. 이름은 2자 이상이어야 합니다.');
        		form.signUpRealName.focus();
        		return false;
        	}
        	
        	if (form.nicknameValidation.value!='checked') {
        		alert('닉네임 중복 체크 버튼을 누르세요.');
        		form.nicknameCheck.focus();
        		return false;
        	}
        	
        	if (!form.age.value.trim()) {
        		alert('출생년도를 선택해 주세요.');
        		form.age.focus();
        		return false;
        	}
        	
        	if (!form.gender.value.trim()) {
        		alert('성별을 선택해 주세요.');
        		form.gender.focus();
        		return false;
        	}
        	
        	if (!form.foreignCheck.checked) {
        		
        		if (!form.nowLarge.value.trim()) {
        		alert('거주 지역을 선택해 주세요.');
        		form.nowLarge.focus();
        		return false;
        		}
        	
	        	if (!form.nowSmall.value.trim()) {
	        		alert('거주 지역을 선택해 주세요.');
	        		form.nowSmall.focus();
	        		return false;
	        	}
        	}
        	
        	
     		
        	if (!form.flexCheckDefault.checked) {
        		alert('이용약관에 동의해 주세요.');
        		form.flexCheckDefault.focus();
        		return false;
        	}
        	
        	return true;
        }
        
        /**
         * 회원 등록
         */
        function save() {
        
        	if ( !isValid() ) {
        		return false;
        	}
        
        	const form = document.getElementById('signup-form');
        	const params = {
        		email: form.signUpMail.value,
        		password: form.signUpPassword.value,
        		nickname: form.signUpNickname.value,
        		realname: form.signUpRealName.value,
        		ageString: form.age.value,
        		gender: form.gender.value,
        		userLargeGov: form.nowLarge.value,
        		userSmallGov: form.nowSmall.value,
        	};
        	
        	if(form.foreignCheck.checked) {
        		params.userLargeGov= '외국';
        		params.userSmallGov= '외국';
        	}
        
        	fetch('/api/signup', {
        		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
        		headers: { 
        			'Content-Type': 'application/json',
        		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
        		body: JSON.stringify(params) /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/
        
        	}).then(response => {
        		if (!response.ok) {
        			if(response.status==500)
        				throw new Error('이미 가입된 회원입니다. 다른 이메일을 사용해 주세요.')
        			else
        				throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
        		}
        
        		alert('저장되었습니다.');
        		location.href = '/signup/second?userEmail='+form.signUpMail.value;
        
        	}).catch(error => {
        		alert('오류가 발생하였습니다. \n'+error);
        	});
        }