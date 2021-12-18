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
	});
	
	// 비밀번호 변경 값 보내기
//	$("#updatePassword").click(function() {
//		$.ajax({
//			type: "POST",
//			url: "/mypage/setting",
//			data: {},
//			dataType: "",
//			success: function(data) {
//        	console.log(data);
//    		}
//		});
//	})
	
	//비밀번호 확인
//	$("#updatePassword").click(function() {
//		//비밀번호 공백 확
//	})
	
	

	// 입력 정보 유효성 검사
	let checkBtn  = document.getElementById('updatePassword');
	let changePw  = document.getElementById('changePassword');
	let confirmPw = document.getElementById('confirmPassword');
	
	checkBtn.addEventListener("click", function() {
		changePw.value.replace(" ","")
		confirmPw.value.replace(" ","")
		
		if(changePw.value || confirmPw.value) {
			alert('비밀번호를 입력하세요.');
			changePw.value = '';
			confirmPw.value = '';
			changePw.focus();
			return false;
		}
		if (changePw.value != confirmPw.value) {
			alert('입력된 두 비밀번호가 다릅니다.');
			changePw.value = '';
			confirmPw.value = '';
			changePw.focus();
			return false;
		}
		if (changePw.value.length < 6) {
			alert('비밀번호를 6자 이상으로 입력하세요.');
			changePw.value = '';
			confirmPw.value = '';
			changePw.focus();
			return false;
		}
		
//		if (!form.signUpPassword.value.trim() || !form.signUpConfirmPassword.value.trim()) {
//			alert('비밀번호를 입력해 주세요.');
//			form.signUpConfirmPassword.value = '';
//			form.signUpPassword.value = '';
//			form.signUpPassword.focus();
//			return false;
//		} else if (form.signUpConfirmPassword.value.trim() != form.signUpPassword.value.trim()) {
//			alert('입력된 두 비밀번호가 다릅니다.')
//			form.signUpPassword.value = '';
//			form.signUpConfirmPassword.value = '';
//			form.signUpPassword.focus();
//			return false;
//		} else if (form.signUpPassword.value.trim().length < 6) {
//			alert('비밀번호가 너무 짧습니다. 6자 이상으로 입력해 주세요.');
//			form.signUpPassword.value = '';
//			form.signUpConfirmPassword.value = '';
//			form.signUpPassword.focus();
//			return false;
//		}
	});

	
});