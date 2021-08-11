<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="totArticles" value="${articleMap.totArticles }" />
<c:set var="section" value="${articleMap.section }" />
<c:set var="pageNum" value="${articleMap.pageNum }" />
<c:set var="searchArticleList1" value="${searchArticleList1 }" />

<%
	request.setCharacterEncoding("utf-8");
%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>MOME QnA</title>
	<style type="text/css">
		.class1 {text-decoration: none;}
		.class2 {text-align: center; font-size: 30px;}
		.no-line {text-decoration: none;}
		.sel-page {text-decoration: none; color: red;}
	</style>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board_menu.css">
<script type="text/javascript">
		var newName,
		    n = 0;
		var popleft,
		    n = 100;
		// 팝업 창 제목 만들기 함수(다중 팝업을 위한..)
		function newWindow(value) {
		    n = n + 1;
		    newName = value + n;
		}
		// 팝업 창 위치
		function locationWindow(value) {
		    n = n + 10;
		    popleft = value + n;
		}
function searchCheck1() {
			
			if (document.frmSearch1.qkeyWord.value.length == 0) {
				alert("검색단어를 입력하세요.");
				return false;
			}
			
			return true;
		}
	</script>
</head>
<body>
	<section class="notice">
		<div class="page-title">
		    <div class="container" OnClick="location.href ='${pageContext.request.contextPath}/Qboard/QlistArticles.do' " style="cursor:pointer;">
		        <h3>MOME QnA</h3>
		    </div>
		</div>
<!-- board menu area -->
		<div class="container">
		    <ul>
				<li><a href="${pageContext.request.contextPath}/intro.html">지도로 돌아가기</a></li>
				<li><a href="${pageContext.request.contextPath}/board/listArticles.do">게시판으로</a></li>
				<li><a onclick="window.open('${pageContext.request.contextPath}/chat.jsp',newName, 'width=630, height=750, left=100, top=150, location=no, status=no, scrollbars=no')" style="cursor:pointer;">채팅하기</a></li>
				<li><a href="${pageContext.request.contextPath}/blog.jsp">Blog</a></li>
		    </ul>
		</div>
	 <!-- board seach area -->
                        <div id="board-search">
                            <div class="container">
                                <div class="search-window">
                                    <div
                                        class="search-wrap">
                                        <!-- 검색 -->
                                        <form action="${pageContext.request.contextPath}/Qboard/QsearchArticles.do" name="frmSearch1">
                                            <select name="qkeyField" class="select">
                                                <option value="q_title">제목</option>
                                                <option value="q_id">작성자</option>
                                            </select>
                                            <input type="text" name="qkeyWord" placeholder="검색어를 입력해주세요." />
                                            <input id="search" name="search" type="submit" class="btn btn-dark"  value="검색" onclick="return searchCheck1()"></form>
                                        </div>
                                    </div>
                                </div>
                            </div>
	<!-- board list area -->
		<div id="board-list">
			<div class="container">
				<table class="board-table">
					<thead>
						<tr style="font-size: 16px;">
							<th scope="col" class="th-num">번호</th>
							<th scope="col" class="th-title">제목</th>
							<th scope="col" class="th-writer">작성자</th>
							<th scope="col" class="th-date">등록일</th>
						</tr>
					</thead>
					<c:choose>
						<c:when test="${empty searchArticleList1}">
							<tr height="10">
								<td colspan="4">
									<p align="center">
										<b><span style="font-size: 15pt;">찾는 글이 없습니다.</span></b>
									</p>
								</td>
							</tr>
						</c:when>
						<c:when test="${!empty searchArticleList1  }">
							<c:forEach var="article" items="${searchArticleList1 }" varStatus="articleNum">			<!--varStatus :반복상태변수이름 --속성들 index, count, first, last  -->
								<tr>
						<td width="10%" align="center">${articleNum.count}</td>
						<td width="50%" align="left" style="padding-left: 5%;">
							<c:choose>
								<c:when test="${article.q_level > 1 }">
									<c:forEach begin="1" end="${article.q_level }" step="1">
										<span style="padding-left: 20px;"></span>
									</c:forEach>
									<c:choose>
										<c:when test="${article.q_newArticle == true }">
											<span style="font-size: 15px; color: red">↪&nbsp;</span><img src="${contextPath}/assets/img/ico_new.gif">&nbsp;<img src="${contextPath}/assets/img/ico_re.gif">
											<a class="class1" href="${contextPath}/Qboard/QviewArticle.do?q_articleNo=${article.q_articleNo}">${article.q_title}</a>
										</c:when>
										<c:otherwise>
											<span style="font-size: 15px; color: red">↪&nbsp;</span><img src="${contextPath}/assets/img/ico_re.gif">
											<a class="class1" href="${contextPath}/Qboard/QviewArticle.do?q_articleNo=${article.q_articleNo}">${article.q_title}</a>
										</c:otherwise>
									</c:choose>
								</c:when>
							<c:otherwise>
									<c:choose>
										<c:when test="${article.q_newArticle == true }">
											<img src="${contextPath}/assets/img/ico_new.gif">
											<a class="class1" href="${contextPath}/Qboard/QviewArticle.do?q_articleNo=${article.q_articleNo}">${article.q_title}</a>
											</c:when>
										<c:otherwise>
											<a class="class1" href="${contextPath}/Qboard/QviewArticle.do?q_articleNo=${article.q_articleNo}">${article.q_title}</a>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</td>
						<td width="20%" align="center">${article.q_id}</td>
						<td width="20%" align="center"><fmt:formatDate value="${article.q_writeDate }" pattern="yyyy-MM-dd HH:mm"/></td>
					</tr>
							</c:forEach>
						</c:when>
					</c:choose>
				</table>
				<div class="class2">
		<c:if test="${totArticles != null }">
			<c:choose>
				<c:when test="${totArticles > 100 }">			<!-- 글 개수가 100 초과인 경우 -->
					<c:forEach var="page" begin="1" end="10" step="1">
						
						<c:if test="${section > 1 && page == 1 }">
							<a class="no-line"  href="${contextPath}/Qboard/QlistArticles.do?section=${section-1}&pageNum=${(section-1)*10 +1}">&nbsp; pre </a>
						</c:if>
						
						<a class="no-line" href="">${(section-1)*10 +page}</a>			<!-- 실제페이지 숫자표시 -->
						
						<c:if test="${page == 10 }">
							<a class="no-line" href="${contextPath}/Qboard/QlistArticles.do?section=${section+1}&pageNum=${section*10 +1}">&nbsp; next </a>
						</c:if>		
					</c:forEach>
				</c:when>
				<c:when test="${totArticles == 100 }">			<!-- 등록된 글 개수가 100개인 경우 -->
					<c:forEach var="page" begin="1" end="10" step="1">
						<a class="no-line" href="#">${page}</a>
					</c:forEach>
				</c:when>
				<c:when test="${totArticles < 100 }">			<!-- 글 개수가 100 미만인 경우 -->
					<c:forEach var="page" begin="1" end="${totArticles/10 +1}" step="1">
						<c:choose >
							<c:when test="${page == pageNum}">
								<a class="sel-page" href="${contextPath}/Qboard/QlistArticles.do?section=${section}&pageNum=${page}">${page}</a>
							</c:when>
							<c:otherwise>
								<a class="no-line" href="${contextPath}/Qboard/QlistArticles.do?section=${section}&pageNum=${page}">${page}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>				
				</c:when>
			</c:choose>
		</c:if>
	</div>
	<a class="class1" href="${contextPath}/Qboard/QarticleForm.do">
		<p class="frm_btn">글쓰기</p>
	</a>
			</div>
		</div>
	</section>
</body>
</html>
