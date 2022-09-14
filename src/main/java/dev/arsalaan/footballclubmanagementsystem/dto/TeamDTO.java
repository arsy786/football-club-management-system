package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TeamDTO {

    private Long teamId;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "City should not be null or empty")
    private String city;

    @NotEmpty(message = "Manager should not be null or empty")
    private String manager;
}
