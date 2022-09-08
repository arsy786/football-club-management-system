package dev.arsalaan.footballclubmanagementsystem.dto;

import lombok.Data;

@Data
public class PlayerDTO {
    private Long playerId;
    private String name;
    private String position;
    private String nationality;
    private Integer age;
}
