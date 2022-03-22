import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FileProcessorThread extends Thread {
	private String fileName;
	private Map<String, FieldData> fileFields = new LinkedHashMap();

	private static Set<String> tableNames = new HashSet<>();
	private MonitorRepository repo = new MonitorRepository();

	public FileProcessorThread(String fileName, Map<String, FieldData> fieldMap) {
		this.fileName = fileName;
		this.fileFields = fieldMap;
		this.start();
	}

	public void run() {
		List<String> keyset = new ArrayList<>(fileFields.keySet());
		File fl = new File("./src/inprocess/" + this.fileName);
		System.out.println("Started to process the file ");
		// Spawn a Reader Thread which validates the file
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader("./src/inprocess/" + this.fileName);
			br = new BufferedReader(fr);
			String line = null;
			int lineNo = 0;
			while ((line = br.readLine()) != null) {
				lineNo++;
				boolean validRecords = true;
				String[] fields = line.trim().split(",");

				if (fields.length != keyset.size()) {
					System.out.println("Record is invalid");
					saveInValidRecToFile(this.fileName, lineNo, line);
					saveToDbInvalidRec(lineNo, line);
					continue;
				} else {

					int counter = 0;
					for (int i = 0; i < fields.length; i++) {
						String keyName = keyset.get(i);
						FieldData fieldName = fileFields.get(keyName);
						int maxLen = fieldName.getMaxLength();
						int minLen = fieldName.getMinLength();
						if (fields[i].length() <= maxLen && fields[i].length() >= minLen) {
							counter++;

							if (counter == fields.length) {
								saveValidRecToFile(this.fileName, lineNo, line);
								saveToDbValidRec(this.fileName, lineNo, fields, keyset);
								break;
							}
						} else {
							saveInValidRecToFile(this.fileName, lineNo, line);
							saveToDbInvalidRec(lineNo, line);
							break;
						}
					}

					continue;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				fr.close();
				br.close();
				fl.renameTo(new File("./src/archieve/" + this.fileName + LocalDate.now()));

				System.out.println("File " + this.fileName + " moved to archieve");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// Saving Valid records to Valid Table
	private void saveToDbValidRec(String fileName, int recNo, String[] line, List<String> fieldNameList) {

		String fname = fileName.replace(".", "_");// a_txt
		String tableName = "Valid_Records_" + fname;

		if (!tableNames.contains(tableName)) {
			repo.createValidRec(tableName, fieldNameList);
			tableNames.add(tableName);
		}
		int id = repo.searchIdinLookUp(this.fileName);
		if (id > 0) {
			repo.saveTovalid(tableName, id, recNo, line);
		}

	}

	// Saving Valid records to Invalid table
	private void saveToDbInvalidRec(int recNo, String line) {
		int id = repo.searchIdinLookUp(this.fileName);
		if (id > 0) {
			repo.saveToInvalid(id, recNo, line);
		}
	}

	// Saving Valid records to out.txt file
	private void saveValidRecToFile(String fileName, int recNo, String line) {
		// TODO Auto-generated method stub
		File validRecFile = new File("out.txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(validRecFile, true);
			bw = new BufferedWriter(fw);
			bw.write(fileName + " " + LocalDate.now() + ", Record No. " + recNo + " is valid, " + line);
			bw.newLine();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Saving InvalidRecords to err.txt file
	private void saveInValidRecToFile(String fileName, int recNo, String line) {
		File invalidRecFile = new File("err.txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(invalidRecFile, true);
			// fw.append(fileName + ", Record No. " + recNo + " is invalid, " + line);
			bw = new BufferedWriter(fw);
			bw.write(fileName + " " + LocalDate.now() + ", Record No. " + recNo + " is invalid, " + line);
			bw.newLine();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
