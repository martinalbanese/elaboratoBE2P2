package it.ttf.elaboratobe2p2.services;

import java.util.*;

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

    /*
     * Metodo privato per confrontare solo la parte "data" (giorno, mese, anno) di due date
     * @param d1 Prima data da confrontare
     * @param d2 Seconda data da confrontare
     * @return true se le due date sono nella stessa giornata, false altrimenti
     */
    private boolean stessaData(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Salva una nuova prova d'esame nel sistema.
     * Controlla che non sia già presente una prova per lo stesso studente e corso.
     * @param studente Lo studente che ha sostenuto la prova
     * @param corso Il corso per cui è stata sostenuta la prova
     * @param voto Il voto ottenuto (deve essere compreso tra 0 e 30)
     * @throws IllegalArgumentException se il voto non è compreso tra 0 e 30
    */
    public String save(Studente studente, Corso corso, int voto) {
        if (voto < 0 || voto > 31) {
            return "Il voto deve essere compreso tra 0 e 31";
        }

        List<Prova> proveEsistenti = provaRepository.findByStudenteIdAndCorsoId(studente.getId(), corso.getId());
        boolean giaSuperata = proveEsistenti.stream().anyMatch(Prova::isSuperata);

        if (giaSuperata) {
            return "Prova già superata per questo corso";
        }

        Prova prova = new Prova(voto, new Date(), studente, corso);
        provaRepository.save(prova);
        return null;
    }

    /**
     * Recupera tutte le prove sostenute da uno studente.
     * @param studenteId Lo studente di cui recuperare le prove
     * @return Lista delle prove sostenute dallo studente
    */
    public List<Prova> getProveByStudenteId(Long studenteId) {
        return provaRepository.findByStudenteId(studenteId);
    }

    /**
     * Calcola la media dei voti di uno studente.
     * @param prove Lista delle prove sostenute dallo studente
     * @return La media dei voti dello studente
    */
    public double calcolaMedia(List<Prova> prove) {
        if (prove == null || prove.isEmpty()) {
            return 0;
        }

        double somma = 0;
        for (Prova p : prove) {
            somma += p.getVoto();
        }

        // calcolo la media dividendo la somma per il numero totale di prove
        return somma / prove.size();
    }

    public List<Prova> getAllProve() {
        return provaRepository.findAll();
    }

    /**
     * Recupera la lista dei corsi non ancora superati da uno studente.
     * @param studente Lo studente di cui recuperare i corsi non superati
     * @return Lista dei corsi non ancora superati
     */
    public List<Corso> corsiNonSuperati(Studente studente) {
        // Recupera tutte le prove dello studente, ordinate per data crescente
        List<Prova> prove = provaRepository.findByStudenteIdOrderByDataAsc(studente.getId());

        // Mappa che tiene solo l'ultima prova per ogni corso (id corso -> prova)
        Map<Long, Prova> ultimaProvaPerCorso = new HashMap<>();

        for (Prova p: prove) {
            Long corsoId = p.getCorso().getId();
            // Se non esiste ancora una prova per questo corso o se la prova corrente è più
            // recente, aggiorna
            if (!ultimaProvaPerCorso.containsKey(corsoId)
                    || p.getData().after(ultimaProvaPerCorso.get(corsoId).getData())) {
                ultimaProvaPerCorso.put(corsoId, p);
            }
        }
        List<Corso> nonSuperati = new ArrayList<>();

        // Verifica se l'ultima prova per ogni corso è superata
        for (Prova ultima : ultimaProvaPerCorso.values()) {
            if (!ultima.isSuperata()) {
                nonSuperati.add(ultima.getCorso());
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
        return provaRepository.findByStudenteIdOrderByDataAsc(studente.getId());
    }
}
