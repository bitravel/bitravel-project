/**
 * 지도에 각 자치단체별 버튼 호출
 */

function initMapList(largeList) {
	var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
	var initOptions = { //지도를 생성할 때 필요한 기본 옵션
		center: new kakao.maps.LatLng(35.79425, 127.6529), //지도의 중심좌표.
		level: 13 //지도의 레벨(확대, 축소 정도)
	};
	var map = new kakao.maps.Map(container, initOptions); //지도 생성 및 객체 리턴

	var zoomControl = new kakao.maps.ZoomControl();
	map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

	for (var i = 0; i < largeList.length; i++) {
		if (!largeList[i].regionLat)
			continue;

		if (largeList[i].largeGov=="경기") {
			largeList[i].regionLat = Number(largeList[i].regionLat)-0.1;
			largeList[i].regionLong = Number(largeList[i].regionLong)+0.1;
		} else if (largeList[i].largeGov=="강원") {
			largeList[i].regionLat = Number(largeList[i].regionLat)-0.2;
			largeList[i].regionLong = Number(largeList[i].regionLong)+0.1;
		}
		
		var markerPosition = new kakao.maps.LatLng(largeList[i].regionLat, largeList[i].regionLong);

		// 마커를 생성합니다
		var marker = new kakao.maps.Marker({
			name: largeList[i].largeGov,
			position: markerPosition
		});

		// 마커가 지도 위에 표시되도록 설정합니다
		marker.setMap(map);

		// 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
		var content = '<div>' +
			'    <span class="fs-5 fw-bold bg-light form-control p-1 pb-0 pt-0 mb-1">' + largeList[i].largeGov + '</span><pre><br></pre>' +
			'</div>';

		// 커스텀 오버레이가 표시될 위치입니다 
		var position = new kakao.maps.LatLng(largeList[i].regionLat, largeList[i].regionLong);

		// 커스텀 오버레이를 생성합니다
		var customOverlay = new kakao.maps.CustomOverlay({
			map: map,
			position: position,
			content: content,
			yAnchor: 1
		});
	}
}

function loadMainList() {

	/**
	 * 광역지자체 리스트 가져오기
	 */
	fetch(`/api/regions/list/cdn`).then(response => {

		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		// 리스트 형태이므로 대괄호를 중괄호로 바꾸어야 올바른 JSON 형식이 됨
		json = JSON.stringify(json);
		json.replace('[', '{').replace(']', '}');
		largeList = JSON.parse(json);

		initMapList(largeList);

	}).catch(error => {
		alert('리스트 불러오기에 실패했습니다.' + error);

	});

}

function loadNavbar() {
	var largeList = document.getElementById('largeList');
	var tag0 = '<li class="col-2 col-lg-1 text-center mt-3"><a class="fs-5 fw-bold text-dark" href="/travel/list/';
	var tag1 = '" id="navbarDropdown';
	var tag3 = '">';
	var tag2 = '</a>';
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
		var tag = "";
		const newList = JSON.parse(json);
		newList.forEach(function (item, index, array) {
			tag += tag0 + item + tag1 + index + tag3 + item + tag2;
			tag += '</li>';
			
		});
		largeList.innerHTML += tag;

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
