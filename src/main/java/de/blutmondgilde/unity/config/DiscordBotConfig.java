package de.blutmondgilde.unity.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;

@Configuration
public class DiscordBotConfig {
    @Value("${discord.bot.token}")
    private String token;

    @Bean
    public <T extends ListenerAdapter> JDA discordBot(List<T> listener) throws LoginException, InterruptedException {
        JDABuilder api = JDABuilder.createDefault(this.token);
        api.enableIntents(GatewayIntent.GUILD_MEMBERS);
        listener.forEach(api::addEventListeners);
        JDA jda = api.build();
        jda.awaitReady();
        return jda;
    }
}
