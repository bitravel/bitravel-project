/**
 * 
 */

$(document).ready(function () {
	loadLargeSelect('largeSelect0', 'smallSelect0');
	/**
	 * 광역지자체 값이 바뀔 때마다 기초자치단체 리스트 가져오기
	 */
	$(document).on("change", "select[name='large']", function () {
		var allSelect = (document.getElementsByTagName('select').length) / 2;
		$("select[name='large']").each(function () {
			var largeId = $(this).attr('id');
			var smallId = 'smallSelect' + largeId[11];
			var largeSelect = document.getElementById(largeId);
			var smallSelect = document.getElementById(smallId);
			if (largeSelect.value == "") {
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
					json = JSON.stringify(json);
					json.replace('[', '{').replace(']', '}');
					const newList = JSON.parse(json);
					var tag = "";
					if (newList.indexOf(smallSelect.value) == -1) {
						newList.forEach(function (item, index, array) {
							tag += "<option value='" + item + "'>" + item + "</option>\n";
						});
						smallSelect.innerHTML = tag;
					}
				}).catch(error => {
					alert('리스트 불러오기에 실패했습니다.');
				});

			}
		});
	});

});


function loadLargeSelect(largeId, smallId) {

	largeFirst = document.getElementById(largeId);

	if (largeFirst.value == "") {
		document.getElementById(smallId).disabled = true;
		document.getElementById(smallId).disabled = 'disabled';
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
			tag += "<option value='" + item + "'>" + item + "</option>\n";
		});

		largeFirst.innerHTML += tag;
	}).catch(error => {
		alert('리스트 불러오기에 실패했습니다.');

	});

}

// Select 하나 추가하기
function addSelect() {
	var allSelect = (document.getElementsByTagName('select').length) / 2;

	if (allSelect == 10) {
		alert('최대 10곳까지 등록할 수 있습니다.');
		return false;
	}

	var largeId = 'largeSelect' + allSelect;
	var smallId = 'smallSelect' + allSelect;
	tag = "<select id='largeSelect" + allSelect + "' name='large' class='form-control large'>\n";
	tag += "<option value=''>시/도</option></select>&nbsp;&nbsp;&nbsp;\n<select id='smallSelect" + allSelect;
	tag += "' name='small' class='form-control small'><option value=''>시/군/구</option></select>&nbsp;&nbsp;&nbsp;\n<button type='button' onclick='addSelect()' class='btn btn-secondary' id='add'>+ 추가</button>\n";
	var div = document.getElementById('only-select');
	var newElement = document.createElement('div');
	newElement.setAttribute('id', 'selectGroup');
	newElement.setAttribute('class', 'd-flex justify-content-xl-center mb-1 pt-1');
	newElement.innerHTML = tag;
	div.appendChild(newElement);
	loadLargeSelect(largeId, smallId);
}

//유효성 검사
function isValid() {
	var flag = 0;
	var all = 1;
	var id = "";
	$("select[name='large']").each(function () {
		var idNow = $(this).attr('id');
		var now = document.getElementById(idNow).value;
		if (now == "") {
			if (all)
				id = idNow;
			all = 0;
		} else {
			flag = 1;
		}
	});

	$("select[name='small']").each(function () {
		var idNow = $(this).attr('id');
		var now = document.getElementById(idNow).value;
		if (now == "") {
			all = 0;
			if (id == "")
				id = idNow;
			return false;
		}
	});

	if (!flag) {
		alert('최소 1개 이상의 선호 여행 지역을 선택해야 합니다.');
		document.getElementById('largeSelect0').focus();
		return false;
	}
	if (!all) {
		alert('선호 여행 지역 선택을 모두 완료해 주세요.')
		document.getElementById(id).focus();
		return false;
	}

	return true;
}


// 결과 UserRegion class에 저장
function save(userEmail) {

	console.log(userEmail);

	if (!isValid())
		return false;
	console.log('validated');

	var allSelect = (document.getElementsByTagName('select').length) / 2;

	var resultList = new Array();

	for (var i = 0; i < allSelect; i++) {
		var largeId = 'largeSelect' + i;
		var smallId = 'smallSelect' + i;

		var data = new Object();

		data.userEmail = userEmail;
		data.largeGov = document.getElementById(largeId).value;
		data.smallGov = document.getElementById(smallId).value;

		resultList.push(data);
	}

	const param = JSON.stringify(resultList);

	fetch('/api/regions', {
		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
		headers: {
			'Content-Type': 'application/json',
		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
		body: JSON.stringify(resultList) /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/

	}).then(response => {
		if (!response.ok) {
			if (response.status == 500)
				throw new Error('서버 오류입니다. 관리자에게 문의해 주세요.');
			else
				throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
		}

		alert('저장되었습니다.');
		location.href = '/signup/third?userEmail=' + userEmail;

	}).catch(error => {
		alert('오류가 발생하였습니다. \n' + error);
	});

}
