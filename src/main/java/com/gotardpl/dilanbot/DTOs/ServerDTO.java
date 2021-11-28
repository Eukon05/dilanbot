package com.gotardpl.dilanbot.DTOs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVERS")
@Getter
@Setter
public class ServerDTO {

    public ServerDTO(Long id){
        this.id=id;
    }

    public ServerDTO() {

    }

    @Id
    @Setter(AccessLevel.NONE)
    private Long id;
    private String prefix;


}
