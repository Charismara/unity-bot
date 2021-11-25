package de.blutmondgilde.unity.service;

import de.blutmondgilde.unity.api.discord.callback.BotJoinedCallback;
import de.blutmondgilde.unity.data.discordapi.Guild;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DiscordEventService extends ListenerAdapter {
    private final ConcurrentHashMap<String, BotJoinedCallback> waitingGuilds = new ConcurrentHashMap<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("Discord Bot successfully initialized");
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //Handle setup stuff

        //Call callback
        String guild = event.getGuild().getId();
        if (waitingGuilds.containsKey(guild)) {
            synchronized (waitingGuilds) {
                waitingGuilds.get(guild).onJoin(guild);
            }
        }
    }

    public void waitForJoin(Guild guild, BotJoinedCallback callback) {
        waitingGuilds.put(guild.getId(), callback);
    }
}
