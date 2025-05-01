package it.ttf.elaboratobe2p2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Prova;
import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.repositories.ProvaRepository;
import org.springframework.stereotype.Service;

/**
 * Servizio per la gestione delle prove d'esame.
 * Fornisce metodi per salvare, recuperare e analizzare le prove degli studenti.
*/
@Service
public class ProvaService {
    @Autowired
    private ProvaRepository provaRepository;

    /**
     * Salva una nuova prova d'esame nel sistema.
     * Controlla che non sia già presente una prova per lo stesso studente e corso.
     * @param studente Lo studente che ha sostenuto la prova
     * @param corso Il corso per cui è stata sostenuta la prova
     * @param voto Il voto ottenuto (deve essere compreso tra 0 e 30)
     * @return La prova salvata
     * @throws IllegalArgumentException se il voto non è compreso tra 0 e 30
    */
    public Prova save(Studente studente, Corso corso, int voto) {
        Date oggi = new Date();
        List<Prova> proveEsistenti = provaRepository.findByStudenteAndCorso(studente, corso);
        for (Prova p : proveEsistenti) {
            if (p.getData().equals(oggi)) {
                throw new IllegalArgumentException("Esiste già una prova oggi per questo corso e studente");
            }
        }
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
     * @param prove Lista delle prove sostenute dallo studente
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

    /**
     * Recupera tutte le prove sostenute da uno studente ordinate per data.
     * @param studente Lo studente di cui recuperare le prove
     * @return Lista delle prove sostenute dallo studente ordinate per data
     */
    public List<Prova> getProveOrdinatePerStudente(Studente studente) {
        return provaRepository.findByStudenteOrderByDataAsc(studente);
    }
}
