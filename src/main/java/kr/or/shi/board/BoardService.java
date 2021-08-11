package kr.or.shi.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}

	public List<ArticleVO> listArticles() {
		List<ArticleVO> articlesList = boardDAO.selectAllArticlesList();
		return articlesList;
	}

	public Map listArticles(Map<String, Integer> pagingMap) {
		List<ArticleVO> articlesList = boardDAO.selectAllArticlesList(pagingMap);		/* 전달된 pagingMap을 사용해 글 목록을 조회함*/
		int totArticles = boardDAO.selectTotArticles();								/* 테이블에 존재하는 글 수를 조회함 */
		
		Map articlesMap = new HashMap();
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		return articlesMap;
	}
	
	public int addArticle(ArticleVO articleVO) {
		return boardDAO.insertNewArticle(articleVO);
		
	}

	public ArticleVO viewArticle(int articleNo) {
		ArticleVO articleVO= null;
		articleVO = boardDAO.selectAllArticles(articleNo);
		return articleVO;
	}

	public void modArticle(ArticleVO articleVO) {
		boardDAO.updateArticle(articleVO);
		
	}

	public List<Integer> removeArticle(int articleNo) {
		List<Integer> articleNoList = boardDAO.selectRemovedArticlesList(articleNo);			//글을 삭제하기전 글 번호들을 ArrayList 객체에 저장함.
		boardDAO.deleteArticle2(articleNo);
		return articleNoList;
	}

	public int addReply(ArticleVO articleVO) {
		return boardDAO.insertNewArticle(articleVO);
	}
	public ArrayList<ArticleVO> serachArticle(String keyWord, String keyField){
	      ArrayList<ArticleVO> articleList = boardDAO.getMemberlist2(keyWord, keyField);
	      
	      return articleList;
	      
	   }
	
}