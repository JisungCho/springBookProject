console.log("Book Module......");

var bookService = (function(){
	
	function search(bookName, callback, error) {
		console.log("bookSearch...............");

		$.ajax({
			type : 'get', //get방식
			url : "https://dapi.kakao.com/v3/search/book",  //url
			headers: {'Authorization': 'KakaoAK '+ '61047743b1338b7db2c2102e0a34b29c'},
			data : {
				query:bookName,
				page:1,
			},
			contentType : "application/json; charset=utf-8", // MIMETYPE : JSON
			success : function(result, status, xhr) { //성공시						
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, er) { //실패시
				if (error) {
						error(er);
					}
				}
		});
	}
	
	function page(bookName,pageNumber,callback, error) {
		console.log("bookPaging...............");

		$.ajax({
			type : 'get', //get방식
			url : "https://dapi.kakao.com/v3/search/book",  //url
			headers: {'Authorization': 'KakaoAK '+ '61047743b1338b7db2c2102e0a34b29c'},
			data : {
				query:bookName,
				page:pageNumber,
			},
			contentType : "application/json; charset=utf-8", // MIMETYPE : JSON
			success : function(result, status, xhr) { //성공시						
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, er) { //실패시
				if (error) {
						error(er);
					}
				}
		});
	}
	
	return {
		search:search,
		page:page
	};

})();