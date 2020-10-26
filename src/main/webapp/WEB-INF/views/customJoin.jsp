<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="./layout/header.jsp"%>
<div class="container">
	<form action="/join" method="post">
		<div class="form-group">
			<label for="userid">Username</label> 
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
		<button id="btn-login" class="btn btn-primary">회원가입</button>
	</form>
</div>
<%@ include file="./layout/footer.jsp"%>



