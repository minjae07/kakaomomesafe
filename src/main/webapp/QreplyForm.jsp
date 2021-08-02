<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("utf-8");
%>    


<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>답글쓰기</title>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board_menu.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
	<script type="text/javascript">
		function readURL(input) {
			if(input.files && input.files[0]) {			//input 태그에 첫번째 선택파일이 있을때
				var reader = new FileReader();
				reader.onload = function(e) {
					$('.file-upload-image').attr('src', e.target.result);		// input file로 이미지 파일을 선택시 	id가 preview인 <img>태그에 src속성 값에 이미지를 바로 보이도록 변경 
					$('.image-upload-wrap').hide();
					$('.file-upload-content').show();
				}
					reader.readAsDataURL(input.files[0]);				// reader가 File내용을 읽어 DataURL형식의 문자열로 저장
			}	else {
			    	removeUpload();
		  		}	
		}
		function removeUpload() {
			$('.file-upload-input').replaceWith($('.file-upload-input').clone());
			$('.file-upload-content').hide();
			$('.image-upload-wrap').show();
		}
		$('.image-upload-wrap').bind('dragover', function () {
			$('.image-upload-wrap').addClass('image-dropping');
		});
		$('.image-upload-wrap').bind('dragleave', function () {
			$('.image-upload-wrap').removeClass('image-dropping');
		});
		function backToList(obj) {
			obj.action ="${contextPath}/Qboard/QlistArticles.do"
			obj.submit();
		}		
		function boardCheck() {
			if (document.frmReply.q_id.value.length == 0) {
				alert("작성자를 입력하세요.");
				return false;
			}
			if (document.frmReply.q_title.value.length == 0) {
				alert("제목을 입력하세요.");
				return false;
			}
			if (document.frmReply.q_content.value.length == 0) {
				alert("내용을 입력하세요.");
				return false;
			}
			return true;
		}
	</script>
</head>
<body>
	<div class="container">
		<section class="notice">
			<div class="page-title">
				<div class="container" style="cursor: default;">
					<h3>MOME QnA</h3>
				</div>
			</div>
			<form action="${contextPath}/Qboard/QaddReply.do" name="frmReply" method="post" enctype="multipart/form-data">
				<table class="table table-hover">
					<tbody>
						<tr> 
							<td style="font-family:돋음; font-size:12" height="16" >
								<div align="left" >
									<input name="q_id" type="text" class="form-control" maxlength="20" value="관리자" readonly />     
								</div> 
							</td> 
						</tr> 
						<tr>
							<td>
								<input type="text" class="form-control" placeholder="글 제목" name="q_title" maxlength="50" width="" >
							</td>
						</tr>	
						<tr>
							<td>
								<textarea type="text" class="form-control" placeholder="글 내용을 작성하세요" name="q_content" maxlength="1024" style="height: 400px;"></textarea>
							</td>
						</tr>
					</tbody>
				</table>
				<tr>
					<td colspan="2">
						<input type="submit" class="btn btn-primary pull-right" value="답글쓰기" onclick="return boardCheck()" /> 
						<input type="button" class="btn btn-primary pull-right" value="취소" onclick="backToList(this.form)" />
					</td>
				</tr>
				<div class="image-upload-wrap">
					<input name="q_imageFileName" class="file-upload-input" type='file' onchange="readURL(this);" accept="image/*" />
					<div class="drag-text">
						<h3>드래그 또는 클릭</h3>
					</div>
				</div>
				<div class="file-upload-content">
					<img class="file-upload-image" src="#" alt="사진" />
					<div class="image-title-wrap">
						<button type="button" onclick="removeUpload()" class="remove-image">사진제거</button>
					</div>
				</div>
			</form>
		</section>
	</div>
</body>
</html>