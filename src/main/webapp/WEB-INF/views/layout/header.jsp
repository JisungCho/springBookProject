<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
<title>메인 페이지</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300&display=swap" rel="stylesheet">
<style>
*{
	font-family: 'Noto Sans KR', sans-serif;
}
.blog-header {
  line-height: 1;
  border-bottom: 1px solid #e5e5e5;
}

.blog-header-logo {
  font-family: "Playfair Display", Georgia, "Times New Roman", serif;
  font-size: 2.25rem;
}

.blog-header-logo:hover {
  text-decoration: none;
}
.nav-scroller {
  position: relative;
  z-index: 2;
  height: 2.75rem;
  overflow-y: hidden;
}

.nav-scroller .nav {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-wrap: nowrap;
  flex-wrap: nowrap;
  padding-bottom: 1rem;
  margin-top: -1px;
  overflow-x: auto;
  text-align: center;
  white-space: nowrap;
  -webkit-overflow-scrolling: touch;
}
</style>
<script type="text/javascript">
	$(document).ready(function(e){
		
		$("#logout").on("click", function(e) {
			e.preventDefault();
			console.log("로그아웃");
			alert("로그아웃 되었습니다");
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/logout");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
		$("#myboard").on("click",function(e){
			e.preventDefault();
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/info/myBoard");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
		$("#myfavorite").on("click", function(e) {
			e.preventDefault();
			console.log("즐겨찾기");
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/info/myFavorite");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
		$("#myinfo").on("click", function(e) {
			e.preventDefault();
			console.log("내 정보");
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/info/myInfo");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
	})
</script>
</head>
<body>
<div class="container">
      <header class="blog-header py-2">
        <div class="row flex-nowrap justify-content-between align-items-center">
          <div class="col-4 pt-1">
          	<sec:authorize access="isAnonymous()">
            	<a class="btn btn-sm btn-outline-secondary" href="/join">회원가입</a>
			</sec:authorize>
          </div>
          <div class="col-4 text-center">
            <a class="blog-header-logo text-dark" href="/board/"><img src="../resources/img/logo.png" alt="Logo"></a>
          </div>
          <div class="col-4 d-flex justify-content-end align-items-center">
          	<sec:authorize access="isAnonymous()">
            	<a class="btn btn-sm btn-outline-secondary" href="/customLogin">로그인</a>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<a id="logout" class="btn btn-sm btn-outline-secondary" href="#">로그아웃</a>
			</sec:authorize>            
          </div>
        </div>
      </header>
	  <sec:authorize access="isAuthenticated()">
        <nav class="nav d-flex justify-content-between">
        	<a class="p-2 text-muted" href="/board/register">글쓰기</a>
          	<a class="p-2 text-muted" id="myinfo" href="#">내 정보 수정</a>
			<a class="p-2 text-muted" id="myboard" href="#">내 글 목록</a>
			<a class="p-2 text-muted" id="myfavorite" href="#">즐겨찾기</a>
        </nav>
       </sec:authorize>
       <form id="headerForm">
	   </form>			
</div>