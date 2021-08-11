<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>MOME Town</title>
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="script/parallax.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="./assets/css/board.css">
<link rel="stylesheet" href="./assets/css/board_menu.css">
<link rel="stylesheet" href="./assets/css/main.css">
<body>
	<div class="menu_container">
		<div class="bird-container bird-container--one">
			<div class="bird bird--one"></div>
		</div>
		<div class="bird-container bird-container--two">
			<div class="bird bird--two"></div>
		</div>
		<div class="bird-container bird-container--three">
			<div class="bird bird--three"></div>
		</div>
		<div class="bird-container bird-container--four">
			<div class="bird bird--four"></div>
		</div>
		<div class="contents">
			<span>
				<a href="${contextPath}/board/listArticles.do">
				<img src="assets/img/car1.png" style="max-width: 250px;" /></a>
			</span>
			<div class="tooltip-content">
				<p>
					<h3>MOME Community</h3><br/>
	                당신의 궁금한 모든 것을<br/>물어보고<br/>
	                몰랐던 최근 경산소식을<br/>만나보세요
				</p>
			</div>
		</div>
		<div class="contents" style="left: 500px; bottom: 6vh;">
			<span>
				<a href="${contextPath}/Qboard/QlistArticles.do">
				<img src="assets/img/car2.png" style="max-width: 250px; " /></a>
			</span>
			<div class="tooltip-content">
				<p>
					<h3>MOME QnA</h3><br/>
					MOMESafe의 <br/>개발자, 운영자와의<br/>
	                소통창구 입니다.<br/><br/>
	                메일제보 : momesafe@safe.com
				</p>
			</div>
		</div>
		<div class="contents" style="left: 1000px;">
			<span>
				<a href="${contextPath}/blog.jsp">
				<img src="assets/img/car3.png" style="max-width: 250px; " /></a>
			</span>
			<div class="tooltip-content">
				<p>
					<h3>블로그 가기</h3><br/>
					개발자들의 블로그입니다<br/>
	                MOMESafe의 전반적 기능과<br/>
	                개발진들을 소개합니다<br/>
				</p>
			</div>
		</div>
		
	</div>
</body>
</html>