function loadNavbar(largeGov) {
	
	var smallList = document.getElementById('smallList');
	var tag0 = '<li class="col-8 col-sm-6 text-center mt-3"><a class="fs-5 text-muted" href="/travel/list/';
	var tag3 = '">';
	var tag2 = '</a>';
	var tag = "";

	tag += tag0 + largeGov + tag3 + '< '+largeGov+' 리스트로 돌아가기';
	tag += tag0 + tag3 + '< '+' 전국 리스트로 돌아가기';
	smallList.innerHTML += tag;

}


/**
 * 뒤로가기
 */
function goBack() {
	/* location.href = `/board/list?page=[]`; 페이지 넘버를 불러오는걸 모르겠습니다..*/
	location.href = "javascript:history.back(-1)";
}
