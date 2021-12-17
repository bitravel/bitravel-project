$(document).ready(function () {

//	document.getElementById('userSmallGov').disabled = true;
	const userLargeGov = document.getElementById('userLargeGov');
	
	// 광역지자체 선택되면 disabled 해제
	if (!userLargeGov.value) {
		document.getElementById('userSmallGov').disabled = false;
	}
	
	// 광역지자체 리스트 가져오기
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
			tag += "<option value='" + item + "'>" + item + "</option>\n";
		});
		userLargeGov.innerHTML += tag;

	}).catch(error => {
		alert('리스트 불러오기에 실패했습니다.');

	});

	// 광역지자체 값이 바뀔 때마다 기초자치단체 리스트 가져오기
	$('#userLargeGov').on("change", function () {

		const userSmallGov = document.getElementById('userSmallGov');

//		if (userLargeGov.value == "") {
//			userSmallGov.disabled = true;
//			userSmallGov.disabled = 'disabled';
//		} else {
//			userSmallGov.disabled = '';
//			userSmallGov.disabled = false;

			var url = '/api/regions/list/' + userLargeGov.value;
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
					tag += "<option value='" + item + "'>" + item + "</option>\n";
				});
				userSmallGov.innerHTML = tag;

			}).catch(error => {
				alert('리스트 불러오기에 실패했습니다.');
			});
//		}
	});
});