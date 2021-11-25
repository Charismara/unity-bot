package de.blutmondgilde.unity.config;

import de.blutmondgilde.unity.service.DiscordBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksConfig {
    private final DiscordBotService discordBotService;

    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron = "0 */5 * * * *") //testing
    public void gatherUserData() {
        log.info("Gathering Discord Server Stats...");
        long start = System.currentTimeMillis();

        discordBotService.gatherTotalUserStats();

        log.info("Finished gathering Discord Server Stats in {}ms", System.currentTimeMillis() - start);
    }
}
