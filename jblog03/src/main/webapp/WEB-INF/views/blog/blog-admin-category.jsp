<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-3.4.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/ejs/ejs.js"></script>
<script>
var listItemTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-item-template.ejs"
});
var listTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-template.ejs"
});

var messageBox = function(title, message, callback){
	$("#dialog-message p").text(message);
	$("#dialog-message")
		.attr("title", title)
		.dialog({
			modal: true,
			buttons: {
				"확인": function() {
					$(this).dialog( "close" );
		        }
			},
			close: callback
		});
};

var fetchList = function(){
	$.ajax({
		url: '${pageContext.request.contextPath }/${authUser.id }/spa/category',
		async: true,
		type: 'get',
		dataType: 'json',
		data: '',
		success: function(response){
			if(response.result != "success"){
				console.error(response.message);
				return;
			}
			
			var contextPath = '${pageContext.request.contextPath}/assets/images/delete.jpg';
			var deleteLink = '${pageContext.servletContext.contextPath }/${authUser.id }/';
			response.contextPath= contextPath;
			
			var html = listTemplate.render(response);
			$(".admin-cat").append(html);
		},
		error: function(xhr, status, e){
			console.error(status + ":" + e);
		}
	});	
}

$(function(){
	fetchList();
	
	$('a').click(function() {
		console.log($(this).data('no'));
	});
	
	$('#add-form').submit(function(event){
		event.preventDefault();
		
		var vo = {};
		vo.name = $("#name").val();
		console.log(vo.name);
		if(vo.name == ''){
			messageBox("카테고리 추가하기", "카테고리명은 필수 항목 입니다.", function(){
				$("#category").focus();
			});
			return;
		}
		
		vo.description = $("#description").val();
		
		console.log(vo.description);
		
		if(vo.password == ''){
			messageBox("카테고리 추가하기", "설명은 필수 항목 입니다.", function(){
				$("#description").focus();
			});
			return;
		}
		
		$.ajax({
			url: '${pageContext.request.contextPath}/${authUser.id }/admin/category',
			async: true,
			type: 'post',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(vo),
			success: function(response){
				console.log('dddd');
				
				if(response.result != "success"){
					console.error(response.message);
					return;
				}
				// rendering
				// render(response.data, true);
				
				var k = $('.admin-cat tr').last().index();
				var num = k + 1;
				response.data.num = num;
				
				var contextPath = '${pageContext.request.contextPath }/assets/images/delete.jpg';
				response.data.contextPath= contextPath;
				
				console.log(response.data);
				// var html = listItemTemplate.render(response.data);
				// $('.admin-cat tr:last').after(html);
				
				// location.reload();
				
				
				$('.admin-cat tr td').remove();
				fetchList();
				
				// form reset
				$("#add-form")[0].reset();
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});
	});
	
	$(document).on('click','.admin-cat tr td a img', function(event){
		event.preventDefault();
		
		var vo = {};
		vo.no = $(this).parent().data('no');
		
		$.ajax({
			url: '${pageContext.request.contextPath}/${authUser.id }/spa/delete',
			async: true,
			type: 'delete',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(vo),
			success: function(response){
				
				if(response.result != "success"){
					console.error(response.message);
					return;
				}
				console.log(response.data.no);
				console.log($('.admin-cat tr[164]'));
				// $(".admin-cat tr td a[data-no=" + response.data.no + "]").parent()
				// .parent().remove();
				
				$('.admin-cat tr td').remove();
				fetchList();
				
				// location.reload();
				
	            return;
				
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});
	});
});
</script>
</head>
<body>
	<div id="container">
		<div id="header">
			<h1><a href="${pageContext.request.contextPath}/${id }">${blogVo.title }</a></h1>
			<c:import url="/WEB-INF/views/includes/blog-header.jsp" />
		</div>
		<div id="wrapper">
			<div id="content" class="full-screen">
				<c:import url="/WEB-INF/views/includes/admin-header.jsp" />
			      	<table class="admin-cat">
			      		<tr id='menu-title'>
							<th>번호</th>
							<th>카테고리명</th>
							<th>포스트 수</th>
							<th>설명</th>
							<th>삭제</th>
						</tr>
						
				      	
			      		<!-- 
				      	<c:forEach items='${list }' var='vo' step='1' varStatus='status'>
				      		<tr>
				      			<td>${listCount-status.index }</td>
				      			<td>${vo.name }</td>
				      			<td>${vo.count }</td>
				      			<td>${vo.description }</td>
				      			<td>
				      			<c:if test="${vo.count == 0 && deleteFalse != 1}">
				      				<a href="${pageContext.servletContext.contextPath }/${authUser.id }/${vo.no }/delete"><img src="${pageContext.request.contextPath}/assets/images/delete.jpg"></a>
				      			</c:if>
				      			</td>
				      		</tr>
				      	</c:forEach>
				      	-->
				
					</table>
      	
				      	
      			<h4 class="n-c">새로운 카테고리 추가</h4>
      			<form id="add-form" action="" method="post">
			      	<table id="admin-cat-add">
			      		<tr>
			      			<td class="t">카테고리명</td>
			      			<td><input id='name' type="text" name="name"></td>
			      		</tr>
			      		<tr>
			      			<td class="t">설명</td>
			      			<td><input id='description' type="text" name="description"></td>
			      		</tr>
			      		<tr>
			      			<td class="s">&nbsp;</td>
			      			<td><input id='add' type="submit" value="카테고리 추가"></td>
			      		</tr>
			      	</table>
		      	</form>
			</div>
			<div id="dialog-message" title="" style="display:none">
  				<p></p>
			</div>
		</div>
		<div id="footer">
			<p>
				<strong>Spring 이야기</strong> is powered by JBlog (c)2016
			</p>
		</div>
	</div>
</body>
</html>