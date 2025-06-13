package it.ttf.elaboratobe2p2.beans;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.repositories.CorsoRepository;
import it.ttf.elaboratobe2p2.repositories.StudenteRepository;
import it.ttf.elaboratobe2p2.services.ProvaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;

/**
 * Bean JSF per la gestione della registrazione di nuove prove d'esame.
 * Recupera l'elenco di studenti e corsi dal database e permette di associare
 * a uno studente un nuovo voto per un determinato corso.
 * Mostra un messaggio di conferma all'utente dopo la registrazione.
 */
@Named
@Data
public class ProvaBean {
    private Long studenteId;
    private Long corsoId;
    private int voto;

    List<Studente> studenti;
    List<Corso> corsi;

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private CorsoRepository corsoRepository;

    @Autowired
    private ProvaService provaService;

    /**
     * Metodo eseguito automaticamente dopo la creazione del bean.
     * Recupera dal database tutti gli studenti e i corsi per popolare
     * i menu a cascata nel form.
     */
    @PostConstruct
    public void init() {
        corsi = corsoRepository.findAll();
    }

    public List<Studente> getStudenti() {
        return studenteRepository.findAllByOrderByCognomeAscNomeAsc();
    }

    /**
     * Registra una nuova prova d'esame per lo studente e il corso selezionati.
     * Verifica che esistano entrambi e delega il salvataggio al service.
     * Alla fine mostra un messaggio di conferma e resetta il form.
     */
    public void registraProva() {
        Studente studente = studenteRepository.findById(studenteId).orElseThrow(() ->
                new RuntimeException("Studente non trovato"));

        Corso corso = corsoRepository.findById(corsoId).orElseThrow(() ->
                new RuntimeException("Corso non trovato"));

        String errore = provaService.save(studente, corso, voto);

        FacesContext context = FacesContext.getCurrentInstance();
        if (errore != null) {
            // Se c’è errore, mostra messaggio di errore
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errore, null));
        } else {
            // Se successo, messaggio di conferma
            context.addMessage(null, new FacesMessage("Prova registrata con successo", null));
            studenteId = null;
            corsoId = null;
            voto = 0;
        }
    }
}
