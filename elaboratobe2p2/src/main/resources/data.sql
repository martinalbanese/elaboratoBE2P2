-- Inserisci studenti
INSERT INTO studente (id, nome) VALUES (1, 'Martina');
INSERT INTO studente (id, nome) VALUES (2, 'Giulia');
INSERT INTO studente (id, nome) VALUES (3, 'Andrea');

-- Inserisci corsi
INSERT INTO corso (id, nome) VALUES (1, 'Backend');
INSERT INTO corso (id, nome) VALUES (2, 'Basi di dati');
INSERT INTO corso (id, nome) VALUES (3, 'Frontend');

-- Relazioni studente-corso
INSERT INTO studente_corso (corso_id, studente_id) VALUES (1, 1);
INSERT INTO studente_corso (corso_id, studente_id) VALUES (2, 1);
INSERT INTO studente_corso (corso_id, studente_id) VALUES (3, 1);