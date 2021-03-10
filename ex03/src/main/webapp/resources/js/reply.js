/**
 * 
 */
console.log("Reply Module.......");

var replyService = (function(){
	
	// add()에서 주의 깊게 봐야 하는 부분은 데이터 전송 타입이 'application/json; charset=utf-8' 방식으로 전송한다는 점과
	// 파라미터로 callback과 error를 함수로 받을 것이라는 점이다.
	// 만일 Ajax 호출이 성공하고, callback값으로 적절한 하수가 존재 한다면 해당 함수를 호출해서 결과를 반영하는 방식이다.
	function add(reply, callback, error){
		console.log("add reply.............");
		
		$.ajax({
			type :'post',
			url : '/replies/new',
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr){
				if (callback){
					callback(result);
				}
			},
			error :function(xhr, status, er){
				if (error){
					error(er);
				}
			}
		})
	}
	// getList()는 param이라는 객체를 통해서 필요한 파라미터를 전달받아 JSON 목록을 호출한다.
	// JSON 형태가 필요하므로 URL 호출 시 확장자를 .json으로 요구한다
	function getList(param, callback, error){
		
		var bno = param.bno;
		var page = param.page || 1;
		
		$.getJSON("/replies/pages/" + bno + "/" + page + ".json",
			function(data){
				if (callback) {
					//callback(data); 댓글 목록만 가져오는 경우
					callback(data.replyCnt, data.list); // 댓글 숫자와 목록을 가져오는 경우
				}
			}).fail(function(xhr, status, err){
				if(error){
					error();
				}
			});
	}
	
	// remove()는 DELETE 방식으로 데이터를 전달하므로 $.ajax()를 이용해서 구체적으로 type속성으로 delete를 저정한다.
	// board/get.jsp에서는 반드시 실제 데이터베이스에 있는 댓글 번호를 이용해서 정상적으로 댓글이 삭제 되었는지 확인한다.
	function remove(rno, callback, error){
		$.ajax({
			type : 'delete',
			url : '/replies/' + rno,
			success : function(deleteResult, status, xhr){
				if(callback){
					callback(deleteResult);
				}
			},
			error : function(xhr, status, er){
				if(error){
					error(er);
				}
			}
		});
	}
	
	function update(reply, callback, error){
		
		console.log("RNO: " + reply.rno);
		
		$.ajax({
			type : 'put',
			url : '/replies/' + reply.rno,
			data : JSON.stringify(reply),
			contentType : 'application/json; charset=utf-8',
			success : function(result, status, xhr){
				if(callback){
					callback(result);
				}
			},
			error : function(xhr, status, er){
				if(error){
					error(er);
				}
			}
		});
	}
	
	function get(rno, callback, error){
		
		$.get("/replies/" + rno + ".json", function(result){
			
			if(callback){
				callback(result);
			}
		}).fail(function(xhr, status, err){
			if(error){
				error();
			}
		});
	}
	
	function displayTime(timeValue){
		
		var today = new Date();
		
		var gap = today.getTime() - timeValue;
		
		var dateObj = new Date(timeValue);
		var str = "";
		
		if (gap < (1000 * 60 * 60 * 24)){
			
			var hh = dateObj.getHours();
			var mi = dateObj.getMinutes();
			var ss = dateObj.getSeconds();
			
			return [ (hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi,
					':', (ss > 9 ? '' : '0') + ss].join('');
		}else{
			var yy = dateObj.getFullYear();
			var mm = dateObj.getMonth() + 1;
			var dd = dateObj.getDate();
			
			return [ yy, '/', (mm > 9 ? '' : '0') + mm, '/',
					(dd > 9 ? '' : '0') + dd].join('');
		}
	}
	;
	
	return {add:add, get:get, getList:getList, remove:remove, update:update, displayTime:displayTime};
})();