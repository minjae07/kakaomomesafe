package kr.or.shi.qboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class FileDownloadController
 */
@WebServlet("/Qdownload.do")
public class QFileDownloadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICLE_IMAGE = "C:\\workspace-sts\\qwebstore_image";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
	 	String q_imageFileName = request.getParameter("q_imageFileName");
	 	String q_articleNo = request.getParameter("q_articleNo");
	 	System.out.println("q_imageFileName : " + q_imageFileName);
	 	
	 	response.setHeader("Cache-Control", "no-chache");
	 	response.addHeader("content-disposition", "attachment;filename=" + q_imageFileName);
	 	
	 	String path = ARTICLE_IMAGE +"\\"+ q_articleNo +"\\"+ q_imageFileName;
	 	File imageFile = new File(path);
	 	OutputStream out = response.getOutputStream();
	 	FileInputStream in = new FileInputStream(imageFile);
	 	
	 	byte[] buffer = new byte[1024*1024*500];
	 	while(true) {
	 		int count = in.read(buffer);
	 		if(count == -1) break;
	 		out.write(buffer, 0, count);
	 	}
	 	
	 	in.close();
	 	out.close();
		
	}

}