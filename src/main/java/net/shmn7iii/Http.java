package net.shmn7iii;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
    public static JsonNode getResult(String urlString) {
        String result = "";
        JsonNode root = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String tmp = "";
            while ((tmp = in.readLine()) != null) {
                result += tmp;
            }
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(result);
            in.close();
            con.disconnect();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    public static JsonNode postJson(String urlString, String json) {
        String result = "";
        JsonNode root = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(
                    new BufferedOutputStream(con.getOutputStream()));
            out.write(json);
            out.close();

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
        }
        return root;
    }

    public static JsonNode putJson(String urlString, String json) {
        String result = "";
        JsonNode root = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(
                    new BufferedOutputStream(con.getOutputStream()));
            out.write(json);
            out.close();

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
        }
        return root;
    }

    // FIXME:ネーミングミスってるだろ流石に
    public static JsonNode deleteJson(String urlString, String json) {
        String result = "";
        JsonNode root = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.connect();

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
        }
        return root;
    }
}
