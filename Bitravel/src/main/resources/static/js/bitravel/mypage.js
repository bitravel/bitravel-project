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
	let checkBtn = document.getElementById('updatePassword');
	let changePw = document.getElementById('changePassword');
	let confirmPw = document.getElementById('confirmPassword');

	checkBtn.addEventListener("click", function () {
		changePw.value.replace(" ", "")
		confirmPw.value.replace(" ", "")

		if (changePw.value || confirmPw.value) {
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

	/**
 * 이미지 등록 후 div background에 이미지 가져오기
 */
	var readURL = function (input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();

			if (!reg_ok(input.files[0])) {
				return false;
			}

			reader.onload = function (e) {
				$('.profile-pic').attr('src', e.target.result);
				$('.profile-pic').css({ "background": "url(" + e.target.result + ")" }); //배경이미지로-
				$('.profile-pic').css('background-size', 'cover');
				$('.profile-pic').css('background-position', 'center center');
				$('.profile-pic').css('background-repeat', 'no-repeat');
				$('.upload-button').focus();
			}

			reader.readAsDataURL(input.files[0]);


		}
	}

	$(".file-upload").on('change', function () {
		readURL(this);
	});

	$(".upload-button").on('click', function () {
		$(".file-upload").click();
	});


});

$('#foreignCheck').on("change", function () {

	const largeSelect = document.getElementById('userLargeGov');
	const smallSelect = document.getElementById('userSmallGov');

	var foreign = document.getElementById('foreignCheck');
	if (foreign.checked) {
		largeSelect.disabled = true;
		largeSelect.disabled = 'disabled';
		smallSelect.disabled = true;
		smallSelect.disabled = 'disabled';
	} else {
		largeSelect.disabled = '';
		largeSelect.disabled = false;
		smallSelect.disabled = '';
		smallSelect.disabled = false;
	}

});

/**
 * 업로드된 파일을 서버에 저장 후 url pattern return
 */
function reg_ok(file) {
	if (confirm('등록 하시겠습니까?')) {

		var imageData = new FormData();
		imageData.append('file', file);

		$.ajax({
			url: '/image', //request 보낼 서버의 경로
			type: 'POST', // 메소드(get, post, put 등)
			enctype: 'multipart/form-data',
			data: imageData, //보낼 데이터,
			processData: false,
			cache: false,
			contentType: false,
			timeout: 5000,
			success: function (url) {
				document.getElementById('userImage').value = url;
			},
			error: function (request, error) {
				//서버로부터 응답이 정상적으로 처리되지 못했을 때 실행
				rtnmsg = request.responseText;
				document.getElementById('userImage').value = rtnmsg;
				alert("Error : " + error);
				console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
			}
		});
		return true;

	} else {
		return false;
	}
}


/**
 * 닉네임 입력값이 변경될 때마다 nickname validation 값을 unchecked로 변경
 */
$('input[name="nickname"]').change(function () {
	var now = $('input[name="nicknameValidation"]');
	now.prop('value', 'unchecked');
	console.log("닉네임 재검증 필요");
});

/**
 * 닉네임 유효성 검사 후 nickname validation 값을 checked로 변경
 */
function isValidNickname() {
	const form = document.getElementById('user-update-form');
	if (!form.nickname.value.trim()) {
		alert('닉네임을 입력해 주세요.');
		form.nickname.value = '';
		form.nickname.focus();
		return false;
	} else if (form.nickname.value.trim().length < 2) {
		alert('닉네임이 너무 짧습니다. 닉네임은 2자 이상이어야 합니다.');
		form.nickname.focus();
		return false;
	} else if (form.nickname.value.trim().length > 8) {
		alert('닉네임이 너무 깁니다. 닉네임은 8자 이하여야 합니다.');
		form.nickname.focus();
		return false;
	} else if (form.nickname.value.trim().indexOf(" ") != -1) {
		alert('닉네임에 공백을 넣을 수 없습니다.');
		form.nickname.value = '';
		form.nickname.focus();
		return false;
	}

	var url = '/api/signup?nickname=' + form.nickname.value.trim();

	fetch(url).then(response => {
		if (response.ok) {
			alert("이미 사용 중인 닉네임입니다.");
			return false;
		} else {
			if (response.status == 500) {
				form.nicknameValidation.value = 'checked';
				console.log("닉네임 검증 확인");
				alert("사용 가능한 닉네임입니다.");
				return true;
			}
			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
		}

	}).catch(error => {
		alert('오류가 발생하였습니다. \n' + error);
	});
}

/**
 * 입력 정보 유효성 검사
 */
function isValid() {
	const form = document.getElementById('user-update-form');

	if (form.nicknameValidation.value != 'checked') {
		alert('닉네임 중복 체크 버튼을 누르세요.');
		form.nicknameCheck.focus();
		return false;
	}

	if (!form.foreignCheck.checked) {

		if (!form.userLargeGov.value.trim()) {
			alert('거주 지역을 선택해 주세요.');
			form.userLargeGov.focus();
			return false;
		}

		if (!form.userSmallGov.value.trim()) {
			alert('거주 지역을 선택해 주세요.');
			form.userSmallGov.focus();
			return false;
		}
	}

	if (!form.userImage.value) {
		if (!confirm("프로필 사진을 등록하지 않았습니다.\n등록하지 않고 진행하시겠습니까?")) {
			document.getElementById('profile-pic').focus();
			return false;
		}
	}

	return true;
}

/**
 * 회원 등록
 */
function save() {

	if (!isValid()) {
		return false;
	}

	const form = document.getElementById('user-update-form');

	var params = {
		email: form.email.value,
		nickname: form.nickname.value,
		userLargeGov: form.userLargeGov.value,
		userSmallGov: form.userSmallGov.value,
		userImage: form.userImage.value
	};

	if (form.foreignCheck.checked) {
		params.userLargeGov = '외국';
		params.userSmallGov = '외국';
	}

	console.log(params);

	fetch('/mypage/update', {
		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
		headers: {
			'Content-Type': 'application/json',
		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
		body: JSON.stringify(params), /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/

	}).then(response => {
		if (!response.ok) {
			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
		}

		alert('저장되었습니다.');
		location.href = '/mypage/setting';

	}).catch(error => {
		alert('오류가 발생하였습니다. \n' + error);
	});
}
