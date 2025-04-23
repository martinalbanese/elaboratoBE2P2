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
 * Rappresenta un corso accademico nel sistema.
 * Un corso può essere seguito da più studenti e uno studente può seguire più corsi.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Corso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    /**
     * Gli studenti iscritti a questo corso.
     * La relazione è bidirezionale con la classe Studente.
     */
    @ManyToMany
    @JoinTable(
        name = "studente_corso",
        joinColumns = @JoinColumn(name = "corso_id"),
        inverseJoinColumns = @JoinColumn(name = "studente_id")
    )
    private List<Studente> studenti;
    
}
