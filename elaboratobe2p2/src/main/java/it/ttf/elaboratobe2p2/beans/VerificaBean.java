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

@Named
@Data
public class VerificaBean {
    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private ProvaService provaService;

    private List<Studente> studenti;

    @PostConstruct
    public void init() {
        studenti = studenteRepository.findAll();
    }

    public double getMediaVoti(Studente studente) {
        return provaService.calcolaMedia(provaService.getProveByStudenteId(studente.getId()));
    }

    public List<Corso> getCorsiNonSuperati(Studente studente) {
        return provaService.corsiNonSuperati(studente);
    }
}
