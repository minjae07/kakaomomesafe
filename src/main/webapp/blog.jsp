<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
	request.setCharacterEncoding("utf-8");
%>

<!DOCTYPE html>
<html lang="en" class="no-js">
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
		<title>MOME Blog</title>
		<meta name="description" content="Blueprint: Background Slideshow" />
		<meta name="keywords" content="blueprint, background image slideshow, fullscreen slideshow, jquery, fullscreen image, web development" />
		<meta name="author" content="Codrops" />
		<link rel="stylesheet" type="text/css" href="/assets/css/default.css" />
		<link rel="stylesheet" type="text/css" href="/assets/css/component.css" />
		<script src="/assets/js/modernizr.custom.js"></script>
	</head>
	<body>
		<div class="container">
			<header class="clearfix" >
				<span>Blog</span>
				<h1>MOME Blog</h1>
			</header>
				<ul>
					<li><a href="${pageContext.request.contextPath}/intro.html">MOME Safe</a></li>
					<li><a href="${pageContext.request.contextPath}/restarea.jsp">MOME Town</a></li>
				</ul>
			<div class="main">
				<ul id="cbp-bislideshow" class="cbp-bislideshow">
					<li>
					<img src="assets/img/편집본2.png" alt="image02"/>
					<h2>MOME Blog에<br/>오신것을<br/>환영합니다!</h2>
					</li>
					<li>
						<img src="assets/img/편집본1.15.png" alt="image02"/>
						<h3>
						안전귀가 시스템 MOME Safe는<br/>
						Store 제작과정에서 착안되어<br/>
						사회적으로 일어나고 있는 범죄를<br/>
						예방하기 위해 제작하게 되었습니다
						</h3>
					</li>
					<li>
					<img src="assets/img/편집본2.png" alt="image03"/>
					<h2>MOME Safe<br/>is<br/>Function</h2>
					</li>
					<li>
						<img src="assets/img/편집본2.2.png" alt="image04"/>
							<h6>MOEM의 Function은!</h6>
							<h7>
								● MOEM의 지도, 검색기능<br/>
								● 가로등, CCTV, 어린이 보호구역, 보건소 위치 표시<br/>
								● MOME Community<br/>
								● MOME CHAT
							</h7>
					</li>
					
					<li>
						<img src="assets/img/편집본3.1.png" alt="image05"/>
						<h6>MOEM의 지도, 검색기능</h6>
						<h4>
							● KAKAO API를 제공받아 구현하여<br/>
								사용자에게 제공합니다<br/>
							● 검색을 통해 원하는 장소로 이동할수 있습니다<br/><br/>
							● 검색후 보여지는 마커로 한눈에 보기 쉽게 하였습니다
						</h4>
					</li>
					<li>
						<img src="assets/img/편집본4.3.png" alt="image06"/>
						<h6>가로등, CCTV, 어린이 보호구역, 보건소 위치 표시</h6>
						<h4>
							● 체크박스를 통해 각각의 해당 <br/>가로등과 CCTV등의 위치를 제공합니다<br/><br/>
							● 지도 줌 인, 아웃 기능을 사용하면 <br/>해당 구역의 가로들의 개수를 제공합니다<br/>
								해당 위치의 가로등의 개수와 도로의 밝기를 <br/>확인할수 있습니다<br/><br/>
							● 검색기능을 활용하여 해당 위치에대한<br/>
								정보를 자세하게제공받을수 있습니다<br/><br/>
							● 코로나 이슈에 대하여 보건소의 위치를 제공합니다<br/><br/>
						</h4>
					</li>
					<li>
						<img src="assets/img/편집본5.5.png" alt="image7"/>
						<h6>MOME Community</h6>
						<h4>
							● 게시판 기능을 이용하여 경산내의<br/>
								커뮤니티를 활성화하여 합니다<br/><br/>
							● 회원가입 없이 아이디와 비밀번호를 설정하면<br/>
								누구나 쉽게 글을 쓸수 있습니다<br/><br/>
							● 문의하기를 통해 개발자와 소통할수 있습니다<br/><br/>
						</h4>
					</li>
					<li>
						<img src="assets/img/편집본6.0.png" alt="image8"/>
						<h6>MOME CHAT</h6>
						<h4>
							● 실시간 채팅기능을 사용하여<br/>
								경산내의 친목도모를 형성하도록 하였습니다<br/><br/>
							● 채팅기능을 악의적으로 사용하는것을 본다면<br/>
								문의하기를 통하여 신고 부탁드립니다<br/><br/>
						</h4>
					</li>
					<li>
						<img src="assets/img/편집본2.png" alt="image09"/>
					<h2>MOME <br/>is <br/>Developer<h2>
					</li>
					<li>
						<img src="assets/img/조원들사진2.png" alt="image11"/>
						<h6>MOME Developer</h6>
					</li>
					<li>
						<img src="assets/img/치호3.png" alt="image12"/>
						<h6>MOME Developer</h6>
						<h5>
							소속 : 대구 가톨릭 대학교<br/>
							학과 : 의공학과
						</h5>
						<h8>
							● 조장<br/><br/>
							● 지도 구현<br/><br/>
							● 사이드바 구현<br/><br/>
						</h8>
					</li>
					<li>
						<img src="assets/img/민재1.png" alt="image13"/>
						<h6>MOME Developer</h6>
						<h9>
							소속 : 대구 가톨릭 대학교<br/>
							학과 : 기계자동차공학과
							<br/><br/><br/>
							● 문의게시판 구현 및 디자인<br/><br/>
							● DB연동<br/><br/>
						</h9>
					</li>
					<li>
						<img src="assets/img/문기1.png" alt="image14"/>
						<h6>MOME Developer</h6>
						<h10>
							소속 : 대구 가톨릭 대학교<br/>
							학과 : 전기공학과
							<br/><br/><br/>
							● 게시판 구현 및 디자인<br/><br/>
							● 지도 검색 기능 구현<br/><br/>
						</h10>
					</li>
					<li>
						<img src="assets/img/승교1.png" alt="image15"/>
						<h6>MOME Developer</h6>
						<h9>
							소속 : 대구 가톨릭 대학교<br/>
							학과 : 전기공학과
							<br/><br/><br/>
							● DB연동<br/><br/>
							● 아이디어 제공<br/><br/>
						</h9>
					</li>
					<li>
						<img src="assets/img/진수1.png" alt="image16"/>
						<h6>MOME Developer</h6>
						<h10>
							소속 : 대구 가톨릭 대학교<br/>
							학과 : 인공지능.빅데이터공학과
							<br/><br/><br/>
							● Blog 구현 및 디자인<br/><br/>
							● 문서 작업<br/><br/>
						</h10>
					</li>
					<li>
						<img src="assets/img/편집본2.png" alt="image017"/>
						<h2>MOME <br/>Blog<br/>끝으로<h2>
					</li>
					<li>
						<img src="assets/img/편집본2.png" alt="image018"/>
						<h6>Finally</h6>
						<h11>
							MOMESafe의 개발진들은 최근 일어나고 있는 범죄 이슈에 대하여<br/>
							이러한 범죄를 예방하기 위해서 어떻게 해야 될까라는 생각에서 출발하였습니다<br/>
							지도 구현부터 방대한 가로등 데이터의 정제와 게시판의 구현 등 여러 가지 난관이 있었지만<br/>
							MOMESafe 기능들로 범죄 예방의 사명으로 달렸습니다<br/>
							몸이 또는 마음이 안전해지는 경산이 만들어지기를 기원하면서<br/>
							저희 MOMESafe를 이용하여 주시고<br/>
							개발자들의 블로그에 찾아와주어서 정말로 감사합니다!<br/><br/><br/>
							&nbsp;&nbsp; - MOME Developer
						</h11>
					</li>
					<li>
						<img src="assets/img/편집본2.png" alt="image019"/>
						<h2>MOME<br/>is<br/>End<h2>
					</li>
					<li>
						<img src="assets/img/편집본2.png" alt="image020"/>
					</li>
					<li>
						<img src="assets/img/편집본2.png" alt="image021"/>
					</li>
				</ul>
				<div id="cbp-bicontrols" class="cbp-bicontrols">
					<span class="cbp-biprev"></span>
					<span class="cbp-bipause"></span>
					<span class="cbp-binext"></span>
				</div>
			</div>
			
		</div>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="assets/js/jquery.imagesloaded.min.js"></script>
		<script src="assets/js/cbpBGSlideshow.min.js"></script>
		<script>
			$(function() {
				cbpBGSlideshow.init();
			});
		</script>
	</body>
</html>