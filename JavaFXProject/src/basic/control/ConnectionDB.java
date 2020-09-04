package basic.control;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;
	 
	public class ConnectionDB 
	{
	    public static Connection dbConn;
	    
	        public static Connection getDB()
	        {
	        	String user = "hr"; //기본 사용자
                String pw = "hr";
                String url = "jdbc:oracle:thin:@localhost:1521:xe";//수정
	            Connection conn = null;
	            try {
	              
	                Class.forName("oracle.jdbc.driver.OracleDriver");        
	                conn = DriverManager.getConnection(url, user, pw);
	                
	                System.out.println("Database에 연결되었습니다.\n");
	                
	            } catch (ClassNotFoundException cnfe) {
	                System.out.println("DB 드라이버 로딩 실패 :"+cnfe.toString());
	            } catch (SQLException sqle) {
	                System.out.println("DB 접속실패 : "+sqle.toString());
	            } catch (Exception e) {
	                System.out.println("Unkonwn error");
	                e.printStackTrace();
	            }
	            return conn;     
	        }
	}



