package it.ttf.elaboratobe2p2.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta una prova d'esame sostenuta da uno studente per un corso.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prova {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int voto;
    private Date data;

    /**
     * Verifica se la prova è stata superata.
     * @return true se il voto è maggiore o uguale a 18, false altrimenti
     */
    public boolean isSuperata() {
        return voto >= 18;
    }

    @ManyToOne 
    private Studente studente;

    @ManyToOne
    private Corso corso;

    /**
     * Costruttore per creare una nuova prova.
     * @param voto Il voto ottenuto (deve essere compreso tra 0 e 30)
     * @param data La data in cui è stata sostenuta la prova
     * @param studente Lo studente che ha sostenuto la prova
     * @param corso Il corso per cui è stata sostenuta la prova
     * @throws IllegalArgumentException se il voto non è compreso tra 0 e 30
     */
    public Prova(int voto, Date data, Studente studente, Corso corso) {
        if (voto < 0 || voto > 30) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 0 e 30");
        }
        this.voto = voto;
        this.data = data;
        this.studente = studente;
        this.corso = corso;
    }
}
