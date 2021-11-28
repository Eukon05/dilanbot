package com.gotardpl.dilanbot.Repositories;

import com.gotardpl.dilanbot.DTOs.ServerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServerRepository extends JpaRepository<ServerDTO, Long> {

}
