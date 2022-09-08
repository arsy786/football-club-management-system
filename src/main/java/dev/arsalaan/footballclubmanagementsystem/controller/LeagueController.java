package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.LeagueDTO;
import dev.arsalaan.footballclubmanagementsystem.service.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/league")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    // [GET] View All Leagues
    @GetMapping("/")
    public ResponseEntity<List<LeagueDTO>> getAllLeagues() {

        List<LeagueDTO> leaguesDTO = leagueService.getAllLeagues();

        if (leaguesDTO == null || leaguesDTO.isEmpty()) {
            return new ResponseEntity<>(leaguesDTO, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(leaguesDTO);
    }

    // [GET] View a specific League by its ID
    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDTO> getLeagueById(@PathVariable("leagueId") Long leagueId) {

        LeagueDTO leagueDTO = leagueService.getLeagueById(leagueId);

        if (leagueDTO == null) {
            return new ResponseEntity("League with id " + leagueId + " does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<LeagueDTO>(leagueDTO, HttpStatus.OK);
    }

    // [POST] Create a League
    @PostMapping("/")
    public ResponseEntity createLeague(/*@Valid*/ @RequestBody LeagueDTO leagueDTO) {
        leagueService.createLeague(leagueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific League by its ID
    @PutMapping("/{leagueId}")
    public ResponseEntity updateLeagueById(@PathVariable("leagueId") Long leagueId, @RequestBody LeagueDTO leagueDTO) {
        leagueService.updateLeagueById(leagueId, leagueDTO);
        return ResponseEntity.ok().build();
    }

    // [DELETE] Remove a specific League by its ID
    @DeleteMapping("/{leagueId}")
    public ResponseEntity deleteLeagueById(@PathVariable("leagueId") Long leagueId) {
        leagueService.deleteLeagueById(leagueId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
