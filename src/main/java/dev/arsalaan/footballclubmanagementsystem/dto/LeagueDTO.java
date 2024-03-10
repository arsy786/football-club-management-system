package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueDTO implements Serializable {
    private Long leagueId;
    private String name;
    private String country;
    private Integer numberOfTeams;
    private List<TeamDTO> teams;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDTO implements Serializable {
        private Long teamId;
        private String name;
    }
}
