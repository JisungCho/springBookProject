<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form role="form" action="/board/register" method="post">
		<!-- 스프링 시큐리티는 get방식을 제외하고 모든 요청에 csrf토큰을 사용 -->
		<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
		<!-- 책 정보 -->
		<input type="hidden" name="thumbnail" value="">
		<input type="hidden" name="title" value="">
		<input type="hidden" name="authors" value="">
		<input type="hidden" name="url" value="">
		
		<div class="form-group w-50">
			<label for="title">Title</label> 
			<input type="text" class="form-control" placeholder="Enter title" name="subject">
		</div>
		<div class="form-group w-50">
			<label for="writer">Writer</label> 
			<input type="text" class="form-control" placeholder="Enter writer" name="writer" value='<sec:authentication property="principal.username"/>' readonly="readonly">
		</div>
		<div class="form-group">
			<label for="content">Content</label>
			<textarea class="form-control summernote" rows="5" name="content"></textarea>
		</div>
		<div id="bookSearch" class="form-group">
			<label for="content">Book</label>
			<br/>
			<button type="button" id="btn-search" class="btn btn-success">도서검색</button>
			<br/>
			<br/>
			
			<!-- 비어있거나 책 목록이 있거나 -->
			<div id="selectBook">
			
			</div>
		</div>
		<button id="btn-write" class="btn btn-primary">글쓰기완료</button>
	</form>

</div>
<script>
	$('.summernote').summernote({
		tabsize : 2,
		height : 300
	});
</script>
<script>
	function getReturnValue(returnValue) { //도서검색 페이지에서 처리할 함수 
		
		console.log(returnValue);
		returnValue.find("button").remove();
		$("#selectBook").html(returnValue);
	}
	
	$(document).ready(function(e){
		var formObj = $("form");
		
		//도서검색 페이지로 이동
		$("#btn-search").on("click", function(e) {
			window.open('/board/bookSearch','window_name','width=600,height=500,location=no,status=no,scrollbars=yes');
		});
		
		//글 등록
		$("#btn-write").on("click",function(e){
			
			e.preventDefault(); 
			//getReturnValue를 통해 선택한 책 정보 태그를 추가하고 
			//input에 value로 값을 넣어줌
			
			var thumbnail = $("#thumbnail").attr("src");
			var title = $("#title").text();
			var authors = $("#authors").text();
			var url = $("#url").attr("href");
			
			$("input[name='thumbnail']").val(thumbnail);
			$("input[name='title']").val(title);
			$("input[name='authors']").val(authors);
			$("input[name='url']").val(url);
			
			//제목, 내용 , 도서정보 미입력시  게시물 등록이 수행되지 않음 
			if($("input[name='subject']").val() == '' || $("input[name='subject']").val() == null ){
				alert("제목을 입력해주세요!");
				return;
			}			
			if($(".summernote").val() == '' || $(".summernote").val() == null ){
				alert("내용을 입력해주세요!");
				return;
			}
 			if($("#selectBook").children().length == 0){
				alert("도서 정보를 입력해주세요");
				return;
			}  
			//제목,작성자,내용,도서정보 등이 전송됨
			formObj.submit();
		});
		
	});
	
</script>
<%@ include file="../layout/footer.jsp"%>



