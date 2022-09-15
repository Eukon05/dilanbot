package com.eukon05.dilanbot.service;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.repository.DiscordServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordServerService {
    @Value("${prefix}")
    private String defaultPrefix;

    private final DiscordServerRepository discordServerRepository;

    public void addServer(Long id) {
        discordServerRepository.save(new DiscordServer(id, defaultPrefix));
    }

    public DiscordServer getServerById(Long id) {
        return discordServerRepository.findById(id).orElse(null);
    }

    public void updateServer(DiscordServer discordServer) {
        discordServerRepository.save(discordServer);
    }
}
