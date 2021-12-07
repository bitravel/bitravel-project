document.getElementById('openAdminUser').onclick = () => {
	findAll();
}

// 첫번째 모달이 열린 상태에서 모달 위 특정 객체에 포커스를 둔 다음 엔터를 치면 searchKeyword() 함수가 작
document.getElementById('myModal_1').addEventListener('keydown', function(event) {
	if (event.keyCode === 13) {
		searchKeyword();
		event.preventDefault();
  };
}, true);


function findAll() {
	fetch('/api/user/list').then(response => {
		if (response.ok) {
   			return response.json();
		}
	}).then(json => {
		drawListUser(json);
	});
}


function searchKeyword() {
	const form = document.getElementById('user-search-form');

	if(!form.userSearch.value.trim()) {
		alert("최소한 한 글자는 입력해야 합니다.");
		return false;
	}

	var url = "/api/user/search/";
	if (form.select1.value=="realname") {
		url += "realname";
		console.log("이름");
	} else if (form.select1.value=="email") {
		url += "email";
		console.log("이메일");
	} else if (form.select1.value=="nickname") {
		url += "nickname";
		console.log("닉네임");
	}
	
	url +="?keyword=";
	url += form.userSearch.value.trim();
	
	fetch(url).then(response => {
        		if (!response.ok) {
        			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
        		}
				return response.json();
        	}).then(json => {
				drawListUser(json);
	}).catch(error => {
        		alert('오류가 발생하였습니다. \n'+error);
        	});
}


function deleteUser() {
	var allInput = document.getElementsByName('user-check');
	var deleteList = new Array();
	for(var i=0;i<allInput.length;i++) {
		if(allInput[i].checked)
			deleteList.push(allInput[i].value);
	}

	if(!confirm("총 "+deleteList.length+"명의 회원 정보 삭제를 실행하시겠습니까?")) {
		return false;
	}

	fetch('/api/user/delete', {
        		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
        		headers: { 
        			'Content-Type': 'application/json',
        		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
        		body: JSON.stringify(deleteList), /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/
        
        	}).then(response => {
        		if (!response.ok) {
        			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
        		}
				
        		alert(deleteList+' 모든 회원들의 삭제가 완료되었습니다.');
        		findAll();
        
        	}).catch(error => {
        		alert('오류가 발생하였습니다. \n'+error);
        	});
}

function drawListUser(json) {
		let html = '';
		if (!json.length) {
			html = '<td colspan="10" style="text-align:center;">조건에 맞는 회원이 없습니다.</td>';
		} else {
			json.forEach((obj, idx) => {
			html += `
			<tr>
			<td>
			<input class="form-check-input" name="user-check" type="checkbox" value="${obj.userId}" id="${obj.userId}">
			</td>
    			<td>${idx+1}</td>
    				<td>
    					${obj.realname}
    				</td>
					<td>${obj.nickname}</td>
					<td>${obj.email}</td>
					<td>${obj.ageString}</td>
					<td>${obj.gender}</td>
					<td>${obj.userLargeGov} ${obj.userSmallGov}</td>
    				<td>${moment(obj.userDate).format('YYYY-MM-DD HH:mm:ss')}</td>
    				<td>${obj.userAuthority}</td>
					</tr>
					`;
					});
				}

		document.getElementById('userList').innerHTML = html;
}
