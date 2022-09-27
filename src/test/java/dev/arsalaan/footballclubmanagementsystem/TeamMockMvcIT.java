package dev.arsalaan.footballclubmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamMockMvcIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TeamDTO teamDTO1;
    private TeamDTO teamDTO2;
    private List<TeamDTO> teamsDTO = new ArrayList<>();
    private LeagueDTO leagueDTO1;

    @BeforeEach
    public void setUp() {
        teamDTO1 = TeamDTO.builder().teamId(1L).name("Manchester United").city("Manchester")
                .manager("Erik ten Hag").build();
        teamDTO2 = TeamDTO.builder().teamId(2L).name("Manchester City").city("Manchester")
                .manager("Pep Guardiola").build();
        teamsDTO.add(teamDTO1);
        teamsDTO.add(teamDTO2);
        leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();
    }

    @Test
    public void givenTeams_whenGetAllTeams_thenStatusOkAndBodyCorrect() throws Exception {

        // given
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamsDTO.get(0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamsDTO.get(1)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(get("/api/v1/team/"))

                // then
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
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void givenTeam_whenGetTeamById_thenStatusOkAndBodyCorrect() throws Exception {

        // given
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(get("/api/v1/team/{teamId}", 1L))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Manchester United"));
    }

    @Test
    public void givenNothing_whenGetTeamById_thenStatusBadRequest() throws Exception {

        // given

        // when
        mockMvc.perform(get("/api/v1/team/{teamId}", 1L))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTeam_whenCreateTeam_thenStatusCreated() throws Exception {

        // given
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/v1/team/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO.get(0)));

        // when
        mockMvc.perform(mockRequest)

                // then
                .andExpect(status().isCreated());
    }


    @Test
    public void givenTeamWithSameName_whenCreateTeam_thenStatusBadRequest() throws Exception {

        // given
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        TeamDTO teamSameNameDTO1 = TeamDTO.builder().teamId(1L).name("Manchester United").city("Mcr").manager("EtH").build();

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
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        TeamDTO teamUpdateDTO1 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();

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
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(delete("/api/v1/team/{teamId}", 2L))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueWithTeams_whenViewAllTeamsForLeague_thenStatusOkAndBodyCorrect() throws Exception {

        // given
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

        // when
        mockMvc.perform(get("/api/v1/team/league/{leagueId}", "1"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeague_thenStatusCreated() throws Exception {

        // given
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
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "1", "50"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongTeamId_thenStatusBadRequest() throws Exception {

        // given
        mockMvc.perform(post("/api/v1/team/")
                .content(objectMapper.writeValueAsString(teamDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/v1/league/")
                .content(objectMapper.writeValueAsString(leagueDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // when
        mockMvc.perform(put("/api/v1/team/{teamId}/league/{leagueId}", "50", "1"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeague_thenStatusNoContent() throws Exception {

        // given
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
        mockMvc.perform(delete("/api/v1/team/{teamId}/league/{leagueId}", "1", "50"))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongTeamId_thenStatusBadRequest() throws Exception {

        // given
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
        mockMvc.perform(delete("/api/v1/team/{teamId}/league/{leagueId}", "50", "1"))

                // then
                .andExpect(status().isBadRequest());
    }

}
