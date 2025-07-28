package org.example;

import com.google.gson.*;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AirTableReader {
    private static final String AIRTABLE_API_URL = "https://api.airtable.com/v0/applRuk7NKRGPrZQ2/tblc9jnICm4a2fMHb";
    private static final String AIRTABLE_API_TOKEN = "patMTxO1o7oyr6wkD.f9dc9c7520933f0b8cc8a19d234bcda11d36ec7272ffc06f0eaf18a2ff9b19bd";

    public static JsonArray fetchRecords() throws Exception {
        URL url = new URL(AIRTABLE_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + AIRTABLE_API_TOKEN);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                return response.getAsJsonArray("records");
            }
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }
}
