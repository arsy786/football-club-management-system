package dev.arsalaan.footballclubmanagementsystem.repository;

import dev.arsalaan.footballclubmanagementsystem.model.Cup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CupRepository extends JpaRepository<Cup, Long> {

    Optional<Cup> findCupByName(String name);

}
