package dev.arsalaan.footballclubmanagementsystem.service;

import dev.arsalaan.footballclubmanagementsystem.dto.OwnerDTO;
import dev.arsalaan.footballclubmanagementsystem.exception.ApiRequestException;
import dev.arsalaan.footballclubmanagementsystem.mapper.OwnerMapper;
import dev.arsalaan.footballclubmanagementsystem.model.Owner;
import dev.arsalaan.footballclubmanagementsystem.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
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

}
