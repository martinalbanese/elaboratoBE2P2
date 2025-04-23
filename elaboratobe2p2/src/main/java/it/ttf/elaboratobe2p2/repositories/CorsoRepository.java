package it.ttf.elaboratobe2p2.repositories;

import it.ttf.elaboratobe2p2.entities.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorsoRepository extends JpaRepository<Corso, Long> {}