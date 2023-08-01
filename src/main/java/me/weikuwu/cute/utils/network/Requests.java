package me.weikuwu.cute.utils.network;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Requests {
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
}
