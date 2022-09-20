package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.mapper.TeamMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import dev.arsalaan.footballclubmanagementsystem.repository.TeamRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    TeamService teamService;

    @Mock
    TeamMapper teamMapper;

    @Mock
    TeamRepository teamRepository;

    private List<Team> teams;
    private List<TeamDTO> teamsDTO;

    @BeforeEach
    void setUp() {
        Team team1 = Team.builder()
                .teamId(1L)
                .name("Manchester United")
                .city("Manchester")
                .manager("Erik ten Hag")
                .build();

        Team team2 = Team.builder()
                .teamId(2L)
                .name("Manchester City")
                .city("Manchester")
                .manager("Pep Guardiola")
                .build();

        Team team3 = Team.builder()
                .teamId(3L)
                .name("Real Madrid")
                .city("Madrid")
                .manager("Carlo Ancelotti")
                .build();

        teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);

        TeamDTO teamDTO1 = TeamDTO.builder()
                .teamId(1L)
                .name("Manchester United")
                .city("Manchester")
                .manager("Erik ten Hag")
                .build();

        TeamDTO teamDTO2 = TeamDTO.builder()
                .teamId(2L)
                .name("Manchester City")
                .city("Manchester")
                .manager("Pep Guardiola")
                .build();

        TeamDTO teamDTO3 = TeamDTO.builder()
                .teamId(3L)
                .name("Real Madrid")
                .city("Madrid")
                .manager("Carlo Ancelotti")
                .build();

        teamsDTO = new ArrayList<>();
        teamsDTO.add(teamDTO1);
        teamsDTO.add(teamDTO2);
        teamsDTO.add(teamDTO3);
    }

    @Test
    public void givenTeamsList_whenGetAllTeams_thenReturnTeamsList() {

        // given list of teams

        // when
        when(teamMapper.toTeamDTOs(teams)).thenReturn(teamsDTO);
        when(teamRepository.findAll()).thenReturn(teams);
        List<TeamDTO> teamsList = teamService.getAllTeams();

        // then
        assertEquals(3, teamsList.size());
        assertThat(teamsList.get(2).getName()).isEqualTo(teams.get(2).getName());
        verify(teamRepository, times(1)).findAll();
    }

}
