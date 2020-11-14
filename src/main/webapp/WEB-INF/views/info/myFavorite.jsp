<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<h1 class="font-weight-bold my-4 text-center">즐겨찾기</h1>
	<div id="bookResult">
		<c:forEach items="${books }" var="result">
			<div id="bookCard'+i+'" class="card mx-auto" style="max-width: 700px;max-height: 300px">
				<div class="row no-gutters">
					<div class="col-sm-3">
						<img id="thumbnail" src="${result.thumbnail }" class="card-img-top h-60" alt="이미지 없음">
					</div>
					<div class="col-sm-7">
						<div class="card-body">
							<h5 id="title" class="card-title">${result.title }</h5>
							<h6 id="authors" class="card-subtitle mb-2 text-muted">${result.authors}</h6>
							<h6 class="card-subtitle mb-2 text-muted">
								<a id="url" target="_blank" href="${result.url }">상세보기</a>
							</h6>
							<button data-number="${result.favoriteId}" class="btn btn-outline-danger">삭제</button>
						</div>
	
					</div>
				</div>
			</div>
			<br/>
		</c:forEach>
			<nav aria-label="Page navigation example">
				<ul class="pagination justify-content-end">
					<c:if test="${pageMaker.prev }">
						<li class="page-item"><a class="page-link" href="${pageMaker.startPage-1 }">Previous</a></li>
					</c:if>
					<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage }">
						<li class="page-item ${pageMaker.cri.pageNum == num ? 'active' : '' }"><a class="page-link" href="${num }">${num}</a></li>
					</c:forEach>
					<c:if test="${pageMaker.next }">
						<li class="page-item"><a class="page-link" href="${pageMaker.endPage+1}">Next</a></li>
					</c:if>
				</ul>
			</nav>
	</div>
	<!-- end Pagination -->
	<form id="actionForm" method="post">
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }"> 
		<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
<script>
	$(document).ready(function() {
		//모든 Ajax 전송 시 CSRF토큰을 같이 전송하도록 세팅
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
		
		$(document).ajaxSend(function(e, xhr,options) {
			xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
		});
		
		var actionForm = $("#actionForm");
		$(".page-link").on(  //Next,Prevois,페이지번호 클릭 시 페이지 이동
				"click",
				function(e) { //<a>태그 클릭시
					e.preventDefault(); //기존 이벤트 중지
					console.log('paging');
					actionForm.find("input[name='pageNum']").val($(this).attr("href"));
					//actionForm의 pageNum의 value값을 눌린 페이지번호로 설정
					actionForm.attr("action", "/info/myFavorite");
					actionForm.submit();
		});
		
		$("button").on("click",function(e){
			var favoriteId = $(this).data("number");
			console.log("좋아요 삭제");
			console.log(favoriteId);
			
			$.ajax({
				type : 'delete',
				url : '/info/delete/'+favoriteId ,
				success : function(deleteResult, status, xhr) {
					alert("삭제되었습니다");
					location.reload();
				}
			})
		});
		
	});
</script>
<%@ include file="../layout/footer.jsp"%>


