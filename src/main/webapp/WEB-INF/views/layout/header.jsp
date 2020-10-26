<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
<title>메인 페이지</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<script type="text/javascript">
	$(document).ready(function(e){
		
		$("#logout").on("click", function(e) {
			console.log("로그아웃");
			e.preventDefault();
			$("#logoutForm").submit();
			
		})
		
	})
</script>
</head>
<body>
	<nav class="navbar navbar-expand-md bg-white navbar-light border-bottom border-light">
		<a class="navbar-brand" href="/board/"><img src="../resources/img/logo.png" alt="Logo" style="width: 77px;"></a>

		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<ul class="navbar-nav mr-auto">
			<li class="nav-item"><a class="nav-link" href="/board/register">글쓰기</a></li>
			<sec:authorize access="isAuthenticated()">	
				<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">내 정보 </a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="#">내정보 수정</a> 
						<a class="dropdown-item" href="#">내 글 목록</a> 
						<a class="dropdown-item" href="#">즐겨찾기</a>
					</div>
				</li>
			</sec:authorize>
				<!--  
				<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">게시판 </a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="#">독서노트</a> 
						<a class="dropdown-item" href="#">독서모임</a> 
					</div>
				</li>
				-->
			</ul>
			<ul class="navbar-nav">
				<sec:authorize access="isAnonymous()">
					<li class="nav-item"><a class="nav-link" href="/join">회원가입</a></li>
					<li class="nav-item"><a class="nav-link" href="/customLogin">로그인</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item"><a id="logout" class="nav-link" href="#">로그아웃</a></li>
				</sec:authorize>
			</ul>
		</div>
    	<form id="logoutForm" method='post' action="/logout">
			<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
		</form>
	</nav>
	<br>