<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>관리자 페이지</title>

<!-- Custom fonts for this template-->
<link href="../resources/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

<!-- Custom styles for this template-->
<link href="../resources/css/sb-admin-2.min.css" rel="stylesheet">
<style type="text/css">
.scroll-to-top {
	right: 0px;
	left: 0px
}
</style>
</head>

<body id="page-top">

	<!-- Page Wrapper -->
	<div id="wrapper">

		<!-- Sidebar -->
		<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

			<!-- Sidebar - Brand -->
			<a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
				<div class="sidebar-brand-icon rotate-n-15">
					<i class="fas fa-laugh-wink"></i>
				</div>
				<div class="sidebar-brand-text mx-3">관리자페이지</div>
			</a>

			<!-- Divider -->
			<hr class="sidebar-divider my-0">

			<!-- Nav Item - Dashboard -->
			<li class="nav-item"><a class="nav-link" href="/admin/"> <i class="fas fa-fw fa-chart-area"></i> <span>통계</span></a></li>
			<li class="nav-item active"><a class="nav-link" href="/admin/boardList"> <i class="fas fa-fw fa-table"></i> <span>게시물관리</span></a></li>
			<li class="nav-item"><a id="logout" class="nav-link" href="/logout"> <i class="fas fa-fw fa-sign-out-alt"></i> <span>로그아웃</span></a></li>

			<!-- Divider -->
			<hr class="sidebar-divider d-none d-md-block">

			<!-- Sidebar Toggler (Sidebar) -->
			<div class="text-center d-none d-md-inline">
				<button class="rounded-circle border-0" id="sidebarToggle"></button>
			</div>

		</ul>
		<!-- End of Sidebar -->

		<!-- Begin Page Content -->
		<div class="container-fluid">

			<br>
			<!-- Page Heading -->
			<h1 class="h3 mb-2 text-gray-800">게시물 관리</h1>
			<!-- DataTales Example -->
			<div class="card shadow mb-4">
				<div class="card-header py-3">
					<h6 class="m-0 font-weight-bold text-primary">게시물관리</h6>
				</div>
				<div class="card-body">
					<div class="table-responsive">
						<form id="adminForm" action="/admin/remove" method="post">
							<input type="hidden" name="boardId" value=''> 
							<input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"></c:out>'> 
							<input type="hidden" name="type" value='<c:out value="${cri.type }"></c:out>'> 
							<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"></c:out>'> 
							<input type="hidden" name="amount" value='<c:out value="${cri.amount }"></c:out>'> 
							<input type='hidden' name="${_csrf.parameterName}" value="${_csrf.token}">
							<table class="table table-bordered text-center" id="dataTable" width="100%" cellspacing="0">
								<thead>
									<tr>
										<th><input type="checkbox" id="selectall" /></th>
										<th>제목</th>
										<th>글쓴이</th>
										<th>작성일</th>
										<th>관리</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list }" var="list">
										<tr>
											<td><input type="checkbox" class="singlechkbox" name="boardIdList" value="${list.boardId }" /></td>
											<td>${list.subject }</td>
											<td>${list.writer }</td>
											<td><fmt:formatDate value="${list.regdate }" pattern="yyyy/MM/dd" /></td>
											<td><button class="btn btn-outline-primary" value="${list.boardId }">상세보기</button></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</form>
					</div>
					<button id="multiRemove" type="button" class="float-right btn btn-sm btn-outline-danger">
						<i class="fas fa-times"></i>선택삭제
					</button>
					<!-- 검색창 -->
					<div class="row">
						<div class="col-lg-12">
							<!-- type , keyword , pageNum , amount가 전송됨  -->
							<form id="searchForm" action="/admin/boardList" method="get">
								<div class="form-row align-items-center">
									<div class="col-auto my-1">
										<select class="form-control" name="type">
											<option value="" <c:out value="${pageMaker.cri.type == null? 'selected' : ''}"/>>--</option>
											<option value="T" <c:out value="${pageMaker.cri.type eq 'T'? 'selected' : ''}"/>>제목</option>
											<option value="W" <c:out value="${pageMaker.cri.type eq 'W'? 'selected' : ''}"/>>작성자</option>
										</select>
									</div>
									<div class="col-auto">
										<input type="text" class="form-control" name="keyword" value="${pageMaker.cri.keyword }"> <input type="hidden" name="pageNum" value="1">
										<!-- 검색시 무조건 1페이지로 이동하게 변경-->
										<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
									</div>
									<button type="submit" class="btn btn-primary">검색</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>

			<!-- pageDTO -->
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
		<!-- /.container-fluid -->

	</div>
	<!-- End of Page Wrapper -->

	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i class="fas fa-angle-up"></i>
	</a>
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
	<form id="headerForm"></form>

	<!-- 페이지 변경시 전송 -->
	<form id="actionForm" action="/admin/boardList" method="get">
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }"> <input type="hidden" name="amount" value="${pageMaker.cri.amount }">
		<!--페이지 이동 시 검색조건과 키워드 전달-->
		<input type="hidden" name="type" value="${pageMaker.cri.type }"> <input type="hidden" name="keyword" value="${pageMaker.cri.keyword }">
	</form>

	<!-- Bootstrap core JavaScript-->
	<script src="../resources/vendor/jquery/jquery.min.js"></script>
	<script src="../resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

	<!-- Core plugin JavaScript-->
	<script src="../resources/vendor/jquery-easing/jquery.easing.min.js"></script>

	<!-- Custom scripts for all pages-->
	<script src="../resources/js/sb-admin-2.min.js"></script>

</body>
<script>
	$(document).ready(function() {
		
		console.log("${cri}");
		
		var actionForm = $("#actionForm");

		$("#logout").on("click", function(e) { //토큰 전달과 로그아웃 처리
			e.preventDefault();
			console.log("로그아웃");
			alert("로그아웃 되었습니다");
			var str = '<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">';
			$("#headerForm").append(str);
			$("#headerForm").attr("action", "/logout");
			$("#headerForm").attr("method", "post");
			$("#headerForm").submit();
		});
		$('body').on('click', '#selectall', function() {
			$('.singlechkbox').prop('checked', this.checked);
		});

		$('body').on('click', '.singlechkbox', function() {
			if ($('.singlechkbox').length == $('.singlechkbox:checked').length) {
				$('#selectall').prop('checked', true);
			} else {
				$("#selectall").prop('checked', false);
			}
		});

		$(".page-link").on("click", function(e) { //페이지 번호 클릭 클릭시 다음 페이지로 이동
			e.preventDefault();
			console.log('paging');
			//actionForm의 pageNum의 value값을 현재 눌린 <a>태그의 href의 값으로 설정 
			actionForm.find("input[name='pageNum']").val($(this).attr("href"));
			actionForm.submit();
		});

		var searchForm = $('#searchForm'); //검색 조건
		$("#searchForm button").on('click', function(e) { // 검색 버튼 클릭 시
			e.preventDefault();

			if (!searchForm.find("option:selected").val()) {//검색 종류가 없으면 alert을 띄움
				alert("검색종류를 선택하세요");
				return false;
			}

			if (!searchForm.find("input[name='keyword']").val()) {//검색 키워드가 없으면 alert을 띄움
				alert("키워드를 입력하세요");
				return false;
			}
			searchForm.submit();
		});
		
		
		var adminForm = $("#adminForm");
		
		//게시물 상세 조회
		$("tr button").on("click",function(e){
			e.preventDefault();
			var boardId = $(this).val();
			console.log(boardId);
			$("input[name='boardId']").attr("value",boardId);
			$("input[name='${_csrf.parameterName}']").attr("disabled",true);
			console.log("삭제");
			adminForm.attr('action',"/board/get")
			adminForm.attr('method',"get")
			adminForm.submit();
		});
		
		//게시물 삭제
		$("#multiRemove").on("click",function(){
			console.log("선택삭제");
			adminForm.submit();
		});
		
		var result = '<c:out value="${result}"/>';
		checkModal(result);
		
		//전달된 result가 0보다 크면 modal의 내용을 바꿔주고나서 모달창을 보여줌
		function checkModal(result) {
			if (result === '' || history.state) {
				return;
			}
			if (result) {
				$(".modal-body").html("선택된 게시글이 삭제되었습니다.");
			}
				$("#myModal").modal("show");
		}
	})
</script>
</html>