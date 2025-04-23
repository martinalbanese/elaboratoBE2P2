package it.ttf.elaboratobe2p2.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
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

    /**
     * I corsi a cui lo studente è iscritto.
     * La relazione è bidirezionale con la classe Corso.
     */
    @ManyToMany
    @JoinTable(
        name = "studente_corso",
        joinColumns = @JoinColumn(name = "studente_id"),
        inverseJoinColumns = @JoinColumn(name = "corso_id")
    )
    private List<Corso> corsi;
    
}
