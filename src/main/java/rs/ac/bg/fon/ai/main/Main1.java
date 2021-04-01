package rs.ac.bg.fon.ai.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.sql.rowset.spi.TransactionalWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.ac.bg.fon.ai.JSONMenjacnica.Transakcija;

public class Main1 {

	private static final String BASE_URL = "http://api.currencylayer.com";
	private static final String API_KEY = "2e4baadf5c5ae6ba436f53ae5558107f";

	public static void main(String[] args) {

		Transakcija t = new Transakcija();
		t.setIzvornaValuta("USD");
		t.setKrajnjaValuta("CAD");
		t.setDatumTransakcije(new GregorianCalendar());

		t.setPocetniIznos(123.0);

		try (FileWriter file = new FileWriter("prva_transakcija.json")) {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			URL url = new URL(BASE_URL + "/live?access_key=" + API_KEY + "&source=" + t.getIzvornaValuta()
					+ "&currencies=" + t.getKrajnjaValuta());

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

			JsonObject rez = gson.fromJson(reader, JsonObject.class);

			if (rez.get("success").getAsBoolean()) {
				System.out.println(rez);

				t.setKonvertovaniIznos(
						rez.get("quotes").getAsJsonObject().get("USDCAD").getAsDouble() * t.getPocetniIznos());

				System.out.println(t);
				gson.toJson(t, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
