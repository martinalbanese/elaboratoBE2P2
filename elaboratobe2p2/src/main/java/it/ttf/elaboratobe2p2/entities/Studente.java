package it.ttf.elaboratobe2p2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta uno studente nel sistema.
 * Uno studente può essere iscritto a più corsi e un corso può avere più studenti.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Studente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
}
