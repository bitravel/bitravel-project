/**
 * 지도에 각 자치단체별 버튼 호출
 */

function initMapList(smallList) {
	let level = 11;
	if(smallList[0].largeGov=='부산' || smallList[0].largeGov=='대구') {
		 level = 9;
	 } else if (smallList[0].largeGov=='인천') {
		 level = 10;
		 smallList[0].regionLat = Number(smallList[0].regionLat)+0.1;
		 smallList[0].regionLong = Number(smallList[0].regionLong)-0.1;
	 } else if (smallList[0].largeGov=='세종' || smallList[0].largeGov=='광주'
	 || smallList[0].largeGov=='대전' || smallList[0].largeGov=='울산') {
		 level = 8;
	 } else if (smallList[0].largeGov=='서울') {
		level = 9;
		smallList[0].regionLat = Number(smallList[0].regionLat)+0.03;
	 } else if (smallList[0].largeGov=='강원') {
		level = 12;
		smallList[0].regionLong = Number(smallList[0].regionLong)-0.03;
		smallList[0].regionLat = Number(smallList[0].regionLat)+0.03;
	 } else if (smallList[0].largeGov=='경북') {
		level = 12;
		smallList[0].regionLat = Number(smallList[0].regionLat)+0.3;
		smallList[0].regionLong = Number(smallList[0].regionLong)+0.8;
	 }


	var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
	var initOptions = { //지도를 생성할 때 필요한 기본 옵션
		center: new kakao.maps.LatLng(smallList[0].regionLat, smallList[0].regionLong), //지도의 중심좌표.
		level: level //지도의 레벨(확대, 축소 정도)
	};
	var map = new kakao.maps.Map(container, initOptions); //지도 생성 및 객체 리턴

	var zoomControl = new kakao.maps.ZoomControl();
	map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

	for (var i = 1; i < smallList.length; i++) {
		if (!smallList[i].regionLat)
			continue;
		var markerPosition = new kakao.maps.LatLng(smallList[i].regionLat, smallList[i].regionLong);

		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
			name: smallList[i].smallGov,
			position: markerPosition
		});

		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);

		// 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
		var content = '<div>' +
			'    <span class="fs-5 fw-bold bg-light form-control p-1 pb-0 pt-0 mb-1">' + smallList[i].smallGov + '</span><pre><br></pre>' +
			'</div>';

		// 커스텀 오버레이가 표시될 위치입니다 
		var position = new kakao.maps.LatLng(smallList[i].regionLat, smallList[i].regionLong);

		// 커스텀 오버레이를 생성합니다
		var customOverlay = new kakao.maps.CustomOverlay({
			map: map,
			position: position,
			content: content,
			yAnchor: 1
		});
	}
}

function loadMainList(largeGov) {

	/**
	 * 광역지자체 리스트 가져오기
	 */
	fetch(`/api/regions/list/cdn/${largeGov}`).then(response => {

		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		// 리스트 형태이므로 대괄호를 중괄호로 바꾸어야 올바른 JSON 형식이 됨
		json = JSON.stringify(json);
		json.replace('[', '{').replace(']', '}');
		smallList = JSON.parse(json);

		initMapList(smallList);

	}).catch(error => {
		alert('리스트 불러오기에 실패했습니다.' + error);

	});

}

function loadNavbar(largeGov) {
	var smallList = document.getElementById('smallList');
	var tag0 = '<li class="col-2 col-xl-1 text-center mt-3"><a class="fs-5 fw-bold text-dark" href="/travel';
	var tag1 = '" id="navbarDropdown';
	var tag3 = '">';
	var tag2 = '</a>';
	/**
 * 기초지자체 리스트 가져오기
 */
	fetch(`/api/regions/list/${largeGov}`).then(response => {

		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {

		// 리스트 형태이므로 대괄호를 중괄호로 바꾸어야 올바른 JSON 형식이 됨
		json = JSON.stringify(json);
		json.replace('[', '{').replace(']', '}');
		var tag = "";
		const newList = JSON.parse(json);
		newList.forEach(function (item, index, array) {
			if(largeGov != "세종") {
				tag += tag0 + '/list/' + largeGov + '/' + item + tag1 + index + tag3 + item + tag2;
				tag += '</li>';
			} else {
				tag += '<li class="col-2 col-xl-1 text-center mt-3"><span class="fs-5 fw-bold text-dark"';
				tag += tag1 + index + tag3 + item + '</span>';
				tag += '</li>';
			}			
		});

		tag += '<li class="col-2 col-xl-1 text-center mt-3"><a class="fs-5 text-muted" href="/travel'+tag1+'99'+tag3+'< 전국';

		smallList.innerHTML += tag;

	}).catch(error => {
		console.log(error);
		alert('리스트 불러오기에 실패했습니다.\n' + error);

	});

}


/**
 * 뒤로가기
 */
function goBack() {
	/* location.href = `/board/list?page=[]`; 페이지 넘버를 불러오는걸 모르겠습니다..*/
	location.href = "javascript:history.back(-1)";
}
