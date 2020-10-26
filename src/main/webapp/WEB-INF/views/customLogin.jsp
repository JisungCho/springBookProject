<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="./layout/header.jsp"%>
<div class="container">
	<form action="/login" method="post">
	<!-- username , password 두개의 값을 가지고 action주소로 이동 -->
		<div class="form-group">
			<label for="username">Username</label> 
			<input type="text" name="username" class="form-control" placeholder="Enter Username">
		</div>
		<div class="form-group">
			<label for="pwd">Password</label> 
			<input type="password"  name="password" class="form-control" placeholder="Enter password">
		</div>
		<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
		<button id="btn-login" class="btn btn-primary">로그인</button>
	</form>
</div>
<%@ include file="./layout/footer.jsp"%>



