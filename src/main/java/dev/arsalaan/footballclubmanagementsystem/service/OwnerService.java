package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.OwnerDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.OwnerMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Owner;
import dev.arsalaan.footballclubmanagementsystem.model.Team;
import dev.arsalaan.footballclubmanagementsystem.repository.OwnerRepository;
import dev.arsalaan.footballclubmanagementsystem.repository.TeamRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final TeamRepository teamRepository;
    private final OwnerMapper ownerMapper;

    public OwnerService(OwnerRepository ownerRepository, TeamRepository teamRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.teamRepository = teamRepository;
        this.ownerMapper = ownerMapper;
    }

    public List<OwnerDTO> getAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        return ownerMapper.toOwnerDTOs(owners);
    }

    public OwnerDTO getOwnerById(Long ownerId) {

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new ApiRequestException("owner with id " + ownerId + " does not exist"));

        return ownerMapper.toOwnerDTO(owner);
    }

    public void createOwner(OwnerDTO ownerDTO) {

        Owner owner = ownerMapper.toOwner(ownerDTO);

        // no owner name check as two owners can have the same name (a name is not unique)

        ownerRepository.save(owner);
    }

    @Transactional
    public void updateOwnerById(Long ownerId, OwnerDTO ownerDTO) {

        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new ApiRequestException("owner with id " + ownerId + " does not exist"));

        if (ownerDTO.getName() != null && ownerDTO.getName().length() > 0 && !Objects.equals(owner.getName(), ownerDTO.getName())) {
            owner.setName(ownerDTO.getName());
        }

        if (ownerDTO.getNetWorth() != null && ownerDTO.getNetWorth().length() > 0 && !Objects.equals(owner.getNetWorth(), ownerDTO.getNetWorth())) {
            owner.setNetWorth(ownerDTO.getNetWorth());
        }

    }

    public void deleteOwnerById(Long ownerId) {

        boolean exists = ownerRepository.existsById(ownerId);

        if (!exists) {
            throw new ApiRequestException("owner with id " + ownerId + " does not exist");
        }

        ownerRepository.deleteById(ownerId);
    }

    public OwnerDTO viewOwnerForTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " does not exist"));

        return ownerMapper.toOwnerDTO(team.getOwner());
    }

    @Transactional
    public void addOwnerToTeam(Long teamId, Long ownerId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " does not exist"));
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new ApiRequestException("Owner with id " + ownerId + " does not exist"));

        if (Objects.nonNull(owner.getTeam())) {
            throw new ApiRequestException("Owner with id " + ownerId + " already assigned to Team with id " + owner.getTeam().getTeamId());
        }

        owner.setTeam(team);
    }

    @Transactional
    public void removeOwnerFromTeam(Long teamId, Long ownerId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ApiRequestException("Team with id " + teamId + " not found"));
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new ApiRequestException("Owner with id " + ownerId + " does not exist"));

        if(!owner.getTeam().getTeamId().equals(team.getTeamId())) {
            throw new ApiRequestException("Owner with id " + ownerId + " is not assigned to Team with id " + teamId);
        }

        if (Objects.isNull(owner.getTeam())) {
            throw new ApiRequestException("Owner with id " + ownerId + " is not assigned to any Team");
        }

        owner.setTeam(null); // sets team field in owner to null instead of removing the parents AND deleting child
    }

}
