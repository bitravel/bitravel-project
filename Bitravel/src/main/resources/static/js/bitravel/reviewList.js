/**
 * 날짜 출력
 */

function date() {
			var date = document.getElementById("date")
			var now = new Date();
			var nowDate = (now.getMonth()+1) + "월 " + now.getDate() + "일의 인기 후기";
			date.innerHTML = nowDate;
		}