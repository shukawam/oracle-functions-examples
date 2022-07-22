package com.example.fn;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HelloFunction {

    private static final String API_ENDPOINT = "https://amaaaaaassl65iqapvzmjf7t3kagzdstep3gz4zw3tdasxdcs2wht3znulyq.opensearch.ap-tokyo-1.oci.oracleiaas.com:9200";

    public Object handleRequest(String input) {
        HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).sslContext(insecureContext()).build();
        return search(httpClient, input);
    }

    private Object search(HttpClient httpClient, String director) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_ENDPOINT + "/movies/_search?q=directors=" + director))
            .header("Content-Type", "application/json")
            .GET()
            .build();
        try {
            HttpResponse response = httpClient.send(request, BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("document is not found.");
        }
    }

    private SSLContext insecureContext() {
        TrustManager[] noopManagers = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {

                    }

                    public void checkServerTrusted(X509Certificate[] xcs, String string) {

                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("ssl");
            sc.init(null, noopManagers, null);
            return sc;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }
    }

}