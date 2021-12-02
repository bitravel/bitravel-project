document.getElementById('openAdminUser').onclick = () => {
	findAll();
}

function findAll() {
	fetch('/api/user/list').then(response => {
		if (response.ok) {
   			return response.json();
		}
	}).then(json => {
		let html = '';

		if (!json.length) {
			html = '<td colspan="4">가입 회원이 없습니다.</td>';
		} else {
			json.forEach((obj, idx) => {
			html += `
			<tr>
			<td>
			<input class="form-check-input" type="checkbox" value="${obj.userId}" id="${obj.userId}">
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

	});
}

function searchKeyword() {
	const form = document.getElementById('user-search-form');
	console.log(form.userSearch.value+"  chechecheck");
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
		let html = '';
		if (!json.length) {
			html = '<td colspan="4">조건에 맞는 회원이 없습니다.</td>';
		} else {
			json.forEach((obj, idx) => {
			html += `
			<tr>
			<td>
			<input class="form-check-input" type="checkbox" value="${obj.userId}" id="${obj.userId}">
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

	}).catch(error => {
        		alert('오류가 발생하였습니다. \n'+error);
        	});
}

function deleteUser() {

}
