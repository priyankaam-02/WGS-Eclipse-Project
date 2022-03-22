import java.io.File;

public class FileMonitor {
	public static void main(String[] args) {
		// Every 30 seconds check if files have arrived
		DBInfra db = new DBInfra();
		FileFormat fileFormat = XmlUtil.loadAndParseXML("CustomerData.xml");
		// System.out.println(fileFormat);
		while (true) {
			System.out.println("checking if new files have arrived ...");
			File[] files = new File("./src/input").listFiles();
			// Spawn a Reader Thread which validates the file
			for (File file : files) {
				FileValidatorThread t = new FileValidatorThread(file, fileFormat);
				t.start();
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
