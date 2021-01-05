<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="./layout/header.jsp"%>
<div class="container">
	<form action="/join" method="post">
		<div class="form-group">
			<label for="userid">Username</label>&nbsp<button id="checkId" class="btn btn-outline-danger btn-sm">아이디 체크</button>
			<input type="text" name="userid" class="form-control" placeholder="Enter Username">
		</div>
		<div class="form-group">
			<label for="pwd">Password</label> 
			<input type="password"  name="userpw" class="form-control" placeholder="Enter password">
		</div>
		<div class="form-group">
			<label for="userName">Username</label> 
			<input type="text"  name="userName" class="form-control" placeholder="Enter username">
		</div>
		<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="auth" value="ROLE_MEMBER">
		<button id="btn-join" class="btn btn-primary">회원가입</button>
	</form>
</div>
<script>
//모든 Ajax 전송 시 CSRF토큰을 같이 전송하도록 세팅
	$(document).ajaxSend(function(e, xhr,options) {
		xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	});
	var csrfHeaderName = "${_csrf.headerName}";
	var csrfTokenValue = "${_csrf.token}";
	$(document).ready(function(e){
		
		var check = false;
		var finalId = null;
		
		$("#checkId").on("click",function(e){
			e.preventDefault();
			var userid = $("input[name='userid']").val(); //입력한 유저아이디
			console.log(userid);
			
			if($("input[name='userid']").val() == ""){
				alert("아이디는 공백일 수 없습니다.");
			}
			
			$.ajax({
	            url:"/checkId",
	            data: userid,
	            contentType : "application/json; charset=utf-8", 
	            type:"post",
             	success : function (result) { //아이디 조회 결과
             		alert("해당 아이디를 사용할 수 있습니다.");
             		check = true;
             		finalId = userid;
            	 },
	            error: function (result) {
	            	alert("해당 아이디를 사용할 수 없습니다.");
				}
	   		}); 
		});
		
		$("#btn-join").on("click",function(e){
			e.preventDefault();
			var id =  $("input[name='userid']").val();
			var name =  $("input[name='userName']").val();
			var pw =  $("input[name='userpw']").val();
			console.log(id);
			console.log(name);
			console.log(pw);
			
			if(id == null || id=="" ){ //id , pw , name이 비어있는 경우
				alert("아이디를 입력해주세요");
			}else if(pw == null || pw==""){
				alert("비밀번호를 입력해주세요");
			}else if(name == null || name==""){
				alert("이름을 입력해주세요")
			}else if( check == false || id != finalId){ //중복 체크를 안했거나 , 중복체크를 한 아이디와 다르게 입력한 경우
				alert("중복 체크를 해주세요");
			}else{
				$("form").submit();
			}

			
		});
	});
</script>
<%@ include file="./layout/footer.jsp"%>



