package dev.arsalaan.footballclubmanagementsystem.repository;

import dev.arsalaan.footballclubmanagementsystem.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {



}
