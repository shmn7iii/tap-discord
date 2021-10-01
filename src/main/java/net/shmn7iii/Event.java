package net.shmn7iii;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

public class Event extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if (event.getName().equals("token")) {

            if (event.getSubcommandName().equals("index")){
                event.deferReply().queue();

                // get string from option
                String num = null;
                if (event.getOption("num") != null){
                    num = event.getOption("num").getAsString();
                }

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapToken().indexToken(num) )
                        .queue();
            }


            else if (event.getSubcommandName().equals("info")){
                event.deferReply().queue();

                // get string from option
                String token_id = event.getOption("token_id").getAsString();

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapToken().getTokenInfo(token_id) )
                        .queue();
            }


            else if (event.getSubcommandName().equals("issue")){
                event.deferReply().queue();

                // get string from option
                String uid = event.getOption("uid").getAsString();
                String data = event.getOption("data").getAsString();

                // reply
                try {
                    event.getHook()
                            .sendMessageEmbeds( new TapToken().issueToken(uid, data) )
                            .queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            else if (event.getSubcommandName().equals("transfer")){
                event.deferReply().queue();

                // get string from option
                String sender_uid = event.getOption("sender_uid").getAsString();
                String receiver_uid = event.getOption("receiver_uid").getAsString();
                String token_id = event.getOption("token_id").getAsString();

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapToken().transferToken(sender_uid, receiver_uid, token_id) )
                        .queue();
            }


            else if (event.getSubcommandName().equals("burn")){
                event.deferReply().queue();

                // get string from option
                String uid = event.getOption("uid").getAsString();
                String token_id = event.getOption("token_id").getAsString();

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapToken().burnToken(uid, token_id) )
                        .queue();
            }
        }


        else if (event.getName().equals("user")) {

            if (event.getSubcommandName().equals("index")){
                event.deferReply().queue();

                // get string from option
                String num = null;
                if (event.getOption("num") != null){
                    num = event.getOption("num").getAsString();
                }

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapUser().indexToken(num) )
                        .queue();
            }


            else if (event.getSubcommandName().equals("info")){
                event.deferReply().queue();

                // get string from option
                String uid = event.getOption("uid").getAsString();

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapUser().getUserInfo(uid) )
                        .queue();
            }


            else if (event.getSubcommandName().equals("create")){
                event.deferReply().queue();

                // get string from option
                String uid = event.getOption("uid").getAsString();

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapUser().createUser(uid) )
                        .queue();
            }


            else if (event.getSubcommandName().equals("destroy")){
                event.deferReply().queue();

                // get string from option
                String uid = event.getOption("uid").getAsString();

                // reply
                event.getHook()
                        .sendMessageEmbeds( new TapUser().destroyUser(uid) )
                        .queue();
            }
        }
    }
}
