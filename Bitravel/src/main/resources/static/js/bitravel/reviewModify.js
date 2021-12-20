
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
        	
        	const rimage = form.summernote.value;
        	const thumbNail = rimage.match("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        	console.log(thumbNail);
			const params = {
            		reviewTitle: form.reviewTitle.value,
            		reviewContent: form.summernote.value,
            		thumbNail : null
            	};

        	if (thumbNail !== null){
        		 params.thumbNail = thumbNail[1]
        	} 

        	console.log(params);
        	
        	fetch(`/api/reviews/${id}`, {
        		method: 'PATCH', /*데이터 생성은 무조건 post 방식 이용*/
        		headers: {'Content-Type': 'application/json'}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
        		body: JSON.stringify(
        				params) /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 게시글 정보를 API 서버로 전달한다.*/
        
        	}).then(response => {
        		if (!response.ok) {
        			throw new Error('Request failed...');
        		}
        			alert('저장되었습니다.');
        			location.href = 'javascript:history.back(-1)';
        		
        		
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
				location.href = 'javascript:history.back(-1)';
		}
		
 		
	/*]]>*/