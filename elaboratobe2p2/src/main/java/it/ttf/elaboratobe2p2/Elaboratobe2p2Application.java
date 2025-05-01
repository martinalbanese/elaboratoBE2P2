package it.ttf.elaboratobe2p2;

import java.util.List;

import it.ttf.elaboratobe2p2.repositories.CorsoRepository;
import it.ttf.elaboratobe2p2.repositories.StudenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.ttf.elaboratobe2p2.entities.Corso;
import it.ttf.elaboratobe2p2.entities.Prova;
import it.ttf.elaboratobe2p2.entities.Studente;
import it.ttf.elaboratobe2p2.services.ProvaService;

@SpringBootApplication
public class Elaboratobe2p2Application implements CommandLineRunner {

	@Autowired
	private ProvaService provaService;

	@Autowired
	private StudenteRepository studenteRepository;

	public static void main(String[] args) {
		SpringApplication.run(Elaboratobe2p2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Recupero dallo script SQL
		Studente s1 = studenteRepository.findById(1L).orElseThrow();
		//Corso c1 = corsoRepository.findById(1L).orElseThrow(); // Backend
		//Corso c2 = corsoRepository.findById(2L).orElseThrow(); // Basi di dati
		//Corso c3 = corsoRepository.findById(3L).orElseThrow(); // Frontend

		// Salvataggio prove
		//provaService.save(s1, c1, 28); // superato
		//provaService.save(s1, c2, 15); // non superato
		//provaService.save(s1, c3, 10); // non superato

		// Recupero prove
		List<Prova> proveMartina = provaService.getProveByStudente(s1);

		// Media
		double media = provaService.calcolaMedia(proveMartina);
		System.out.println("Media voti Martina: " + media);

		// Corsi non superati
		List<Corso> nonSuperati = provaService.corsiNonSuperati(s1);
		System.out.println("Corsi non superati:");
		for (Corso corso : nonSuperati) {
			System.out.println("- " + corso.getNome());
		}
	}
}

