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

    @PostConstruct
    public void init() {
        studenti = studenteRepository.findAll();
        corsi = corsoRepository.findAll();
    }

    public void registraProva() {
        Studente studente = studenteRepository.findById(studenteId).orElseThrow(() ->
                new RuntimeException("Studente non trovato"));

        Corso corso = corsoRepository.findById(corsoId).orElseThrow(() ->
                new RuntimeException("Corso non trovato"));

        provaService.save(studente, corso, voto);

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Prova registrata con successo"));
    }
}
