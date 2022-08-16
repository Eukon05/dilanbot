package com.eukon05.dilanbot.repository;

import com.eukon05.dilanbot.domain.DiscordServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiscordServerRepository extends JpaRepository<DiscordServer, Long> {
}
