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
			html = '<td colspan="4">등록된 게시글이 없습니다.</td>';
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
