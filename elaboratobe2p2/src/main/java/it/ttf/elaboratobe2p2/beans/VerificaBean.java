package it.ttf.elaboratobe2p2.beans;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.repositories.StudenteRepository;
import it.ttf.elaboratobe2p2.services.ProvaService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import lombok.Data;

/**
 * Bean JSF per la visualizzazione dello stato accademico degli studenti.
 * Permette di mostrare la media dei voti e i corsi non ancora superati.
 * I dati vengono usati tipicamente in una pagina JSF con tabella.
 */
@Named
@Data
public class VerificaBean {
    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private ProvaService provaService;

    private List<Studente> studenti;

    /**
     * Metodo eseguito automaticamente dopo la creazione del bean.
     * Recupera la lista completa degli studenti dal database.
     */
    @PostConstruct
    public void init() {

    }

    public List<Studente> getStudenti() {
        return studenteRepository.findAllByOrderByCognomeAscNomeAsc();
    }
    /**
     * Calcola e restituisce la media dei voti per uno specifico studente.
     * @param studente Lo studente di cui calcolare la media
     * @return La media aritmetica dei voti
     */
    public double getMediaVoti(Studente studente) {
        return provaService.calcolaMedia(provaService.getProveByStudenteId(studente.getId()));
    }

    /**
     * Restituisce l'elenco dei corsi che lo studente non ha ancora superato.
     * @param studente Lo studente di cui controllare i corsi
     * @return Lista dei corsi non superati
     */
    public List<Corso> getCorsiNonSuperati(Studente studente) {
        return provaService.corsiNonSuperati(studente);
    }
}