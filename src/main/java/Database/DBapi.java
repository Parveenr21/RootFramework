package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBapi{
	
	private static DBapi DBConInstance;
	public  Connection con;
    private  final String Driver = "oracle.jdbc.driver.OracleDriver";
    private  final String ConnectionString = "jdbc:oracle:thin:@hsotname:1521:SID";
    private  final String user = "UserID";
    private  final String pwd = "UserPAsswd";

    
    /**
     * Making constructor private
     */
    
    private DBapi(){
    	
    	
    }
    
    
    /**
     * create Database object
     */
     public static DBapi Database() {
    
    	if(DBConInstance==null)
    	{
    		DBConInstance = new DBapi();
    	}
    	return DBConInstance;
    }

    
     /**
     * to load the database base driver
     * @return a database connection
     * @throws SQLException throws an exception if an error occurs
     */
    public Connection loadDriver() throws SQLException {
        try {
            Class.forName(Driver);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        con = DriverManager.getConnection(ConnectionString, user, pwd);
        return con;
    }

    /**
     * to get a result set of a query
     * @param query custom query
     * @return a result set of custom query
     * @throws SQLException throws an exception if an error occurs
     */
    public ResultSet getResultSet(String query) throws SQLException {
        Connection con = loadDriver();
        ResultSet rs;
        PreparedStatement st;
              
       //con.getMetaData();
      
        st = con.prepareStatement(query);
          	  rs = st.executeQuery();
    	   return rs;
      
    }

   
    
    
    
    
    
    
    /* *//**
     * to run an update query such as update, delete
     * @param query custom query
     * @throws SQLException throws an exception if an error occurs
     *//*
    public static void runQuery(String query) throws SQLException {
        Connection con = loadDriver();
        ResultSet rs;
        PreparedStatement st = con.prepareStatement(query);
        st.executeUpdate();
    }*/
	
	
  
}