import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MonitorRepository implements Repository {
	private static java.util.Date date = new java.util.Date();
	private static java.sql.Date sqlDate = new java.sql.Date(date.getTime());

	@Override
	public void saveToLookUp(String fileName) {
		PreparedStatement stmt = DBInfra.getPreparedStatement("saveLookup");
		try {
			stmt.setDate(1, sqlDate);
			stmt.setString(2, fileName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int searchIdinLookUp(String fileName) {
		PreparedStatement stmt = DBInfra.getPreparedStatement("findId");
		try {
			stmt.setDate(1, sqlDate);
			stmt.setString(2, fileName);
			ResultSet result = stmt.executeQuery();
//			result.next();
			while (result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void saveToInvalid(int id, int recNo, String record) {
		PreparedStatement stmt = DBInfra.getPreparedStatement("saveInvalidRec");
		try {
			stmt.setInt(1, id);
			stmt.setInt(2, recNo);
			stmt.setString(3, record);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createValidRec(String tableName, List<String> fieldNameList) {
		DBRepo.createValidtable("createValidRecTable", tableName);
		DBRepo.alterValidtable(tableName, fieldNameList);
	}

	@Override
	public void saveTovalid(String tableName, int id, int recNo, String[] record) {

		DBRepo.saveToValidTable(tableName, id, recNo, record);

	}

}
