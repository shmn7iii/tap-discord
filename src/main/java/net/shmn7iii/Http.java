package net.shmn7iii;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nullable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {

    public enum METHOD{
        GET,
        POST,
        PUT,
        DELETE
    }

    public static JsonNode sendRequest2API(METHOD _method, String _url, @Nullable String _jsonBody) throws JsonProcessingException {
        String result = "";
        JsonNode root = null;
        String method = "";

        switch (_method){
            case GET:   method = "GET"; break;
            case POST:  method = "POST"; break;
            case PUT:   method = "PUT"; break;
            case DELETE:   method = "DELETE"; break;
        }

        try {
            URL url = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            if (method.equals("GET")){
                con.connect();
            } else {
                con.setRequestMethod(method);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(
                        new BufferedOutputStream(con.getOutputStream()));
                out.write(_jsonBody);
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String tmp = "";
            while ((tmp = in.readLine()) != null) {
                result += tmp;
            }
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(result);
            in.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            String json = "{" +
                    "\"error\": {" +
                    "\"message\": \"" + e.getMessage() + "\"" +
                    "\"string\": \"" + e.toString() + "\"" +
                    "} " +
                    "}";
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(json);
        }
        return root;
    }
}
