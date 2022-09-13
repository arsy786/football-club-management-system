package dev.arsalaan.footballclubmanagementsystem.controller;

import dev.arsalaan.footballclubmanagementsystem.dto.OwnerDTO;
import dev.arsalaan.footballclubmanagementsystem.service.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // [GET] View All Owners
    @GetMapping("/")
    public ResponseEntity<List<OwnerDTO>> getAllOwners() {

        List<OwnerDTO> ownersDTO = ownerService.getAllOwners();

        if (ownersDTO == null || ownersDTO.isEmpty()) {
            return new ResponseEntity<>(ownersDTO, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(ownersDTO);
    }

    // [GET] View a specific Owner by its ID
    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable("ownerId") Long ownerId) {

        OwnerDTO ownerDTO = ownerService.getOwnerById(ownerId);

        if (ownerDTO == null) {
            return new ResponseEntity("Owner with id " + ownerId + " does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<OwnerDTO>(ownerDTO, HttpStatus.OK);
    }

    // [POST] Create a Owner
    @PostMapping("/")
    public ResponseEntity createOwner(/*@Valid*/ @RequestBody OwnerDTO ownerDTO) {
        ownerService.createOwner(ownerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [PUT] Update a specific Owner by its ID
    @PutMapping("/{ownerId}")
    public ResponseEntity updateOwnerById(@PathVariable("ownerId") Long ownerId, @RequestBody OwnerDTO ownerDTO) {
        ownerService.updateOwnerById(ownerId, ownerDTO);
        return ResponseEntity.ok().build();
    }

    // [DELETE] Remove a specific Owner by its ID
    @DeleteMapping("/{ownerId}")
    public ResponseEntity deleteOwnerById(@PathVariable("ownerId") Long ownerId) {
        ownerService.deleteOwnerById(ownerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // [GET] View Owner for Team ID
    @GetMapping("/team/{teamId}")
    public ResponseEntity<OwnerDTO> viewOwnerForTeam(@PathVariable("teamId") Long teamId) {

        OwnerDTO ownerDTO = ownerService.viewOwnerForTeam(teamId);

        if (ownerDTO == null) {
            return new ResponseEntity("Team with id " + teamId + " does not have an Owner", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ownerDTO);
    }

    // [POST] Add an Owner to a specific Team
    @PostMapping("/{ownerId}/team/{teamId}")
    public ResponseEntity addOwnerToTeam(@PathVariable("teamId") Long teamId,
                                           @PathVariable("ownerId") Long ownerId) {
        ownerService.addOwnerToTeam(teamId, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // [DELETE] Remove a specific Owner from a Team
    @DeleteMapping("/{ownerId}/team/{teamId}")
    public ResponseEntity removeOwnerFromTeam(@PathVariable("teamId") Long teamId,
                                                @PathVariable("ownerId") Long ownerId) {
        ownerService.removeOwnerFromTeam(teamId, ownerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
