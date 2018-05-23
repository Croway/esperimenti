package it.croway.esperimenti;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ConnectUsersTest {

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		FileInputStream fis = new FileInputStream(new File(Vars.fileLoc));
		Workbook wb = new XSSFWorkbook(fis);
		Sheet s = wb.getSheetAt(0);
		int i = 0;
		for (Row row : s) {
			if (i > Vars.maxUser)
				return;

			String codFis = row.getCell(0).getStringCellValue().trim();
			Thread.sleep(100);

			System.out.println("connecting " + i + " -> " + codFis);
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:7001/sol-alpi12/init")
					.openConnection();
			connection.setRequestMethod("GET");
			String encoded = Base64.getEncoder()
					.encodeToString((codFis + ":" + codFis).getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			connection.setDoInput(true);
			connection.connect();

			int responseCode = connection.getResponseCode();

			System.out.println("connected " + i + " -> " + codFis + " resp code -> " + responseCode);
			i++;
		}

	}

}
