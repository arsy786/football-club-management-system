package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TeamDTO {
    private String name;
    private String city;
    private String manager;
}
