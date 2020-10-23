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
		<textarea class="form-control summernote" rows="8" name="content">${board.content }</textarea>
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
	<!-- 댓글 창 -->
	<div class="row">
		<div class="col-lg-12">
			<div class="card">
				<div class="card-header">
					<i class="fa fa-comments fa-fw align-middle"></i><span class="align-middle">댓글</span>
					<button id="addReplyBtn" class="btn btn-primary btn-sm float-right">댓글추가</button>
				</div>
				<div class="card-body">
					<ul class="list-group list-group-flush">
					</ul>
				</div>
				<div class="card-footer">
				
				</div>
			</div>
		</div>
	</div>
	<br/>
	<button data-oper="modify" class="btn btn-primary">수정하기</button>
	<button data-oper="close" class="btn btn-info">뒤로가기</button>

	<form id='operForm' action="/board/modify" method="get">
		<input type="hidden" id="boardId" name="boardId" value='<c:out value="${board.boardId }"></c:out>'> <input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"></c:out>'> <input type="hidden" name="type" value='<c:out value="${cri.type }"></c:out>'> <input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"></c:out>'> <input type="hidden" name="amount" value='<c:out value="${cri.amount }"></c:out>'>
	</form>
</div>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">댓글</h4>
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label>Replyer</label>
					<input class="form-control" name="replyer" value="">
				</div>			
				<div class="form-group">
					<label>Reply</label>
					<input class="form-control" name="reply" value="">
				</div>
				<div class="form-group">
					<label>Reply Date</label>
					<input class="form-control" name="replyDate" value="">
				</div>								
			</div>
			<div class="modal-footer">
				<button id="modalModBtn" type="button" class="btn btn-warning">Modify</button>
				<button id="modalRemoveBtn" type="button" class="btn btn-danger">Remove</button>
				<button id="modalRegisterBtn" type="button" class="btn btn-primary">Register</button>
				<button id="modalCloseBtn" type="button" class="btn btn-default">Close</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script type="text/javascript" src="/resources/js/replyService.js"></script>
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
<script>
	$(document).ready(function(e){
		
		var boardId = ${board.boardId};
		console.log("게시물 번호 : "+boardId);
		
		var replyUL = $(".list-group"); // 댓글목록의 ul
		var pageNum = 1; //페이지번호
		var replyPageFooter = $(".card-footer"); //게시물 번호 div
		
		showList(1); //처음 조회페이지로 간 경우에는 1페이지를 보여줌		
		
		var modal = $("#myModal"); //모달
		var modalInputReply = modal.find("input[name='reply']"); //reply
		var modalInputReplyer = modal.find("input[name='replyer']"); //replyer
		var modalInputReplyDate = modal.find("input[name='replyDate']"); // replyDate
		
		var modalModBtn = $("#modalModBtn"); //수정버튼
		var modalRemoveBtn = $("#modalRemoveBtn"); //삭제버튼
		var modalRegisterBtn = $("#modalRegisterBtn"); //등록버튼
		
		$("#addReplyBtn").on("click",function(e){ // 댓글 추가 버튼 클릭시
			modalInputReplyDate.closest("div").hide();
			modalModBtn.hide();
			modalRemoveBtn.hide();
			$("#myModal").modal("show");
		});
		
		//글 등록 버튼 클릭시
		modalRegisterBtn.on("click", function() {
			var data = {
					reply:modalInputReply.val(),
					replyer:modalInputReplyer.val(),
					boardId:boardId
			}
			replyService.reigster(data, function(result) {
						alert(result);
						modal.find("input").val("");
						modal.modal("hide");
						showList(-1);//등록 시 마지막 페이지로 이동
			});
		});
		
		//댓글 조회
		$(".card-body ul").on("click","li", function() {
			console.log("댓글조회");
			var replyId = $(this).data("replyid");
			
			replyService.get(replyId, function(result) {
				$("input[name='reply']").val(result.reply);
				$("input[name='replyer']").attr("readonly","readonly").val(result.replyer);
				$("input[name='replyDate']").attr("readonly","readonly").val(result.replyDate);
				modal.data("replyId",replyId);
				modalRegisterBtn.hide();
				modal.modal("show");
			});
		});
		
		modalModBtn.on("click",function(e){
			var reply = {
				replyId:modal.data("replyId"),
				reply:$("input[name='reply']").val()
			};
			replyService.update(reply, function(result) {
				alert(result);
				modal.modal("hide");
				//목록갱신
				showList(pageNum);
			});
		});
		
		modalRemoveBtn.on("click",function(e){
			var replyId = modal.data("replyId");
			
			replyService.remove(replyId, function(result) {
				alert(result);
				modal.modal("hide");
				//목록갱신
				showList(pageNum);
			});
		});
		
		//댓글목록 갱신
		//page 번호가 -1 로 전달되면 마지막 페이지를 찾아서 다시 호출
		function showList(page){
			console.log("page : "+page);
			
			replyService.getList({boardId:boardId, page:page||1}, function(replyCnt,list) {
				//page가 있으면 page번호를 번호를 보내고 page가 없으면 1을 보냄
				if(page == -1){
					pageNum = Math.ceil(replyCnt/5.0); // 마지막 페이지 찾기
					showList(pageNum); //마지막 페이지 호출
					return;
				}
				var str="";
				if(list == null || list.length==0){
					return;
				}
				
				
				for(var i=0, len=list.length || 0; i<len;i++){
					str += "<li class='list-group-item' data-replyId='"+list[i].replyId+"'>";
					str += "<div><div class='header'><strong class='primary-font'>"+list[i].replyer+"</strong>";
					str += "<small class='pull-right' text-muted>"+list[i].replyDate+"</small></div>";
					str += "<p>"+list[i].reply+"</p></div></li>";
				}
                
				replyUL.html(str); //기존 요소를 지우고 추가
				
				showReplyPage(replyCnt); //페이징 처리
				
			});
		}
		
		function showReplyPage(replyCnt) { //댓글의 갯수가 파라미터로 옴
			var endNum = Math.ceil(pageNum/10.0) * 10; //끝번호 
			var startNum = endNum - 9; //시작번호
			
			var prev = startNum != 1; 
			var next = false;
			
			if(endNum * 5 >= replyCnt){ //데이터 수의 맞게 페이지 조정
				endNum = Math.ceil(replyCnt/5.0);
			}
			if(endNum * 5 < replyCnt){
				next = true;
			}
				
			 var str = "<ul class='pagination pull-right'>";
		      
		      if(prev){
		        str+= "<li class='page-item'><a class='page-link' href='"+(startNum -1)+"'>Previous</a></li>";
		      }
		      
		      for(var i = startNum ; i <= endNum; i++){
		        
		        var active = pageNum == i? "active":""; //같은 페이지면 활성화
		        
		        str+= "<li class='page-item "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
		      }
		      
		      if(next){
		        str+= "<li class='page-item'><a class='page-link' href='"+(endNum + 1)+"'>Next</a></li>";
		      }
		      
		      str += "</ul></div>";
		      
		      console.log(str);
		      
		      replyPageFooter.html(str);			
		};
		
		replyPageFooter.on("click","li a", function(e){ //페이지 클릭 했을 때
	        e.preventDefault();
	        console.log("page click");
	        
	        var targetPageNum = $(this).attr("href"); //page번호 가져오기
	        
	        console.log("targetPageNum: " + targetPageNum);
	        
	        pageNum = targetPageNum;
	        
	        showList(pageNum); //갱신
	      });  
	});

</script>
<%@ include file="../layout/footer.jsp"%>



