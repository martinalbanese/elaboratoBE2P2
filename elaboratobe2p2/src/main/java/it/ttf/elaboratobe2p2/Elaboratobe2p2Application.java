package it.ttf.elaboratobe2p2;

import java.util.ArrayList;
import java.util.List;

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

	public static void main(String[] args) {
		SpringApplication.run(Elaboratobe2p2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Mock Studenti
		Studente s1 = new Studente();
		s1.setId(1L);
		s1.setNome("Martina");
		s1.setCorsi(new ArrayList<>());

		Studente s2 = new Studente();
		s2.setId(2L);
		s2.setNome("Giulia");
		s2.setCorsi(new ArrayList<>());

		Studente s3 = new Studente();
		s3.setId(3L);
		s3.setNome("Andrea");
		s3.setCorsi(new ArrayList<>());

		// Mock corsi
		Corso c1 = new Corso();
		c1.setId(1L);
		c1.setNome("Backend");
		c1.setStudenti(new ArrayList<>());

		Corso c2 = new Corso();
		c2.setId(2L);
		c2.setNome("Basi di dati");
		c2.setStudenti(new ArrayList<>());

		Corso c3 = new Corso();
		c3.setId(3L);
		c3.setNome("Frontend");
		c3.setStudenti(new ArrayList<>());

		// Inizializzazione relazioni studenti-corsi
		s1.getCorsi().add(c1);
		s1.getCorsi().add(c2);
		s1.getCorsi().add(c3);
		
		c1.getStudenti().add(s1);
		c2.getStudenti().add(s1);
		c3.getStudenti().add(s1);

		// Creazione e salvataggio delle prove
		provaService.save(s1, c1, 28);
		provaService.save(s1, c2, 15); // non superato
		provaService.save(s1, c3, 10); // non superato

		// Recupero delle prove di Martina
		List<Prova> proveMartina = provaService.getProveByStudente(s1);

		// Metodo: calcolo media
		double media = provaService.calcolaMedia(proveMartina);
		System.out.println("Media voti Martina: " + media);

		// Metodo: corsi non superati
		List<Corso> nonSuperati = provaService.corsiNonSuperati(s1);
		System.out.println("Corsi non superati:");
		for (Corso corso : nonSuperati) {
			System.out.println("- " + corso.getNome());
		}
	}
}
