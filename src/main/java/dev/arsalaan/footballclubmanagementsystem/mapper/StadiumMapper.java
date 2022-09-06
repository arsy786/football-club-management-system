package dev.arsalaan.footballclubmanagementsystem.mapper;

import dev.arsalaan.footballclubmanagementsystem.dto.StadiumDTO;
import dev.arsalaan.footballclubmanagementsystem.model.Stadium;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StadiumMapper {
    
    StadiumDTO toStadiumDTO (Stadium stadium);

    List<StadiumDTO> toStadiumDTOs(List<Stadium> stadiums);

    Stadium toStadium(StadiumDTO stadiumDTO);
    
}
