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
   <title>문의글 상세보기</title>
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
         if (input.files && input.files[0]) {                     /* 이미지 파일 첨부 시 미리 보기 기능 */
            var reader = new FileReader();
            reader.onload = function(e) {
               $('#preview').attr("src", e.target.result);            /* e.target은 이벤트가 일어난 대상, 즉 input 자신을 가리킴*/
            }                                             /* result는 첨부파일들이 특수하게 가공된 URL을 출력해 줄것임. */
            reader.readAsDataURL(input.files[0]);
         }
      }
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
      function fn_remove_article(url, q_articleNo) {
         var form = document.createElement("form");
         form.setAttribute("method", "post");
         form.setAttribute("action", url);
         
         var articleNoInput = document.createElement("input");
         articleNoInput.setAttribute("type", "hidden");
         articleNoInput.setAttribute("name", "q_articleNo");
         articleNoInput.setAttribute("value", q_articleNo);
         
         form.appendChild(articleNoInput);
         document.body.appendChild(form);
         form.submit();
      }
      
      function backToList(obj) {
         obj.action = "${contextPath}/Qboard/QlistArticles.do"
         obj.submit();
      }
      
      function backToArticle(obj) {
          obj.action = "${contextPath}/Qboard/QviewArticle.do?q_articleNo=${article.q_articleNo}"
          obj.submit();
       }
      
      function fn_modify_article(obj) {
         obj.action = "${contextPath}/Qboard/QmodArticle.do"
         obj.submit();
      }
      
      function fn_reply_form(url, q_parentNo) {
         var form = document.createElement("form");
         form.setAttribute("method", "post");
         form.setAttribute("action", url);
         
         var parentNoInput = document.createElement("input");
         parentNoInput.setAttribute("type", "hidden");
         parentNoInput.setAttribute("name", "q_parentNo");
         parentNoInput.setAttribute("value", q_parentNo);
         
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
		    <h3>MOME QnA</h3>
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
               <input class="form-control"type="text" value="${article.q_articleNo }" disabled />
               <input class="form-control"type="hidden" name="q_articleNo" value="${article.q_articleNo }" />
            </td>
         </tr>     
         <tr>
            <td style = "font-family:돋음; font-size:12" height="16">
               작성자
            </td>
            <td>
               <input type="text" value="${article.q_id }" name="q_id" class="form-control" disabled />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               제목
            </td>
            <td>
               <input class="form-control"type="text" value="${article.q_title }" name="q_title" id="title_modify" disabled />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               내용
            </td>
            <td>
               <textarea class="form-control" rows="20" cols="60" name="q_content" id="content_modify" disabled style="resize: none;">${article.q_content}</textarea>
            </td>
         </tr>
         
         <c:if test="${not empty article.q_imageFileName && article.q_imageFileName != 'null' }">
            <tr>
               <td style="font-family:돋음; font-size:12" height="16">
                  이미지
               </td>
               <td>
                  <input type="hidden" name="originalFileName" value="${article.q_imageFileName }" />
                  <img src="${contextPath}/Qdownload.do?q_articleNo=${article.q_articleNo}&q_imageFileName=${article.q_imageFileName}" width=400><br/>
               </td>
            </tr>
            <tr id="tr_img_modify">
               <td>
                  <input type="file" name="q_imageFileName" id="imageFileName_modify" disabled onchange="readURL(this);" />         <!-- 이미지 파일 선택 기능 -->
               </td>
            </tr>
         </c:if>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               등록일자
            </td>
            <td>
               <input class="form-control" type="text" value='<fmt:formatDate value="${article.q_writeDate }" pattern="yyyy년 MM월 dd일 HH시 mm분"/>' disabled />
            </td>
         </tr>
         <tr id="tr_btn">
            <td colspan="2" align="center">
               <input type="button" class="btn btn-primary" value="수정하기" onclick="fn_enable(this.form)" />
               <input type="button" class="btn btn-primary" value="삭제하기" onclick="fn_remove_article('${contextPath}/Qboard/QremoveArticle.do', ${article.q_articleNo })" />
               <input type="button" class="btn btn-primary" value="게시글목록" onclick="backToList(frmArticle)"/>
               <input type="button" class="btn btn-primary" value="답글달기" onclick="fn_reply_form('${contextPath}/Qboard/QreplyForm.do', ${article.q_articleNo})" />   <!-- 요청명과 글번호를 전달함  -->
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
               <input class="form-control"type="text" value="${article.q_articleNo }" disabled />
               <input class="form-control"type="hidden" name="q_articleNo" value="${article.q_articleNo }" />
            </td>
         </tr>     
         <tr>
            <td style = "font-family:돋음; font-size:12" height="16">
               작성자
            </td>
            <td>
               <input type="text" value="${article.q_id }" name="q_id" class="form-control" disabled />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               제목
            </td>
            <td>
               <input class="form-control"type="text" value="${article.q_title }" name="q_title" id="title_modify" />
            </td>
         </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               내용
            </td>
            <td>
               <textarea class="form-control" rows="20" cols="60" name="q_content" id="content_modify" style="resize: none;">${article.q_content}</textarea>
            </td>
         </tr>
         
            <tr>
               <td style="font-family:돋음; font-size:12" height="16">
                  이미지
               </td>
               <td>
                  <input type="hidden" name="originalFileName" value="${article.q_imageFileName }" />
                  <img alt="사진" src="${contextPath}/Qdownload.do?q_articleNo=${article.q_articleNo}&q_imageFileName=${article.q_imageFileName}" id="preview" width="400"><br/>
                  <br/><input type="file" name="q_imageFileName" id="imageFileName_modify" onchange="readURL(this);" />         <!-- 이미지 파일 선택 기능 -->
               </td>
            </tr>
         
         <tr>
            <td style="font-family:돋음; font-size:12" height="16">
               등록일자
            </td>
            <td>
               <input class="form-control" type="text" value='<fmt:formatDate value="${article.q_writeDate }" pattern="yyyy년 MM월 dd일 HH시 mm분" />' disabled />
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
</body>
</html>