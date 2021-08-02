<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
   request.setCharacterEncoding("utf-8");
%>    

<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
   <meta charset="UTF-8">
   <title>글 상세보기</title>
   <style type="text/css">
		#tr_btn_modify {
			display: none;
		}
		#tr_img_modify {
			display: none;
		}
   </style>
   <script src="http://code.jquery.com/jquery-latest.min.js"></script>
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
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
      /* 
		function fn_enable(obj) {
			document.getElementById("title_modify").disabled=false;
			document.getElementById("content_modify").disabled=false;
			document.getElementById("imageFileName_modify").disabled=false;
			document.getElementById("tr_btn_modify").style.display='inline-block';
			document.getElementById("tr_img_modify").style.display='inline-block';
			document.getElementById("tr_btn").style.display='none';
		}
		 */
		 function fn_enable(obj) {
				document.getElementById("div_view_article").style.display='none';
				document.getElementById("div_modify_form").style.display='block';
			}
      function fn_remove_article(url, articleNo) {
         var form = document.createElement("form");
         form.setAttribute("method", "post");
         form.setAttribute("action", url);
         
         var articleNoInput = document.createElement("input");
         articleNoInput.setAttribute("type", "hidden");
         articleNoInput.setAttribute("name", "articleNo");
         articleNoInput.setAttribute("value", articleNo);
         
         form.appendChild(articleNoInput);
         document.body.appendChild(form);
         form.submit();
      }
      
      function backToList(obj) {
         obj.action = "${contextPath}/board/listArticles.do"
         obj.submit();
      }
      
      function backToArticle(obj) {
          obj.action = "${contextPath}/board/viewArticle.do?articleNo=${article.articleNo}"
          obj.submit();
       }
      
      function fn_modify_article(obj) {
         obj.action = "${contextPath}/board/modArticle.do"
         obj.submit();
      }
      
      function fn_reply_form(url, parentNo) {
         var form = document.createElement("form");
         form.setAttribute("method", "post");
         form.setAttribute("action", url);
         
         var parentNoInput = document.createElement("input");
         parentNoInput.setAttribute("type", "hidden");
         parentNoInput.setAttribute("name", "parentNo");
         parentNoInput.setAttribute("value", parentNo);
         
         form.appendChild(parentNoInput);
         document.body.appendChild(form);
         form.submit();
      }
   
   </script>
</head>
<body>
	<div class="container">
		<div class="page-title">
		    <div class="container" style="cursor: default;">
		    <h3>MOME Community</h3>
		</div>
	</div>
	
	<!-- 게시글 보기 -->
	<div id="div_view_article">
	<table class="table table-hover">    
      	<tr style="font-family:돋음; font-size:12; height:16; display: none;">
            <td >
               글번호
            </td>
            <td>
               <input class="form-control"type="text" value="${article.articleNo }" disabled />
               <input class="form-control"type="hidden" name="articleNo" value="${article.articleNo }" />
            </td>
         </tr>     
         <tr>
            <td style = "font-family:돋음; font-size:12" height="16">
               작성자
            </td>
            <td>
               <input type="text" value="${article.id }" name="id" class="form-control" disabled />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               제목
            </td>
            <td>
               <input class="form-control"type="text" value="${article.title }" name="title" id="title_modify" disabled />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               내용
            </td>
            <td>
               <textarea class="form-control" rows="20" cols="60" name="content" id="content_modify" disabled style="resize: none;">${article.content}</textarea>
            </td>
         </tr>
         
         <c:if test="${not empty article.imageFileName && article.imageFileName != 'null' }">
            <tr>
               <td style="font-family:돋음; font-size:12" height="16">
                  이미지
               </td>
               <td>
                  <input type="hidden" name="originalFileName" value="${article.imageFileName }" />
                  <img src="${contextPath}/download.do?articleNo=${article.articleNo}&imageFileName=${article.imageFileName}" width=400><br/>
               </td>
            </tr>
            <tr id="tr_img_modify">
               <td>
                  <input type="file" name="imageFileName" id="imageFileName_modify" disabled onchange="readURL(this);" />         <!-- 이미지 파일 선택 기능 -->
               </td>
            </tr>
         </c:if>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               등록일자
            </td>
            <td>
               <input class="form-control" type="text" value='<fmt:formatDate value="${article.writeDate }" pattern="yyyy년 MM월 dd일 HH시 mm분"/>' disabled />
            </td>
         </tr>
         <tr id="tr_btn">
            <td colspan="2" align="center">
               <input type="button" class="btn btn-primary" value="수정하기" onclick="fn_enable(this.form)" />
               <input type="button" class="btn btn-primary" value="삭제하기" onclick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNo })" />
               <input type="button" class="btn btn-primary" value="게시글목록" onclick="backToList(frmArticle)"/>
               <input type="button" class="btn btn-primary" value="답글달기" onclick="fn_reply_form('${contextPath}/board/replyForm.do', ${article.articleNo})" />   <!-- 요청명과 글번호를 전달함  -->
            </td>
         </tr>
      </table>
      </div>
      
		<!-- 수정버튼 누른 후 -->
	<div id="div_modify_form" style="display: none;">
   <form action="${contextPath}" name="frmArticle" method="post" enctype="multipart/form-data">
      <table class="table table-hover">    
      	<tr style="font-family:돋음; font-size:12; height:16; display: none;">
            <td >
               글번호
            </td>
            <td>
               <input class="form-control"type="text" value="${article.articleNo }" disabled />
               <input class="form-control"type="hidden" name="articleNo" value="${article.articleNo }" />
            </td>
         </tr>     
         <tr>
            <td style = "font-family:돋음; font-size:12" height="16">
               작성자
            </td>
            <td>
               <input type="text" value="${article.id }" name="id" class="form-control" disabled />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               제목
            </td>
            <td>
               <input class="form-control"type="text" value="${article.title }" name="title" id="title_modify" />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               내용
            </td>
            <td>
               <textarea class="form-control" rows="20" cols="60" name="content" id="content_modify" style="resize: none;">${article.content}</textarea>
            </td>
         </tr>
         
            <tr>
               <td style="font-family:돋음; font-size:12" height="16">
                  이미지
               </td>
               <td>
					<input type="hidden" name="originalFileName" value="${article.imageFileName }" />
					<div class="image-upload-wrap">
						<input type='file' name="imageFileName" class="file-upload-input"id="imageFileName_modify" onchange="readURL(this);" accept="image/*" />
						<div class="drag-text">
							<h3>드래그 또는 클릭</h3>
						</div>
					</div>
					<div class="file-upload-content">
						<img class="file-upload-image" src="${contextPath}/download.do?articleNo=${article.articleNo}&imageFileName=${article.imageFileName}" alt="미리보기" />
						<div class="image-title-wrap">
							<button type="button" onclick="removeUpload()" class="remove-image">사진제거</button>
						</div>
					</div>
               </td>
            </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               등록일자
            </td>
            <td>
               <input class="form-control" type="text" value='<fmt:formatDate value="${article.writeDate }" pattern="yyyy년 MM월 dd일 HH시 mm분" />' disabled />
            </td>
         </tr>
         <!-- 수정하기 클릭시에 나타나는 두개 메뉴  -->
         <tr>
            <td colspan="2" align="center" >
               <input type="button" class="btn btn-primary" value="수정반영하기"  onclick="fn_modify_article(frmArticle)" />
               <input type="button" class="btn btn-primary" value="취소" onclick="backToArticle(this.form)"/>
            </td>
         </tr> 
      </table>
   </form>
   </div>
   </div>
   
   <!-- 댓글 -->
   <div>

    <form method="post" action="/reply/write">
    
        <p>
            <label>댓글 작성자</label> <input type="text" name="writer">
        </p>
        <p>
            <textarea rows="5" cols="50" name="content"></textarea>
        </p>
        <p>
        	<input type="hidden" name="bno" value="${view.bno}">
            <button type="submit">댓글 작성</button>
        </p>
    </form>
    
</div>
   <!-- 댓글 끝 -->
</body>
</html>