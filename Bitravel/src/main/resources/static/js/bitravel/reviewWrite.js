
/*<![CDATA[*/
		/**
		 * 사진 저장
		 */	
		function sendFile(file, el) {
			var form_data = new FormData();
			form_data.append('file', file);
			$.ajax({
				data : form_data,
				type : "POST",
				url : '/image',
				cache : false,
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(url) {
					$(el).summernote('insertImage', url, function($image) {
						$image.css('width', "50%");
						console.log(url);
					});
				}
			});
		}
			
		/**
		* 여행지 이름으로 리스트 조회
		**/
		 
		 function findTravelName() {
			
			let input = document.getElementById('name');
			var url = '/api/travels/name/' + input.value;
			
			fetch(url).then(response => {
				if (!response.ok) {
					console.log("Request failed...");
					throw new Error('Request failed...');
				}
				return response.json();
				console.log("good")
			}).then(json => {
				let html = '';

				if (!json.length) {
					html = '<td colspan="4">등록된 여행지가 없습니다.</td>';
				} else {
					json.forEach((obj, idx) => {
						html += `
							<tr>
    							<td>${json.length - idx}</td>
    							<td class="text-left">
    								<a href="javascript: void(0);" onclick="put('${obj.travelId}','${obj.travelName}','${obj.latitude}','${obj.longitude}');">${obj.travelName}</a>
    							</td>
    							<td>${obj.address}</td>
    							<td>${obj.travelId}</td>
							</tr>
						`;
					});
				}

				document.getElementById('list').innerHTML = html;
			});
		}
		
		/**
	        * 유효성 검사
	        */
	       function isValid() {
	       
	         	const form = document.getElementById('form');
	       
	       	if (!form.reviewTitle.value.trim()) {
	       		alert('제목을 입력해 주세요.');
	       		form.reviewTitle.value = '';
	       		form.reviewTitle.focus();
	       		return false;
	       	}
	       
	       
	       	if (!form.summernote.value.trim()) {
	       		alert('내용을 입력해 주세요.');
	       		form.summernote.value = '';
	       		form.summernote.focus();
	       		return false;
	       	}
	       
	       	return true;
	       }
		
		/**
         * 게시글 등록(생성/수정)
         */
        function save() {
        
        	if ( !isValid() ) {
        		return false;
        	}
        	
        	const form = document.getElementById('form');
        	var allTravel = document.getElementsByName('form-line').length;
        	var tIdArray = [];
			var isLikedArray = [];
        	
        	for (var i = 0; i < allTravel; i++) {
        		var travelId = 'travelId' + i;
       			var like = 'like' + i;
				var dislike = 'dislike' + i;
				var tid = document.getElementById(travelId).value
        		var tLiked = document.getElementById(like).checked
				var tDisLike = document.getElementById(dislike).checked
       			if(tid !== "" && tLiked) {
       				tIdArray.push(tid);
					isLikedArray.push(1);
        		} else if (tid !== "" && tDisLike) {
					tIdArray.push(tid);
					isLikedArray.push(0);
				} else {
					alert("여행지와 좋아요 선택을 해주세요.")
					return false;
				}
        	}
        	
        	const rimage = form.summernote.value;
        	const thumbNail = rimage.match("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        	console.log(thumbNail);
			const params = {
            		reviewTitle: form.reviewTitle.value,
            		reviewContent: form.summernote.value,
            		travelId: tIdArray,
					isLiked: isLikedArray,
            		thumbNail : null
            	};
			
        	if (thumbNail !== null){
        		 params.thumbNail = thumbNail[1]
        	} 
        	console.log(params);

        	const uri = ( id ) ? `/api/reviews/${id}` : '/api/reviews';
        	const method = ( id ) ? 'PATCH' : 'POST';
        	
        	fetch(uri, {
        		method: method, /*데이터 생성은 무조건 post 방식 이용*/
        		headers: {'Content-Type': 'application/json'}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
        		body: JSON.stringify(
        				params) /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/
        
        	}).then(response => {
        		if (!response.ok) {
        			throw new Error('Request failed...');
        		}
        		if (!id) {
        			alert('저장되었습니다.');
        			location.href = '/review';
        		} else {
        			alert('저장되었습니다.');
					location.href = '/review/'+id;
        		}
        		
        	}).catch(error => {
        		alert('오류가 발생하였습니다.');
        	});
        }
		
		/**
		 * 리뷰글 상세 조회
		 */
		function findreview() {

		    if ( !id ) {
		    	return false;
		    }
		    fetch(`/api/reviews/${id}`).then(response => {
		    	if (!response.ok) {
					throw new Error('Request failed...');
			    }
		    	console.log("id: "+id);
		    	return response.json();

		   	}).then(json => {
		   		console.table(json);
		   		const form = document.getElementById('form');
		   		form.reviewTitle.value = json.reviewTitle;
		   		$('#summernote').summernote('code', json.reviewContent); // summernote html코드로 받으려면 'code'사용!
		   		

		   	}).catch(error => {
		    	alert('게시글 정보를 찾을 수 없습니다.');
		    	location.href = '/review';
		   	});
		}
		
		/**
		* 뒤로가기 
		**/
		
		function goBack() {
			
			if(!id)
				location.href = '/review';
			else
				location.href = '/review/'+id;
			
		}
		/**
         * 여행지 삽입
         **/
		function put(travelId, travelName) {
        	var travelId = travelId;
        	var travelName = travelName;
        	var allSelect = (document.getElementsByName('form-line').length);

        	//모달창 닫기
        	if (allSelect == 1){
        		$("#myModal_1").modal("hide");
        		$("#travelId0").val(travelId);
            	$("#travelName0").val(travelName);
        	}else if(allSelect == 2){
        		$("#myModal_1").modal("hide");
        		$("#travelId1").val(travelId);
            	$("#travelName1").val(travelName);
        	}else {
        		$("#myModal_1").modal("hide");
        		$("#travelId2").val(travelId);
            	$("#travelName2").val(travelName);
       		}	
        }
		
		/*여행지 추가전에 유효성 검사*/
		function addValid() {
			const form = document.getElementById('form');
			var allSelect = (document.getElementsByName('form-line').length);
		
		if (allSelect == 1) {
			if (!form.travelName0.value) {
				alert('여행지를 먼저 선택해 주세요.');
				return false;
				}
			
			}else if(allSelect == 2) {
				if (!form.travelName1.value) {
					alert('여행지를 먼저 선택해 주세요.');
					return false;
					}
				}
		return true;
		}
		
		/**
		*여행지 하나 추가하기
 		**/
 		
 		function addTravel() {
 			
			if(!addValid()) {
				return false;
			}
			var allSelect = (document.getElementsByName('form-line').length);
			var allSelect1 = allSelect + 1;
			
 			if (allSelect == 3) {
 				alert('최대 3곳까지 등록할 수 있습니다.');
 				return false;
 			}

 			var travelId = 'travelId' + allSelect;
 			var travelName = 'travelName' + allSelect;
 			//tag = "<label for= reviewTravel' name='line'>여행지</label>\n";
 			tag = "<div style='display: flex;'>\n";
			tag += "<div class='blog-content justify-content-center'><div class='d-flex justify-content-md-evenly text-primary fw-semibold small pb-2  border-primary'>";
			tag += "<input type='radio' class='btn-check' id='like" + allSelect + "' value='like" + allSelect + "' name='card" + allSelect + "' autocomplete='off'> <label class='btn btn-rise btn-outline-info m-2 p-0 size-40 d-flex rounded-circle' for='like" + allSelect + "'>";
			tag += "<div class='btn-rise-bg bg-info'></div><div class='btn-rise-text'><i class='fs-5 bi bi-hand-thumbs-up'></i></div></label>";
			tag += "<input type='radio' class='btn-check' id='dislike" + allSelect + "' value='dislike" + allSelect + "' name='card" + allSelect + "' autocomplete='off'><label class='btn btn-rise btn-outline-danger m-2 p-0 size-40 d-flex rounded-circle' for='dislike" + allSelect + "'>";
			tag += "<div class='btn-rise-bg bg-tint-danger'></div><div class='btn-rise-text'><i class='fs-5 bi bi-hand-thumbs-down'></i></div></label></div></div>";
 			tag += "<input type='hidden' id='travelId" + allSelect + "'class='form-control'placeholder='여행지 Id'/>\n";
 			tag += "<input type='text' id='travelName" + allSelect + "' class='form-control mb-2' placeholder='여행지를 선택해 주세요.' readonly/>\n ";
 			tag += "<button type='button' class='btn btn-outline-danger mb-2' id='removeBt" + allSelect + "'onclick='removeTravel(this.id)' style='width: 200px; margin-left: 10px;'>삭제</button>\n </div>"
 			var div = document.getElementsByName('only-form')[0];
 			var newElement = document.createElement('div');
 			var firstChild = div.firstChild;
 			newElement.setAttribute('class', 'form-group');
 			newElement.setAttribute('name', 'form-line');
 			newElement.setAttribute('id', 'removeId'+allSelect)
 			newElement.innerHTML = tag;
 			div.insertBefore(newElement, firstChild);
 		}
		
 		/**
 		* 여행지 삭제
 		**/
 		function removeTravel(id) {
 			var allSelect = (document.getElementsByName('form-line').length);
 			
 			var ele1 = document.getElementById('removeId1');
 			var ele2 = document.getElementById('removeId2');
 			
 			if (id == 'removeBt1') {
 				ele1.remove();
 				return true;
 			} else if (id == 'removeBt2'){
 				ele2.remove();
 				return true;
 			} else {
 				alert("다시한번 시도해 주세요.")
 				return false;
 			}
 		}
 		
	/*]]>*/