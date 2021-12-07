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
	var allInput = document.getElementsByName('report-check');
	var checkList = new Array();
	for (var i=0;i<allInput.length;i++) {
		if(allInput[i].checked)
			checkList.push(allInput[i].value);
	}

	var result = prompt("선택한 신고 내역들의 처리 내용을 입력하세요.");
	if(!confirm(result+"의 내용으로 총 "+checkList.length+"개의 신고 처리를 실행하시겠습니까?")) {
		return false;
	}

	var param = {
		list:checkList,
		result:result
	}

	fetch('/api/reports/check', {
        		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
        		headers: { 
        			'Content-Type': 'application/json',
        		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
        		body: JSON.stringify(param), /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/
        
        	}).then(response => {
        		if (!response.ok) {
        			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
        		}
				
        		alert(checkList+'번 신고 내역 처리가 '+result+'로 완료되었습니다.');
        		findAllReport();
        
        	}).catch(error => {
        		alert('오류가 발생하였습니다. \n'+error);
        	});

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
			<input class="form-check-input" name="report-check" type="checkbox" value="${obj.reportId}" id="${obj.reportId}">
			</td>
    			<td>${idx+1}</td>
    				<td>
    					${obj.reporterEmail}
    				</td>
					<td>${obj.reportedEmail}</td>
					<td>${obj.reportTitle}</td>
					<td><a href=${obj.reportContent} target="_blank">바로가기</a></td>
					`;
					if(obj.reportComment) {
						html += `
						<td>${obj.reportComment}</td>
						`;
					} else {
						html += `
						<td>-</td>
						`;
					}
					html += `
						<td>${moment(obj.reportDate).format('YYYY-MM-DD HH:mm:ss')}</td>
					`;
					if(obj.checkResult) {
						html += `
					<td>${obj.checkResult}</td>
					`;
					} else {
						html += `
						<td>-</td>
						`;
					}
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