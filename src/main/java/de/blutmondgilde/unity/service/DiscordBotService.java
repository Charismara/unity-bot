package de.blutmondgilde.unity.service;

import de.blutmondgilde.unity.api.discord.callback.BotJoinedCallback;
import de.blutmondgilde.unity.data.discordapi.Guild;
import de.blutmondgilde.unity.data.jpa.GuildSettings;
import de.blutmondgilde.unity.data.jpa.GuildSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordBotService extends ListenerAdapter {
    private final ConcurrentHashMap<String, BotJoinedCallback> waitingGuilds = new ConcurrentHashMap<>();
    private Optional<JDA> discordBot = Optional.empty();
    private final GuildSettingsRepository guildSettingsRepository;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.discordBot = Optional.of(event.getJDA());
        log.info("Discord Bot successfully initialized");
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        String guild = event.getGuild().getId();

        //Handle setup stuff
        GuildSettings guildSettings = guildSettingsRepository.findById(guild).orElseGet(() -> {
            GuildSettings settings = new GuildSettings();
            settings.setId(guild);
            settings.setOwner(event.getGuild().getOwnerIdLong());
            settings.setAllowedRoles(List.of());
            return settings;
        });

        //Update or create data
        guildSettingsRepository.save(guildSettings);

        //Call callback
        if (waitingGuilds.containsKey(guild)) {
            synchronized (waitingGuilds) {
                waitingGuilds.get(guild).onJoin(guild);
            }
        }
    }

    public void waitForJoin(Guild guild, BotJoinedCallback callback) {
        waitingGuilds.put(guild.getId(), callback);
    }

    public boolean isInGuild(Guild guild) {
        if (this.discordBot.isEmpty()) return false;
        return this.discordBot.get().getGuildById(guild.getId()) != null;
    }

    public Optional<net.dv8tion.jda.api.entities.Guild> getGuild(String id) {
        if (this.discordBot.isEmpty()) return Optional.empty();
        return Optional.ofNullable(this.discordBot.get().getGuildById(id));
    }

    public Optional<Member> findUser(net.dv8tion.jda.api.entities.Guild guild, String userName, String discriminator) {
        return Optional.ofNullable(guild.getMemberByTag(userName, discriminator));
    }
}
