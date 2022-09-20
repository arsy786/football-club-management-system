package dev.arsalaan.footballclubmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arsalaan.footballclubmanagementsystem.controller.TeamController;
import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    private List<TeamDTO> teamsDTO;

    @BeforeEach
    public void setUp() {

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
    public void givenTeams_whenGetAllTeams_thenSuccess() throws Exception {

        // given

        MockHttpServletRequestBuilder mockRequest1 = MockMvcRequestBuilders
                .post("/api/v1/team/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO.get(0)));
        mockMvc.perform(mockRequest1);

        MockHttpServletRequestBuilder mockRequest2 = MockMvcRequestBuilders
                .post("/api/v1/team/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO.get(1)));
        mockMvc.perform(mockRequest2);

        MockHttpServletRequestBuilder mockRequest3 = MockMvcRequestBuilders
                .post("/api/v1/team/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamsDTO.get(2)));
        mockMvc.perform(mockRequest3);

        // when
        mockMvc.perform(get("/api/v1/team/"))

                // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Real Madrid")));

    }

    @Test
    public void givenTeam_whenCreateTeam_thenIsCreated() throws Exception {

        // given
//        teamService.createTeam(teamsDTO.get(0));
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


}
