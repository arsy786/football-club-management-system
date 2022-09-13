package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeagueDTO {
    private Long leagueId;
    private String name;
    private String country;
    private Integer numberOfTeams;

    private List<TeamDTO> teams;
}
