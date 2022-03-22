import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DBRepo {
    static DBInfra dbinfra = new DBInfra();
    static PreparedStatement createValidRecTable;
    static PreparedStatement alterValidRecTable;
    
	public static String createValidtable(String id, String tableName) {
		String sql = "Create table " + tableName + "(\r\n" + "SrNo int(20),\r\n" + "custId int(30),\r\n"
				+ "Foreign key (SrNo) references Lookup(Id)\r\n" + ")";
		try {
			createValidRecTable = dbinfra.getConn().prepareStatement(sql);
			createValidRecTable.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableName;
	}

	public static void alterValidtable(String tableName, List<String> fieldNameList) {
		for (String f : fieldNameList) {
			String s = "Alter table " + tableName + " add " + f + " varchar(30)";
			try {
				alterValidRecTable = dbinfra.getConn().prepareStatement(s);
				alterValidRecTable.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void saveToValidTable(String tableName, int id, int recNo, String[] record) {
		String s = "insert into " + tableName + " values(?,?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = dbinfra.getConn().prepareStatement(s);
			stmt.setInt(1, id);
			stmt.setInt(2, recNo);
			int j = 3;
			for (int i = 0; i < record.length; i++) {
				stmt.setString(j++, record[i]);
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
