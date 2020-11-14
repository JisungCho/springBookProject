<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<h1 class="font-weight-bold text-center py-2">내 글 목록</h1>
	<c:forEach items="${list}" var="list">
		<div class="card m-2 bg-light text-dark">
			<div class="card-header">
				<div class="card-text float-left">${list.writer }</div>
				<div class="card-text float-right">
					<fmt:formatDate pattern="yyyy-MM-dd" value="${list.regdate}" />
				</div>
			</div>
			<div class="card-body">
				<h4 class="card-title">${list.subject }</h4>
				<button data-number="${list.boardId }" class="btn btn-primary">상세내용</button>
			</div>
		</div>
	</c:forEach>
	<!-- end Pagination -->
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
	<!-- 현재페이지 번호와 한페이지에 보여줄 갯수 -->
	<form id="actionForm" method="post">
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }"> 
		<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
	</form>
</div>
<script>
	$(document).ready(function() {
		var actionForm = $("#actionForm");
		var result = '<c:out value="${result}"/>';

		$(".page-link").on("click",function(e) { //Next,Prevois,페이지번호 클릭 시
			e.preventDefault(); //기존 이벤트 중지
			console.log('paging');
			//actionForm의 pageNum의 value값을 앞으로 이동할 페이지번호로 설정
			actionForm.find("input[name='pageNum']").val($(this).attr("href")); 
			
			var str = '<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			actionForm.append(str);
			actionForm.attr("action", "/info/myBoard");
			actionForm.submit();
			});
		
		$(".card-body button").on("click",function(e) { //상세내용 클릭시 상세 조회 페이지로 이동
			e.preventDefault();
			console.log("detail");
			actionForm.append("<input type='hidden' name='boardId' value='"+$(this).data("number")+ "'/>");
			//상세내용 버튼 클릭시 pageNum, amount , boardId를 /board/get으로 전송 
			actionForm.attr("action", "/board/get");
			actionForm.attr("method","get")
			actionForm.submit();
			});
	})
</script>
<%@ include file="../layout/footer.jsp"%>


