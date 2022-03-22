import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBInfra {
	private static Map<String, PreparedStatement> preparedStatementMap = new HashMap();// Collection-Map
	private static Connection conn;

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		DBInfra.conn = conn;
	}
	static {
		try {// Exception handling using try catch
				// JDBC Connection
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/MonitorDb", "root", "root");
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	static String fileName;
	static String sql4;
	static PreparedStatement createLookUpTable;
	static PreparedStatement createInvalidRecTable;
	
	static PreparedStatement saveLookup;
	static PreparedStatement findId;
	static PreparedStatement saveInvalidRec;


	static {
		try {
			String sql = "Create table Lookup(\r\n" + "	Id int(200) auto_increment,\r\n" + "    Date date,\r\n"
					+ "    Filename varchar(30),\r\n" + "    Primary key(Id)\r\n" + ")";
			String sql3 = "Create table invalidRec(\r\n" + "SrNo int(200),\r\n" + "RecordNo int(30),\r\n"
					+ "Record varchar(100),\r\n" + "Foreign key (srNo) references Lookup(Id)\r\n" + ")";
			createLookUpTable = conn.prepareStatement(sql);
			createLookUpTable.executeUpdate();
			createInvalidRecTable = conn.prepareStatement(sql3);
			createInvalidRecTable.executeUpdate();

			saveLookup = conn.prepareStatement("insert into Lookup(Date,Filename) values(?,?)");
			findId = conn.prepareStatement("select * from Lookup where Date = ? && Filename = ?");
			saveInvalidRec = conn.prepareStatement("insert into InvalidRec values(?,?,?)");

			preparedStatementMap.put("saveLookup", saveLookup);
			preparedStatementMap.put("findId", findId);
			preparedStatementMap.put("saveInvalidRec", saveInvalidRec);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static PreparedStatement getPreparedStatement(String id) {
		return preparedStatementMap.get(id);
	}
	
	

}
