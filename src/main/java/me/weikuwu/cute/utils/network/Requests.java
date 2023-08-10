package me.weikuwu.cute.utils.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Requests {
    private static JsonParser parser;

    public static String get(String url) throws IOException {
        String response = null;

        HttpClientBuilder client = HttpClients.custom().addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {

            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0");
            request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");

            if (url.contains("cdn.discordapp.com")) {
                request.addHeader("Host", "cdn.discordapp.com");
            }
        });

        HttpGet request = new HttpGet(url);
        CloseableHttpResponse httpResponse = client.build().execute(request);
        if (httpResponse.getStatusLine().getStatusCode() < 299) {
            response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } else {
            System.out.println("Error code " + httpResponse.getStatusLine().getStatusCode());
        }

        return response;
    }

    public static List<String> getListFromUrl(final String url, final String name) {
        final List<String> ret = new ArrayList<String>();
        try {
            final JsonObject json = (JsonObject)Requests.parser.parse(getContent(url));
            json.getAsJsonArray(name).forEach(je -> ret.add(je.getAsString()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getContent(final String url) throws Exception {
        final URL website = new URL(url);
        final URLConnection connection = website.openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    static {
        Requests.parser = new JsonParser();
    }

}
