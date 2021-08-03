package client;

import com.application.Population;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public class Client {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final MediaType JSON_TYPE = MediaType.get("application/json");
    private static final MediaType XML_TYPE = MediaType.get("application/xml");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private OkHttpClient client;

    private String BASE_URL = "https://localhost:8443/api/";

    public Client() throws Exception {
        NonSecureClientBuilder clientBuilder = new NonSecureClientBuilder();
        client = clientBuilder.getClientBuilder().addInterceptor(new BasicAuthInterceptor(USERNAME, PASSWORD)).build();

    }

    private String executeRequest(String endpoint, String method, String bodyStr, MediaType mediaType) throws IOException {
        RequestBody body = bodyStr != null ? RequestBody.create(
                bodyStr, mediaType) : null;
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint).header(HttpHeaders.ACCEPT, mediaType.toString())
                .method(method, body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.code() + ": " + response.body().string();
        }
    }

    public static void main(String[] args) throws Exception {
        Client example = new Client();

        System.out.println(example.executeRequest("populations", "POST", toJson(new Population(1L, "Berlin", 3645000)), JSON_TYPE));
        System.out.println(example.executeRequest("populations", "POST", toJson(new Population(2L, "Warsaw", 1765000)), JSON_TYPE));
        System.out.println(example.executeRequest("populations", "POST", toJson(new Population(3L, "Wroclaw", 100000)), JSON_TYPE));

        System.out.println(example.executeRequest("populations", "GET", null, XML_TYPE));
        System.out.println(example.executeRequest("populations2", "GET", null, JSON_TYPE));

        System.out.println(example.executeRequest("healthcheck", "GET", null, JSON_TYPE));

        System.out.println(example.executeRequest("populations/byname?name=Mouse", "PUT", toJson(new Population(-1L, "Madrid", 2000000)), JSON_TYPE));
        System.out.println(example.executeRequest("populations", "GET", null, JSON_TYPE));
        System.out.println(example.executeRequest("populations/1", "DELETE", null, XML_TYPE));
        System.out.println(example.executeRequest("populations/2", "DELETE", null, XML_TYPE));
        System.out.println(example.executeRequest("populations", "GET", null, XML_TYPE));

    }

    private static String toJson(Population population) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(population);
    }
}