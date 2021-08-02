<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
            <title>회원 정보 출력창</title>
            <link rel="stylesheet" href="./assets/css/board.css">
            <link rel="stylesheet" href="./assets/css/board_menu.css">
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
                </script>
                </head>
                <body>
                    <section class="notice">
                        <div class="page-title">
                            <div class="container" OnClick="location.href ='./intro.html' " style="cursor:pointer;">
                                <h3>MOME Community</h3>
                            </div>
                        </div>
                        <!-- board menu area -->
                        <div class="container">
                            <ul>
                                <li><a href="./intro.html">지도로 돌아가기</a></li>
                                <li><a href="#">문의하기</a></li>
                                <li><a onclick="window.open('./chat.jsp',newName, 'width=630, height=750, left=100, top=150, location=no, status=no, scrollbars=no')" style="cursor:pointer;">채팅하기</a></li>
                                <li><a href="#">Blog</a></li>
                            </ul>
                        </div>
                        <!-- board seach area -->
                        <div id="board-search">
                            <div class="container">
                                <div class="search-window">
                                    <div
                                        class="search-wrap">
                                        <!-- 검색 -->
                                        <form action="${pageContext.request.contextPath}/mem3.do">
                                            <select name="action" class="select">
                                                <option value="listMembers">전체</option>
                                                <option value="selectMemberById">아이디</option>
                                                <option value="selectMemberByPwd">제목</option>
                                            </select>
                                            <input type="text" name="value"/>
                                            <input id="search" type="submit" class="btn btn-dark" name="" placeholder="검색어를 입력해주세요." value="검색"></form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- board list area -->
                            <div id="board-list">
                                <div class="container">
                                    <table class="board-table">
                                        <thead>
                                            <tr>
                                                <th scope="col" class="th-num">번호</th>
                                                <th scope="col" class="th-title">제목</th>
                                                <th scope="col" class="th-writer">작성자</th>
                                                <th scope="col" class="th-date">등록일</th>
                                            </tr>
                                        </thead>
                                        <c:forEach var="member" items="${membersList }">
                                            <tr align="center">
                                                <td>${member.articleNo }</td>
                                                <td>${member.title }</td>
                                                <td>${member.name }</td>
                                                <td>${member.joinDate }</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                    <a class="class1" href="${contextPath}/shi/articleForm.jsp">
                                        <p class="class2">글쓰기</p>
                                    </a>
                                </body>
                            </html>