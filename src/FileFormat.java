import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="files")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileFormat {
	@XmlElementWrapper(name="listOfFiles")
	@XmlElement(name="file")
	static
	private List<FileData> fileData = new ArrayList<FileData>();
   
	public static List<FileData> getFileData() {
		return fileData;
	}

	public void setFileData(List<FileData> fileData) {
		this.fileData = fileData;
		
	}
	
	@Override
	public String toString() {
		return "FileFormat [fileData=" + fileData + "]";
	}
	

}
