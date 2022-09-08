package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

@Data
public class CupDTO {
    private Long cupId;
    private String name;
    private Integer numberOfTeams;
}
