/**
 * 여행지 조회
 */

function findTravel(id) {

	var url = "/api/travels/" + id;
	fetch(url).then(response => {
		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		console.table(json);
		travel = json;
		Object.keys(json).forEach(key => {
			const elem = document.getElementById(key);
			if (elem && json[key] != "") {
				elem.innerHTML = json[key];
			}

		});

		if (travel['travelImage'])
			document.getElementById('tImage').setAttribute('src', travel['travelImage']);

		if (travel['largeGov'] != "서울" && travel['largeGov'] != "부산" &&
			travel['largeGov'] != "대구" && travel['largeGov'] != "광주" &&
			travel['largeGov'] != "울산" && travel['largeGov'] != "대전" &&
			travel['largeGov'] != "인천") {
			document.getElementById('largeGov').innerText = travel['smallGov'];
		} else if (travel['smallGov'].endsWith("군")) {
			document.getElementById('largeGov').innerText = travel['smallGov'];
		}


		document.getElementById('travelName1').innerText = travel['travelName'];
		document.getElementById('travelName2').innerText = travel['travelName'];

		findWeather(json);
		initMap(json);

	}).catch(error => {
		console.log(error);
		alert('해당 여행지 정보를 찾을 수 없습니다.');
		goBack();
	});

}

function initMap(travel) {
	var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
	var options = { //지도를 생성할 때 필요한 기본 옵션
					center: new kakao.maps.LatLng(travel['latitude'], travel['longitude']), //지도의 중심좌표.
					level: 3 //지도의 레벨(확대, 축소 정도)
				};	
	var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

	var markerPosition  = new kakao.maps.LatLng(travel['latitude'], travel['longitude']); 

	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
		position: markerPosition
	});

	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);

	// 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	var content = '<div>' +
    '    <span class="fw-bold bg-light form-control p-1 pb-0 pt-0 mb-2">'+ travel['travelName'] +'</span><pre><br></pre>' +
    '</div>';

	// 커스텀 오버레이가 표시될 위치입니다 
	var position = new kakao.maps.LatLng(travel['latitude'], travel['longitude']);  

	// 커스텀 오버레이를 생성합니다
	var customOverlay = new kakao.maps.CustomOverlay({
		map: map,
		position: position,
		content: content,
		yAnchor: 1 
	});

}

function findWeather(travel) {

	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var day = now.getDate();
	var hour = now.getHours();
	var minute = now.getMinutes();
	var week = ['일', '월', '화', '수', '목', '금', '토'];

	var front = '<div><i class="fs-1 bi bi-';
	var middle = '"></i><div class="label small"><span class="text-muted">';

	var regionCode = "";
	if (travel['largeGov'] == "서울" || travel['largeGov'] == "인천" ||
		travel['largeGov'] == "경기") {
		regionCode = "11B00000";
	} else if (travel['smallGov'] == "고성군" || travel['smallGov'] == "속초시" ||
		travel['smallGov'] == "양양군" || travel['smallGov'] == "강릉시" ||
		travel['smallGov'] == "동해시" || travel['smallGov'] == "삼척시" ||
		travel['smallGov'] == "태백시") {
		regionCode = "11D20000";
	} else if (travel['largeGov'] == "강원") {
		regionCode = "11D10000";
	} else if (travel['largeGov'] == "대전" || travel['largeGov'] == "세종" ||
		travel['largeGov'] == "충남") {
		regionCode = "11C20000";
	} else if (travel['largeGov'] == "충북") {
		regionCode = "11C10000";
	} else if (travel['largeGov'] == "광주" || travel['largeGov'] == "전남") {
		regionCode = "11F20000";
	} else if (travel['largeGov'] == "전북") {
		regionCode = "11F10000";
	} else if (travel['largeGov'] == "대구" || travel['largeGov'] == "경북") {
		regionCode = "11H10000";
	} else if (travel['largeGov'] == "부산" || travel['largeGov'] == "울산" ||
		travel['largeGov'] == "경남") {
		regionCode = "11H20000";
	} else if (travel['largeGov'] == "제주") {
		regionCode = "11G00000";
	}

	if (hour < 10) {
		hour = '0' + hour;
	}
	if (minute < 10) {
		minute = '0' + minute;
	}
	if (month < 10) {
		month = '0' + month;
	}
	var dateBefore = new Date();
	dateBefore.setDate(dateBefore.getDate() - 1);
	var monthBefore = dateBefore.getDate();
	var dayBefore = dateBefore.getDate();
	if (day < 10) {
		day = '0' + day;
	}
	if (monthBefore < 10) {
		monthBefore = '0' + monthBefore;
	}
	if (dayBefore < 10) {
		dayBefore = '0' + dayBefore;
	}
	today = year + "" + month + "" + day;
	var timeMiddle = "";
	if (hour < 6) {
		timeMiddle = year + "" + monthBefore + "" + dayBefore + "1800";
	} else if (hour < 18) {
		timeMiddle = today + "0600";
	} else {
		timeMiddle = today + "1800";
	}

	var timeShort = "";
	var dayShort = timeMiddle.slice(0, 8);
	if (hour < 3) {
		timeShort = "2300";
	} else if (hour < 6) {
		timeShort = "0200";
		dayShort = today;
	} else if (hour < 9) {
		timeShort = "0500";
	} else if (hour < 12) {
		timeShort = "0800";
	} else if (hour < 15) {
		timeShort = "1100";
	} else if (hour < 18) {
		timeShort = "1400";
	} else if (hour < 21) {
		timeShort = "1700";
	} else if (hour < 24) {
		timeShort = "2000";
	}

	const param = {
		timeShort: timeShort,
		dayShort: dayShort,
		timeMiddle: timeMiddle,
		regionId: regionCode,
		latitude: travel['latitude'],
		longitude: travel['longitude']
	};


	fetch("/api/weather/short", {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(param)

	}).then(response => {
		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		json = json['weather']
		json = json.replace("{response={header={resultCode=00, resultMsg=NORMAL_SERVICE}, body={dataType=JSON, items={item=[", "");
		json = json.replaceAll('{', '{"');
		json = json.replaceAll('=', '":"');
		json = json.replaceAll(', ', '", "');
		json = json.replaceAll('}', '"}');
		json = json.replaceAll('}", "{', '}; {');
		json = json.replace(']', "; ");
		var list = json.split("; ");

		var skyList = new Array();
		var ptyList = new Array();

		var now = new Date();
		var nowH = now.getHours() + 1 + '00';
		var i = 0;
		list.forEach((obj) => {
			if (obj.startsWith('"')) {
				return false;
			}
			var row = JSON.parse(obj);
			if (row['fcstTime'] == nowH)
				if (row['category'] == "SKY") {
					skyList.push(row['fcstValue']);
				} else if (row['category'] == "PTY") {
					ptyList.push(row['fcstValue']);
					i++;
				}
		});
		for (var i = 0; i < 3; i++) {
			var id = 'weather' + i;
			var tag = front;
			var value = "";
			if (skyList[i] == '1') {
				value = "맑음";
				tag += "sun";
			} else if (skyList[i] == '3') {
				if (ptyList[i] == '0') {
					value = "구름많음";
					tag += "cloud";
				} else if (pytList[i] == '1') {
					value = "구름많고 비";
					tag += "cloud-rain";
				} else if (ptyList[i] == '2') {
					value = "구름많고 비/눈";
					tag += "cloud-sleet";
				} else if (ptyList[i] == '3') {
					value = "구름많고 눈";
					tag += "cloud-snow";
				} else if (ptyList[i] == '4') {
					value = "구름많고 소나기";
					tag += "cloud-rain-heavy";
				}
			} else if (skyList[i] == '4') {
				if (ptyList[i] == '0') {
					value = "흐림";
					tag += "cloud-fill";
				} else if (pytList[i] == '1') {
					value = "흐리고 비";
					tag += "cloud-rain-fill";
				} else if (ptyList[i] == '2') {
					value = "흐리고 비/눈";
					tag += "cloud-sleet-fill";
				} else if (ptyList[i] == '3') {
					value = "흐리고 눈";
					tag += "cloud-snow-fill";
				} else if (ptyList[i] == '4') {
					value = "흐리고 소나기";
					tag += "cloud-rain-heavy-fill";
				}
			}

			var then = new Date();
			then.setDate(then.getDate() + i);
			var thisDay = (then.getMonth() + 1) + '월 ' + then.getDate() + '일 (' + week[then.getDay()] + ')</span><br>';

			tag += middle;

			if (i == 0)
				tag += "오늘</span><br>";
			else if (i == 1)
				tag += "내일</span><br>";
			else
				tag += thisDay;
			tag += value;
			tag += "</div>";

			const elem = document.getElementById(id);
			elem.innerHTML = tag;
		};
	});

	fetch("/api/weather/mid", {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(param)

	}).then(response => {
		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		json = json['weather']
		json = json.replace("{response={header={resultCode=00, resultMsg=NORMAL_SERVICE}, body={dataType=JSON, items={item=[", "");
		json = json.replace("]}, pageNo=1, numOfRows=10, totalCount=1}}}", "");
		json = json.replace('{', '{"');
		json = json.replaceAll('=', '":"');
		json = json.replaceAll(', ', '", "');
		json = json.replace('}', '"}');
		json = JSON.parse(json);

		for (var i = 3; i < 7; i++) {
			var id = 'weather' + i;
			var tag = front;
			var key = 'wf' + i;
			if (now.getHours() < 12) {
				key += 'Am';
			} else {
				key += 'Pm';
			}
			var value = json[key];
			var then = new Date();
			then.setDate(then.getDate() + i);
			var thisDay = (then.getMonth() + 1) + '월 ' + then.getDate() + '일 (' + week[then.getDay()] + ')</span><br>';

			if (value == "맑음") {
				tag += 'sun';
			} else if (value = "구름많음") {
				tag += 'cloud';
			} else if (value = "구름많고 비") {
				tag += 'cloud-rain';
			} else if (value = "구름많고 눈") {
				tag += 'cloud-snow';
			} else if (value = "구름많고 비/눈") {
				tag += 'cloud-sleet';
			} else if (value = "구름많고 소나기") {
				tag += 'cloud-rain-heavy';
			} else if (value = "흐림") {
				tag += 'cloud-fill';
			} else if (value = "흐리고 비") {
				tag += 'cloud-rain-fill';
			} else if (value = "흐리고 눈") {
				tag += 'cloud-snow-fill';
			} else if (value = "흐리고 비/눈") {
				tag += 'cloud-sleet-fill';
			} else if (value = "흐리고 소나기") {
				tag += 'cloud-rain-heavy-fill';
			}

			tag += middle;
			tag += thisDay;
			tag += value;
			tag += "</div>";

			const elem = document.getElementById(id);
			elem.innerHTML = tag;
		};

	});
}

/**
 * 뒤로가기
 */
function goBack() {
	/* location.href = `/board/list?page=[]`; 페이지 넘버를 불러오는걸 모르겠습니다..*/
	//location.href = "javascript:history.back(-1)";
}
