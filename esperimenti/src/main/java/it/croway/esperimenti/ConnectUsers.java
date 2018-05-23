package it.croway.esperimenti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ConnectUsers {

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		String user = "PPLLSN61H43H501S";
		for (int i = 0; i < 1; i++) {
			if (i > Vars.maxUser)
				return;

			String codFis = user;
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

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());

			System.out.println("connected " + i + " -> " + codFis + " resp code -> " + responseCode);
			i++;
		}

	}

}
