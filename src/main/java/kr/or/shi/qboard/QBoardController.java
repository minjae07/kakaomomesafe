package kr.or.shi.qboard;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@WebServlet("/Qboard/*")
public class QBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICLE_IMAGE = "C:\\workspace-sts\\qwebstore_image";
	QBoardService boardService;
	QArticleVO articleVO;

	public void init(ServletConfig config) throws ServletException {
		boardService = new QBoardService();
		articleVO = new QArticleVO();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String showPage = "";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		HttpSession session;
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		
		try {
			//List<ArticleVO> articlesList = new ArrayList<>();
			
			if(action == null) {
				/*	<페이징 기능 구현>
				 * 		- 하단에 보이는 숫자는 페이지 번호임 ( pre 1 2 3 4 5 6 ....next)
				 * 		- 한 페이지마다 10개의 글이 표시되고, 이 페이지가 10개가 모여 한개의 색션(section)이 됨 => 한개의 색션은 10개 페이지로 이루어짐.
				 * 		- 색션 하나는 첫번째 페이지부터 열번째 페이지임.
				 * 		- 두번째 셕션 열한번째 페이지부터 스무번째 페이지임.
				 * 		- => 사용자가 글목록 페이지에서 [2]를 클릭하면 -> 브라우저는 서버에 section값으로는 1, pageNum값으로는 2를 전송함.
				 * */
				String section_ = request.getParameter("section");
				String pageNum_ = request.getParameter("pageNum");
				
				int section = Integer.parseInt(((section_== null)? "1" : section_));
				int pageNum = Integer.parseInt(((pageNum_ == null)? "1" : pageNum_));
				
				Map<String, Integer> pagingMap = new HashMap<>();			/*section값과 pageNum값을 HashMap에 저장*/
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				
				Map articleMap = boardService.listArticles(pagingMap);
				articleMap.put("section", section);
				articleMap.put("pageNum", pageNum);
							
				request.setAttribute("articleMap", articleMap);
				showPage = "/QlistArticles.jsp";
			}
			else if(action.equals("/QlistArticles.do")) {
				String section_ = request.getParameter("section");
				String pageNum_ = request.getParameter("pageNum");
				
				int section = Integer.parseInt(((section_== null)? "1" : section_));
				int pageNum = Integer.parseInt(((pageNum_ == null)? "1" : pageNum_));
				
				Map<String, Integer> pagingMap = new HashMap<>();			/*section값과 pageNum값을 HashMap에 저장*/
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				
				Map articleMap = boardService.listArticles(pagingMap);
				articleMap.put("section", section);
				articleMap.put("pageNum", pageNum);
							
				request.setAttribute("articleMap", articleMap);
				showPage = "/QlistArticles.jsp";			
			}
			else if(action.equals("/QarticleForm.do")) {
				showPage = "/QarticleForm.jsp";
			}
			else if(action.equals("/QaddArticle.do")) {
				int q_articleNo = 0;							//글번호 (최대값)
				
				//이미지 처리
				Map<String, String> articleMap = upload(request, response);
				
				String q_title = articleMap.get("q_title");
				String q_id = articleMap.get("q_id");
				String q_content = articleMap.get("q_content");
				String q_imageFileName = articleMap.get("q_imageFileName");
				
				articleVO.setQ_parentNo(0);
				articleVO.setQ_id(q_id);
				articleVO.setQ_title(q_title);
				articleVO.setQ_content(q_content);
				articleVO.setQ_imageFileName(q_imageFileName);
				
				q_articleNo = boardService.addArticle(articleVO);
				
				//이미지 업로드 위치 후속처리
				if(q_imageFileName != null && q_imageFileName.length() != 0) {
					//이미지 파일 위치 생성(임시)
					File srcFile = new File(ARTICLE_IMAGE +"\\"+ "temp" +"\\"+ q_imageFileName);
					//이미지 저장될 디렉토리 생성
					File destDir = new File(ARTICLE_IMAGE +"\\"+ q_articleNo);
					
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
				}
				
				//업로드 완료시 팝업 생성
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + " alert('새글이 추가되었습니다!');" + " location.href=' "+ request.getContextPath() +"/Qboard/QlistArticles.do ';    " + "</script>");
				
				return;
				
			}
			else if (action.equals("/QviewArticle.do")) {
				String q_articleNo = request.getParameter("q_articleNo");
				articleVO = boardService.viewArticle(Integer.parseInt(q_articleNo));
				request.setAttribute("article", articleVO);
				showPage = "/QviewArticle.jsp";
			}
			
			else if (action.equals("/QmodArticle.do")) {
				Map<String, String> articleMap = upload(request, response);
				String q_articleNo = articleMap.get("q_articleNo");
				articleVO.setQ_articleNo(Integer.parseInt(q_articleNo));
				String q_title = articleMap.get("q_title");
				String q_content = articleMap.get("q_content");
				String q_imageFileName = articleMap.get("q_imageFileName");
				
				articleVO.setQ_title(q_title);
				articleVO.setQ_content(q_content);
				articleVO.setQ_imageFileName(q_imageFileName);
				
				boardService.modArticle(articleVO);
				
				// 이미지가 수정이 된 경우 해당 이미지로 업로드 구현
				if (q_imageFileName != null && q_imageFileName.length() != 0) {
					String originalFileName = articleMap.get("originalFileName");
					
					File srcFile = new File(ARTICLE_IMAGE +"\\"+ "temp" +"\\"+ q_imageFileName);
					File destFile = new File(ARTICLE_IMAGE +"\\"+ q_articleNo);
					
					destFile.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destFile, true);
					
					//기존 수정전 이미지 파일을 삭제처리
					File oldFile = new File(ARTICLE_IMAGE +"\\"+ q_articleNo +"\\"+ originalFileName);
					oldFile.delete();
				}
				
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + " alert('글을 수정했습니다.'); "
						+ " location.href='"+request.getContextPath()+"/Qboard/QviewArticle.do?q_articleNo="+q_articleNo+"';"
						+ "</script>");
				return;
				
				
				
			}
			else if(action.equals("/QremoveArticle.do")) {
				int q_articleNo = Integer.parseInt(request.getParameter("q_articleNo"));
				List<Integer> articleNoList = boardService.removeArticle(q_articleNo);
				
				//게시글에서 업로드했던 이미지파일 삭제
				for(int q_articleNo_ : articleNoList) {
					File imgDir = new File(ARTICLE_IMAGE +"\\"+ q_articleNo_); 
					if(imgDir.exists()) {
						FileUtils.deleteDirectory(imgDir);
					}
				}
				
				//삭제되었음을 alert 출력
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + "alert('글을 삭제했습니다.');" + " location.href='"+request.getContextPath()+"/Qboard/QlistArticles.do';" + "</script>");
				return;
			}
			else if (action.equals("/QreplyForm.do")) {
				int q_parentNo = Integer.parseInt(request.getParameter("q_parentNo"));
				session = request.getSession();
				session.setAttribute("q_parentNo", q_parentNo);
				showPage = "/QreplyForm.jsp";
			}
			else if(action.equals("/QaddReply.do")) {
				session = request.getSession();
				int q_parentNo = (Integer)session.getAttribute("q_parentNo");
				session.removeAttribute("q_parentNo");
				
				Map<String, String> articleMap = upload(request, response);
				String q_id = articleMap.get("q_id");
				String q_title = articleMap.get("q_title");
				String q_content = articleMap.get("q_content");
				String q_imageFileName = articleMap.get("q_imageFileName");
				
				articleVO.setQ_parentNo(q_parentNo);
				articleVO.setQ_id(q_id);
				articleVO.setQ_title(q_title);
				articleVO.setQ_content(q_content);
				articleVO.setQ_imageFileName(q_imageFileName);
				
				int q_articleNo = boardService.addReply(articleVO);
				
				//이미지 후속처리
				if(q_imageFileName != null && q_imageFileName.length() != 0) {
					
					File srcFile = new File(ARTICLE_IMAGE +"\\"+ "temp" +"\\"+ q_imageFileName);			//이미지 파일 위치 생성 (임시)
					File destFile = new File(ARTICLE_IMAGE +"\\"+ q_articleNo);							//이미지 저장될 디렉토리 생성	
					
					destFile.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destFile, true);					
				}
				
				//업로드 완료시 팝업 생성
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + " alert('새글이 추가되었습니다!');" + " location.href='" +request.getContextPath()+ "/Qboard/QlistArticles.do';" + "</script>");
				return;
				
			}
			else {
				showPage = "/QlistArticles.jsp";
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(showPage);
			dispatcher.forward(request, response);
			
		} catch (Exception e) {
			e.getMessage();
			//e.printStackTrace();
		}
	}
	
	//파일 업로드
	public Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> articleMap = new HashMap<>();
		
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE);
		DiskFileItemFactory factory = new DiskFileItemFactory();		//메모리나 파일로 업로드 데이터를 보관하는 FileItem의 factory 생성
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024*1024*500);
		ServletFileUpload upload = new ServletFileUpload(factory);		//업로드 요청을 처리하는 ServletFileUpload 생성
		
		try {
			List items = upload.parseRequest(request);					//업로드 요청 파싱해서 fileItem 목록요청함.
			
			for(int i=0; i<items.size(); i++) {
				FileItem fileItem = (FileItem)items.get(i);
				
				if(fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() +"="+ fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				}
				else {
					System.out.println("파라미터 명 : " + fileItem.getFieldName());
					System.out.println("파일 크기 : " + fileItem.getSize() + "bytes");
					
					if(fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						
						if(idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}
						
					    String fileName = fileItem.getName().substring(idx + 1);
					    System.out.println("파일 명 : " + fileName);
					    
					    articleMap.put(fileItem.getFieldName(), fileName);
					    
					    File uploadFile = new File(currentDirPath +"\\temp\\"+ fileName);
					    fileItem.write(uploadFile);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleMap;
	}
}
