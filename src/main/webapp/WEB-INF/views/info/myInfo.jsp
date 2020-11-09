<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>
<div class="container">
		<h1 class="font-weight-bold my-4 text-center">내 정보 수정</h1>
		<div class="form-group">
			<label for="userid">ID</label> 
			<input type="text" name="userid" class="form-control" placeholder="Enter Username" value="${member.userid}" readonly="readonly">
		</div>
		<c:if test="${member.authList[0].auth == 'ROLE_MEMBER' }">
			<div class="form-group">
				<label for="pwd">New Password</label> 
				<input type="password"  id="check1" class="form-control" placeholder="Enter password" >
			</div>
			<div class="form-group">
				<label for="pwd">New Password Check</label>&nbsp<button id="checkPw" class="btn btn-outline-primary btn-sm">비밀번호 체크</button>
				<input type="password" id="check2" name="userpw" class="form-control" placeholder="Enter password" >
			</div>
		</c:if>
		<div class="form-group">
			<label for="userName">Username</label> 
			<input type="text"  name="userName" class="form-control" placeholder="Enter username" value="${member.userName }">
		</div>
		<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="checkable" value=true/>
		<button id="btn-info" class="btn btn-primary">수정</button>
</div>
<script>
	$(document).ready(function(e){
		
		console.log
		
		
		//모든 Ajax 전송 시 CSRF토큰을 같이 전송하도록 세팅
		$(document).ajaxSend(function(e, xhr,options) {
			xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
		});
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
		var checkable = false;
		var userid = $("input[name='userid']").val();
		var userpw = null;
		var userName = $("input[name='userName']").val()
		
		
		$("#checkPw").on("click",function(e){ //비밀번호 체크 클릭
			e.preventDefault();
			var pw1 = $("#check1").val();
			var pw2 = $("#check2").val();
			if(pw1 == pw2){
				
				alert("비밀번호가 일치합니다");
				userpw = pw2;
				checkable = true;
				return;
			}else if(pw1==""||pw2==""){
				alert("비밀번호는 필수 정보입니다.")
				return;
			}else if(pw1 != pw2){
				alert("비밀번호가 일치하지 않습니다.");
				return;
			}
		});
		
		$("#btn-info").on("click",function(e){ //회원수정 버튼 클릭
			var pw1 = $("#check1").val();
			var pw2 = $("#check2").val();
			
			
			if( $("input[name='userName']").val() == "" ){
				alert("유저이름은 필수 정보입니다.");
				return;
			}
			//비밀번호 체크 했고 , 다시 비밀번호 입력 폼의 비밀번호가 같은지 확인
			if(checkable == true && (pw1 == pw2)){
				var data = {userid:userid , userpw:userpw , userName:userName};
		         
				$.ajax({
			            url:"/info/update",
			            data: JSON.stringify(data),
			            contentType : "application/json; charset=utf-8", 
			            type:"post",
		             	success : function (result) {
		             	alert("회원정보가 수정되었습니다.");
		             	self.location="/"
		             }
			   }); 
			}else{
				alert("다시 확인해주세요");
			}
		});
	});
</script>
<%@ include file="../layout/footer.jsp"%>



