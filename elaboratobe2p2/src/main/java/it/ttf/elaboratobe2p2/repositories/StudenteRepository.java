package it.ttf.elaboratobe2p2.repositories;

import it.ttf.elaboratobe2p2.entities.Studente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long> {}

