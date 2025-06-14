package it.ttf.elaboratobe2p2.service;

import it.ttf.elaboratobe2p2.model.Corso;
import it.ttf.elaboratobe2p2.repository.CorsoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorsoService {

    private final CorsoRepository repository;

    public CorsoService(CorsoRepository repository) {
        this.repository = repository;
    }

    public List<Corso> getAll() {
        return repository.findAll();
    }

    public void save(Corso corso) {
        repository.save(corso);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
