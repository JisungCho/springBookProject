<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<div class="form-group w-50">
		<label for="title">Title</label> <input type="text" class="form-control" placeholder="Enter title" name="subject" value="${board.subject }" readonly="readonly">
	</div>
	<div class="form-group w-50">
		<label for="writer">Writer</label> <input type="text" class="form-control" placeholder="Enter writer" name="writer" value="${board.writer }" readonly="readonly">
	</div>
	<div class="form-group">
		<label for="content">Content</label>
		<textarea class="form-control summernote" rows="5" name="content">${board.content }</textarea>
	</div>
	<div id="bookSearch" class="form-group">
		<label for="content">Book</label> <br />
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
	<button data-oper="modify" class="btn btn-primary">수정하기</button>
	<button data-oper="close" class="btn btn-info">뒤로가기</button>
	
	<form id='operForm' action="/board/modify" method="get">
		<input type="hidden" id="boardId" name="boardId" value='<c:out value="${board.boardId }"></c:out>'>
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
	$(document).ready(function(e){
		$('.summernote').summernote('disable');
		
		
		var operForm = $("#operForm");
	
		//close버튼 클릭시 메인페이지로 이동하는데 이동할 떄 pageNum과 amount를 전송
		$("button[data-oper='close']").on("click",function(e){
			console.log("close");
			operForm.find('#boardId').remove();
			operForm.attr("action","/board/");
			operForm.submit();
		});
		
		$("button[data-oper='modify']").on("click",function(e){
			console.log("modify");
			operForm.submit();
		});
	});
</script>
<%@ include file="../layout/footer.jsp"%>



