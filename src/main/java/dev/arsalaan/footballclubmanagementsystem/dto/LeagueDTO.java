package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

@Data
public class LeagueDTO {
    private Long leagueId;
    private String name;
    private String country;
    private Integer numberOfTeams;
}
