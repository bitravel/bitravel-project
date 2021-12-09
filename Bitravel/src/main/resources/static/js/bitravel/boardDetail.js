/**
* 게시글 상세 조회
*/
function findBoard() {

	fetch(`/api/boards/${id}`).then(response => {
		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {
		console.table(json);
		json.boardDate = moment(json.boardDate).format('YYYY-MM-DD HH:mm:ss');
		Object.keys(json).forEach(key => {
			const elem = document.getElementById(key);
			if (elem) {
				elem.innerHTML = json[key];
			}

		});

	}).catch(error => {
		alert('게시글 정보를 찾을 수 없습니다.');
		goList();
	});
}

/**
 * 댓글 수정 창
 */
function openModal(commentId, nickname, content) {

	$("#commentModal").modal("toggle");

	document.getElementById("modalContent").value = content;

	document.getElementById("btnCommentUpdate").setAttribute("onclick", "updateComment(" + commentId + ")");
	document.getElementById("btnCommentDelete").setAttribute("onclick", "deleteComment(" + commentId + ")");
}

/**
 * 댓글 수정 창 닫기
 */
function closeModal() {

	$("#commentModal").modal("hide");
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
	if(content.value.length>1000) {
		alert('댓글은 최대 1000자까지 입력 가능합니다.');
		content.value = content.value.slice(0,999);
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

	var uri = `/api/boards/comments/${id}`;
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = { "boardId": id, "commentContent": content.value };
	$.ajax({
		url: uri,
		type: "POST",
		headers: headers,
		dataType: 'json',
		data: JSON.stringify(params),
		success: function (response) {
			if (response.result==false) {
				alert("댓글 등록에 실패하였습니다.");
				return false;
			}
			printCommentList();
			content.value = "";
		},
		error: function (xhr, status, error) {
			alert("일시적인 오류가 발생하였습니다.");
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
	if(content.value.length>1000) {
		alert('댓글은 최대 1000자까지 입력 가능합니다.');
		content.value = content.value.slice(0,999);
		content.focus();
		return false;
	}

	var uri = `/api/boards/comments/${commentId}`;
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "PATCH" };
	var params = { "commentId": commentId, "commentContent": content.value };

	$.ajax({
		url: uri,
		type: "PATCH",
		headers: headers,
		dataType: "json",
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

	var uri = '/api/boards/comments/' + commentId;
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
* 댓글 조회
**/
function printCommentList() {

	fetch(`/api/boards/comments/${id}`).then(response => {
		if (response.ok) {
			return response.json();
		}
	}).then(json => {
		console.table(json) //확인
		let html = '';

		if (!json.length) {
			html = '<td colspan="10" style="text-align:center;">등록된 댓글이 없습니다.</td>';
		} else {
			json.forEach((obj) => {
				html += `
									<tr class="form-control mb-2">
										<td style="width:20%;"><span class="fw-bold">${obj.nickname}</span></td>
										<td style="width:78%;"><span class="desc">${obj.commentContent}</span></td>			
										<td style="width:2%;"><button type="button" onclick="openModal(${obj.bcommentId}, '${obj.nickname}', '${obj.commentContent}' )" class="btn btn-sm btn-outline-default btn-circle"><i class="bi bi-pencil-fill" aria-hidden="true"></i></button></td>
									</tr>
								`;
			});
		}
		$(".notice-list").html(html);
	});
}
/*[- end of function -]*/



/**
 * 뒤로가기
 */
function goList() {
	const url = window.location.search;
	location.href = `/board/list` + url;
	/* location.href = "javascript:history.back(-1)";*/
}

/**
 * 수정하기
 */
function goWrite() {
	location.href = `/board/write?id=${id}`;
}

/**
 * 삭제하기
 */
function deleteBoard() {

	if (!confirm(`${id}번 게시글을 삭제할까요?`)) {
		return false;
	}

	fetch(`/api/boards/${id}`, {
		method: 'DELETE',
		headers: { 'Content-Type': 'application/json' },

	}).then(response => {
		if (!response.ok) {
			throw new Error('Request failed...');
		}

		alert('삭제되었습니다.');
		goList();

	}).catch(error => {
		alert('오류가 발생하였습니다.');
	});
}