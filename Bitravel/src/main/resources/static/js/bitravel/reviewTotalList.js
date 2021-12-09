/*<![CDATA[*/

/**
* 게시글 조회
*/
function goView(id) {
	location.href = `/review/detail/${id}`;
}
/**
* 검색 옵션 변경
**/
function option_bt(act) {
	if (act == 'all') {
		document.edit.action = "/review/search/all/list";
	} else if (act == 'title') {
		document.edit.action = "/review/search/title/list";
	} else if (act == 'titleandcontent') {
		document.edit.action = "/review/search/titleandcontent/list";
	} else {
		document.edit.action = "/review/search/nickname/list";
	}
}

/*]]>*/