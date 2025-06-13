package it.ttf.elaboratobe2p2;

import java.util.List;

import it.ttf.elaboratobe2p2.repositories.CorsoRepository;
import it.ttf.elaboratobe2p2.repositories.StudenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Prova;
import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.services.ProvaService;

@SpringBootApplication
@Slf4j
public class Elaboratobe2p2Application implements CommandLineRunner {

	@Autowired
	private ProvaService provaService;

	@Autowired
	private StudenteRepository studenteRepository;

	public static void main(String[] args) {
		SpringApplication.run(Elaboratobe2p2Application.class, args);
	}

	public void run(String... args) throws Exception {
		Studente studente = studenteRepository.findById(1L).orElseThrow();
        log.info("Studente trovato: {} {}", studente.getNome(), studente.getCognome());

		List<Prova> proveStudente = provaService.getProveByStudenteId(studente.getId());

		double media = provaService.calcolaMedia(proveStudente);
        log.info("Media voti Martina: {}", media);

		List<Corso> nonSuperati = provaService.corsiNonSuperati(studente);
		log.info("Corsi non superati:");
		for (Corso corso : nonSuperati) {
            log.info("- {}", corso.getNome());
		}
	}
}

