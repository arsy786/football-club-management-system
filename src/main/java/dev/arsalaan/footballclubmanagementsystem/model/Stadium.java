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
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stadiumId;
    private String name;
    private Integer capacity;

    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "teamId") //fk
    private Team team;

}
