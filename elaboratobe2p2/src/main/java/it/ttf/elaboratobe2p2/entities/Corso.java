package it.ttf.elaboratobe2p2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String sigla;
}


/*
 * package it.ttf.elaboratobe2p2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Corso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // eventuali attributi aggiuntivi (es: descrizione, CFU, docente, ecc.)
}

 */