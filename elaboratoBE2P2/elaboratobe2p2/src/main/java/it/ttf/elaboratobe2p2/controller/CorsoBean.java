package it.ttf.elaboratobe2p2.bean;

import it.ttf.elaboratobe2p2.model.Corso;
import it.ttf.elaboratobe2p2.service.CorsoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CorsoBean implements Serializable {

    private final CorsoService corsoService;

    private List<Corso> corsi;
    private Corso nuovoCorso = new Corso();

    public CorsoBean(CorsoService corsoService) {
        this.corsoService = corsoService;
    }

    @PostConstruct
    public void init() {
        corsi = corsoService.getAll();
    }

    public void salva() {
        corsoService.save(nuovoCorso);
        nuovoCorso = new Corso();  // pulisci il form
        corsi = corsoService.getAll(); // aggiorna lista
    }

    // Getter & Setter
    public List<Corso> getCorsi() {
        return corsi;
    }

    public Corso getNuovoCorso() {
        return nuovoCorso;
    }

    public void setNuovoCorso(Corso nuovoCorso) {
        this.nuovoCorso = nuovoCorso;
    }
}
