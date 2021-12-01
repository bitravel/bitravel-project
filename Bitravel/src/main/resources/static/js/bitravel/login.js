/**
 * 
 */
 async function login () {
				var email = document.getElementById('email').value;
				var password = document.getElementById('password').value;
				var param = {'email':email, 'password':password};
				loginToken(param);
			};
			
			async function loginToken(param) {
				
				fetch('/api/login', {
	        		method: 'POST', /*데이터 생성은 무조건 post 방식 이용*/
	        		headers: { 
	        			'Content-Type': 'application/json',
	        		}, /*API 호출 시, GET 방식이 아닌 요청은 Content-Type을 application/json으로 설정한다. */
	        		body: JSON.stringify(param), /*데이터 전달에 사용되는 옵션으로, params 객체에 담긴 정보를 API 서버로 전달한다.*/
	        
	        	}).then(response => {
	        		if (!response.ok) {
	        			throw new Error('일시적인 오류입니다. 다시 시도해 보세요.');
	        		}
	        		return response.json();
	        	}).then(json => {
	        		var now = new Date();
	        		var time = now.getTime();
	        		var expireDate = time+24*60*60*1000;
	        		now.setTime(expireDate);
	        		// js에서는 Http only를 설정할 수 없고, server에서 설정할 수 있어야 하나
	        		// 현재 spring에서 server-less 설정이 되어 있고 Spring security 적용 중이어 cookie를 다루는 것이 까다롭다.
	        		// 시간대는 UTC 기준이다. (한국보다 9시간 느림)
	        		document.cookie = "Authorization="+json.token+"; expires="+now.toUTCString()+"; path=/; domain=localhost;secure=true;";
	        		
	        		location.href = '/';
	        		
	        	}).catch(error => {
	        		alert('오류가 발생하였습니다. \n'+error);
	        	});
			};