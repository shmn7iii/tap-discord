package net.shmn7iii;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        String token = null;
        String cred_path = null;

        for (int i=0; i<args.length; ++i) {
            if ("-token".equals(args[i])) {
                token = args[++i];
            } else if ("-credential".equals(args[i])) {
                cred_path = args[++i];
            }
        }

        if (token == null || cred_path == null){
            System.out.println("-token <token_id> -credential <credential file path>");
            System.exit(1);
        }


        // firebase
        Firebase fb = Firebase.getInstance();
        fb.init(cred_path);


        // discord
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new Event())
                .build();

        jda.awaitReady();

        createCommands(jda);

        jda.getGuildById("865554173231104021").updateCommands().queue();
        jda.updateCommands().queue();

    }

    public static void createCommands(JDA jda){
        CommandData tokenCommand = new CommandData("token", "About tokens.");
        tokenCommand.addSubcommands(
                new SubcommandData("index", "Show index of tokens.")
                        .addOption(OptionType.INTEGER, "num", "how many tokens do you want", false),

                new SubcommandData("info", "Show info of the token.")
                        .addOption(OptionType.STRING, "token_id", "token's id", true),

                new SubcommandData("issue", "Issue token.")
                        .addOption(OptionType.STRING, "uid", "type your uid", true)
                        .addOption(OptionType.STRING, "data", "type image url", true) //,

//                new SubcommandData("transfer", "Transfer token to someone.")
//                        .addOption(OptionType.STRING, "token_id", "token's id", true)
//                        .addOption(OptionType.STRING, "sender_uid", "sender's uid", true)
//                        .addOption(OptionType.STRING, "receiver_uid", "receiver's uid", true),
//
//                new SubcommandData("burn", "Burn token.")
//                        .addOption(OptionType.STRING, "uid", "type your uid", true)
//                        .addOption(OptionType.STRING, "token_id", "token's id", true)
                );

        CommandData userCommand = new CommandData("user", "About users.");
        userCommand.addSubcommands(
                new SubcommandData("index", "Show index of users.")
                        .addOption(OptionType.INTEGER, "num", "how many users do you want", false),

                new SubcommandData("info", "Show info and token of the user.")
                        .addOption(OptionType.STRING, "uid", "uid", true),

                new SubcommandData("create", "Create user.")
                        .addOption(OptionType.STRING, "uid", "type your uid", true),

                new SubcommandData("destroy", "Destroy user.")
                        .addOption(OptionType.STRING, "uid", "uid", true)
        );


        jda.getGuildById("865554173231104021").upsertCommand(tokenCommand).queue();
        jda.getGuildById("865554173231104021").upsertCommand(userCommand).queue();
        jda.upsertCommand(tokenCommand).queue();
        jda.upsertCommand(userCommand).queue();
    }
}
