package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

@Data
public class TeamDTO {
    private Long teamId;
    private String name;
    private String city;
    private String manager;
}
