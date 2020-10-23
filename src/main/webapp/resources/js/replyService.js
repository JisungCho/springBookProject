console.log("Reply Module......");

var replyService = (function(){
	
	function register(reply, callback, error) {
		console.log("reply add...............");

		$.ajax({
			type : 'post', //get방식
			url : "/reply/new",  //url
			data : JSON.stringify(reply), //자바 스크립트객체를 JSON데이터로 변경
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
	};
	
	function getList(param, callback, error) {
		console.log("reply list...............");
		
		var boardId = param.boardId;
		var page = param.page || 1;
		
		$.getJSON("/reply/pages/"+boardId+"/"+page+".json",
			function(data){
				if(callback){
					callback(data.replyCnt, data.list);
				}
			}).fail(function(xhr,status,err){
				if(error){
					error(err);
				}
		});
	}	
	
	function remove(replyId, callback, error) {
		console.log("reply remove...............");

		$.ajax({
			type : 'post', //get방식
			url : "/reply/"+replyId,  //url
			contentType : "application/json; charset=utf-8", // MIMETYPE : JSON
			success : function(result, status, xhr) { //성공시						
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, err) { //실패시
				if (error) {
						error(err);
					}
				}
		});
	};
	
    function update(reply, callback, error) {
		console.log("update");


		$.ajax({
			type : 'put', //REST PUT
			url : '/reply/'+ reply.replyId ,
			data : JSON.stringify(reply), // js데이터를 JSON 문자열로 변환
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		});
	};
	
	function get(replyId, callback, error) {
		console.log("reply get...............");
		
		
		$.getJSON("/reply//"+replyId+".json",
			function(data){
				if(callback){
					callback(data);
				}
			}).fail(function(xhr,status,err){
				if(error){
					error(err);
				}
		});
	}			
	
	return {
		reigster:register,
		getList:getList,
		remove:remove,
		update:update,
		get:get
	};

})();