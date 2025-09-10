INSERT INTO UTENTE (id, email, password) VALUES (1, 'admin@admin.admin', 'admin');
INSERT INTO UTENTE (id, email, password) VALUES (2, 'jbezos@amazon.com', 'jeff');
INSERT INTO UTENTE (id, email, password) VALUES (3, 'emusk@x.com', 'twitter');

INSERT INTO DATI_UTENTE (id, id_utente, username, email, ruolo, stato_utente)
VALUES (1, 1, 'Admin', 'admin@admin.admin', 'ADMIN', 'ATTIVO');

INSERT INTO DATI_UTENTE (id, id_utente, username, email, ruolo, stato_utente)
VALUES (2, 2, 'Jeff', 'jbezos@amazon.com', 'VENDOR', 'ATTIVO');

INSERT INTO DATI_UTENTE (id, id_utente, username, email, ruolo, stato_utente)
VALUES (3, 3, 'Elon', 'emusk@x.com', 'USER', 'ATTIVO');
