/**
 * 
 */
var count = 0;
var checkedArr = new Array();
var userEmail;

$(document).ready(function () {

	/**
	* 처음 여행지 리스트를 불러와서 card div로 띄우기
	*/
	const rowStart = '<article class="ml-10 mr-10 border-0 d-flex" id="article';
	const rowStart1 = '" style="width:70%;margin-bottom:40px;">\n<div class="card-body card-hover position-relative p-0 m-4" id="card';

	const cardRear = '<input type="radio" class="btn-check" id="like';
	const cardRear1 = '" value="like'
	const cardRear0 = '" name="card';
	const cardRear2 = '" autocomplete="off">\n<label class="btn btn-rise btn-outline-info m-2 p-0 size-40 d-flex rounded-circle" for="like';
	const cardRear3 = '">  <div class="btn-rise-bg bg-info"></div>\n<div class="btn-rise-text"><i class="fs-5 bi bi-hand-thumbs-up"></i></div></label>\n<input type="radio" class="btn-check" id="dislike';
	const cardRear4 = '" value="dislike';
	const cardRear5 = '" autocomplete="off">\n<label class="btn btn-rise btn-outline-danger m-2 p-0 size-40 d-flex rounded-circle" for="dislike';
	const cardRear6 = '">\n<div class="btn-rise-bg bg-tint-danger"></div>\n<div class="btn-rise-text"><i class="fs-5 bi bi-hand-thumbs-down"></i></div></label>\n</div></div>';
	const rowRear = '</article>';

	const nowUrl = new URL(window.location.href);
	const urlParams = nowUrl.searchParams;

	userEmail = urlParams.get('userEmail');

	console.log(userEmail);
	url = '/api/regions/travels' + "?userEmail=" + userEmail;

	fetch(url).then(response => {

		if (!response.ok) {
			throw new Error('Request failed...');
		}
		return response.json();

	}).then(json => {

		// 리스트 형태이므로 대괄호를 중괄호로 바꾸어야 올바른 JSON 형식이 됨
		json = JSON.stringify(json);
		json.replace('[', '{').replace(']', '}');
		const newList = JSON.parse(json);
		var rowCount = 0;
		var section = document.getElementById('cardList');

		// 반복문 시작
		newList.forEach(function (item, index, array) {

			var tag = '';
			var allCards = document.getElementsByClassName('card-body card-hover position-relative p-0').length;
			var newElement = document.createElement('div');

			if (index % 3 == 0) {
				newElement.setAttribute('class', 'row justify-content-center');
				tag += (rowStart + rowCount + rowStart1);
				tag += allCards;
				tag += '">\n';
			} else {
				var cardId = 'card' + allCards;
				newElement.setAttribute('class', 'card-body card-hover position-relative p-0 m-4');
				newElement.setAttribute('id', cardId);
			}

			tag += ('<input type="hidden" id="travel' + allCards + '" value="' + item.travelId + '">');
			tag += '<div class="d-block position-relative overflow-hidden mb-3">\n';
			tag += '<img src="/assets/img/800x600/2.jpg" class="img-fluid img-zoom" alt=""></div>';
			tag += ('<h5 class="mb-2 text-center fw-bold">' + item.travelName + '</h5>\n');
			tag += '<div class="blog-content justify-content-center">\n';
			tag += '<div class="d-flex justify-content-md-evenly text-primary fw-semibold small pb-2  border-primary">';
			tag += (cardRear + allCards + cardRear1 + allCards + cardRear0 + allCards + cardRear2 + allCards + cardRear3);
			tag += (allCards + cardRear4 + allCards + cardRear0 + allCards + cardRear5 + allCards + cardRear6);

			var articleId = 'article' + rowCount;
			var div = document.getElementById(articleId);

			if (index % 3 == 2) {
				tag += rowRear;
				rowCount++;
			} else if (index % 3 == 0) {
				tag += '</div>';
			}
			newElement.innerHTML = tag;
			if (index % 3 == 0) {
				section.appendChild(newElement);
			} else {
				div.appendChild(newElement);
			}

		});

		/**
		 * 각각 radio button의 선택 여부에 따라 label 색 바꿔줌
		 */
		$('input:radio').change(function () {
			var tmp = 0;
			$('input:radio:checked').each(function () {
				tmp++;
				var value = $(this).val();
				var nowName = $(this).prop('name');
				if (checkedArr.indexOf(nowName) == -1) {
					checkedArr.push(nowName);
				}
				var checked = $(this).prop('checked');
				var $label = $(this).next();

				if (checked)
					$label.prop('checked', true);
				else
					$label.prop('cheched', false);

				if (count < tmp) {
					count = tmp;
				}
			});
		});


	}).catch(error => {
		alert('리스트 불러오기에 실패했습니다.\n' + error);
	});

});



// 유효성 검사
function isValid() {

	var allCards = document.getElementsByClassName('card-body card-hover position-relative p-0').length;

	for (var i = 0; i < allCards; i++) {
		var inputId = 'like' + i;
		var nowId = 'card' + i;
		if (checkedArr.indexOf(nowId) == -1) {
			blankId = inputId;
			break;
		}
	}

	if (count < 10) {
		if (confirm("10곳 이상의 여행지에 설문을 완료해야 회원 가입 포인트를 얻을 수 있습니다.\n계속 진행하시겠습니까?")) {
			return true;
		} else {
			document.getElementById(blankId).focus();
			return false;
		}
	}

	return true;
}

// 결과 UserTravel class에 저장
function save() {

	if (!isValid())
		return false;
	console.log('validated');

	var allCards = document.getElementsByClassName('card-body card-hover position-relative p-0').length;

	var resultList = new Array();

	for (var i = 0; i < allCards; i++) {
		var likeNow = 'like' + i;
		var dislikeNow = 'dislike' + i;
		var travelNow = 'travel' + i;

		if (document.getElementById(likeNow).checked) {
			var data = new Object();

			data.userEmail = userEmail;
			data.travelId = document.getElementById(travelNow).value;
			data.isLiked = '1';
			data.isVisited = '0';
			data.isWishlisted = '0';
			resultList.push(data);

		} else if (document.getElementById(dislikeNow).checked) {
			var data = new Object();

			data.userEmail = userMail;
			data.travelId = document.getElementById(travelNow).value;
			data.isLiked = '0';
			data.isVisited = '0';
			data.isWishlisted = '0';
			resultList.push(data);
		}
	}

	const params = JSON.stringify(resultList);

	fetch('/api/signup/fin', {
		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
		headers: {
			'Content-Type': 'application/json',
		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
		body: params, /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/

	}).then(response => {
		if (!response.ok) {
			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
		}

		alert('BITravel 회원 가입을 완료하였습니다.');
		location.href = '/';

	}).catch(error => {
		alert('오류가 발생하였습니다. \n' + error);
	});

}
