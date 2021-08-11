package kr.or.shi.qboard;

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

import kr.or.shi.board.ArticleVO;

public class QBoardDAO {
	private DataSource dataFactory;
	Connection conn;
	PreparedStatement pstmt;
	
	public QBoardDAO() {
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
	
	public List<QArticleVO> selectAllArticlesList() {
		List<QArticleVO> articlesList = new ArrayList<>();
		try {
			conn = dataFactory.getConnection();
			/* level : 오라클에서 제공하는 가상 컬럼으로 글의 깊이를 나타냄 */
			String sql = "SELECT q_articleNo, q_parentNo, q_id, q_title, q_content, q_writeDate"
						+ " FROM q_board"
						+ " START WITH q_parentNo=0"						/* 계층형 구조에서 최상위 로우(row)를 식별하는 조건을 명시함.  parentNo=0에 부모글에서 시작해 계층형 구조를 만든다는 의미*/
						+ " CONNECT BY PRIOR q_articleNo=q_parentNo"		/* 계층형 구조가 어떤 식으로 연결되는지를 기술함. parentNo에 부모 글번호가 있으므로 CONNECT BY PRIOR */
						+ " ORDER SIBLINGS BY q_articleNo DESC";			/* 계층형 구조로 조회된 정보를 다시 articleNo를 이용해 내림차순으로 정렬하여 최종 출력  */
			
			
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int q_articleNo = rs.getInt("q_articleNo");
				int q_parentNo = rs.getInt("q_parentNo");
				String q_title = rs.getString("q_title");
				String q_content = rs.getString("q_content");
				String q_id = rs.getString("q_id");
				Date q_writeDate = rs.getDate("q_writeDate");
				
				QArticleVO ArticleVO = new QArticleVO();
				
				ArticleVO.setQ_articleNo(q_articleNo);
				ArticleVO.setQ_parentNo(q_parentNo);
				ArticleVO.setQ_title(q_title);
				ArticleVO.setQ_content(q_content);
				ArticleVO.setQ_id(q_id);
				ArticleVO.setQ_writeDate(q_writeDate);
				
				articlesList.add(ArticleVO);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articlesList;
	}
	
	public List selectAllArticlesList(Map<String, Integer> pagingMap) {
		List<QArticleVO> articlesList = new ArrayList<>();
		
		int section = pagingMap.get("section");
		int pageNum = pagingMap.get("pageNum");
		
		try {
			conn = dataFactory.getConnection();
						
			String sql = "select * from (\r\n"
					+ "                select rownum as recNum, q_lvl\r\n"
					+ "                      ,q_articleNo\r\n"
					+ "                      ,q_parentNo\r\n"
					+ "                      ,q_title\r\n"
					+ "                      ,q_id\r\n"
					+ "                      ,q_writeDate\r\n"
					+ "                      ,q_newArticle\r\n"
					+ "                from (select level as q_lvl\r\n"
					+ "                              ,q_articleNo\r\n"
					+ "                              ,q_parentNo\r\n"
					+ "                              ,q_title\r\n"
					+ "                              ,q_id\r\n"
					+ "                              ,q_writeDate\r\n"
					+ "                              ,decode(round(sysdate - q_writedate),0,'true','false') q_newArticle\r\n"
					+ "                        from q_board\r\n"
					+ "                        start WITH q_parentNo=0              \r\n"
					+ "                        CONNECT by PRIOR q_articleNo=q_parentNo \r\n"
					+ "                        order SIBLINGS by q_articleNo desc \r\n"
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
				
				int q_level = rs.getInt("q_LVL");
				int q_articleNo = rs.getInt("q_articleNo");
				int q_parentNo = rs.getInt("q_parentNo");
				String q_title = rs.getString("q_title");
				String q_id = rs.getString("q_id");
				Date q_writeDate = rs.getDate("q_writeDate");
				boolean q_newArticle = Boolean.parseBoolean(rs.getString("q_newArticle"));
				
				QArticleVO articleVO = new QArticleVO();
				
				articleVO.setQ_level(q_level);
				articleVO.setQ_articleNo(q_articleNo);
				articleVO.setQ_parentNo(q_parentNo);
				articleVO.setQ_title(q_title);
				articleVO.setQ_id(q_id);
				articleVO.setQ_writeDate(q_writeDate);
				articleVO.setQ_newArticle(q_newArticle); 
				
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
	

	public int insertNewArticle(QArticleVO articleVO) {
		
		//글번호 (1증가된 글번호 얻기)
		int q_articleNo = getNewArticleNo();
		
		try {
			conn = dataFactory.getConnection();
			
			int q_parentNo = articleVO.getQ_parentNo();
			String q_title = articleVO.getQ_title();
			String q_content = articleVO.getQ_content();
			String q_id = articleVO.getQ_id();
			String q_imageFileName = articleVO.getQ_imageFileName();
			
			String sql = "INSERT INTO q_board (q_articleNo, q_parentNo, q_id, q_title, q_content, q_imageFileName)"
						+" VALUES (?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, q_articleNo);
			pstmt.setInt(2, q_parentNo);
			pstmt.setString(3, q_id);
			pstmt.setString(4, q_title);
			pstmt.setString(5, q_content);
			pstmt.setString(6, q_imageFileName);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return q_articleNo;
	}

	public int getNewArticleNo() {
		
		try {
			conn = dataFactory.getConnection();
			
			String sql = "SELECT max(q_articleNo) from q_board";
			
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

	public QArticleVO selectArticleVo(int q_articleNo) {
		QArticleVO articleVO = new QArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "select q_articleNo, q_parentNo, q_title, q_content, NVL(q_imageFileName,'null') as q_imageFileName, q_id, q_writeDate"
						+" from q_board"
						+" where q_articleNo=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, q_articleNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			int q_articleNo_ = rs.getInt("q_articleNo");
			int q_parentNo = rs.getInt("q_parentNo");
			String q_title = rs.getString("q_title");
			String q_content = rs.getString("q_content");
			
			//파일이름에 특수문자가 있을 경우 인코딩함
			String q_imageFileName = URLEncoder.encode(rs.getString("q_imageFileName"), "utf-8");
			String q_id = rs.getString("q_id");
			Date q_writeDate = rs.getDate("q_writeDate");
					
			articleVO.setQ_articleNo(q_articleNo_);
			articleVO.setQ_parentNo(q_parentNo);
			articleVO.setQ_title(q_title);
			articleVO.setQ_content(q_content);
			articleVO.setQ_imageFileName(q_imageFileName);
			articleVO.setQ_id(q_id);
			articleVO.setQ_writeDate(q_writeDate);
						
			rs.close();
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleVO;
	}

	public void updateArticle(QArticleVO articleVO) {
		int q_articleNo = articleVO.getQ_articleNo();
		String q_title = articleVO.getQ_title();
		String q_content = articleVO.getQ_content();
		String q_imageFileName = articleVO.getQ_imageFileName();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "update q_board set q_title=?, q_content=?";
			
			if (q_imageFileName != null && q_imageFileName.length() != 0) {
				sql += ", q_imageFileName=?";
			}
			
			sql += " where q_articleNo=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, q_title);
			pstmt.setString(2, q_content);
			
			if (q_imageFileName != null && q_imageFileName.length() != 0) {
				pstmt.setString(3, q_imageFileName);
				pstmt.setInt(4, q_articleNo);
			}
			else {
				pstmt.setInt(3, q_articleNo);
			}
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Integer> selectRemovedArticlesList(int q_articleNo) {
		List<Integer> articleNoList = new ArrayList<>();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "SELECT q_articleNo"
					+ " FROM q_board"
					+ " START WITH q_articleNo=?"						
					+ " CONNECT BY PRIOR q_articleNo=q_parentNo";	
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, q_articleNo);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				q_articleNo = rs.getInt("q_articleNo");
				articleNoList.add(q_articleNo);
			}
			
			pstmt.close();
			conn.close();
			rs.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articleNoList;
	}

	public void deleteArticle2(int q_articleNo) {
		try {
			conn = dataFactory.getConnection();
			String sql = "delete from q_board"
						+" WHERE q_articleNo in("
						+"  SELECT q_articleNo"
						+ "  FROM q_board"
						+ "  START WITH q_articleNo=?"						
						+ "  CONNECT BY PRIOR q_articleNo=q_parentNo)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, q_articleNo);
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
			String sql = "select count(q_articleNo) from q_board";
			
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
	
	public QArticleVO selectAllArticles(int q_articleNo) {
		QArticleVO articleVO = new QArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			String sql = "select q_articleNo, q_parentNo, q_title, q_content, NVL(q_imageFileName,'null') as q_imageFileName, q_id, q_writeDate"
						+" from q_board"
						+" where q_articleNo=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, q_articleNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			int q_articleNo_ = rs.getInt("q_articleNo");
			int q_parentNo = rs.getInt("q_parentNo");
			String q_title = rs.getString("q_title");
			String q_content = rs.getString("q_content");
			
			//파일이름에 특수문자가 있을 경우 인코딩함
			String q_imageFileName = URLEncoder.encode(rs.getString("q_imageFileName"), "utf-8");
			String q_id = rs.getString("q_id");
			Date q_writeDate = rs.getDate("q_writeDate");
					
			articleVO.setQ_articleNo(q_articleNo_);
			articleVO.setQ_parentNo(q_parentNo);
			articleVO.setQ_title(q_title);
			articleVO.setQ_content(q_content);
			articleVO.setQ_imageFileName(q_imageFileName);
			articleVO.setQ_id(q_id);
			articleVO.setQ_writeDate(q_writeDate);
			
			rs.close();
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleVO;
	}
	public ArrayList<QArticleVO> getMemberlist3(String qkeyWord, String qkeyField) {
	      ArrayList<QArticleVO> articlesList = new ArrayList<>();
	     
	      try {
	         conn = dataFactory.getConnection();
	         /* level : 오라클에서 제공하는 가상 컬럼으로 글의 깊이를 나타냄 */

	         String sql ="select * from q_board  WHERE "+qkeyField+"  LIKE '%"   +qkeyWord+   "%' order by q_id";
	         
	           
	            pstmt = conn.prepareStatement(sql);
	            
	            ResultSet rs = pstmt.executeQuery();
	            
	            System.out.println("rs : " + rs.getRow());
	         
	         while(rs.next()) {
	        	   
					int articleNo = rs.getInt("q_articleNo");
					int parentNo = rs.getInt("q_parentNo");
					String title = rs.getString("q_title");
					String id = rs.getString("q_id");
					String content = rs.getString("q_content");
					Date writeDate = rs.getDate("q_writeDate");
					
					
					QArticleVO articleVO = new QArticleVO();
					
					
					articleVO.setQ_articleNo(articleNo);
					articleVO.setQ_parentNo(parentNo);
					articleVO.setQ_title(title);
					articleVO.setQ_id(id);
					articleVO.setQ_content(content);
					articleVO.setQ_writeDate(writeDate);
					 
					
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
	public ArrayList<ArticleVO> getMemberlist(){
	       
        ArrayList<ArticleVO> list = new ArrayList<ArticleVO>();
       
        try{//실행
           String sql = "select * from t_board";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(sql);
           
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
