package com.eukon05.dilanbot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVERS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscordServer {

    @Id
    private Long id;

    @Setter
    private String prefix;


}
