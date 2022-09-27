package dev.arsalaan.footballclubmanagementsystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.service.LeagueService;
import dev.arsalaan.footballclubmanagementsystem.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Unit test
// (cannot involve Spring and the whole request & response lifecycle as then it is no longer a unit test.)

// Requires @WebMvcTest annotation (used for Spring MVC tests. It disables full auto-configuration and instead apply only configuration relevant to MVC tests, such as MockMvc instance).
// Controller is dependent on Service, but we do not need its implementation details as we are interested in what service does in this test, so can mock it using Mockito Annotation @MockBean.
// Need @Autowired MockMvc - this is for server side testing. It allows us to test controller without running a servlet container.

@ExtendWith(SpringExtension.class)
@WebMvcTest({TeamController.class, LeagueController.class})
class TeamControllerTest {

    @MockBean
    private TeamService teamService;

    @MockBean
    private LeagueService leagueService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TeamDTO teamDTO1;
    private TeamDTO teamDTO2;
    private List<TeamDTO> teamsDTO = new ArrayList<>();
    private LeagueDTO leagueDTO1;

    @BeforeEach
    void setUp() {
        teamDTO1 = TeamDTO.builder().teamId(1L).name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        teamDTO2 = TeamDTO.builder().teamId(2L).name("Manchester City").city("Manchester")
                .manager("Pep Guardiola").build();
        teamsDTO.add(teamDTO1);
        teamsDTO.add(teamDTO2);
        leagueDTO1 = LeagueDTO.builder().leagueId(1L).name("Premier League").country("England")
                .numberOfTeams(20).build();
    }

    @Test
    void givenTeamsList_whenGetAllTeams_thenStatusOkAndBodyCorrect() throws Exception {

        // given (arrange)
        given(teamService.getAllTeams()).willReturn(teamsDTO);

        // when (act)
        mockMvc.perform(get("/api/v1/team/"))

                // then (assert)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("Manchester City")));
    }

    @Test
    public void givenNothing_whenGetAllTeams_thenStatusNoContent() throws Exception {

        // given

        // when
        mockMvc.perform(get("/api/v1/team/"))

                // then (assert)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenTeam_whenGetTeamById_thenStatusOkAndBodyCorrect() throws Exception {

        // given
        given(teamService.getTeamById(1L)).willReturn(teamDTO1);

        // when
        mockMvc.perform(get("/api/v1/team/{teamId}", 1L))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Manchester United"));
    }

    @Test
    public void givenNothing_whenGetTeamById_thenStatusBadRequest() throws Exception {

        // given
        given(teamService.getTeamById(1L)).willThrow(
                new ApiRequestException("team with id " + 1L + " does not exist"));

        // when
        mockMvc.perform(get("/api/v1/team/{teamId}", 1L))

                // then
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenTeam_whenCreateTeam_thenStatusCreated() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);

        // when
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isCreated());
    }

    @Test
    public void givenTeamWithSameName_whenCreateTeam_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        TeamDTO teamSameNameDTO1 = TeamDTO.builder().teamId(1L).name("Manchester United").city("Mcr").manager("EtH").build();

        willThrow(new ApiRequestException("team name " + teamSameNameDTO1.getName() + " already exists"))
                .given(teamService).createTeam(teamSameNameDTO1);

        // when
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamSameNameDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTeam_whenUpdateTeamById_thenStatusOk() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        TeamDTO teamUpdateDTO1 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}", 1L)
                        .content(objectMapper.writeValueAsString(teamUpdateDTO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk());
    }

    @Test
    public void givenTeam_whenUpdateTeamByIdWithWrongId_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        TeamDTO teamUpdateDTO1 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();

        willThrow(new ApiRequestException("team with id " + 2L + " does not exist"))
                .given(teamService).updateTeamById(2L, teamUpdateDTO1);

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}", 2L)
                        .content(objectMapper.writeValueAsString(teamUpdateDTO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTeam_whenDeleteTeamById_thenStatusNoContent() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(delete("/api/v1/team/{teamId}", 1L))

                // then
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenTeam_whenDeleteTeamByIdWithWrongId_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        willThrow(new ApiRequestException("team with id " + 2L + " does not exist"))
                .given(teamService).deleteTeamById(2L);

        // when
        mockMvc.perform(delete("/api/v1/team/{teamId}", 2L))

                // then
                .andExpect(status().isBadRequest());
    }

//    FIX, status 204 (no content) not OK (200) for GET!
    @Test
    public void givenLeagueWithTeams_whenViewAllTeamsForLeague_thenStatusOkAndBodyCorrect() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(teamService).createTeam(teamDTO2);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "1"));
        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "2", "1"));

        given(teamService.viewAllTeamsForLeague(1L)).willReturn(teamsDTO);

        // when
        mockMvc.perform(get("/api/v1/team/league/{leagueId}", "1"))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("Manchester City")));
    }

    @Test
    public void givenLeagueWithNoTeams_whenViewAllTeamsForLeague_thenStatusNoContent() throws Exception {

        // given
        willDoNothing().given(leagueService).createLeague(leagueDTO1);
        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(get("/api/v1/team/league/{leagueId}", "1"))

                // then
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenNothing_whenViewAllTeamsForLeague_thenStatusBadRequest() throws Exception {

        // given
        willThrow(new ApiRequestException("League with id " + 1L + " does not exist"))
                .given(teamService).viewAllTeamsForLeague(1L);

        // when
        mockMvc.perform(get("/api/v1/team/league/{leagueId}", "1"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeague_thenStatusCreated() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "1"))

                // then
                .andExpect(status().isCreated());
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongLeagueId_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        willThrow(new ApiRequestException("League with id " + 50L + " does not exist"))
                .given(teamService).addTeamToLeague(50L, 1L);

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "50"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongTeamId_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        willThrow(new ApiRequestException("Team with id " + 50L + " does not exist"))
                .given(teamService).addTeamToLeague(1L, 50L);

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "50", "1"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeague_thenStatusNoContent() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "1"));

        // when
        mockMvc.perform(delete("/api/v1/team/{teamId}/league/{leagueId}", "1", "1"))

                // then
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongLeagueId_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "1"));

        willThrow(new ApiRequestException("League with id " + 50L + " does not exist"))
                .given(teamService).removeTeamFromLeague(50L, 1L);

        // when
        mockMvc.perform(delete("/api/v1/team/{teamId}/league/{leagueId}", "1", "50"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongTeamId_thenStatusBadRequest() throws Exception {

        // given
        willDoNothing().given(teamService).createTeam(teamDTO1);
        willDoNothing().given(leagueService).createLeague(leagueDTO1);

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "1"));

        willThrow(new ApiRequestException("League with id " + 50L + " does not exist"))
                .given(teamService).removeTeamFromLeague(1L, 50L);

        // when
        mockMvc.perform(delete("/api/v1/team/{teamId}/league/{leagueId}", "50", "1"))

                // then
                .andExpect(status().isBadRequest());
    }

}