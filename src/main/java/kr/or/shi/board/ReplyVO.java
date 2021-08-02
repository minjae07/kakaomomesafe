package kr.or.shi.board;

import java.util.Date;

public class ReplyVO {

	private int articleNo;
	private int rno;
	private String content;
	private String writer;
	private Date regdate;
	public int getBno() {
		return articleNo;
	}
	public void setBno(int articleNo) {
		this.articleNo = articleNo;
	}
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	
	@Override
	public String toString() {
		return "ReplyVO [articleNo=" + articleNo + ", rno=" + rno + ", content=" + content + ", writer=" + writer + ", regdate="
				+ regdate + "]";
	}
	
	
}