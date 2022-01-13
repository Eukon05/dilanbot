package com.eukon05.dilanbot.Services;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Repositories.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServerService {

    @Value("${prefix}")
    private String defaultPrefix;

    private final ServerRepository serverRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository){
        this.serverRepository=serverRepository;
    }

    public void addServer(Long id){
        ServerDTO dto = new ServerDTO(id);
        dto.setPrefix(defaultPrefix);
        serverRepository.save(dto);
    }

    public ServerDTO getServerById(Long id){
        Optional<ServerDTO> dtoOptional = serverRepository.findById(id);
        return dtoOptional.orElse(null);
    }

    public void updateServer(ServerDTO serverDTO){
        serverRepository.save(serverDTO);
    }

}
