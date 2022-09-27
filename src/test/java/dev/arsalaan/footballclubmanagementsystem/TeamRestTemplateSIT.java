package dev.arsalaan.footballclubmanagementsystem;

import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamRestTemplateSIT {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

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
        leagueDTO1 = LeagueDTO.builder().name("Premier League").country("England")
                .numberOfTeams(20).build();
    }

    @Test
    public void givenTeams_whenGetAllTeams_thenStatusOkAndBodyCorrect() {

        // given
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path, teamDTO1, HttpHeaders.class);
        restTemplate.postForEntity(path, teamDTO2, HttpHeaders.class);

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Manchester United", "Manchester City");
    }

    @Test
    public void givenNothing_whenGetAllTeams_thenStatusNoContent() {

        // given
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenTeam_whenGetTeamById_thenStatusOkAndBodyCorrect() {

        // given
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, HttpHeaders.class);

        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(path2, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Manchester United");
    }


// do not think this is possible given Java Bean validation in DTO class now!
    //    @Test
//    public void givenEmptyTeam_whenGetTeamById_thenStatusNotFound() {
//
//        // given
//        TeamDTO teamDTO = null;
//        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
//        ResponseEntity<HttpHeaders> response1 = restTemplate.postForEntity(path1, null, HttpHeaders.class);
//
//        String path = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
//
//        // when
//        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class, "1");
//
//        // then
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//
//    }

    @Test
    public void givenNothing_whenGetTeamById_thenStatusBadRequest() {

        // given
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void givenTeam_whenCreateTeam_thenStatusCreated() {

        // given
        String path = "http://localhost:"+randomServerPort+"/api/v1/team/";

        // when
        ResponseEntity<HttpHeaders> response = restTemplate.postForEntity(path, teamDTO1, HttpHeaders.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void givenTeamWithSameName_whenCreateTeam_thenStatusBadRequest() {

        // given
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        TeamDTO teamSameNameDTO1 = TeamDTO.builder().teamId(1L).name("Manchester United").city("Mcr").manager("EtH").build();

        // when
        ResponseEntity<ApiRequestException> response = restTemplate.postForEntity(path1, teamSameNameDTO1, ApiRequestException.class);

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenTeam_whenUpdateTeamById_thenStatusOk() {

        // given
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        TeamDTO teamUpdateDTO1 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<TeamDTO> requestEntity = new HttpEntity<>(teamUpdateDTO1);

        // when
        ResponseEntity<String> response = restTemplate
                .exchange(path2, HttpMethod.PUT, requestEntity, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenTeam_whenUpdateTeamByIdWithWrongId_thenStatusBadRequest() {

        // given
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        TeamDTO teamUpdateDTO1 = TeamDTO.builder().name("MUFC").city("MCR").manager("EtH").build();
        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<TeamDTO> requestEntity = new HttpEntity<>(teamUpdateDTO1);

        // when
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(path2, HttpMethod.PUT, requestEntity, ApiRequestException.class, "2");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenTeam_whenDeleteTeamById_thenStatusNoContent() {

        // given
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<?> requestEntity = HttpEntity.EMPTY;

        // when
        ResponseEntity<String> response = restTemplate
                .exchange(path2, HttpMethod.DELETE, requestEntity, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenTeam_whenDeleteTeamByIdWithWrongId_thenStatusBadRequest() {

        // given
        String path1 = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(path1, teamDTO1, String.class);

        String path2 = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}";
        HttpEntity<?> requestEntity = HttpEntity.EMPTY;

        // when
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(path2, HttpMethod.DELETE, requestEntity, ApiRequestException.class, "2");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenLeagueWithTeams_whenViewAllTeamsForLeague_thenStatusOkAndBodyCorrect() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);
        restTemplate.postForEntity(pathCreateTeam, teamDTO2, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, HttpHeaders.class, "1", "1");
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, HttpHeaders.class, "2", "1");

        String pathViewAllTeamsForLeague = "http://localhost:"+randomServerPort+"/api/v1/team/league/{leagueId}";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(pathViewAllTeamsForLeague, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Manchester United", "Manchester City");
    }

    @Test
    public void givenLeagueWithNoTeams_whenViewAllTeamsForLeague_thenStatusNoContent() {

        // given
        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathViewAllTeamsForLeague = "http://localhost:"+randomServerPort+"/api/v1/team/league/{leagueId}";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(pathViewAllTeamsForLeague, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenNothing_whenViewAllTeamsForLeague_thenStatusBadRequest() {

        // given
        String pathViewAllTeamsForLeague = "http://localhost:"+randomServerPort+"/api/v1/team/league/{leagueId}";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(pathViewAllTeamsForLeague, String.class, "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeague_thenStatusCreated() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";

        // when
        ResponseEntity<String> response = restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongLeagueId_thenStatusBadRequest() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";

        // when
        ResponseEntity<String> response = restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "50");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenLeagueAndTeam_whenAddTeamToLeagueWithWrongTeamId_thenStatusBadRequest() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";

        // when
        ResponseEntity<String> response = restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "50", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeague_thenStatusNoContent() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        String pathRemoveTeamFromLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";

        // when
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(pathRemoveTeamFromLeague, HttpMethod.DELETE, HttpEntity.EMPTY, ApiRequestException.class, "1", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongLeagueId_thenStatusBadRequest() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        String pathRemoveTeamFromLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";

        // when
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(pathRemoveTeamFromLeague, HttpMethod.DELETE, HttpEntity.EMPTY, ApiRequestException.class, "1", "50");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenLeagueAndTeam_whenRemoveTeamFromLeagueWithWrongTeamId_thenStatusBadRequest() {

        // given
        String pathCreateTeam = "http://localhost:"+randomServerPort+"/api/v1/team/";
        restTemplate.postForEntity(pathCreateTeam, teamDTO1, HttpHeaders.class);

        String pathCreateLeague = "http://localhost:"+randomServerPort+"/api/v1/league/";
        restTemplate.postForEntity(pathCreateLeague, leagueDTO1, HttpHeaders.class);

        String pathAddTeamToLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";
        restTemplate.exchange(pathAddTeamToLeague, HttpMethod.PUT, HttpEntity.EMPTY, String.class, "1", "1");

        String pathRemoveTeamFromLeague = "http://localhost:"+randomServerPort+"/api/v1/team/{teamId}/league/{leagueId}";

        // when
        ResponseEntity<ApiRequestException> response = restTemplate
                .exchange(pathRemoveTeamFromLeague, HttpMethod.DELETE, HttpEntity.EMPTY, ApiRequestException.class, "50", "1");

        // then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
