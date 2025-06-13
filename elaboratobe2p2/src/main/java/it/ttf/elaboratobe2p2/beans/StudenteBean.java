package it.ttf.elaboratojsf.beans;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ttf.elaboratojsf.entities.Corso;
import it.ttf.elaboratojsf.entities.Prova;
import it.ttf.elaboratojsf.entities.Studente;
import it.ttf.elaboratojsf.repositories.CorsoRepository;
import it.ttf.elaboratojsf.repositories.ProvaRepository;
import it.ttf.elaboratojsf.repositories.StudenteRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bean JSF per la gestione degli studenti.
 * Permette di visualizzare, inserire, modificare ed eliminare studenti dal database.
 * Inoltre, consente di caricare corsi associabili e gestisce messaggi per l’interfaccia utente.
 */
@Named
@Data
@Component
@ViewScoped
@NoArgsConstructor
public class StudenteBean implements Serializable {
    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private CorsoRepository corsoRepository;

    @Autowired
    private ProvaRepository provaRepository;

    private Studente studente = new Studente();

    private List<Studente> studenti;

    private List<Corso> corsi;

    private Long studenteSelezionatoId;

    private Long corsoSelezionatoId;

    private int voto;

    /**
     * Metodo eseguito dopo la creazione del bean.
     * Carica inizialmente tutti gli studenti e corsi dal database.
     */
    @PostConstruct
    public void init() {
        caricaStudenti();
        caricaCorsi();
    }

    /**
     * Carica l’elenco di tutti gli studenti dal database, ordinati alfabeticamente.
     * Mostra un messaggio di errore in caso di fallimento.
     */
    public void caricaStudenti() {
        try {
            studenti = studenteRepository.findAllByOrderByCognomeAscNomeAsc();
        } catch (Exception e) {
            addErrorMessage("Errore nel caricamento degli studenti: " + e.getMessage());
        }
    }

    /**
     * Carica l’elenco completo dei corsi disponibili.
     * Utile per future associazioni con prove d’esame.
     */
    public void caricaCorsi() {
        try {
            corsi = corsoRepository.findAll();
        } catch (Exception e) {
            addErrorMessage("Errore nel caricamento dei corsi: " + e.getMessage());
        }
    }

    /**
     * Salva uno studente nel database (nuovo o aggiornamento).
     * Controlla che nome e cognome siano presenti.
     * Dopo il salvataggio ricarica la lista e resetta il form.
     */
    public void salvaStudente() {
        try {
            if (studente.getNome() == null || studente.getNome().trim().isEmpty()) {
                addErrorMessage("Il nome è obbligatorio");
                return;
            }
            if (studente.getCognome() == null || studente.getCognome().trim().isEmpty()) {
                addErrorMessage("Il cognome è obbligatorio");
                return;
            }

            studenteRepository.save(studente);

            // Reset del form e ricarica elenco
            studente = new Studente();
            caricaStudenti();

            addInfoMessage("Studente salvato con successo!");

        } catch (Exception e) {
            addErrorMessage("Errore nel salvataggio dello studente: " + e.getMessage());
        }
    }

    /**
     * Elimina uno studente dal database.
     * Prima verifica che non siano presenti prove associate.
     * Se presenti, mostra un messaggio di avviso.
     */
    public void eliminaStudente(Studente studenteDaEliminare) {
        try {
            List<Prova> prove = provaRepository.findByStudenteId(studenteDaEliminare.getId());

            if (!prove.isEmpty()) {
                addWarningMessage("Impossibile eliminare: lo studente ha " + prove.size() + " prove associate");
                return;
            }

            studenteRepository.delete(studenteDaEliminare);
            caricaStudenti();
            addInfoMessage("Studente eliminato con successo!");

        } catch (Exception e) {
            addErrorMessage("Errore nell'eliminazione dello studente: " + e.getMessage());
        }
    }

    /**
     * Prepara il form per modificare uno studente esistente.
     * I dati vengono copiati nel bean `studente`.
     */
    public void modificaStudente(Studente studenteDaModificare) {
        this.studente = new Studente();
        this.studente.setId(studenteDaModificare.getId());
        this.studente.setNome(studenteDaModificare.getNome());
        this.studente.setCognome(studenteDaModificare.getCognome());
    }

    /**
     * Mostra un messaggio informativo all’utente nell’interfaccia JSF.
     */
    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }

    /**
     * Mostra un messaggio di errore all’utente nell’interfaccia JSF.
     */
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", message));
    }

    /**
     * Mostra un messaggio di avviso all’utente nell’interfaccia JSF.
     */
    private void addWarningMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Attenzione", message));
    }

    /**
     * Reset del form studente.
     * Utile dopo un salvataggio o per pulire il form.
     */
    public void resetForm() {
        studente = new Studente();
    }
}
