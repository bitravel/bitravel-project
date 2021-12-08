/*<![CDATA[*/

/**
* 게시글 조회
*/
function goView(id) {
	location.href = `/board/detail/${id}`;
}
/**
* 검색 옵션 변경
**/
function option_bt(act) {
	if (act == 'all') {
		document.edit.action = "/board/search/all/list";
	} else if (act == 'title') {
		document.edit.action = "/board/search/title/list";
	} else if (act == 'titleandcontent') {
		document.edit.action = "/board/search/titleandcontent/list";
	} else {
		document.edit.action = "/board/search/nickname/list";
	}
}

/*]]>*/