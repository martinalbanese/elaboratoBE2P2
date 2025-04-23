package it.ttf.elaboratobe2p2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Prova;
import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.repositories.ProvaRepository;

/**
 * Servizio per la gestione delle prove d'esame.
 * Fornisce metodi per salvare, recuperare e analizzare le prove degli studenti.
*/
public class ProvaService {
    @Autowired
    private ProvaRepository provaRepository;

    /**
     * Salva una nuova prova d'esame nel sistema.
     * @param studente Lo studente che ha sostenuto la prova
     * @param corso Il corso per cui è stata sostenuta la prova
     * @param voto Il voto ottenuto (deve essere compreso tra 0 e 30)
     * @return La prova salvata
     * @throws IllegalArgumentException se il voto non è compreso tra 0 e 30
    */
    public Prova save(Studente studente, Corso corso, int voto) {
        Prova prova = new Prova(voto, new Date(), studente, corso);
        provaRepository.save(prova);
        return prova;
    }

    /**
     * Recupera tutte le prove sostenute da uno studente.
     * @param studente Lo studente di cui recuperare le prove
     * @return Lista delle prove sostenute dallo studente
    */
    public List<Prova> getProveByStudente(Studente studente) {
        return provaRepository.findByStudente(studente);
    }

    /**
     * Calcola la media dei voti di uno studente.
     * @param studente Lo studente di cui calcolare la media
     * @return La media dei voti dello studente
    */
    public double calcolaMedia(List<Prova> prove) {
        if (prove == null || prove.isEmpty()) {
            return 0.0;
        }

        int somma = 0;
        for (Prova p : prove) {
            somma += p.getVoto();
        }

        // calcolo la media dividendo la somma per il numero totale di prove
        return somma / (double) prove.size();
    }

    /**
     * Recupera la lista dei corsi non ancora superati da uno studente.
     * @param studente Lo studente di cui recuperare i corsi non superati
     * @return Lista dei corsi non ancora superati
     */
    public List<Corso> corsiNonSuperati(Studente studente) {
        List<Corso> nonSuperati = new ArrayList<>();
        List<Prova> prove = getProveByStudente(studente);

        for (Prova p : prove) {
            if (!p.isSuperata()) {
                nonSuperati.add(p.getCorso());
            }
        }

        return nonSuperati;
    }
}
