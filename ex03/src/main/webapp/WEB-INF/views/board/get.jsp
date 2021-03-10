<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp" %>


<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Read</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
		
			<div class="panel panel-heading">Board Read Page</div>
			<div class="panel-body">
					<div class="form-group">
						<label>Bno</label> 
						<input class="form-control" name="bno" value='<c:out value="${board.bno }"/>' readonly="readonly">
					</div>
					<div class="form-group">
						<label>Title</label>
						<input class="form-control" name='title' value='<c:out value="${board.title }"/>' readonly="readonly">
					</div>
					<div class="form-group">
						<label>Text area</label>
						<textarea class="form-control" rows="3" name="content" readonly="readonly"><c:out value="${board.content }"/></textarea>
					</div>
					<div class="form-group">
						<label>Writer</label>
						<input class="form-control" name='writer' value='<c:out value="${board.writer }"/>' readonly="readonly">
					</div>
					<button data-oper='modify' class="btn btn-default" onclick="location.href='/board/modify?bno=<c:out value="${board.bno }"/>'">Modify</button>
					<button data-oper='list' class="btn btn-info" onclick="location.href='/board/list'">List</button>
					
					<form id='operForm' action="/board/modify" method="get">
						<input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno }"/>'>
						<input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>
						<input type='hidden' name='amount' value='<c:out value="${cri.amount }"/>'>
						<input type='hidden' name='keyword' value='<c:out value="${cri.keyword }"/>'>
						<input type='hidden' name='type' value='<c:out value="${cri.type }"/>'>
					</form>
					</div>
			</div>
		</div>
	</div>
<div class='row'>
	<div class="col-lg-12">
		<div class="panel panel-default">
<!-- 			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i>Reply
			</div>
 -->			
			<div class="panel-heading">
			<i class="fa fa-comments fa-fw"></i>Reply
				<button id='addReplyBtn' class="btn btn-primary btn-xs pull-right">new Reply</button>
			</div>
			<div class="panel-body">
				<ul class="chat">
					<li class="left clearfix" data-rno='12'>
						<div>
							<div class="header">
								<strong class="primary-font">user00</strong>
								<small class="pull-right text-muted">2020-02-11 12:41</small>
							</div>
							<p>Good job!</p>
						</div>
				</ul>
				
			</div>
			<div class="panel-footer">
			
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label>Reply</label>
					<input class="form-control" name='reply' value='New Reply!!!!'>
				</div>
				<div class="form-group">
					<label>Replyer</label>
					<input class="form-control" name='replyer' value='replyer'>
				</div>
				<div class="form-group">
					<label>Reply Date</label>
					<input class="form-control" name='replyDate' value=''>
				</div>
			</div>
<div class="modal-footer">
	<button id='modalModBtn' type="button" class="btn btn-warning">Modify</button>
	<button id='modalRemoveBtn' type="button" class="btn btn-danger">Remove</button>
	<button id='modalRegisterBtn' type="button" class="btn btn-primary">Register</button>
	<button id='modalCloseBtn' type="button" class="btn btn-default">Close</button>
</div>
		</div>
	</div>
</div>
<script type="text/javascript"  src="/resources/js/reply.js"></script>
<script>
$(document).ready(function(){
	
	var bnoValue = '<c:out value="${board.bno}"/>';
	var replyUL = $(".chat");
	
	// showList()는 페이지 번호를 파라미터로 받도록 설계하고, 만일 파라미터가 없는 경우에는 자동으로 1페이지가 되도록 설정한다.
	// 브라우저에서 DOM처리가 끝나면 자동으로 showList()가 호출되면서 <ul> 태그 내에 내용으로 처리된다.
	// 만일 1페이지가 아닌 경우라면 <ul>에 <li>들이 추가되는 형태이다.
		showList(1);
		
		function showList(page){
			
			console.log("show list " + page);
			
			replyService.getList({bno:bnoValue,page: page|| 1}, function(replyCnt, list){
				
				console.log("replyCnt: "+ replyCnt);
				console.log("list: " + list);
				console.log(list);
				
				if(page == -1){
					pageNum = Math.ceil(replyCnt/10.0);
					showList(pageNum);
					return;
				}
				
				var str="";
				
				if(list == null || list.length == 0){
					replyUL.html("");
					
					return;
				}
				for (var i = 0, len = list.length || 0; i < len; i++){
					
					str +="<li class='left clearfix' data-rno='" +list[i].rno+"'>";
					str +="   <div><div class='header'><strong class='primary-font'>"+list[i].replyer+"</strong>";
					str +="		<small class='pull-right text-muted'>"+replyService.displayTime(list[i].replyDate)+"</small></div>";
					str +="		<p>"+list[i].reply+"</p></div></li>";	
				}
				
				replyUL.html(str);
				
				showReplyPage(replyCnt);
			});
		}
		
		var modal = $(".modal");
		var modalInputReply = modal.find("input[name='reply']");
		var modalInputReplyer = modal.find("input[name='replyer']");
		var modalInputReplyDate = modal.find("input[name='replyDate']");
		
		var modalModBtn = $("#modalModBtn");
		var modalRemoveBtn = $("#modalRemoveBtn");
		var modalRegisterBtn = $("#modalRegisterBtn");
		
		$("#addReplyBtn").on("click", function(e){
			
			modal.find("input").val("");
			modalInputReplyDate.closest("div").hide();
			modal.find("button[id !='modalCloseBtn']").hide();
			
			modalRegisterBtn.show();
			
			$(".modal").modal("show");
		});
		
		modalRegisterBtn.on("click",function(e){
			
			var reply= {
					reply : modalInputReply.val(),
					replyer : modalInputReplyer.val(),
					bno : bnoValue
			};
			replyService.add(reply, function(result){
			
				alert(result);
				
				modal.find("input").val("");
				modal.modal("hide");
				
				// showList(1)을 추가해서 댓글이 추가된 후 그 사이에 새로운 댓글도 가져오는 역활을 한다.
				//showList(1);
				showList(-1);
			});
		});
		
		// 댓글을 조회하는 행위는 현재의 경우 모든 내용이 화면에 있기 때문에 별도로 조회할 필요는 없지만, 원칙적으로 ajax로 댓글을 조회한 후 수정/삭제를 하는것이 정상이다.
		// 댓글을 가져온 후에는 필요한 항목들을 채우고 수정과 삭제에 필요한 댓글 번호(rno)는 'data-rno' 속성을 만들어서 추가해둔다.
		// 브라우저에서 특정 댓글을 클릭하면 필요한 내용들만 보이게 된다.
		$(".chat").on("click", "li", function(e){
			
			var rno = $(this).data("rno");
			
			replyService.get(rno, function(reply){
				
				modalInputReply.val(reply.reply);
				modalInputReplyer.val(reply.reply);
				modalInputReplyDate.val(replyService.displayTime(reply.replyDate)).attr("readonly", "readonly");
				modal.data("rno", reply.rno);
				
				modal.find("button[id !='modalColseBtn']").hide();
				modalModBtn.show();
				modalRemoveBtn.show();
				
				$(".modal").modal("show");
			});
		});
		
		modalModBtn.on("click", function(e){
			
			var reply = {rno:modal.data("rno"), reply:modalInputReply.val()};
			
			replyService.update(reply, function(result){
				
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		modalRemoveBtn.on("click", function(e){
			
			var rno = modal.data("rno");
			
			replyService.remove(rno, function(result){
				
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		var pageNum = 1;
		var replyPageFooter = $(".panel-footer");
		
		// showReplyPage()는 기존에 JAVA로 작성된 PageMaker의 JavaScript 버전에 해당된다
		function showReplyPage(replyCnt){
			
			var endNum = Math.ceil(pageNum / 10.0)*10;
			var startNum = endNum - 9;
			
			var prev = startNum != 1;
			var next = false;
			
			if(endNum * 10 >= replyCnt){
				endNum = Math.ceil(replyCnt/10.0);
			}
			
			if(endNum * 10 < replyCnt){
				next = true;
			}
			
			var str = "<ul class='pagination pull-right'>";
			
			if(prev){
				str+= "<li class='page-item'><a class='page-link' href='"+ (startNum -1) +"'>Previous</a></li>";
			}
			
			for(var i = startNum; i <= endNum; i++){
				
				var active = pageNum == i? "active":"";
				
				str+= "<li class='page-item "+ active +" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
			}
			
			if(next){
				str+= "<li class='page-item'><a class='page-link' href='"+ (endNum +1) +"'>Next</a></li>";
			}
			
			str += "</ul></div>";
			
			console.log(str);
			
			replyPageFooter.html(str);
		}
		
		// 페이지 번호는 <a> 태그 내에 존재하므로 이벤트 처리에는 <a> 태그의 기본 동작을 제한하고 preventDefault() 댓글 페이지 번호를 변경한 후 해당 페이지의
		// 댓글을 가져오도록 한다.
		replyPageFooter.on("click", "li a", function(e){
			e.preventDefault();
			console.log("page click");
			
			var targetPageNum = $(this).attr("href");
			
			console.log("targetPageNum: " + targetPageNum);
			
			pageNum = targetPageNum;
			
			showList(pageNum);
		});
});
</script>

<!-- <script>
console.log("======================");
console.log("JS TEST");

var bnoValue = '<c:out value="${board.bno}"/>';

replyService.getList({bno:bnoValue, page:1}, function(list){
	
	for(var i = 0, len = list.length||0; i < len; i++){
		console.log(list[i]);
	}
});

//for replyService add test
replyService.add(
		{reply:"JS TEST", replyer:"tester", bno:bnoValue}
		,
		function(result){
			alert("RESULT: " + result);
		}
	);

replyService.remove(23, function(count){
	
	console.log(count);
	
	if(count === "success"){
		alert("REMOVEED");
	}
		}, function(err){
			alert("ERROR..");
	});

replyService.update({
	rno : 22,
	bno : bnoValue,
	reply : "Modified Reply.."
	},function(result){
		alert("수정완료....");
	});

replyService.get(10, function(data){
	console.log(data);
});
</script>-->

<script type="text/javascript">
$(document).ready(function(){
	
	console.log(replyService);
	
});
</script>

<script type="text/javascript">
$(document).ready(function(){
	
	var operForm = $("#operForm");
	
	$("button[data-oper='modify']").on("click", function(e){
	
		operForm.attr("action", "/board/modify").submit();
	});
	
	$("button[data-oper='list']").on("click", function(e){
		
		operForm.find("#bno").remove();
		operForm.attr("action", "/board/list")
		operForm.submit();
	});
});
</script>
<%@ include file="../includes/footer.jsp" %>