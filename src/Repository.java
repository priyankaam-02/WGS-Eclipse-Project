import java.util.List;

public interface Repository {
	void saveToLookUp(String fileName);
	int searchIdinLookUp(String fileName);
	void saveToInvalid(int id,int recNo,String record);
	void createValidRec(String tableName,List<String> fieldNameList);
	void saveTovalid(String tableName,int id,int recNo,String[] record);
}
