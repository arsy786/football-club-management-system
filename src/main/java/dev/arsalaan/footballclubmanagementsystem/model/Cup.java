package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cupId;
    private String name;
    private Integer numberOfTeams;

    @ManyToMany
    @JoinTable(
            name = "team_cup_map",
            joinColumns = @JoinColumn(name = "cup_id", referencedColumnName = "cupId"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "teamId")
    ) // both fk's for intermediate table
    private List<Team> teams;

}
