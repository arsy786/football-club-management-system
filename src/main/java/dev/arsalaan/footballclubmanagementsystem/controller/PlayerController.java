package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.PlayerDTO;
import dev.arsalaan.footballclubmanagementsystem.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // [GET] View All Players
    @GetMapping("/")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {

        List<PlayerDTO> playersDTO = playerService.getAllPlayers();

        if (playersDTO == null || playersDTO.isEmpty()) {
            return new ResponseEntity<>(playersDTO, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(playersDTO);
    }

    // [GET] View a specific Player by its ID
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable("playerId") Long playerId) {

        PlayerDTO playerDTO = playerService.getPlayerById(playerId);

        if (playerDTO == null) {
            return new ResponseEntity("Player with id " + playerId + " does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<PlayerDTO>(playerDTO, HttpStatus.OK);
    }

    // [POST] Create a Player
    @PostMapping("/")
    public ResponseEntity createPlayer(/*@Valid*/ @RequestBody PlayerDTO playerDTO) {
        playerService.createPlayer(playerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Player by its ID
    @PutMapping("/{playerId}")
    public ResponseEntity updatePlayerById(@PathVariable("playerId") Long playerId, @RequestBody PlayerDTO playerDTO) {
        playerService.updatePlayerById(playerId, playerDTO);
        return ResponseEntity.ok().build();
    }

    // [DELETE] Remove a specific Player by its ID
    @DeleteMapping("/{playerId}")
    public ResponseEntity deletePlayerById(@PathVariable("playerId") Long playerId) {
        playerService.deletePlayerById(playerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}