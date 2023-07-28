package me.weikuwu.cute.modules.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.time.OffsetDateTime;

public class DiscordRPC {
    private static final long APPLICATION_ID = 1132221372291104849L;
    private static final String stateLine = "aaa";
    private static final String detailsLine = "bbbb";

    public static void start() throws NoDiscordClientException {
        IPCClient client = new IPCClient(APPLICATION_ID);
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setState(stateLine)
                        .setDetails(detailsLine)
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("mainrpcimage", "Cat Mod");//                        .setSmallImage("https://cdn.discordapp.com/attachments/774586121505996801/1132854907628683346/107178319_p0.jpg", "test");//                        .setSmallImage("ptb-small", "Discord PTB")//                        .setParty("party1234", 1, 6)//                        .setMatchSecret("xyzzy")//                        .setJoinSecret("join")//                        .setSpectateSecret("look");
                client.sendRichPresence(builder.build());
            }
        });
        client.connect();
    }
}

