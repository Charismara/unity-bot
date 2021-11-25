package de.blutmondgilde.unity.service;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DiscordEventService extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("Discord Bot successfully initialized");
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getGuild().getDefaultChannel().sendMessage("I'm here").queue();
    }
}
