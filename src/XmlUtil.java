import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlUtil {
	static FileFormat loadAndParseXML(String filename) {
		FileFormat fd = null;
		File xmlFile = new File(filename);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(FileFormat.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			fd = (FileFormat) jaxbUnmarshaller.unmarshal(xmlFile);
			for (FileData f : fd.getFileData()) {
				f.populateMap();
			}
			//System.out.println(fd);		
			return fd;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
