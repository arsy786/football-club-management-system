package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamDTO implements Serializable {
    private Long teamId;
    private String name;
    private String city;
    private String manager;
}
