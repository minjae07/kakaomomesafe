package kr.or.shi.qboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class QBoardService {
	QBoardDAO boardDAO;
	
	public QBoardService() {
		boardDAO = new QBoardDAO();
	}

	public List<QArticleVO> listArticles() {
		List<QArticleVO> articlesList = boardDAO.selectAllArticlesList();
		return articlesList;
	}

	public Map listArticles(Map<String, Integer> pagingMap) {
		List<QArticleVO> articlesList = boardDAO.selectAllArticlesList(pagingMap);		/* 전달된 pagingMap을 사용해 글 목록을 조회함*/
		int totArticles = boardDAO.selectTotArticles();								/* 테이블에 존재하는 글 수를 조회함 */
		
		Map articlesMap = new HashMap();
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		return articlesMap;
	}
	
	public int addArticle(QArticleVO articleVO) {
		return boardDAO.insertNewArticle(articleVO);
		
	}

	public QArticleVO viewArticle(int q_articleNo) {
		QArticleVO articleVO= null;
		articleVO = boardDAO.selectAllArticles(q_articleNo);
		return articleVO;
	}

	public void modArticle(QArticleVO articleVO) {
		boardDAO.updateArticle(articleVO);
		
	}

	public List<Integer> removeArticle(int q_articleNo) {
		List<Integer> articleNoList = boardDAO.selectRemovedArticlesList(q_articleNo);			//글을 삭제하기전 글 번호들을 ArrayList 객체에 저장함.
		boardDAO.deleteArticle2(q_articleNo);
		return articleNoList;
	}

	public int addReply(QArticleVO articleVO) {
		return boardDAO.insertNewArticle(articleVO);
	}
	
}