package dev.arsalaan.footballclubmanagementsystem.repository;

import dev.arsalaan.footballclubmanagementsystem.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {



}
