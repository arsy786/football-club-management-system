package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class LeagueDTO implements Serializable {
    private final Long leagueId;
    private final String name;
    private final String country;
    private Integer numberOfTeams;
    private final List<TeamDTO> teams;

    @Data
    public static class TeamDTO implements Serializable {
        private final Long teamId;
        private final String name;
    }
}
