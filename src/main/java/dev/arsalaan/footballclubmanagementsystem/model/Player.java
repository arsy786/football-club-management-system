package dev.arsalaan.footballclubmanagementsystem.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;
    private String name;
    private String position;
    private String nationality;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "teamId") //fk
    private Team team;

}
