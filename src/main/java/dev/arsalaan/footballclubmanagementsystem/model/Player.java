package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long player_id;
    private String name;
    private String position;
    private String nationality;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "teamId") //fk
    private Team team;

}
