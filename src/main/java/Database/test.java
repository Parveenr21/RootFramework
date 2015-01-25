package Database;

import java.sql.ResultSet;
import java.sql.SQLException;

class test{
	ResultSet rs;
	
	
	
	void database(String query) throws SQLException
	{
		
		try{
		rs=DBapi.Database().getResultSet(query);
		
		while(rs.next())
		{
			
			/*System.out.print(rs.getString("ASSET_ID"));
			System.out.print("    ");
			System.out.print(rs.getString("ASSET_NAME"));
			System.out.print("    ");
			System.out.print(rs.getString("ALTERNATIVE_TEXT"));
			System.out.println("   ");*/
		
			/*
			System.out.print(rs.getString(1));
			System.out.print("    ");
			System.out.println("   ");*/
			
			
		
			System.out.print(rs.getString(1));
			System.out.print("    ");
			System.out.print(rs.getString(2));
			System.out.print("    ");
			System.out.print(rs.getString(3));
			System.out.println("   ");
		
			
			
		}
		}
		
		finally{
			DBapi.Database().con.close();	
		}
		
		
	}
	
	public static void main(String ...xxx) throws SQLException
	{
		
	//new test().database("select * from contentasset where rownum<10 order by asset_id desc");
		//new test().database("SELECT *  FROM user_tables");
	new test().database("SELECT username, status, logon_time  FROM v$session WHERE status = 'ACTIVE'");
				
	}
	
}