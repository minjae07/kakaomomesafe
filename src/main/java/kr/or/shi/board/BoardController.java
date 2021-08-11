package kr.or.shi.board;

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

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICLE_IMAGE = "C:\\workspace-sts\\webstore_image";
	BoardService boardService;
	ArticleVO articleVO;

	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();
		articleVO = new ArticleVO();
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
				showPage = "/listArticles.jsp";
			}
			else if(action.equals("/listArticles.do")) {
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
				showPage = "/listArticles.jsp";			
			}
			else if(action.equals("/searchArticles.do")) {
	            String section_ = request.getParameter("section");
	            String pageNum_ = request.getParameter("pageNum");
	            String keyWord = request.getParameter("keyWord");
	            String keyField = request.getParameter("keyField");
//	            String keyField = request.getParameter("keyField");
//	            String keyWord = request.getParameter("keyWord");

	            int section = Integer.parseInt(((section_== null)? "1" : section_));
	            int pageNum = Integer.parseInt(((pageNum_ == null)? "1" : pageNum_));
	   
	            Map<String, Integer> pagingMap = new HashMap<>();         /*section값과 pageNum값을 HashMap에 저장*/
	            pagingMap.put("section", section);
	            pagingMap.put("pageNum", pageNum);

	            Map articleMap = boardService.listArticles(pagingMap);
	            articleMap.put("section", section);
	            articleMap.put("pageNum", pageNum);
	            
	            //boardService.serachArticle(keyField, keyWord);
	            ArrayList<ArticleVO> searchArticleList = boardService.serachArticle(keyWord, keyField);
	            
	            request.setAttribute("searchArticleList", searchArticleList);
	            
	            showPage = "/searchArticles.jsp";         
	         }
			else if(action.equals("/articleForm.do")) {
				showPage = "/articleForm.jsp";
			}
			else if(action.equals("/addArticle.do")) {
				int articleNo = 0;							//글번호 (최대값)
				
				//이미지 처리
				Map<String, String> articleMap = upload(request, response);
				
				String title = articleMap.get("title");
				String id = articleMap.get("id");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				articleVO.setParentNo(0);
				articleVO.setId(id);
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				
				articleNo = boardService.addArticle(articleVO);
				
				//이미지 업로드 위치 후속처리
				if(imageFileName != null && imageFileName.length() != 0) {
					//이미지 파일 위치 생성(임시)
					File srcFile = new File(ARTICLE_IMAGE +"\\"+ "temp" +"\\"+ imageFileName);
					//이미지 저장될 디렉토리 생성
					File destDir = new File(ARTICLE_IMAGE +"\\"+ articleNo);
					
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				
				//업로드 완료시 팝업 생성
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + " alert('새글이 추가되었습니다!');" + " location.href=' "+ request.getContextPath() +"/board/listArticles.do ';    " + "</script>");
				
				return;
				
			}
			else if (action.equals("/viewArticle.do")) {
				String articleNo = request.getParameter("articleNo");
				articleVO = boardService.viewArticle(Integer.parseInt(articleNo));
				request.setAttribute("article", articleVO);
				showPage = "/viewArticle.jsp";
			}
			
			else if (action.equals("/modArticle.do")) {
				Map<String, String> articleMap = upload(request, response);
				String articleNo = articleMap.get("articleNo");
				articleVO.setArticleNo(Integer.parseInt(articleNo));
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				
				boardService.modArticle(articleVO);
				
				// 이미지가 수정이 된 경우 해당 이미지로 업로드 구현
				if (imageFileName != null && imageFileName.length() != 0) {
					String originalFileName = articleMap.get("originalFileName");
					
					File srcFile = new File(ARTICLE_IMAGE +"\\"+ "temp" +"\\"+ imageFileName);
					File destFile = new File(ARTICLE_IMAGE +"\\"+ articleNo);
					
					destFile.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destFile, true);
					
					//기존 수정전 이미지 파일을 삭제처리
					File oldFile = new File(ARTICLE_IMAGE +"\\"+ articleNo +"\\"+ originalFileName);
					oldFile.delete();
				}
				
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + " alert('글을 수정했습니다.'); "
						+ " location.href='"+request.getContextPath()+"/board/viewArticle.do?articleNo="+articleNo+"';"
						+ "</script>");
				return;
				
				
				
			}
			else if(action.equals("/removeArticle.do")) {
				int articleNo = Integer.parseInt(request.getParameter("articleNo"));
				List<Integer> articleNoList = boardService.removeArticle(articleNo);
				
				//게시글에서 업로드했던 이미지파일 삭제
				for(int articleNo_ : articleNoList) {
					File imgDir = new File(ARTICLE_IMAGE +"\\"+ articleNo_); 
					if(imgDir.exists()) {
						FileUtils.deleteDirectory(imgDir);
					}
				}
				
				//삭제되었음을 alert 출력
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + "alert('글을 삭제했습니다.');" + " location.href='"+request.getContextPath()+"/board/listArticles.do';" + "</script>");
				return;
			}
			else if (action.equals("/replyForm.do")) {
				int parentNo = Integer.parseInt(request.getParameter("parentNo"));
				session = request.getSession();
				session.setAttribute("parentNo", parentNo);
				showPage = "/replyForm.jsp";
			}
			else if(action.equals("/addReply.do")) {
				session = request.getSession();
				int parentNo = (Integer)session.getAttribute("parentNo");
				session.removeAttribute("parentNo");
				
				Map<String, String> articleMap = upload(request, response);
				String id = articleMap.get("id");
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				articleVO.setParentNo(parentNo);
				articleVO.setId(id);
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				
				int articleNo = boardService.addReply(articleVO);
				
				//이미지 후속처리
				if(imageFileName != null && imageFileName.length() != 0) {
					
					File srcFile = new File(ARTICLE_IMAGE +"\\"+ "temp" +"\\"+ imageFileName);			//이미지 파일 위치 생성 (임시)
					File destFile = new File(ARTICLE_IMAGE +"\\"+ articleNo);							//이미지 저장될 디렉토리 생성	
					
					destFile.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destFile, true);					
				}
				
				//업로드 완료시 팝업 생성
				PrintWriter pWriter = response.getWriter();
				pWriter.print("<script>" + " alert('새글이 추가되었습니다!');" + " location.href='" +request.getContextPath()+ "/board/listArticles.do';" + "</script>");
				return;
				
			}
			else {
				showPage = "/listArticles.jsp";
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
