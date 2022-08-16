package com.eukon05.dilanbot.service;

import com.eukon05.dilanbot.domain.DiscordServer;
import com.eukon05.dilanbot.repository.DiscordServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscordServerService {
    @Value("${prefix}")
    private String defaultPrefix;

    private final DiscordServerRepository discordServerRepository;

    public void addServer(Long id) {
        DiscordServer dto = new DiscordServer(id, defaultPrefix);
        discordServerRepository.save(dto);
    }

    public DiscordServer getServerById(Long id) {
        Optional<DiscordServer> dtoOptional = discordServerRepository.findById(id);
        return dtoOptional.orElse(null);
    }

    public void updateServer(DiscordServer discordServer) {
        discordServerRepository.save(discordServer);
    }
}
