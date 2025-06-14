package main.java.it.ttf.elaboratobe2p2.beans;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.repositories.CorsoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bean JSF per la gestione dei corsi.
 * Permette di visualizzare, inserire, modificare ed eliminare corsi dal database.
 */
@Named
@Data
@Component
@ViewScoped
@NoArgsConstructor
public class CorsoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private CorsoRepository corsoRepository;

    private Corso corso = new Corso();

    private List<Corso> corsi;

    /**
     * Metodo chiamato dopo la creazione del bean.
     * Carica l’elenco dei corsi.
     */
    @PostConstruct
    public void init() {
        caricaCorsi();
    }

    /**
     * Carica tutti i corsi dal database.
     */
    public void caricaCorsi() {
        try {
            corsi = corsoRepository.findAll();
        } catch (Exception e) {
            addErrorMessage("Errore nel caricamento dei corsi: " + e.getMessage());
        }
    }

    /**
     * Salva un nuovo corso o aggiorna uno esistente.
     */
    public void salvaCorso() {
        try {
            if (corso.getNome() == null || corso.getNome().trim().isEmpty()) {
                addErrorMessage("Il nome del corso è obbligatorio.");
                return;
            }

            corsoRepository.save(corso);
            corso = new Corso(); // reset form
            caricaCorsi();
            addInfoMessage("Corso salvato con successo!");

        } catch (Exception e) {
            addErrorMessage("Errore nel salvataggio del corso: " + e.getMessage());
        }
    }

    /**
     * Elimina un corso selezionato.
     */
    public void eliminaCorso(Corso corsoDaEliminare) {
        try {
            corsoRepository.delete(corsoDaEliminare);
            caricaCorsi();
            addInfoMessage("Corso eliminato con successo!");
        } catch (Exception e) {
            addErrorMessage("Errore nell'eliminazione del corso: " + e.getMessage());
        }
    }

    /**
     * Prepara il corso da modificare.
     */
    public void modificaCorso(Corso corsoDaModificare) {
        this.corso = new Corso();
        this.corso.setId(corsoDaModificare.getId());
        this.corso.setNome(corsoDaModificare.getNome());
    }

    /**
     * Reset del form corso.
     */
    public void resetForm() {
        corso = new Corso();
    }

    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", message));
    }
}
