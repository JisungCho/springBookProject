<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<div class="form-group w-50">
		<label for="title">제목</label> <input type="text" class="form-control" placeholder="Enter title" name="subject" value="${board.subject }" readonly="readonly">
	</div>
	<div class="form-group w-50">
		<label for="writer">작성자</label> <input type="text" class="form-control" placeholder="Enter writer" name="writer" value="${board.writer }" readonly="readonly">
	</div>
	<div class="form-group">
		<label for="content">내용</label>
		<textarea class="form-control summernote" rows="8" name="content">${board.content }</textarea>
	</div>
	<div id="bookSearch" class="form-group">
		<label for="content">책</label> <br/>
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
							<!-- 좋아요 버튼 -->
							<div id="favoriteBox">
								<c:choose>
									<c:when test="${favorite == false }">
										<button id="likebtn" class="btn"><i class="fa fa-heart-o" style="font-size:30px;color:red"></i></button>
									</c:when>
									<c:when test="${favorite == true}">
										<button id="unlikebtn" class="btn"><i class="fa fa-heart" style="font-size:30px;color:red"></i></button>
									</c:when>
								</c:choose>
							</div>
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
					<!-- 로그인한 회원이라면 댓글추가 가능 -->
					<sec:authorize access="isAuthenticated()">
						<button id="addReplyBtn" class="btn btn-primary btn-sm float-right">댓글추가</button>
					</sec:authorize>
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
	<sec:authentication property="principal" var="principal"/>
	<sec:authorize access="isAuthenticated()">
		<c:if test="${principal.username == board.writer }">
		<!-- 로그인한 회원이면서 현재 게시물이 자기가 쓴 글인경우 수정하기 버튼 생성-->
			<button data-oper="modify" class="btn btn-primary">수정하기</button>
		</c:if>
	</sec:authorize>
	<button data-oper="close" class="btn btn-info">뒤로가기</button>
	
	<!--  -->
	<form id='operForm' action="/board/modify" method="get">
		<input type="hidden" id="boardId" name="boardId" value='<c:out value="${board.boardId }"></c:out>'> 
		<input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"></c:out>'> 
		<input type="hidden" name="type" value='<c:out value="${cri.type }"></c:out>'> 
		<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"></c:out>'> 
		<input type="hidden" name="amount" value='<c:out value="${cri.amount }"></c:out>'>
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
	</div>
</div>
<!-- /.modal -->

<script type="text/javascript" src="/resources/js/replyService.js"></script>
<script>
	$(document).ready(function(e){
		
		//모든 Ajax 전송 시 CSRF토큰을 같이 전송하도록 세팅
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
		
		$(document).ajaxSend(function(e, xhr,options) {
			xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
		});
		
		$('.summernote').summernote({
			tabsize : 2,
			height : 300
		});
		
		$('.summernote').summernote('disable');
		
		
		var operForm = $("#operForm");
	
		//close버튼 클릭시 메인페이지로 이동하는데 이동할 떄 pageNum과 amount를 전송해서 현재 페이지유지
		$("button[data-oper='close']").on("click",function(e){
			operForm.find('#boardId').remove();
			operForm.attr("action","/board/");
			operForm.submit();
		});
		
		//수정버튼 클릭 시 get방식으로 /board/modify에 boardId , pageNum, amount , type ,keyword 넘겨줌
		$("button[data-oper='modify']").on("click",function(e){
			operForm.submit();
		});
	});
	
	$(document).ready(function(e){
		
		var boardId = ${board.boardId}; //게시물 번호
		
		var replyUL = $(".list-group"); // 댓글목록의 ul
		var pageNum = 1; //페이지번호
		var replyPageFooter = $(".card-footer"); //
		
	
		var replyer = null;	
		<sec:authorize access="isAuthenticated()">
			replyer = '<sec:authentication property="principal.username"/>';
			replyer = decodeHTMLEntities(replyer); // html entity decode
			console.log(replyer);
		</sec:authorize>
		
		
		//모달 창 설정
		var modal = $("#myModal"); 
		var modalInputReply = modal.find("input[name='reply']"); //reply
		var modalInputReplyer = modal.find("input[name='replyer']"); //replyer
		var modalInputReplyDate = modal.find("input[name='replyDate']"); // replyDate

		//모달창에서 수정 ,삭제 ,등록,닫기 버튼 
		var modalModBtn = $("#modalModBtn"); //수정버튼
		var modalRemoveBtn = $("#modalRemoveBtn"); //삭제버튼
		var modalRegisterBtn = $("#modalRegisterBtn"); //등록버튼
		var modalCloseBtn = $("#modalCloseBtn"); //닫기버튼
		
		// html entity 디코딩
		function decodeHTMLEntities(text) { 
			  var textArea = document.createElement('textarea');
			  textArea.innerHTML = text;
			  return textArea.value;
		}
		
		//댓글목록 갱신
		//page 번호가 -1 로 전달되면 마지막 페이지를 찾아서 다시 호출
		function showList(page){
			console.log("page : "+page);
			
			replyService.getList({boardId:boardId, page:page||1}, 
			//해당 게시글의 댓글 수, 해당 페이지의 댓글목록
			function(replyCnt,list) {
				if(page == -1){ //마지막 페이지로 이동
					pageNum = Math.ceil(replyCnt/5.0); // 마지막 페이지 찾기 ex) 댓글이 19개면  4페이지
					showList(pageNum); //마지막 페이지 호출
					return;
				}
				
				var str="";
				
				if(list == null || list.length==0){
					return;
				}
				
				for(var i=0, len=list.length || 0; i<len;i++){
					str += "<li class='list-group-item' data-replyId='"+list[i].replyId+"'>";
					str += "<div><div class='header'><small class='font-weight-bold'>"+list[i].replyer+"</small>";
					str += "<small class='pull-right' text-muted>"+list[i].replyDate+"</small></div>";
					str += "<p>"+list[i].reply+"</p></div></li>";
				}
                
				replyUL.html(str); //기존 요소를 지우고 추가
				
				showReplyPage(replyCnt); //footer에 페이지 번호 처리
				
			});
		}
		
		function showReplyPage(replyCnt) { //댓글의 갯수가 파라미터로 옴
			var endNum = Math.ceil(pageNum/10.0) * 10; //끝번호  1~10페이지는 10 
			var startNum = endNum - 9; //시작번호
			
			var prev = startNum != 1; 
			var next = false;
			
			//데이터 수의 맞게 페이지 끝번호 조정
			if(endNum * 5 >= replyCnt){ 
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
		      
		      //footer에 추가
		      replyPageFooter.html(str);			
		};
		
		/* html 로딩 후 실행 */
		showList(1); //처음 조회페이지로 간 경우에는 1페이지를 보여줌	
		
		
		
		// 댓글 추가 버튼 클릭시
		$("#addReplyBtn").on("click",function(e){ 
			//replydate 
			modalInputReplyDate.closest("div").hide();
			//내용 작성할 수 있게 
			modalInputReply.removeAttr("readonly");
			modalInputReply.val("");
			//댓글 작성자가 현재 로그인한 사람으로 고정
			modalInputReplyer.val(replyer);
			modalInputReplyer.attr("readonly","readonly");
			//댓글수정 버튼 숨김
			modalModBtn.hide();
			//댓글제거 버튼 숨김
			modalRemoveBtn.hide();
			//등록 버튼 보여줌
			modalRegisterBtn.show();
			//모달창 보여줌
			$("#myModal").modal("show");
		});
		
		//댓글 등록 버튼 클릭시
		modalRegisterBtn.on("click", function() {
			//댓글 등록자
			var callerId = modalInputReplyer.val();
			var receiverId = "${board.writer }";
			var content = callerId + "님이 <a type='external' href='/board/get?pageNumber=1&boardId="+boardId+"'>" + boardId + "</a>번 글에 댓글을 달았습니다.";
			
			var data = {
					reply:modalInputReply.val(),
					replyer:modalInputReplyer.val(),
					boardId:boardId
			}
			replyService.reigster(data, function(result) {
						alert(result);
						modal.find("input").val("");
						modal.modal("hide");
						//알람설정 2020-12-27
						//알람 DB에 저장 
						
						
						//만약에 현재로그인한 사람과 댓글을 다는 사람이 같으면 알람db에 저장하지 않음
						if(replyer != receiverId){
							var AlarmData = {
									receiverId : receiverId,
									callerId : callerId,
									content : content
							};
							$.ajax({
								type : 'post',
								url : '/alarm/saveAlarm',
								data : JSON.stringify(AlarmData),
								contentType: "application/json; charset=utf-8",
								dataType : 'text',
								success : function(data){
									if(socket){
										let socketMsg = "reply," +receiverId  +","+ callerId +","+boardId;
										console.log("msg : " + socketMsg);
										socket.send(socketMsg);
									}
						 
								},
								error : function(err){
									console.log(err);
								}
							});
						}
						showList(-1);//등록 시 마지막 페이지로 이동
			});
		});
		
		//댓글 조회
		$(".card-body ul").on("click","li", function() {
			console.log("댓글조회");
			var replyId = $(this).data("replyid");
			replyService.get(replyId, function(result) {
		
				if(replyer == result.replyer){ // 현재 로그인한 회원과 댓글의 회원이 같은 회원이면
					console.log("같은회원");
					$("input[name='reply']").removeAttr("readonly");
					$("input[name='reply']").val(result.reply);
				}else{
					$("input[name='reply']").attr("readonly","readonly").val(result.reply);
				}
				
				$("input[name='replyer']").attr("readonly","readonly").val(result.replyer);
				$("input[name='replyDate']").attr("readonly","readonly").val(result.replyDate);
				modal.data("replyId",replyId);
				modalRegisterBtn.hide();
				modalModBtn.show();
				modalRemoveBtn.show();
				modal.modal("show");
			});
		});
		
		//댓글 수정버튼 클릭시
		modalModBtn.on("click",function(e){	
			//로그인이 안되어있으면
			if(!replyer){
				alert("로그인 후 수정이 가능합니다.");
				modal.modal("hide");
				return;
			}
			
			var origin = modalInputReplyer.val();
			
			if(replyer != origin){
				alert("댓글 작성자만 수정이 가능합니다.");
				modal.modal("hide");
				return;
			}
			
			var reply = {
				replyId:modal.data("replyId"),
				reply:$("input[name='reply']").val(),
				replyer:origin,
			};
			replyService.update(reply, function(result) {
				alert(result);
				modal.modal("hide");
				//목록갱신
				showList(pageNum);
			});
		});
		
		//모달창 닫기 버튼 클릭 시
		modalCloseBtn.on("click", function(e) {
			modal.modal("hide");
		});
		
		//모달창 삭제버튼 클릭 시
		modalRemoveBtn.on("click",function(e){
			var replyId = modal.data("replyId");
			
			//로그인이 안되어있으면
			if(!replyer){
				alert("로그인 후 삭제가 가능합니다.");
				modal.modal("hide");
				return;
			}
			
			var origin = modalInputReplyer.val();
			
			if(replyer != origin){
				alert("댓글 작성자만 삭제가 가능합니다.");
				modal.modal("hide");
				return;
			}
			
			replyService.remove(replyId, origin ,function(result) {
				alert(result);
				modal.modal("hide");
				//목록갱신
				showList(pageNum);
			});
		});
		
		replyPageFooter.on("click","li a", function(e){ //페이지 클릭 했을 때
	        e.preventDefault();
	        console.log("page click");
	        
	        var targetPageNum = $(this).attr("href"); //page번호 가져오기
	        
	        console.log("targetPageNum: " + targetPageNum);
	        
	        pageNum = targetPageNum;
	        
	        showList(pageNum); //갱신
	      }); 
		
		//좋아요 추가 버튼 클릭
		 $("#favoriteBox").on("click","#likebtn",function(e){
			 var thumbnail = "${board.book.thumbnail}";
			 var title = "${board.book.title}";  
			 var authors = "${board.book.authors}";
			 var url ="${board.book.url}";
			 
			 var data = {
					 userid:encodeURIComponent(replyer),
					 thumbnail:thumbnail, 
					 title :title,
					 authors :authors,
					 url:url,
				};
			 
			if(!replyer){
					alert("로그인 후 가능합니다.");
					return;
			};

	         $.ajax({
	             url:"/board/favorite",
	             data: JSON.stringify(data),
	             contentType : "application/json; charset=utf-8", 
	             type:"post",
	             success : function (result) {
	             	alert("좋아요가 추가되었습니다.");
	             	$("#favoriteBox").empty();
	             	var str = '<button id="unlikebtn" class="btn"><i class="fa fa-heart" style="font-size:30px;color:red"></i></button>';
	             	$("#favoriteBox").append(str);
	             }
	         });
		 });
		
		//좋아요 삭제 버튼 클릭
		 $("#favoriteBox").on("click","#unlikebtn",function(e){
			 var url ="${board.book.url}";
			 var data = {
				 userid:replyer,
				 url:url
			 };
			 

	         $.ajax({
	             url:"/board/unFavorite",
	             data: JSON.stringify(data),
	             contentType : "application/json; charset=utf-8", 
	             type:"post",
	             success : function (result) {
	            	 if(result){
	 	             	alert("좋아요가 삭제되었습니다.");
		             	$("#favoriteBox").empty();
		             	//하트모양 바꿈
		             	var str = '<button id="likebtn" class="btn"><i class="fa fa-heart-o" style="font-size:30px;color:red"></i></button>';
		             	$("#favoriteBox").append(str); 
	            	 }
	             }
	         });
		 });
		
	});
</script>

<%@ include file="../layout/footer.jsp"%>



