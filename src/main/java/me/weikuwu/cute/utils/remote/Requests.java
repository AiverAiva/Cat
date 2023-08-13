package me.weikuwu.cute.utils.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Requests {
    private static final JsonParser parser = new JsonParser();

    public static String get(String url) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0");
            request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");

            HttpResponse httpResponse = client.execute(request);

            if (httpResponse.getStatusLine().getStatusCode() < 299) {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            } else {
                System.out.println("Error code " + httpResponse.getStatusLine().getStatusCode());
                return null;
            }
        }
    }

    public static List<String> getListFromUrl(String url, String name) {
        List<String> ret = new ArrayList<>();
        try {
            JsonObject json = parser.parse(getContent(url)).getAsJsonObject();
            json.getAsJsonArray(name).forEach(je -> ret.add(je.getAsString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getContent(String url) throws IOException {
        URL website = new URL(url);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
}
