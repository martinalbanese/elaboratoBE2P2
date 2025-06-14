package it.ttf.elaboratobe2p2.model;

import jakarta.persistence.*;

@Entity
public class Corso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Costruttori
    public Corso() {}

    public Corso(String nome) {
        this.nome = nome;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
