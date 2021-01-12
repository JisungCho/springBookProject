<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- 양식 다시 제출확인 처리 -->
<%  
response.setHeader("Cache-Control","no-store");  
response.setHeader("Pragma","no-cache");  
response.setDateHeader("Expires",0);  
if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
%>
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
<!-- google chart -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<!-- 소켓라이브러리 -->
<!-- 로그인되어있는 경우에만 소켓연결 -->
<sec:authorize access="isAuthenticated()">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>
</sec:authorize>
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

.popover-body {
    height: 100px;
    overflow-y: auto;
}

.alert_list{
	font-size: 11px; color:grey;
}

.alert_li {
  font-size: 11px; 
  color:grey;
  padding:10px 0px 2px 0px;
  border-bottom: thin solid #c0c0c0;
}

.alert_li:hover{background-color:#eee;}

.turn_off_alert{
	float:right;
	margin-bottom :1px;
	color:red;
}

</style>
<script type="text/javascript">
	$(document).ready(function(e){
		
		//모든 Ajax 전송 시 CSRF토큰을 같이 전송하도록 세팅
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
		
		$(document).ajaxSend(function(e, xhr,options) {
			xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
		});
		
		var alarm="";
		
		//알람 목록
		function getAlarm(callback,error){
			$.getJSON("/alarm/getAlarm.json",
					function(data){
						if(callback){
							callback(data);
						}
					}).fail(function(xhr,status,err){
						if(error){
							error();
						}
			});
		}
		//By 2021-01-06 , 01-07
		//ajax를 통해서 유저아이디를 보내는데 해당아이디로 db를 조회해서 모든 checked를 true로 만들어줌
		function allChecked(){
			
			var receiverId = null;			
			<sec:authorize access="isAuthenticated()">
				receiverId = '<sec:authentication property="principal.username"/>';
			</sec:authorize>
			
			$.ajax({
				type : 'put', //put방식
				url : '/alarm/allChecked',  //url
				success : function(result, status, xhr) { //성공시
					console.log("checked all true");
				},
				error : function(xhr, status, er) { //실패시
					console.log("error");
				}
			})
		}
		
		$("#logout").on("click", function(e) { //토큰 전달과 로그아웃 처리
			e.preventDefault();
			console.log("로그아웃");
			alert("로그아웃 되었습니다");
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/logout");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
		$("#myboard").on("click",function(e){ //내글 목록 클릭 시 토큰 전달과 내글 목록으로 이동
			e.preventDefault();
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/info/myBoard");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
		$("#myfavorite").on("click", function(e) { // 북마크 이동
			e.preventDefault();
			console.log("즐겨찾기");
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/info/myFavorite");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
		$("#myinfo").on("click", function(e) { // 내 정보 수정으로 이동
			e.preventDefault();
			console.log("내 정보");
			var str ='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action","/info/myInfo");
			$("#headerForm").attr("method","post");
			$("#headerForm").submit();
		});
		
 		//notification클릭
		$("#news").click(function(e) {
			e.preventDefault();
			
			//현재페이지에서 notification모양을 바꿔줌
			$("#bell").empty();
			$("#bell").attr("class", "fa fa-bell");
			
			//해당아이디의 checked를 true로 만들어줌
			allChecked();
			
			//알람 목록을 가져옴
			getAlarm(function(list) {
		  		for (var i = 0; i < list.length; i++) {
 					alarm += "<li data-alert_id='" + list[i].alarmId+"' class='alert_li'>";
					alarm +=  list[i].content;
					alarm += "</li>"; 
				}
		  		$(".list-unstyled").html(alarm);
			});
			
		}); 
		
		$("#news").popover({
			  'title' : '알람목록', 
			  'html' : true,
			  'placement' : 'bottom',
			  'content' : $(".alert_list").html()
		});
	})
</script>
<script type="text/javascript" src="/resources/js/index.js"></script>
</head>
<body>
<div class="container">
      <header class="blog-header py-2">
        <div class="row flex-nowrap justify-content-between align-items-center">
          <div class="col-4 pt-1">
          	<sec:authorize access="isAnonymous()">
            	<a class="btn btn-sm btn-outline-secondary" href="/join">회원가입</a>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<c:if test="${sessionScope.count > 0}">
					<a class="p-2 text-muted" id="news" href="#"><span id="bell" class="badge badge-danger">${sessionScope.count}</span></a>
				</c:if>
				<c:if test="${sessionScope.count == 0}">
					<a class="p-2 text-muted" id="news" href="#"><span id="bell" class="fa fa-bell"></span></a>
				</c:if>
				<div style="display:none" class="alert_list">
				  <ul class="list-unstyled">
	    
				  </ul>
				</div>
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
      <!-- 로그인 상태-->
	  <sec:authorize access="isAuthenticated()">
        <nav class="nav d-flex justify-content-between">
        	<a class="p-2 text-muted" href="/board/register">글쓰기</a>
          	<a class="p-2 text-muted" id="myinfo" href="#">내 정보 수정</a>
			<a class="p-2 text-muted" id="myboard" href="#">내 글 목록</a>
			<a class="p-2 text-muted" id="myfavorite" href="#">북마크</a>
        </nav>
       </sec:authorize>
       <form id="headerForm">
	   </form>
		<!-- Modal -->
		<div class="modal fade" id="alarmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="myModalLabel">Alarm</h4>
					</div>
					<div id="alarm-body" class="modal-body"></div>
					<div class="modal-footer">
						<button type="button" id="modal-close" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->			
</div>