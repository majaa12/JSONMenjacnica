package rs.ac.bg.fon.ai.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.ac.bg.fon.ai.JSONMenjacnica.Transakcija;

public class Main2 {

	private static final String BASE_URL = "http://api.currencylayer.com";
	private static final String API_KEY = "2e4baadf5c5ae6ba436f53ae5558107f";
	private static final String SOURCE = "USD";
	private static final double IZNOS = 100.0;

	public static void main(String[] args) {
		
		Transakcija t1 = new Transakcija();
		t1.setIzvornaValuta(SOURCE);
		t1.setKrajnjaValuta("EUR");
		t1.setDatumTransakcije(new GregorianCalendar(2020, 5, 12).getTime());
		t1.setPocetniIznos(IZNOS);

		Transakcija t2 = new Transakcija();
		t2.setIzvornaValuta(SOURCE);
		t2.setKrajnjaValuta("CHF");
		t2.setDatumTransakcije(new GregorianCalendar(2020, 5, 12).getTime());
		t2.setPocetniIznos(IZNOS);

		Transakcija t3 = new Transakcija();
		t3.setIzvornaValuta(SOURCE);
		t3.setKrajnjaValuta("CAD");
		t3.setDatumTransakcije(new GregorianCalendar(2020, 5, 12).getTime());
		t3.setPocetniIznos(IZNOS);

		Transakcija[] transakcije = { t1, t2, t3 };

		try {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			URL url = new URL(BASE_URL + "/historical?access_key=" + API_KEY + "&date=2020-06-12" + "&source=" + SOURCE
					+ "&currencies=" + t1.getKrajnjaValuta() + "," + t2.getKrajnjaValuta() + ","
					+ t3.getKrajnjaValuta());

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

			JsonObject rez = gson.fromJson(reader, JsonObject.class);

			if (rez.get("success").getAsBoolean()) {
				System.out.println(rez);

				for (Transakcija t : transakcije) {
					t.setKonvertovaniIznos(
							rez.get("quotes").getAsJsonObject().get("USD" + t.getKrajnjaValuta()).getAsDouble()
									* t.getPocetniIznos());

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (FileWriter file = new FileWriter("ostale_transakcije.json")) {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(transakcije, file);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
