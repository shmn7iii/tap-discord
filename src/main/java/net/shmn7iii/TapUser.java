package net.shmn7iii;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TapUser {
    String APIROOTPATH = "https://tap-api.shmn7iii.net/users/";

    public TapUser(){}

    public MessageEmbed generateUserEmbed(String uid, String wallet_id, String created_at){
        // build embed
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("User Info")
                .addField(uid, "-uid", false)
                .addField(wallet_id, "-wallet_id", false)
                .setColor(Color.green)
                .setFooter("Issue at: " + created_at);

        return eb.build();
    }

    public ArrayList<MessageEmbed> indexToken(long num){
        // send API request
        JsonNode json = Http.sendRequest2API(Http.METHOD.GET, APIROOTPATH + num, null);

        // create embed
        ArrayList<MessageEmbed> embeds = new ArrayList<MessageEmbed>();
        for (int i = 0; i < num; i++ ){
            JsonNode user = json.get("data").get(i);
            String uid = user.get("uid").textValue();
            String wallet_id = user.get("wallet_id").textValue();
            String created_at = user.get("created_at").textValue();
            TapUser tu = new TapUser();
            MessageEmbed embed = tu.generateUserEmbed(uid, wallet_id, created_at);
            embeds.add(embed);
        }

        return embeds;
    }


    public ArrayList<MessageEmbed> getUserInfo(String uid){
        ArrayList<MessageEmbed> embeds = new ArrayList<MessageEmbed>();

        // send API request
        JsonNode json = Http.sendRequest2API(Http.METHOD.GET, APIROOTPATH + "info/" + uid, null);

        String wallet_id = json.get("data").get("wallet_id").textValue();
        String created_at = json.get("data").get("created_at").textValue();


        // create embed
        TapUser tu = new TapUser();
        MessageEmbed embed =  tu.generateUserEmbed(uid, wallet_id, created_at);
        embeds.add(embed);

        // get tokens
        var tokens = json.get("data").get("tokens");
        ArrayList<String> lists = StreamSupport.stream(tokens.spliterator(), false)
                                    .map(JsonNode::asText)
                                    .collect(Collectors.toCollection(ArrayList::new));
        for (String token_id: lists){
            TapToken tt = new TapToken();
            MessageEmbed e = tt.getTokenInfo(token_id);
            embeds.add(e);
        }

        return embeds;
    }


    public ArrayList<MessageEmbed> createUser(String uid){
        // send API request
        String str_json = "{ \"uid\": \"" + uid + "\" }";
        JsonNode json = Http.sendRequest2API(Http.METHOD.POST, APIROOTPATH, str_json);

        // create embed
        return getUserInfo(uid);
    }

    public MessageEmbed destroyUser(String uid){
        // send API request
        JsonNode json = Http.sendRequest2API(Http.METHOD.DELETE, APIROOTPATH + uid, "{}");

        // create embed
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("User Info")
                .addField(uid, "-uid", false)
                .setColor(Color.green);

        return eb.build();
    }
}
