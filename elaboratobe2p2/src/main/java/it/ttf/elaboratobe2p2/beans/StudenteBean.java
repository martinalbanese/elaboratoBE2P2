package it.ttf.elaboratobe2p2.beans;

import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Prova;
import it.ttf.elaboratobe2p2.repositories.StudenteRepository;
import it.ttf.elaboratobe2p2.repositories.CorsoRepository;
import it.ttf.elaboratobe2p2.repositories.ProvaRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Managed Bean per la gestione degli studenti.
 * Gestisce le operazioni CRUD sugli studenti e le funzionalità correlate.
 */
@Component
@ViewScoped
@Data
@NoArgsConstructor
public class StudenteBean implements Serializable {

    @Autowired
    private StudenteRepository studenteRepository;
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    @Autowired
    private ProvaRepository provaRepository;

    // Studente corrente per inserimento/modifica
    private Studente studente = new Studente();
    
    // Lista di tutti gli studenti
    private List<Studente> studenti;
    
    // Lista di tutti i corsi (per dropdown)
    private List<Corso> corsi;
    
    // ID studente selezionato per operazioni
    private Long studenteSelezionatoId;
    
    // ID corso selezionato per registrazione prova
    private Long corsoSelezionatoId;
    
    // Voto per registrazione prova
    private int voto;

    /**
     * Inizializza i dati del bean
     */
    public void init() {
        caricaStudenti();
        caricaCorsi();
    }

    /**
     * Carica la lista di tutti gli studenti
     */
    public void caricaStudenti() {
        try {
            studenti = studenteRepository.findAll();
        } catch (Exception e) {
            addErrorMessage("Errore nel caricamento degli studenti: " + e.getMessage());
        }
    }

    /**
     * Carica la lista di tutti i corsi
     */
    public void caricaCorsi() {
        try {
            corsi = corsoRepository.findAll();
        } catch (Exception e) {
            addErrorMessage("Errore nel caricamento dei corsi: " + e.getMessage());
        }
    }

    /**
     * Salva un nuovo studente o aggiorna uno esistente
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
            
            // Reset del form
            studente = new Studente();
            caricaStudenti();
            
            addInfoMessage("Studente salvato con successo!");
            
        } catch (Exception e) {
            addErrorMessage("Errore nel salvataggio dello studente: " + e.getMessage());
        }
    }

    /**
     * Elimina uno studente
     */
    public void eliminaStudente(Studente studenteDaEliminare) {
        try {
            // Verifica se lo studente ha prove associate
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
     * Prepara la modifica di uno studente
     */
    public void modificaStudente(Studente studenteDaModificare) {
        this.studente = new Studente();
        this.studente.setId(studenteDaModificare.getId());
        this.studente.setNome(studenteDaModificare.getNome());
        this.studente.setCognome(studenteDaModificare.getCognome());
    }

    /**
     * Registra una nuova prova per uno studente
     */
    public void registraProva() {
        try {
            if (studenteSelezionatoId == null) {
                addErrorMessage("Seleziona uno studente");
                return;
            }
            if (corsoSelezionatoId == null) {
                addErrorMessage("Seleziona un corso");
                return;
            }
            if (voto < 0 || voto > 30) {
                addErrorMessage("Il voto deve essere compreso tra 0 e 30");
                return;
            }

            Studente studenteSelezionato = studenteRepository.findById(studenteSelezionatoId)
                    .orElseThrow(() -> new RuntimeException("Studente non trovato"));
            
            Corso corsoSelezionato = corsoRepository.findById(corsoSelezionatoId)
                    .orElseThrow(() -> new RuntimeException("Corso non trovato"));

            // Verifica se esiste già una prova superata per questo studente e corso
            List<Prova> proveEsistenti = provaRepository.findByStudenteIdAndCorsoId(studenteSelezionatoId, corsoSelezionatoId).stream()
                    .filter(Prova::isSuperata)
                    .collect(Collectors.toList());

            if (!proveEsistenti.isEmpty()) {
                addWarningMessage("Lo studente ha già superato questo corso");
                return;
            }

            Prova nuovaProva = new Prova(voto, new java.util.Date(), studenteSelezionato, corsoSelezionato);
            provaRepository.save(nuovaProva);

            // Reset form
            studenteSelezionatoId = null;
            corsoSelezionatoId = null;
            voto = 0;

            addInfoMessage("Prova registrata con successo!");

        } catch (Exception e) {
            addErrorMessage("Errore nella registrazione della prova: " + e.getMessage());
        }
    }

    /**
     * Calcola la media dei voti per uno studente
     */
    public double getMediaVoti(Studente studente) {
        List<Prova> proveSuperateStudente = provaRepository.findByStudenteId(studente.getId()).stream()
                .filter(Prova::isSuperata)
                .collect(Collectors.toList());

        if (proveSuperateStudente.isEmpty()) {
            return 0.0;
        }

        double somma = proveSuperateStudente.stream()
                .mapToInt(Prova::getVoto)
                .sum();

        return somma / proveSuperateStudente.size();
    }

    /**
     * Ottiene i corsi non ancora superati da uno studente
     */
    public List<Corso> getCorsiNonSuperati(Studente studente) {
        List<Long> corsiSuperatiIds = provaRepository.findByStudenteId(studente.getId()).stream()
                .filter(Prova::isSuperata)
                .map(p -> p.getCorso().getId())
                .collect(Collectors.toList());

        return corsi.stream()
                .filter(c -> !corsiSuperatiIds.contains(c.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Conta le prove superate da uno studente
     */
    public long getNumeroProveSuperateStudente(Studente studente) {
        return provaRepository.findByStudenteId(studente.getId()).stream()
                .filter(Prova::isSuperata)
                .count();
    }

    /**
     * Ottiene tutte le prove di uno studente
     */
    public List<Prova> getProveStudente(Studente studente) {
        return provaRepository.findByStudenteIdOrderByDataAsc(studente.getId());
    }

    // Metodi di utilità per i messaggi
    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", message));
    }

    private void addWarningMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Attenzione", message));
    }

    // Reset del form studente
    public void resetForm() {
        studente = new Studente();
    }
}