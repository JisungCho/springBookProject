//전역변수 선언-모든 홈페이지에서 사용 할 수 있게 index에 저장
   var socket = null;
 
   $(document).ready(function (){
	   connectWs();
   });
 
   function connectWs(){
   	sock = new SockJS("/echo");
   	socket = sock;
 
	//소켓이 열리면
   	sock.onopen = function() {
           console.log('info: connection opened.');

    };
 
	//소켓에 메세지가 연결되면
    sock.onmessage = function(evt) {
	 	var data = evt.data;
	   	console.log("ReceivMessage : " + data + "\n");

		//알림의 갯수 가져오기
		$.ajax({
			url : '/alarm/countAlarm',
			type : 'GET',
			contentType : "application/json; charset=utf-8",
			dataType: 'text',
			success : function(data) {
				if(data == '0'){
				}else{
					//notification의 모양을 알림 갯수로 바꿔줌
					$('#bell').attr("class","badge badge-danger");
					$('#bell').text(data);
				}
			},
			error : function(err){
				alert('err');
			}
	   	});
		
		// 모달 알림
		$("#alarm-body").html(data);
		$("#alarmModal").modal("show");
	};
 	
	//소켓이 닫히면
    sock.onclose = function() {
      	console.log('connect close');
    };
 	
	//에러가 발생했을 때
    sock.onerror = function (err) {console.log('Errors : ' , err);};
 
   }