package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.TeamDTO;
import dev.arsalaan.footballclubmanagementsystem.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // [GET] View All Teams
    @GetMapping("/")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {

        List<TeamDTO> teamsDTO = teamService.getAllTeams();

        if (teamsDTO == null || teamsDTO.isEmpty()) {
            return new ResponseEntity<>(teamsDTO, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(teamsDTO);
    }

    // [GET] View a specific Team by its ID
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable("teamId") Long teamId) {

        TeamDTO teamDTO = teamService.getTeamById(teamId);

        if (teamDTO == null) {
            return new ResponseEntity("Team with id " + teamId + " does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<TeamDTO>(teamDTO, HttpStatus.OK);
    }

    // [POST] Create a Team
    @PostMapping("/")
    public ResponseEntity createTeam(/*@Valid*/ @RequestBody TeamDTO teamDTO) {
        teamService.createTeam(teamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Team by its ID
    @PutMapping("/{teamId}")
    public ResponseEntity updateTeamById(@PathVariable("teamId") Long teamId, @RequestBody TeamDTO teamDTO) {
        teamService.updateTeamById(teamId, teamDTO);
        return ResponseEntity.ok().build();
    }


    // [DELETE] Remove a specific Team by its ID
    @DeleteMapping("/{teamId}")
    public ResponseEntity deleteTeamById(@PathVariable("teamId") Long teamId) {
        teamService.deleteTeamById(teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
