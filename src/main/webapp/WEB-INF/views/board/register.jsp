<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form role="form" action="/board/register" method="post">
		<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="thumbnail" value="">
		<input type="hidden" name="title" value="">
		<input type="hidden" name="authors" value="">
		<input type="hidden" name="url" value="">
		
		<div class="form-group w-50">
			<label for="title">Title</label> 
			<input type="text" class="form-control" placeholder="Enter title" name="subject">
		</div>
		
		<!-- 사라질 항목 (로그인한 회원이 작성자가 되게...) ------------------------------------------------->
		<div class="form-group w-50">
			<label for="writer">Writer</label> 
			<input type="text" class="form-control" placeholder="Enter writer" name="writer" value='<sec:authentication property="principal.username"/>' readonly="readonly">
		</div>
		<!-- ----------------------------------------------------------------------------- -->
		
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
	function getReturnValue(returnValue) { 
		
		console.log(returnValue);
		returnValue.find("button").remove();
		$("#selectBook").html(returnValue);
	}
	
	$(document).ready(function(e){
		var formObj = $("form");
		
		$("#btn-search").on("click", function(e) {
			window.open('/board/bookSearch','window_name','width=430,height=500,location=no,status=no,scrollbars=yes');
		});
		
		$("#btn-write").on("click",function(e){ 
			
			e.preventDefault(); 
			//2020-10-12 
			//Ajax통해 받아온 책의 정보를 form태그의 input태그 값으로 넣어줘서 전달
			//제목, 내용 , 도서정보 미입력시  게시물 등록이 수행되지 않음 
			
			var thumbnail = $("#thumbnail").attr("src");
			var title = $("#title").text();
			var authors = $("#authors").text();
			var url = $("#url").attr("href");
			
			$("input[name='thumbnail']").val(thumbnail);
			$("input[name='title']").val(title);
			$("input[name='authors']").val(authors);
			$("input[name='url']").val(url);
			;
			if($("input[name='subject']").val() == '' || $("input[name='subject']").val() == null ){
				alert("제목을 입력해주세요!");
				return;
			}
			if($("input[name='writer']").val() == '' || $("input[name='writer']").val() == null ){
				alert("작성자를 입력해주세요!");
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
	
			formObj.submit();
		});
		
	});
	
</script>
<%@ include file="../layout/footer.jsp"%>



