package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Unit test
// (cannot involve Spring and the whole request & response lifecycle as then it is no longer a unit test.)

// Requires @WebMvcTest annotation (used for Spring MVC tests. It disables full auto-configuration and instead apply only configuration relevant to MVC tests, such as MockMvc instance).
// Controller is dependent on Service, but we do not need its implementation details as we are interested in what service does in this test, so can mock it using Mockito Annotation @MockBean.
// Need @Autowired MockMvc - this is for server side testing. It allows us to test controller without running a servlet container.

@ExtendWith(SpringExtension.class)
@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @MockBean
    private TeamService teamService;

    @Autowired
    MockMvc mockMvc;

    private List<TeamDTO> teams;

    @BeforeEach
    void setUp() {
        TeamDTO team1 = TeamDTO.builder()
                .teamId(1L)
                .name("Manchester United")
                .city("Manchester")
                .manager("Erik ten Hag")
                .build();

        TeamDTO team2 = TeamDTO.builder()
                .teamId(2L)
                .name("Manchester City")
                .city("Manchester")
                .manager("Pep Guardiola")
                .build();

        TeamDTO team3 = TeamDTO.builder()
                .teamId(3L)
                .name("Real Madrid")
                .city("Madrid")
                .manager("Carlo Ancelotti")
                .build();

        teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
    }

    @Test
    void givenTeamsList_whenGetAllTeams_thenReturnTeamsList() throws Exception {

        // given (arrange)
        Mockito.when(teamService.getAllTeams()).thenReturn(teams);

        // when (act)
        mockMvc.perform(get("/api/v1/team/"))

                // then (assert)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Real Madrid")));
    }

    @Test
    void getTeamById() {
    }

    @Test
    void createTeam() {
    }

    @Test
    void updateTeamById() {
    }

    @Test
    void deleteTeamById() {
    }

    @Test
    void viewAllTeamsForLeague() {
    }

    @Test
    void addTeamToLeague() {
    }

    @Test
    void removeTeamFromLeague() {
    }

}