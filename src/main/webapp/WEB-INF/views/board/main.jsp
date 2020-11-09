<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../layout/header.jsp"%>
<div class="container">
	<h1 class="font-weight-bold text-center py-2">독서노트</h1>
	<c:forEach items="${list}" var="list">
		<div class="card m-2 bg-light text-dark">
			<div class="card-header">
				<div class="card-text float-left small">${list.writer }</div>
				<div class="card-text float-right small">
					<!-- 날짜 정보를 담고 있는 객체를 포맷팅하여 출력할 때 사용하는 태그 -->
					<fmt:formatDate pattern="yyyy-MM-dd" value="${list.regdate}" />
				</div>
			</div>
			<div class="card-body">
				<h4 class="card-title">${list.subject }</h4>
				<button data-number="${list.boardId }" class="btn btn-primary">상세내용</button>
			</div>
		</div>
	</c:forEach>
	
	<!-- 검색창 -->
	<div class="row">
		<div class="col-lg-12">
			<!-- type , keyword , pageNum , amount가 전송됨  -->
			<form id="searchForm" action="/board/" method="get">
				<div class="form-row align-items-center">
					<div class="col-auto my-1">
						<select class="form-control" name="type">
							<option value="" <c:out value="${pageMaker.cri.type == null? 'selected' : ''}"/>>--</option>
							<option value="T" <c:out value="${pageMaker.cri.type eq 'T'? 'selected' : ''}"/>>제목</option>
							<option value="C" <c:out value="${pageMaker.cri.type eq 'C'? 'selected' : ''}"/>>내용</option>
							<option value="TC" <c:out value="${pageMaker.cri.type eq 'TC'? 'selected' : ''}"/>>제목+내용</option>
							<option value="W" <c:out value="${pageMaker.cri.type eq 'W'? 'selected' : ''}"/>>작성자</option>
						</select>
					</div>
					<div class="col-auto">
						<input type="text" class="form-control" name="keyword" value="${pageMaker.cri.keyword }"> 
						<input type="hidden" name="pageNum" value="1"> <!-- 검색시 무조건 1페이지로 이동하게 변경-->
						<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
					</div>
					<button type="submit" class="btn btn-primary">검색</button>
				</div>
			</form>
		</div>
	</div>
	
	<!-- 게시물 상세 조회시 전송 -->
	<form id="actionForm" action="/board/get" method="get">
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }"> 
		<input type="hidden" name="amount" value="${pageMaker.cri.amount }"> 
        <!--페이지 이동 시 검색조건과 키워드 전달-->
		<input type="hidden" name="type" value="${pageMaker.cri.type }">
		<input type="hidden" name="keyword" value="${pageMaker.cri.keyword }">
	</form>
	
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
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Modal title</h4>
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			</div>
			<div class="modal-body">처리가 완료되었습니다.</div>
			<div class="modal-footer">
				<button type="button" id="modal-close" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<script>
	$(document).ready(function() { // main 페이지가 로딩되면
			var actionForm = $("#actionForm");
			//result가 전송되었으면 result 출력
			var result = '<c:out value="${result}"/>';
			console.log("번호 : " + result);
			checkModal(result);
			history.replaceState({}, null, null);
			
			//모달 체크
			function checkModal(result) {
				if (result === '' || history.state) {
					return;
				}
				if (parseInt(result) > 0) {
					$(".modal-body").html("게시글 " + parseInt(result)+ "번이 등록되었습니다.");
				}
					$("#myModal").modal("show");
			}

			$(".page-link").on("click",function(e) { //페이지 번호 클릭 클릭시 다음 페이지로 이동
				e.preventDefault(); 
				console.log('paging');
				//actionForm의 pageNum의 value값을 현재 눌린 <a>태그의 href의 값으로 설정
				actionForm.find("input[name='pageNum']").val($(this).attr("href"));
				actionForm.attr("action", "/board/");
				actionForm.submit();
			});

			$(".card-body button").on("click",function(e) { //상세내용 클릭
				e.preventDefault();
				console.log("detail");
				actionForm.append("<input type='hidden' name='boardId' value='"+ $(this).data("number")+ "'/>");
				//상세내용 버튼 클릭시 pageNum, amount , boardId를 /board/get으로 전송 
				actionForm.submit();
			});
						
			var searchForm = $('#searchForm'); //검색 조건
						
			$("#searchForm button").on('click',function(e){ // 검색 버튼 클릭 시
				e.preventDefault(); 
				
				if(!searchForm.find("option:selected").val()){//검색 종류가 없으면 alert을 띄움
					alert("검색종류를 선택하세요");
					return false;
				}

				if(!searchForm.find("input[name='keyword']").val()){//검색 키워드가 없으면 alert을 띄움
					alert("키워드를 입력하세요");
					return false;
				}
					searchForm.submit();
			});
		})
</script>
<%@ include file="../layout/footer.jsp"%>


