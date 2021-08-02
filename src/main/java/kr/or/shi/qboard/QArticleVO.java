package kr.or.shi.qboard;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;

public class QArticleVO {
	private int q_level;
	private int q_articleNo;
	private int q_parentNo;
	private String q_id;
	private String q_title;
	private String q_content;
	private String q_imageFileName;
	private Date q_writeDate;
	private boolean q_newArticle;
	
	public QArticleVO() {
		
	}

	public QArticleVO(int q_level, int q_articleNo, int q_parentNo, String q_id, String q_title, String q_content,
			String q_imageFileName) {
		super();
		this.q_level = q_level;
		this.q_articleNo = q_articleNo;
		this.q_parentNo = q_parentNo;
		this.q_id = q_id;
		this.q_title = q_title;
		this.q_content = q_content;
		this.q_imageFileName = q_imageFileName;
	}


	public int getQ_level() {
		return q_level;
	}

	public void setQ_level(int q_level) {
		this.q_level = q_level;
	}

	public int getQ_articleNo() {
		return q_articleNo;
	}

	public void setQ_articleNo(int q_articleNo) {
		this.q_articleNo = q_articleNo;
	}

	public int getQ_parentNo() {
		return q_parentNo;
	}

	public void setQ_parentNo(int q_parentNo) {
		this.q_parentNo = q_parentNo;
	}

	public String getQ_id() {
		return q_id;
	}

	public void setQ_id(String q_id) {
		this.q_id = q_id;
	}

	public String getQ_title() {
		return q_title;
	}

	public void setQ_title(String q_title) {
		this.q_title = q_title;
	}

	public String getQ_content() {
		return q_content;
	}

	public void setQ_content(String q_content) {
		this.q_content = q_content;
	}

	public Date getQ_writeDate() {
		return q_writeDate;
	}

	public void setQ_writeDate(Date q_writeDate) {
		this.q_writeDate = q_writeDate;
	}

	public boolean isQ_newArticle() {
		return q_newArticle;
	}

	public void setQ_newArticle(boolean q_newArticle) {
		this.q_newArticle = q_newArticle;
	}



	public String getQ_imageFileName() {
		try {
			
			if(q_imageFileName != null && q_imageFileName.length() != 0) {
				q_imageFileName = URLDecoder.decode(q_imageFileName, "utf-8");
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return q_imageFileName;
	}

	public void setQ_imageFileName(String q_imageFileName) {
		try {
			if(q_imageFileName != null && q_imageFileName.length() != 0) {
				this.q_imageFileName = URLEncoder.encode(q_imageFileName, "utf-8");
			}
		} catch (UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		
		
	}

}