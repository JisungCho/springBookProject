<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="./layout/header.jsp"%>
<div class="container">
	<form action="/join" method="post">
		<div class="form-group">
			<label for="userid">아이디</label>&nbsp
			<button id="checkId" class="btn btn-outline-danger btn-sm">아이디 체크</button>
			<input type="text" name="userid" class="form-control" placeholder="Enter Username">
		</div>
		<div class="form-group">
			<label for="pwd">비밀번호</label> <input type="password" name="userpw1" class="form-control" placeholder="Enter password">
		</div>
		<div class="form-group">
			<label for="pwd">비밀번호 확인</label> <input type="password" name="userpw" class="form-control" placeholder="Enter password">
		</div>
		<!-- 패스워드 확인 -->
		<div class="alert alert-success" id="alert-success">비밀번호가 일치합니다.</div>
		<div class="alert alert-danger" id="alert-danger">비밀번호가 일치하지 않습니다.</div>
		<div class="form-group">
			<label for="userName">이름</label> <input type="text" name="userName" class="form-control" placeholder="Enter username">
		</div>
		<!-- 이메일인증 -->
		<div class="form-group">
			<label for="email">이메일 인증</label> 
			<input type="text" name="email" class="form-control" placeholder="Enter email">
		</div>
		<div class="form-inline mb-3">
			<div class="mail_check_input_box" id="mail_check_input_box_false">
				<input type="text" class="mail_check_input form-control col-8" disabled="disabled">
				<button id="mail_check_button" class="btn btn-outline-primary btn-sm">인증번호 전송</button>
			</div>
		</div>
		<!-- 인증번호 확인 -->
		<div class="alert alert-success" id="alert-success-email">인증번호가 일치합니다.</div>
		<div class="alert alert-danger" id="alert-danger-email">인증번호가 일치하지 않습니다.</div>
		
		<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}"> 
		<input type="hidden" name="auth" value="ROLE_MEMBER">
		<button id="btn-join" class="btn btn-primary">회원가입</button>
	</form>
</div>
<script>
	//모든 Ajax 전송 시 CSRF토큰을 같이 전송하도록 세팅
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	});
	var csrfHeaderName = "${_csrf.headerName}";
	var csrfTokenValue = "${_csrf.token}";
	$(document).ready(function(e) {
		
		//비밀번호 알림창 가림
		$("#alert-success").hide();
		$("#alert-danger").hide();
		//인증번호 알림창 가림
		$("#alert-success-email").hide();
		$("#alert-danger-email").hide();
		
		//아이디 중복
		var checkId = false;
		var finalId = null;
		var checkPw = false;
		var finalPw = null;
		var checkCode = false;

		//아이디 중복 검사
		$("#checkId").on("click", function(e) {
			e.preventDefault();
			var userid = $("input[name='userid']").val(); //입력한 유저아이디
			console.log(userid);

			if ($("input[name='userid']").val() == "") {
				alert("아이디는 공백일 수 없습니다.");
				return false;
			}

			$.ajax({
				url : "/checkId",
				data : userid,
				contentType : "application/json; charset=utf-8",
				type : "post",
				success : function(result) { //아이디 조회 결과
					alert("해당 아이디를 사용할 수 있습니다.");
					checkId = true;
					finalId = userid;
				},
				error : function(result) {
					alert("해당 아이디를 사용할 수 없습니다.");
				}
			});
		});

		//비밀번호 체크
		$("input[type='password']").keyup(function() {
			var pwd1 = $("input[name='userpw1']").val();
			var pwd2 = $("input[name='userpw']").val();
			if (pwd1 != "" || pwd2 != "") {
				if (pwd1 == pwd2) {
					$("#alert-success").show();
					$("#alert-danger").hide();
					finalPw = pwd2;
					checkPw = true;
				} else {
					$("#alert-success").hide();
					$("#alert-danger").show();
					checkPw = false;
				}
			}
		});

		//회원가입 버튼 클릭
		$("#btn-join").on("click", function(e) {
			e.preventDefault();
			var id = $("input[name='userid']").val();
			var name = $("input[name='userName']").val();
			var pw = $("input[name='userpw']").val();
			
			console.log("회원가입_아이디: "+id);
			console.log("회원가입_이름: "+name);
			console.log("회원가입_비밀번호: "+pw);
			
			//아이디,비밀번호 유효성 검사
			if (id == null || id == "") { 
				alert("아이디를 입력해주세요");
				return false;
			} 
			if (pw == null || pw == "") {
				alert("비밀번호를 입력해주세요");
				return false;
			} 
			if (name == null || name == "") {
				alert("이름을 입력해주세요");
				return false;
			} 
			if (checkId == false || id != finalId) { 
				alert("아이디 중복 체크를 해주세요");
				return false;
			} 
			if(checkPw == false || pw != finalPw) {
				alert("비밀번호를 정확히 입력하세요");
				return false;
			}
			if(checkCode == false){
				alert("인증번호를 확인해주세요");
				return false;
			}
			$("form").submit();
		});
		
		var code = "";
		
		//인증번호 이메일 전송
		$("#mail_check_button").on("click",function(e){
			e.preventDefault();
			var email = $("input[name='email']").val();
			var checkBox = $(".mail_check_input");
			
			$.ajax({
				type:"GET",
				url : "/mailCheck",
				data : {email : email},
				contentType :"text/plain;charset=UTF-8",
				success : function(data){ //인증번호를 가져옴
					checkBox.attr("disabled",false); //인증번호 입력 가능
					checkBox.val(''); // 기존에 값이 있었으면 지워줌
					$("#alert-success-email").hide();
					$("#alert-danger-email").hide();
					checkCode = false;
					code = data; // 인증번호를 변수에 저장
				}
			});
		});
		
		//인증코드 입력 시 동일성 확인
		$(".mail_check_input").keyup(function() {
			var inputCode = $(".mail_check_input").val();
			if (inputCode != "" || code != "") {
				if (inputCode == code) {
					$("#alert-success-email").show();
					$("#alert-danger-email").hide();
					$(".mail_check_input").attr("disabled",true); //인증번호 입력 멈춤
					checkCode = true;
				} else {
					$("#alert-success-email").hide();
					$("#alert-danger-email").show();
					checkCode = false;
				}
			}
		});
	});
</script>
<%@ include file="./layout/footer.jsp"%>



