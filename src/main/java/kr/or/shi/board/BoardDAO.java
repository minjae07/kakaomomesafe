package kr.or.shi.board;

import java.io.Reader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class BoardDAO {
	private DataSource dataFactory;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public BoardDAO() {
		try {
			Context context = new InitialContext();
			Context envContext = (Context)context.lookup("java:/comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/dbconn");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* 추가된 부분*/
	
	public List<ArticleVO> selectAllArticlesList() {
		List<ArticleVO> articlesList = new ArrayList<>();
		
		try {
			conn = dataFactory.getConnection();
			/* level : 오라클에서 제공하는 가상 컬럼으로 글의 깊이를 나타냄 */
			String sql = "SELECT articleNo, parentNo, id, title, content, writeDate"
						+ " FROM t_board"
						+ " START WITH parentNo=0"						/* 계층형 구조에서 최상위 로우(row)를 식별하는 조건을 명시함.  parentNo=0에 부모글에서 시작해 계층형 구조를 만든다는 의미*/
						+ " CONNECT BY PRIOR articleNo=parentNo"		/* 계층형 구조가 어떤 식으로 연결되는지를 기술함. parentNo에 부모 글번호가 있으므로 CONNECT BY PRIOR */
						+ " ORDER SIBLINGS BY articleNo DESC";			/* 계층형 구조로 조회된 정보를 다시 articleNo를 이용해 내림차순으로 정렬하여 최종 출력  */
			
			
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int articleNo = rs.getInt("articleNo");
				int parentNo = rs.getInt("parentNo");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				
				ArticleVO ArticleVO = new ArticleVO();
				
				ArticleVO.setArticleNo(articleNo);
				ArticleVO.setParentNo(parentNo);
				ArticleVO.setTitle(title);
				ArticleVO.setContent(content);
				ArticleVO.setId(id);
				ArticleVO.setWriteDate(writeDate);
				
				articlesList.add(ArticleVO);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articlesList;
	}
	
	public List selectAllArticlesList(Map<String, Integer> pagingMap) {
		List<ArticleVO> articlesList = new ArrayList<>();
		
		int section = pagingMap.get("section");
		int pageNum = pagingMap.get("pageNum");
		
		try {
			conn = dataFactory.getConnection();
						
			String sql = "select * from (\r\n"
					+ "                select rownum as recNum, lvl\r\n"
					+ "                      ,articleNo\r\n"
					+ "                      ,parentNo\r\n"
					+ "                      ,title\r\n"
					+ "                      ,id\r\n"
					+ "                      ,writeDate\r\n"
					+ "                      ,newArticle\r\n"
					+ "                from (select level as lvl\r\n"
					+ "                              ,articleNo\r\n"
					+ "                              ,parentNo\r\n"
					+ "                              ,title\r\n"
					+ "                              ,id\r\n"
					+ "                              ,writeDate\r\n"
					+ "                              ,decode(round(sysdate - writedate),0,'true','false') newArticle\r\n"
					+ "                        from t_board\r\n"
					+ "                        start WITH parentNo=0              \r\n"
					+ "                        CONNECT by PRIOR articleNo=parentNo \r\n"
					+ "                        order SIBLINGS by articleNo desc \r\n"
					+ "                      )  \r\n"
					+ "            )\r\n"
					+ "where recNum BETWEEN (?-1)*100+(?-1)*10+1 and (?-1)*100+?*10"
					;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, section);
			pstmt.setInt(2, pageNum);
			pstmt.setInt(3, section);
			pstmt.setInt(4, pageNum);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				int level = rs.getInt("LVL");
				int articleNo = rs.getInt("articleNo");
				int parentNo = rs.getInt("parentNo");
				String title = rs.getString("title");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				boolean newArticle = Boolean.parseBoolean(rs.getString("newArticle"));
				
				ArticleVO articleVO = new ArticleVO();
				
				articleVO.setLevel(level);
				articleVO.setArticleNo(articleNo);
				articleVO.setParentNo(parentNo);
				articleVO.setTitle(title);
				articleVO.setId(id);
				articleVO.setWriteDate(writeDate);
				articleVO.setNewArticle(newArticle); 
				
				articlesList.add(articleVO);
			}
			
			rs.close();
			pstmt.close();
			conn.close();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articlesList;
	}
	

	public int insertNewArticle(ArticleVO articleVO) {
		
		//글번호 (1증가된 글번호 얻기)
		int articleNo = getNewArticleNo();
		
		try {
			conn = dataFactory.getConnection();
			
			int parentNo = articleVO.getParentNo();
			String title = articleVO.getTitle();
			String content = articleVO.getContent();
			String id = articleVO.getId();
			String imageFileName = articleVO.getImageFileName();
			
			
			String sql = "INSERT INTO t_board (articleNo, parentNo, id, title, content, imageFileName)"
						+" VALUES (?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNo);
			pstmt.setInt(2, parentNo);
			pstmt.setString(3, id);
			pstmt.setString(4, title);
			pstmt.setString(5, content);
			pstmt.setString(6, imageFileName);
			
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return articleNo;
	}

	public int getNewArticleNo() {
		
		try {
			conn = dataFactory.getConnection();
			
			String sql = "SELECT max(articleNo) from t_board";
			
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return (rs.getInt(1) + 1);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public ArticleVO selectArticleVo(int articleNo) {
		ArticleVO articleVO = new ArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "select articleNo, parentNo, title, content, NVL(imageFileName,'null') as imageFileName, id, writeDate"
						+" from t_board"
						+" where articleNo=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			int articleNo_ = rs.getInt("articleNo");
			int parentNo = rs.getInt("parentNo");
			String title = rs.getString("title");
			String content = rs.getString("content");
			
			//파일이름에 특수문자가 있을 경우 인코딩함
			String imageFileName = URLEncoder.encode(rs.getString("imageFileName"), "utf-8");
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
					
			articleVO.setArticleNo(articleNo_);
			articleVO.setParentNo(parentNo);
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			articleVO.setId(id);
			articleVO.setWriteDate(writeDate);
						
			rs.close();
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleVO;
	}

	public void updateArticle(ArticleVO articleVO) {
		int articleNo = articleVO.getArticleNo();
		String title = articleVO.getTitle();
		String content = articleVO.getContent();
		String imageFileName = articleVO.getImageFileName();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "update t_board set title=?, content=?";
			
			if (imageFileName != null && imageFileName.length() != 0) {
				sql += ", imageFileName=?";
			}
			
			sql += " where articleNo=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			
			if (imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNo);
			}
			else {
				pstmt.setInt(3, articleNo);
			}
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Integer> selectRemovedArticlesList(int articleNo) {
		List<Integer> articleNoList = new ArrayList<>();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "SELECT articleNo"
					+ " FROM t_board"
					+ " START WITH articleNo=?"						
					+ " CONNECT BY PRIOR articleNo=parentNo";	
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNo);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				articleNo = rs.getInt("articleNo");
				articleNoList.add(articleNo);
			}
			
			pstmt.close();
			conn.close();
			rs.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articleNoList;
	}

	public void deleteArticle2(int articleNo) {
		try {
			conn = dataFactory.getConnection();
			String sql = "delete from t_board"
						+" WHERE articleNo in("
						+"  SELECT articleNo"
						+ "  FROM t_board"
						+ "  START WITH articleNo=?"						
						+ "  CONNECT BY PRIOR articleNo=parentNo)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNo);
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int selectTotArticles() {
		try {
			conn = dataFactory.getConnection();
			String sql = "select count(articleNo) from t_board";
			
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return (rs.getInt(1));
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return 0;
	}
	
	public static SqlSessionFactory sqlMapper = null;
		
	private static SqlSessionFactory getInstance() {
		
		if (sqlMapper == null) {
			try {
				String resource = "mybatis/SqlMapConfig.xml";
				Reader reader = Resources.getResourceAsReader(resource);
				sqlMapper = new SqlSessionFactoryBuilder().build(reader);
				
				reader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return sqlMapper;
	}
	
	public ArticleVO selectAllArticles(int articleNo) {
		ArticleVO articleVO = new ArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "select articleNo, parentNo, title, content, NVL(imageFileName,'null') as imageFileName, id, writeDate"
						+" from t_board"
						+" where articleNo=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			int articleNo_ = rs.getInt("articleNo");
			int parentNo = rs.getInt("parentNo");
			String title = rs.getString("title");
			String content = rs.getString("content");
			
			//파일이름에 특수문자가 있을 경우 인코딩함
			String imageFileName = URLEncoder.encode(rs.getString("imageFileName"), "utf-8");
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
					
			articleVO.setArticleNo(articleNo_);
			articleVO.setParentNo(parentNo);
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			articleVO.setId(id);
			articleVO.setWriteDate(writeDate);
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articleVO;
	}
	public ArrayList<ArticleVO> getMemberlist2(String keyWord, String keyField) {
	      ArrayList<ArticleVO> articlesList = new ArrayList<>();
	     
	      try {
	         conn = dataFactory.getConnection();
	         /* level : 오라클에서 제공하는 가상 컬럼으로 글의 깊이를 나타냄 */

	         String sql ="select * from t_board  WHERE "+keyField+"  LIKE '%"   +keyWord+   "%' order by id";
	         
	           
	            pstmt = conn.prepareStatement(sql);
	            
	            rs = pstmt.executeQuery();
	            
	            System.out.println("rs : " + rs.getRow());
	         
	         while(rs.next()) {
	        	   
					int articleNo = rs.getInt("articleNo");
					int parentNo = rs.getInt("parentNo");
					String title = rs.getString("title");
					String id = rs.getString("id");
					String content = rs.getString("content");
					Date writeDate = rs.getDate("writeDate");
					
					
					ArticleVO articleVO = new ArticleVO();
					
					
					articleVO.setArticleNo(articleNo);
					articleVO.setParentNo(parentNo);
					articleVO.setTitle(title);
					articleVO.setId(id);
					articleVO.setContent(content);
					articleVO.setWriteDate(writeDate);
					 
					
					articlesList.add(articleVO);
					
	            

	         }  
	         rs.close();
				pstmt.close();
				conn.close();
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      
	      
	      return articlesList;
	   }
	   /**
	     * member테이블의 모든 레코드 검색하(Select)는 메서드 작성
	     */
	   
	    public ArrayList<ArticleVO> getMemberlist(){
	       
	        ArrayList<ArticleVO> list = new ArrayList<ArticleVO>();
	       
	        try{//실행
	           String sql = "select * from t_board";
	            pstmt = conn.prepareStatement(sql);
	            rs = pstmt.executeQuery(sql);
	           
	            while(rs.next()){
	               ArticleVO vo = new ArticleVO();
	               
	               vo.setArticleNo(rs.getInt(1));
	                vo.setId(rs.getString(2));
	                vo.setTitle(rs.getString(3));
	               
	                vo.setWriteDate(rs.getDate(4));
	                
	                list.add(vo);
	                
	                
	            }
	            rs.close();
                pstmt.close();
                conn.close();
	        }catch(Exception e){          
	            e.printStackTrace();     
	        } 
	        
	        return list;
	    }
	    
	 
}
