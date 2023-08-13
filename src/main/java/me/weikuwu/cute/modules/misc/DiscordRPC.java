package me.weikuwu.cute.modules.misc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import me.weikuwu.cute.CatMod;

import java.time.OffsetDateTime;

public class DiscordRPC {
    private static final long APPLICATION_ID = 1132221372291104849L;
    private static final String stateLine = "aaa";
    private static final String detailsLine = "bbbb";

    public static void start() {
        try (IPCClient client = new IPCClient(APPLICATION_ID)) {
            client.setListener(new IPCListener() {
                @Override
                public void onReady(IPCClient client) {
                    RichPresence.Builder builder = new RichPresence.Builder();
                    builder.setState(stateLine)
                            .setDetails(detailsLine)
                            .setStartTimestamp(OffsetDateTime.now())
                            .setLargeImage("mainrpcimage", "CatMod");
                    client.sendRichPresence(builder.build());
                }
            });
            client.connect();
        } catch (NoDiscordClientException e) {
            System.out.println("Could not connect to Discord client");
            e.printStackTrace();
        }
    }
}
