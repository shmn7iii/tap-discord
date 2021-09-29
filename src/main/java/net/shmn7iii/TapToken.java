package net.shmn7iii;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;

public class TapToken {
    String APIROOTPATH = "https://tap-api.shmn7iii.net/tokens/";

    public TapToken(){}

    public MessageEmbed generateTokenEmbed(String token_id, String data, String created_at){
        // get download url from data URI
        String image_url = Firebase.getInstance().getDownloadURL(data);

        // build embed
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Token Info")
                .addField(token_id, "-token_id", false)
                .setColor(Color.green)
                .setFooter("Issue at: " + created_at)
                .setImage(image_url);

        return eb.build();
    }


    public ArrayList<MessageEmbed> indexToken(long num){
        // send API request
        JsonNode json = Http.sendRequest2API(Http.METHOD.GET,APIROOTPATH + num, null);

        // create embed
        ArrayList<MessageEmbed> embeds = new ArrayList<MessageEmbed>();
        for (int i = 0; i < num; i++ ){
            JsonNode token = json.get("data").get(i);
            String token_id = token.get("token_id").textValue();
            String data = token.get("data").textValue();
            String created_at = token.get("created_at").textValue();
            TapToken tt = new TapToken();
            MessageEmbed embed =  tt.generateTokenEmbed(token_id, data, created_at);
            embeds.add(embed);
        }

        return embeds;
    }


    public MessageEmbed getTokenInfo(String token_id){
        // send API request
        JsonNode json = Http.sendRequest2API(Http.METHOD.GET,APIROOTPATH + "info/" + token_id, null);

        String data = json.get("data").get("data").textValue();
        String created_at = json.get("data").get("created_at").textValue();

        // create embed
        TapToken tt = new TapToken();
        MessageEmbed embed =  tt.generateTokenEmbed(token_id, data, created_at);

        return embed;
    }


    public MessageEmbed issueToken(String uid, String data){
        // send API request
        String str_json = "{ \"uid\": \"" + uid + "\", \"data\": \"" + data + "\" }";
        JsonNode json = Http.sendRequest2API(Http.METHOD.POST, APIROOTPATH, str_json);

        String token_id = json.get("data").get("token_id").textValue();
        String data_r = json.get("data").get("data").textValue();
        String created_at = json.get("data").get("created_at").textValue();

        // create embed
        TapToken tt = new TapToken();
        MessageEmbed embed =  tt.generateTokenEmbed(token_id, data_r, created_at);

        return embed;
    }


    // FIXME:動かん
    public MessageEmbed transferToken(String sender_uid, String receiver_uid, String token_id){
        // send API request
        String str_json = "{ \"sender_uid\": \"" + sender_uid + "\", \"receiver_uid\": \"" + receiver_uid + "\" }";
        JsonNode json = Http.sendRequest2API(Http.METHOD.PUT, APIROOTPATH + token_id, str_json);

        String txid = json.get("data").get("txid").textValue();

        // create embed
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Transaction Info")
                .addField(txid, "-txid", false)
                .addField(token_id, "-token_id", false)
                .addField(sender_uid, "-sender_uid", false)
                .addField(receiver_uid, "-receiver_uid", false)
                .setColor(Color.green);

        return eb.build();
    }

    // FIXME:動かん
    public MessageEmbed burnToken(String uid, String token_id){
        // send API request
        String str_json = "{ \"uid\": \"" + uid + "\" }";
        JsonNode json = Http.sendRequest2API(Http.METHOD.DELETE, APIROOTPATH + token_id, str_json);

        String txid = json.get("data").get("txid").textValue();

        // create embed
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Transaction Info")
                .addField(txid, "-txid", false)
                .addField(token_id, "-token_id", false)
                .addField(uid, "-uid", false)
                .setColor(Color.green);

        return eb.build();
    }
}
