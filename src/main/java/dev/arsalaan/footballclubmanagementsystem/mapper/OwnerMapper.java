package dev.arsalaan.footballclubmanagementsystem.mapper;

import dev.arsalaan.footballclubmanagementsystem.dto.OwnerDTO;
import dev.arsalaan.footballclubmanagementsystem.model.Owner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    OwnerDTO toOwnerDTO (Owner owner);

    List<OwnerDTO> toOwnerDTOs(List<Owner> owners);

    Owner toOwner(OwnerDTO ownerDTO);
    
}
