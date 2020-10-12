console.log("Book Module......");

var bookService = (function(){
	
	function search(bookName, callback, error) {
		console.log("bookSearch...............");

		$.ajax({
			type : 'get', //post방식
			url : "https://dapi.kakao.com/v3/search/book",  //url
			headers: {'Authorization': 'KakaoAK '+ '61047743b1338b7db2c2102e0a34b29c'},
			data : {
				query:bookName,
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
		search:search
	};

})();