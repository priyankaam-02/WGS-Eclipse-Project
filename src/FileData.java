import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import java.util.Collections;
import java.util.Comparator;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class FileData {
	String fileName;
	String outFileName;
	String timeToArrive;
	@XmlElementWrapper(name = "format")
	@XmlElement(name = "field")
	private List<FieldData> fieldData = new ArrayList<FieldData>();
	private Map<String, FieldData> nameofFieldDataMap = new HashMap<String, FieldData>();

	public Map<String, FieldData> getNameofFieldDataMap() {
		return nameofFieldDataMap;
	}

	public void setNameofFieldDataMap(Map<String, FieldData> nameofFieldDataMap) {
		this.nameofFieldDataMap = nameofFieldDataMap;
	}

	@Override
	public String toString() {
		return "FileData [fileName=" + fileName + ", outFileName=" + outFileName + ", timeToArrive=" + timeToArrive
				+ ", fieldData=" + fieldData + ", nameofFieldDataMap=" + nameofFieldDataMap + "]";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public Date getTimeToArrive() {
		String[] timeSplit = timeToArrive.split(":");
		int minute = Integer.parseInt(timeSplit[1]);
		int hour = Integer.parseInt(timeSplit[0]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		return date;
		// return timeToArrive;
	}

	public void setTimeToArrive(String timeToArrive) {
		this.timeToArrive = timeToArrive;
	}

	public List<FieldData> getFieldData() {
		return fieldData;
	}

	public void setFieldData(List<FieldData> fieldData) {
		this.fieldData = fieldData;
	}

	public void populateMap() {
		for (FieldData f : fieldData) {
			String name = f.getName();
			nameofFieldDataMap.put(name, f);
		}
	}

	public Map<String, FieldData> listOfFieldData() {
		List<Entry<String, FieldData>> listOfEntry = new LinkedList<>(nameofFieldDataMap.entrySet());

		// Sort listOfEntry using Collections.sort() by passing customized Comparator
		Collections.sort(listOfEntry, new Comparator<Entry<String, FieldData>>() {
			@Override
			public int compare(Entry<String, FieldData> o1, Entry<String, FieldData> o2) {
				return o1.getValue().getOrder() - (o2.getValue().getOrder());
			}
		});

		// Insert all elements of listOfEntry into new LinkedHashMap which maintains
		// insertion order

		Map<String, FieldData> sortedFieldMap = new LinkedHashMap<String, FieldData>();
		for (Entry<String, FieldData> entry : listOfEntry) {
			sortedFieldMap.put(entry.getKey(), entry.getValue());
		}
		System.out.println(sortedFieldMap + " :after sorting values");
		return sortedFieldMap;
	}

}
