package dev.arsalaan.footballclubmanagementsystem.mapper;

import dev.arsalaan.footballclubmanagementsystem.dto.CupDTO;
import dev.arsalaan.footballclubmanagementsystem.model.Cup;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CupMapper {

    CupDTO toCupDTO (Cup cup);

    List<CupDTO> toCupDTOs(List<Cup> cups);

    Cup toCup(CupDTO cupDTO);

}
