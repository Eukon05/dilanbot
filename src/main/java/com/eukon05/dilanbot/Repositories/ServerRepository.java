package com.eukon05.dilanbot.Repositories;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServerRepository extends JpaRepository<ServerDTO, Long> {

}
