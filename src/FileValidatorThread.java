import java.io.File;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class FileValidatorThread extends Thread {
	private File file;
	private FileFormat fd;
	private FileData fileData;
	private static Map<String, String> fileNameExists = new HashMap();
	private Map<String, FieldData> fieldMap = new LinkedHashMap();
	private MonitorRepository repo = new MonitorRepository();

	FileValidatorThread(File f, FileFormat fileFormat) {
		this.file = f;
		this.fd = fileFormat;
	}

	public void run() {
		Calendar now = Calendar.getInstance();
		String fileArrivalDate = now.get(Calendar.DATE) + "/" + (now.get(Calendar.MONTH) + 1) + "/"
				+ now.get(Calendar.YEAR);
		String currentFileTime = now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE);
		if (isNameValid() && !isLate(currentFileTime) && !isDuplicate(fileArrivalDate)) {
			fileNameExists.put(file.getName(), fileArrivalDate);// Map of filename and date
			this.file.renameTo(new File("./src/inprocess/" + this.file.getName()));// Moving valid file to inprocess																					// folder
			System.out.println("File " + file.getName() + " moved to inprocess");
			repo.saveToLookUp(this.file.getName());// saving file entry in lookup table in db
			FileProcessorThread processor = new FileProcessorThread(this.file.getName(), fieldMap);
		} else {
			// Delete File
			System.out.println("File " + file.getName() + " not valid");
			file.delete();
		}
	}

	// Duplicate file validation
	private boolean isDuplicate(String fileArrivalDate) {
		String fileDate = null;
		System.out.println("Already file exists : " + fileNameExists.containsKey(file.getName()));

		if (fileNameExists.containsKey(file.getName())) {
			fileDate = fileNameExists.get(file.getName());
			System.out.println(fileDate + ":" + fileArrivalDate);
			if (fileDate.equals(fileArrivalDate)) {
				System.out.println("File duplicacy found Deleting file...");
				return true;
			} else {
//				System.out.println("File duplicacy not found");
				return false;
			}
		}
		return false;
	}

	// Time validation of file
	private boolean isLate(String currentFileTime) {
		for (FileData fdata : fd.getFileData()) {
			if (fdata.getFileName().equals(file.getName()) && file.lastModified() < fdata.getTimeToArrive().getTime()) {
				System.out.println("File arrived in time");
				return false;
			}
		}
		return true;
	}

	// Filename validation
	private boolean isNameValid() {
		for (FileData fdescription : fd.getFileData()) {
			if (fdescription.getFileName().equals(this.file.getName())) {
				System.out.println("File Name is Valid: " + file.getName());
				fieldMap = fdescription.listOfFieldData();
				return true;
			}
		}
		return false;
	}

}
