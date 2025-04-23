package it.ttf.elaboratobe2p2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Prova;
import it.ttf.elaboratobe2p2.entities.Studente;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvaRepository extends JpaRepository<Prova, Long> {
    List<Prova> findByStudente(Studente studente) ;
    List<Prova> findByCorso(Corso corso);
    List<Prova> findByStudenteAndCorso(Studente studente, Corso corso);
}
