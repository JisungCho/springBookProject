<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
<title>메인 페이지</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
</head>
<body>
	<div class="container">
		<h2>도서 검색</h2>
		<div class="input-group mb-3">
			<input type="text" class="form-control" name="bookName" placeholder="찾으실 도서명을 입력하세요">
			<div class="input-group-append">
				<button id="kakaoBook" class="btn btn-success">검색</button>
			</div>
		</div>
		<div id="bookResult">
		</div>
	</div>
</body>
<script type="text/javascript" src="/resources/js/bookService.js"></script>
<script>
	$(document).ready(function(e){
		var bookResult = $("#bookResult");
		var pageNumber = 1;
		
		$("#kakaoBook").on("click", function(e) { //검색버튼 클릭
			var bookName = $('input[name="bookName"]').val();
			var str ="";
			var pageNumber = 1;
			
			//검색 결과로 책 목록을 가져와서 화면에 보여줌
			bookService.search(bookName, function(result) {
				for(var i=0;i<result.documents.length;i++){
				str += '<div id="bookCard'+i+'" class="card" style="max-width: 500px;">';
						str += '<div class="row no-gutters">';
							str += '<div class="col-sm-4">';
								str += '<img id="thumbnail" src="'+result.documents[i].thumbnail+'" class="card-img-top h-60" alt="이미지 없음">';
							str += '</div>';
						str += '<div class="col-sm-7">';
							str += '<div class="card-body">';
								str +=	'<h5 id="title" class="card-title">'+result.documents[i].title+'</h5>';
								str += 	'<h6 id="authors" class="card-subtitle mb-2 text-muted">'+result.documents[i].authors+'</h6>';
								str += 	'<h6 class="card-subtitle mb-2 text-muted">';
									str +=	'<a id="url" target="_blank" href="'+result.documents[i].url+'">상세보기</a>';
								str += '</h6><br/>';
							  	str +='<button type="button" data-number="'+i+'" class="btn btn-primary">선택</button>';
							str += '</div>';
						str += '</div>';
					str += '</div>';
				str += '</div><br/>';
				}
				
								
				if (pageNumber == 1) { //첫번째 페이지이면 Prevoius 버튼  disable
					str += '<ul class="pagination justify-content-center"><li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>';
				} else{ 
					str += '<ul class="pagination justify-content-center"><li id="prev" data-page="1" class="page-item"><a class="page-link" href="#">Previous</a></li>'
				}
				
				if(result.meta.is_end){ // 마지막 페이지이면 Next 버튼 disable
					str += '<li class="page-item disabled"><a class="page-link" href="#">Next</a></li></ul>'
				}else{
					str += '<li id="next" data-page="1" class="page-item"><a class="page-link" href="#">Next</a></li></ul>';
				}
				bookResult.html(str);
			});
		});
		
		$("#bookResult").on("click","button",function(e){
			var cardNumber = $(this).data("number");
			console.log(cardNumber); 	
			window.opener.getReturnValue($("#bookCard"+cardNumber).clone()); //부모창에 해당 책의 div를 넘겨줌 (아이디가 bookCard숫자)
			// getReturnValue() 메소드에서는 선택버튼을 제거하고 부모창에 html을 추가해줌
			window.close(); 
		});
		
		$("#bookResult").on("click","li",function(e){ //페이지 번호 클릭시 페이지 이동
			var bookName = $('input[name="bookName"]').val();
			var str = "";
			
			if($(this).attr("id")==="next"){ // next버튼 클릭
				pageNumber += 1; // 페이지 번호 1 증가
				console.log("next");
				
				bookService.page(bookName,pageNumber,function(result){ // 다음 페이지에대한 북 리스트를 가져옴
					for(var i=0;i<result.documents.length;i++){
						str += '<div id="bookCard'+i+'" class="card" style="max-width: 500px;">';
								str += '<div class="row no-gutters">';
									str += '<div class="col-sm-4">';
										str += '<img id="thumbnail" src="'+result.documents[i].thumbnail+'" class="card-img-top h-60" alt="이미지 없음">';
									str += '</div>';
								str += '<div class="col-sm-7">';
									str += '<div class="card-body">';
										str +=	'<h5 id="title" class="card-title">'+result.documents[i].title+'</h5>';
										str += 	'<h6 id="authors" class="card-subtitle mb-2 text-muted">'+result.documents[i].authors+'</h6>';
										str += 	'<h6 class="card-subtitle mb-2 text-muted">';
											str +=	'<a id="url" target="_blank" href="'+result.documents[i].url+'">상세보기</a>';
										str += '</h6><br/>';
									  	str +='<button type="button" data-number="'+i+'" class="btn btn-primary">선택</button>';
									str += '</div>';
								str += '</div>';
							str += '</div>';
						str += '</div><br/>';
						}
						
						
						
						if (pageNumber == 1) { //첫번째 페이지이면 Prevoius 버튼  disable
							str += '<ul class="pagination justify-content-center"><li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>';
						} else{
							str += '<ul class="pagination justify-content-center"><li id="prev" class="page-item"><a class="page-link" href="#">Previous</a></li>'
						}
						
						if(result.meta.is_end){ // 마지막 페이지이면 Next 버튼 disable
							str += '<li class="page-item disabled"><a class="page-link" href="#">Next</a></li></ul>'
						}else{
							str += '<li id="next" class="page-item"><a class="page-link" href="#">Next</a></li></ul>';
						}
						
						console.log("현재페이지 : "+pageNumber);
						bookResult.html(str);	
				});
			}else if($(this).attr("id")==="prev"){ //previous버튼 클릭시 이전 페이지로 이동
				pageNumber -= 1; // 페이지번호 1감소
				console.log("prev");
				
				bookService.page(bookName,pageNumber,function(result){ // 이전 페이지에대한 북 리스트 가져옴
					for(var i=0;i<result.documents.length;i++){
						str += '<div id="bookCard'+i+'" class="card" style="max-width: 500px;">';
								str += '<div class="row no-gutters">';
									str += '<div class="col-sm-5">';
										str += '<img id="thumbnail" src="'+result.documents[i].thumbnail+'" class="card-img-top h-60" alt="이미지 없음">';
									str += '</div>';
								str += '<div class="col-sm-7">';
									str += '<div class="card-body">';
										str +=	'<h5 id="title" class="card-title">'+result.documents[i].title+'</h5>';
										str += 	'<h6 id="authors" class="card-subtitle mb-2 text-muted">'+result.documents[i].authors+'</h6>';
										str += 	'<h6 class="card-subtitle mb-2 text-muted">';
											str +=	'<a id="url" href="'+result.documents[i].url+'">상세보기</a>';
										str += '</h6><br/>';
									  	str +='<button type="button" data-number="'+i+'" class="btn btn-primary">선택</button>';
									str += '</div>';
								str += '</div>';
							str += '</div>';
						str += '</div><br/>';
						}
						
						
						
						if (pageNumber == 1) {//첫번째 페이지이면 Prevoius 버튼  disable
							str += '<ul class="pagination justify-content-center"><li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>';
						} else{
							str += '<ul class="pagination justify-content-center"><li id="prev" class="page-item"><a class="page-link" href="#">Previous</a></li>'
						}
						
						if(result.meta.is_end){// 마지막 페이지이면 Next 버튼 disable
							str += '<li class="page-item disabled"><a class="page-link" href="#">Next</a></li></ul>'
						}else{
							str += '<li id="next" class="page-item"><a class="page-link" href="#">Next</a></li></ul>';
						}
						
						console.log("현재페이지 : "+pageNumber);
						bookResult.html(str);
					});
				}
		});
		
	
	});

</script>
</html>

