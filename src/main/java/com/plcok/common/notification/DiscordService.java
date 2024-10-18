package com.plcok.common.notification;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DiscordService implements CommandLineRunner, INotificationService {
    @Value("${discord.token}")
    private String token;

    @Value("${discord.guild_id}")
    private String guildId;

    @Value("${discord.channel_id}")
    private String channelId;

    private JDA jda;

    @Override
    public void run(String...args) {
        jda = JDABuilder.createLight(token).build();
    }

    private TextChannel getChannel() {
        var guild = jda.getGuildById(guildId);
        if (guild == null) throw new RuntimeException("guild not found");

        var channel = guild.getChannelById(TextChannel.class, channelId);
        if (channel == null) throw new RuntimeException("channel not found");

        return channel;
    }

    @Override
    public void report(Exception e) {
        var message = "에러 발생 \n " + e.getMessage() + " \n" + Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));

        getChannel().sendMessage(message.substring(0, 2000)).queue();
    }
}
