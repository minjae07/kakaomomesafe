<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원 정보 출력창</title>
</head>
<body>
	<table border="1" align="center" width="80%">
		<tr align="center" bgcolor="lightgreen">
			<td><b>번호</b></td>
			<td><b>제목</b></td>
			<td><b>내용</b></td>
			<td><b>이름</b></td>
			<td><b>등록일</b></td>
		</tr>
		<c:forEach var="member" items="${membersList }">
			<tr align="center">
				<td>${member.articleNo }</td>
				<td>${member.title }</td>
				<td>${member.content }</td>
				<td>${member.name }</td>
				<td>${member.joinDate }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>