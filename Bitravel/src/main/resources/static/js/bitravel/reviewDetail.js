/*<![CDATA[*/

/**
 * 후기 상세 조회
 */
function reviewList() {

	fetch(`/api/reviews/${id}`).then(response => {
		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		console.table(json);
		console.table(json.reviewDate);
		json.reviewDate = moment(json.reviewDate).format('YYYY-MM-DD HH:mm');
		Object.keys(json).forEach(key => {
			const elem = document.getElementById(key);
			if (elem) {
				elem.innerHTML = json[key];
				if (json.userImage) {
					$("#userImage").attr("src", json.userImage)
				} else {
					$("#userImage").attr("src", "/assets/img/avatar/2.jpg")
				}
				if (json.userEmail) {
					$("#nickname").attr("href", "/user?email="+json.userEmail)
				}
			}
		});

	}).catch(error => {
		alert('게시글 정보를 찾을 수 없습니다.');
		goList();
	});
}

/**
 * 비회원 댓글 작성 막기
 */
function initComment() {
	fetch(`/api/user`).then(response => {
		if (!response.ok) {
			document.getElementById('content').disabled = true;
			return false;
		}
		return true;
	});
}

/**
 * 댓글 수정 창
 */
function openModal(commentId, email, content) {

	fetch(`/api/user`).then(response => {
		if (!response.ok) {
			return false;
		}
		return response.json();
	}).then(json => {
		console.log(json.email);

		if (json.email != email && json.email != 'admin') {
			alert("해당 댓글을 수정할 권한이 없습니다.");
			return false;
		}

		$("#commentModal").modal("toggle");

		document.getElementById("modalContent").value = content;

		document.getElementById("btnCommentUpdate").setAttribute("onclick", "updateComment(" + commentId + ")");
		document.getElementById("btnCommentDelete").setAttribute("onclick", "deleteComment(" + commentId + ")");
	});
}


/**
* 댓글 유효성 검사
*/
function isValid() {

	const form = document.getElementById('content');

	if (!content.value.trim()) {
		alert('내용을 입력해 주세요.');
		content.value = '';
		content.focus();
		return false;
	}
	if (content.value.length > 1000) {
		alert('댓글은 최대 1000자까지 입력 가능합니다.');
		content.value = content.value.slice(0, 999);
		content.focus();
		return false;
	}

	return true;
}

/**
* 댓글 작성
**/
function insertComment() {

	if (!isValid()) {
		return false;
	}

	var content = document.getElementById("content");

	var uri = `/api/reviews/comments/${id}`;
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = { "reviewId": id, "commentContent": content.value };
	console.log(params);
	$.ajax({
		url: uri,
		type: "POST",
		headers: headers,
		dataType: 'json',
		contentType: "application/json",
		data: JSON.stringify(params),
		success: function (response) {
			if (response.result == false) {
				alert("댓글 등록에 실패하였습니다.");
				return false;
			}
			printCommentList();
			content.value = "";
		},
		error: function (xhr, status, error) {
			alert("에러가 발생하였습니다.");
			return false;
		}
	});
}

/**
* 댓글 수정
**/
function updateComment(commentId) {

	var content = document.getElementById("modalContent");

	if (!content.value.trim()) {
		alert('내용을 입력해 주세요.');
		content.value = '';
		content.focus();
		return false;
	}
	if (content.value.length > 1000) {
		alert('댓글은 최대 1000자까지 입력 가능합니다.');
		content.value = content.value.slice(0, 999);
		content.focus();
		return false;
	}

	var uri = `/api/reviews/comments/${commentId}`;
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "PATCH" };
	var params = { "commentId": commentId, "commentContent": content.value };

	$.ajax({
		url: uri,
		type: "PATCH",
		headers: headers,
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify(params),
		success: function (response) {
			if (response.result == false) {
				alert("댓글 수정에 실패하였습니다.");
				return false;
			}

			printCommentList();
			$("#commentModal").modal("hide");
		},
		error: function (xhr, status, error) {
			alert("에러가 발생하였습니다.");
			return false;
		}
	});
}

/**
* 댓글 삭제
**/
function deleteComment(commentId) {

	if (!confirm('댓글을 삭제하시겠어요?')) {
		return false;
	}

	var uri = `/api/reviews/comments/` + commentId;
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "DELETE" };

	$.ajax({
		url: uri,
		type: "DELETE",
		headers: headers,
		dataType: "json",
		success: function (response) {
			if (response.result == false) {
				alert("댓글 삭제에 실패하였습니다.");
				return false;
			}

			printCommentList();
			$("#commentModal").modal("hide");
		},
		error: function (xhr, status, error) {
			alert("에러가 발생하였습니다.");
			return false;
		}
	});
}


/**
 * 글 신고하기
 */
function reportPost() {
	var url = '/api/reports/post';

	var reason = prompt("신고 사유를 입력해 주세요.");

	if (!reason)
		return false;

	if (!confirm("입력하신 사유로 신고하시겠습니까?\n신고 사유 : " + reason)) {
		return false;
	}

	const params = {
		reportType: 'r',
		reportTitle: reason,
		postId: id
	};

	fetch(url, {
		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
		headers: {
			'Content-Type': 'application/json',
		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
		body: JSON.stringify(params), /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/

	}).then(response => {
		if (!response.ok) {
			alert("해당 글을 신고할 수 없습니다.");
			return false;
		}
		alert('게시물 신고를 완료하였습니다.');
	}).catch(error => {
		alert('오류가 발생하였습니다. \n' + error);
	});

}

/**
 * 댓글 신고하기
 */
function reportComment(cid) {

	if (!confirm("해당 댓글을 신고하시겠습니까?"))
		return false;

	var url = '/api/reports/comment/' + 'r' + cid;

	fetch(url, {
	}).then(response => {
		if (!response.ok) {
			alert("해당 댓글을 신고할 수 없습니다.");
			return false;
		}
		alert('댓글 신고를 완료하였습니다.');
	}).catch(error => {
		alert('오류가 발생하였습니다. \n' + error);
	});
}

/**
* 댓글 조회
**/
function printCommentList() {

	fetch(`/api/reviews/comments/${id}`).then(response => {
		if (response.ok) {
			return response.json();
		}
	}).then(json => {
		console.table(json) //확인
		const count = json.length;
		let html = '';
		var cCount = document.getElementById("count");
		cCount.innerText = "댓글(" + count + ")";
		
		if (!json.length) {
			html = '<td colspan="10" style="text-align:center;>등록된 게시글이 없습니다.</td>';
		} else {
			json.forEach((obj) => {
				html += `
										<tr class="form-control mb-2">
											<td style="width:20%;"><span class="fw-bold"><a class="text-dark" href="/user?email=${obj.userEmail}">${obj.nickname}</a></span></td>
											<td style="width:78%;"><span class="desc">${obj.commentContent}</span></td>
											<td style="width:1%;"><button type="button" onclick="openModal(${obj.rcommentId}, '${obj.userEmail}', '${obj.commentContent}' )" class="btn btn-sm btn-outline-default btn-circle"><i class="bi bi-pencil-fill" aria-hidden="true"></i></button></td>
											<td style="width:1%;"><button type="button" onclick="reportComment(${obj.rcommentId}, '${obj.userEmail}' )" class="btn btn-sm btn-outline-default btn-circle"><i class="bi bi-emoji-expressionless-fill" aria-hidden="true"></i></button></td>
										</tr>
									`;
			});
		}
		$(".notice-list").html(html);
	});
}


/**
 * 뒤로가기
 */
function goList() {
	//	const url = window.location.href;
	//	location.href = url.slice(0, url.indexOf(id)) + '' + window.location.search;
	location.href = "javascript:history.back(-1)";
}

/**
 * 수정하기
 */
function goWrite() {
	fetch(`/api/reviews/modify/${id}`).then(response => {
		if (response.status == 401) {
			alert("해당 글을 수정할 권한이 없습니다.");
			return false;
		} else if (!response.ok) {
			throw new Error('Request failed...');
		}
		location.href = `/review/modify?id=${id}`;
	});
}

/**
 * 삭제하기
 */
function deleteBoard() {

	fetch(`/api/reviews/modify/${id}`).then(response => {
		if (response.status == 401) {
			alert("해당 글을 수정할 권한이 없습니다.");
			return false;
		} else if (!response.ok) {
			throw new Error('Request failed...');
		}


		if (!confirm(`${id}번 게시글을 삭제할까요?`)) {
			return false;
		}

		fetch(`/api/reviews/${id}`, {
			method: 'DELETE',
			headers: { 'Content-Type': 'application/json' },

		}).then(response => {
			if (!response.ok) {
				throw new Error('삭제가 불가능합니다. 관리자에게 문의하세요.');
			}

			alert('삭제되었습니다.');
			goList();

		}).catch(error => {
			alert('오류가 발생하였습니다.');
		});
	});
}


var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = {
		center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
		level: 3 // 지도의 확대 레벨
	};

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다


function panTo(latitude, longitude, travelName) {
	// 이동할 위도 경도 위치를 생성합니다 
	var moveLatLon = new kakao.maps.LatLng(latitude, longitude);

	var markerPosition = new kakao.maps.LatLng(latitude, longitude);

	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
		position: markerPosition
	});

	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);

	// 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	var content = '<div>' +
		'    <span class="fw-bold bg-light form-control p-1 pb-0 pt-0 mb-2">' + travelName + '</span><pre><br></pre>' +
		'</div>';

	// 커스텀 오버레이가 표시될 위치입니다 
	var position = new kakao.maps.LatLng(latitude, longitude);

	// 커스텀 오버레이를 생성합니다
	var customOverlay = new kakao.maps.CustomOverlay({
		map: map,
		position: position,
		content: content,
		yAnchor: 1
	});
	// 지도 중심을 부드럽게 이동시킵니다
	// 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
	map.panTo(moveLatLon);
}




/*]]>*/
