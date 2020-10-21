<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form id='operForm' action="/board/modify" method="post">
		<div class="form-group w-50">
			<label for="title">Title</label> <input type="text" class="form-control" placeholder="Enter title" name="subject" value="${board.subject }">
		</div>
		<div class="form-group w-50">
			<label for="writer">Writer</label> <input type="text" class="form-control" placeholder="Enter writer" name="writer" value="${board.writer }" readonly="readonly">
		</div>
		<div class="form-group">
			<label for="content">Content</label>
			<textarea class="form-control summernote" rows="5" name="content">${board.content }</textarea>
		</div>
		<div id="bookSearch" class="form-group">
			<label for="content">Book</label><br/>
			<button type="button" id="btn-search" class="btn btn-success">도서수정</button>
			<br/>
			<br/>
			<!-- 비어있거나 책 목록이 있거나 -->
			<div id="selectBook">
				<div id="bookCard0" class="card" style="max-width: 500px;">
					<div class="row no-gutters">
						<div class="col-sm-5">
							<img id="thumbnail" src="${board.book.thumbnail }" class="card-img-top h-60" alt="이미지 없음">
						</div>
						<div class="col-sm-7">
							<div class="card-body">
								<h5 id="title" class="card-title">${board.book.title }</h5>
								<h6 id="authors" class="card-subtitle mb-2 text-muted">${board.book.authors}</h6>
								<h6 class="card-subtitle mb-2 text-muted">
									<a id="url" target="_blank" href="${board.book.url }">상세보기</a>
								</h6>
								<br>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<button data-oper="modify" class="btn btn-primary">수정완료</button>
		<button data-oper="delete" class="btn btn-danger">삭제하기</button>
		<button data-oper="close" class="btn btn-info">뒤로가기</button>
		
		<!-- 게시물 번호 -->
		<input type="hidden" name="boardId" value="${board.book.boardId }">
		
		<!-- 수정한 책 정보 -->
		<input type="hidden" name="bookId" value="${board.book.bookId }">
		<input type="hidden" name="thumbnail" value="${board.book.thumbnail }">
		<input type="hidden" name="title" value="${board.book.title}">
		<input type="hidden" name="authors" value="${board.book.authors }">
		<input type="hidden" name="url" value="${board.book.url }">
	
		<!-- 페이지,검색 정보-->
		<input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"></c:out>'> 
		<input type="hidden" name="type" value='<c:out value="${cri.type }"></c:out>'> 
		<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"></c:out>'> 
		<input type="hidden" name="amount" value='<c:out value="${cri.amount }"></c:out>'> 
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
		
		
		var operForm = $("#operForm");
	
		//close버튼 클릭시 메인페이지로 이동하는데 이동할 떄 pageNum과 amount를 전송
		$("button[data-oper='close']").on("click",function(e){
			console.log("close");
			operForm.find('#boardId').remove();
			operForm.attr("action","/board/");
			operForm.submit();
		});
		
		//수정완료 버튼 클릭시
		$("button[data-oper='modify']").on("click",function(e){
			e.preventDefault();
			console.log("수정완료");
			
			
			var thumbnail = $("#thumbnail").attr("src");
			var title = $("#title").text();
			var authors = $("#authors").text();
			var url = $("#url").attr("href");
			
			
			$("input[name='thumbnail']").val(thumbnail);
			$("input[name='title']").val(title);
			$("input[name='authors']").val(authors);
			$("input[name='url']").val(url);
			
			operForm.submit(); 
		});
		
		$("button[data-oper='delete']").on("click",function(e){
			e.preventDefault();
			console.log("삭제하기");
			operForm.attr("action","/board/remove");
			operForm.submit(); 
		});
		
		//도서 검색 클릭시
		$("#btn-search").on("click", function(e) {
			window.open('/board/bookSearch','window_name','width=430,height=500,location=no,status=no,scrollbars=yes');
		});
	});
</script>
<%@ include file="../layout/footer.jsp"%>



