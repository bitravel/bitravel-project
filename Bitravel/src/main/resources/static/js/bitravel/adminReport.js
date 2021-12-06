var nowResult;

document.getElementById('openAdminReport').onclick = () => {
	findAllReport();
}

// 첫번째 모달이 열린 상태에서 모달 위 특정 객체에 포커스를 둔 다음 엔터를 치면 searchKeyword() 함수가 작
document.getElementById('myModal_2').addEventListener('keydown', function(event) {
	if (event.keyCode === 13) {
		searchReport();
		event.preventDefault();
  };
}, true);

$('input[name="listType"]').change(function() {
	const form = document.getElementById('report-search-form');
	if(form.listType.value=="all") {
		drawList(nowResult);
	} else if(form.listType.value=="board") {
		var newResult = new Array();
		nowResult.forEach((obj, index) => {
			if(!obj.reportComment && obj.reportContent.indexOf('board')>-1) {
				newResult.push(obj);
			}
		});
		drawList(newResult);
	} else if(form.listType.value=="review") {
		var newResult = new Array();
		nowResult.forEach((obj, index) => {
			if(!obj.reportComment && obj.reportContent.indexOf('review')>-1) {
				newResult.push(obj);
			}
		});
		drawList(newResult);
	} else if(form.listType.value=="bcomment") {
		var newResult = new Array();
		nowResult.forEach((obj, index) => {
			if(obj.reportComment && obj.reportContent.indexOf('board')>-1) {
				newResult.push(obj);
			}
		});
		drawList(newResult);
	} else if(form.listType.value=="rcomment") {
		var newResult = new Array();
		nowResult.forEach((obj, index) => {
		if(obj.reportComment && obj.reportContent.indexOf('review')>-1) {
				newResult.push(obj);
			}
		});
		drawList(newResult);
	} 

});

function findAllReport() {
	fetch('/api/reports/list').then(response => {
		if (response.ok) {
   			return response.json();
		}
	}).then(json => {
		nowResult = json;
		drawList(json);
	});
}

function findUncheckedReport() {
	fetch('/api/reports/notchecked').then(response => {
		if (response.ok) {
   			return response.json();
		}
	}).then(json => {
		nowResult = json;
		drawList(json);
	});
}

function findCheckedReport() {
	fetch('/api/reports/checked').then(response => {
		if (response.ok) {
   			return response.json();
		}
	}).then(json => {
		nowResult = json;
		drawList(json);
	});
}


function searchReport() {
	const form = document.getElementById('report-search-form');

	if(!form.reportSearch.value.trim()) {
		alert("최소한 한 글자는 입력해야 합니다.");
		return false;
	}

	var url = "/api/reports/search/";
	if (form.select2.value=="t") {
		url += "title";
		console.log("제목");
	} else if (form.select2.value=="c") {
		url += "content";
		console.log("글/댓글 번호");
	} else if (form.select2.value=="reporter") {
		url += "reporter";
		console.log("신고한 유저");
	} else if (form.select2.value=="reported") {
		url += "reported";
		console.log("신고받은 유저");
	}
	
	url +="?keyword=";
	url += form.reportSearch.value.trim();
	
	fetch(url).then(response => {
        		if (!response.ok) {
        			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
        		}
				return response.json();
        	}).then(json => {
				nowResult = json;
				drawList(json);
				form.allCheck.checked = true;
	}).catch(error => {
        		alert('오류가 발생하였습니다. \n'+error);
        	});
}


function checkReport() {
	var allInput = document.getElementsByClassName('form-check-input');
}

function drawList(json) {
	let html = '';
	if (!json.length) {
		html = '<td colspan="10" style="text-align:center;">결과를 찾을 수 없습니다.</td>';
	} else {
		json.forEach((obj, idx) => {
			html += `
			<tr>
			<td>
			<input class="form-check-input" type="checkbox" value="${obj.reportId}" id="${obj.reportId}">
			</td>
    			<td>${idx+1}</td>
    				<td>
    					${obj.reporterEmail}
    				</td>
					<td>${obj.reportedEmail}</td>
					<td>${obj.reportTitle}</td>
					<td><a href=${obj.reportContent} target="_blank">바로가기</a></td>
					<td>${obj.reportComment}</td>
    				<td>${moment(obj.reportDate).format('YYYY-MM-DD HH:mm:ss')}</td>
					<td>${obj.checkResult}</td>
					`;
					if(obj.checkResult) {
						html += `
						<td>${moment(obj.checkDate).format('YYYY-MM-DD HH:mm:ss')}</td>
						</tr>
						`;
					} else {
						html += `
						<td>미처리</td>
						</tr>
						`;
					}
				});
	}

	document.getElementById('reportList').innerHTML = html;
}