-- =============================================================================
-- Script di creazione database Book Recommender (Lab B)
-- Target DBMS: PostgreSQL
-- =============================================================================

-- 1. Pulizia preliminare (opzionale: rimuove le tabelle se esistono già per ripartire da zero)
DROP TABLE IF EXISTS consigli CASCADE;
DROP TABLE IF EXISTS valutazioni CASCADE;
DROP TABLE IF EXISTS libri_x_librerie CASCADE;
DROP TABLE IF EXISTS librerie CASCADE;
DROP TABLE IF EXISTS utenti CASCADE;
DROP TABLE IF EXISTS libri CASCADE;

-- =============================================================================
-- 2. Creazione Tabella utenti
-- Contiene i dati degli utenti che si registrano all'applicazione.
-- Rif: Specifiche [cite: 106, 113]
-- =============================================================================
CREATE TABLE utenti (
    userid          VARCHAR(50) PRIMARY KEY, -- UserID scelto dall'utente
    nome            VARCHAR(50) NOT NULL,
    cognome         VARCHAR(50) NOT NULL,
    codice_fiscale  CHAR(16) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(256) NOT NULL -- Lunghezza sufficiente per hash (es. SHA-256)
);

-- =============================================================================
-- 3. Creazione Tabella libri
-- Repository dei libri importati dal dataset CSV.
-- Rif: Specifiche [cite: 56-62]
-- =============================================================================
CREATE TABLE libri (
    id                  SERIAL PRIMARY KEY, -- ID autogenerato
    titolo              VARCHAR(500) NOT NULL,
    autori              VARCHAR(500),       -- Lista autori come stringa dal CSV
    anno_pubblicazione  INTEGER,
    editore             VARCHAR(200),       -- Campo opzionale
    categorie           VARCHAR(200)        -- Campo opzionale
);

-- =============================================================================
-- 4. Creazione Tabella librerie
-- Rappresenta le raccolte create dagli utenti.
-- Rif: Specifiche [cite: 122-127]
-- =============================================================================
CREATE TABLE librerie (
    id      SERIAL PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL,
    userid  VARCHAR(50) NOT NULL,

    -- Vincolo: Se un utente viene cancellato, le sue librerie vengono cancellate
    CONSTRAINT fk_utente_libreria
        FOREIGN KEY (userid) REFERENCES utenti(userid)
            ON DELETE CASCADE
);

-- =============================================================================
-- 5. Creazione Tabella composizione_librerie (Relazione Molti-a-Molti)
-- Collega i Libri alle Librerie. Un libro può stare in più librerie.
-- =============================================================================
CREATE TABLE libri_x_librerie (
    libreria_id INTEGER NOT NULL,
    libro_id    INTEGER NOT NULL,

    PRIMARY KEY (libreria_id, libro_id),

    CONSTRAINT fk_libreria
        FOREIGN KEY (libreria_id) REFERENCES librerie(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_libro
        FOREIGN KEY (libro_id) REFERENCES libri(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- 6. Creazione Tabella valutazioni
-- Contiene i voti (1-5) e le note (max 256 char) per ogni criterio.
-- Rif: Specifiche [cite: 135-140]
-- =============================================================================
CREATE TABLE valutazioni (
    id          SERIAL PRIMARY KEY,
    userid      VARCHAR(50) NOT NULL,
    libro_id    INTEGER NOT NULL,

    -- Punteggi (Scala 1-5)
    stile           INTEGER NOT NULL CHECK (stile BETWEEN 1 AND 5),
    contenuto       INTEGER NOT NULL CHECK (contenuto BETWEEN 1 AND 5),
    gradevolezza    INTEGER NOT NULL CHECK (gradevolezza BETWEEN 1 AND 5),
    originalita     INTEGER NOT NULL CHECK (originalita BETWEEN 1 AND 5),
    edizione        INTEGER NOT NULL CHECK (edizione BETWEEN 1 AND 5),
    voto_finale     INTEGER NOT NULL CHECK (voto_finale BETWEEN 1 AND 5), -- Media calcolata

    -- Note opzionali (max 256 caratteri come da specifiche)
    note_stile          VARCHAR(256),
    note_contenuto      VARCHAR(256),
    note_gradevolezza   VARCHAR(256),
    note_originalita    VARCHAR(256),
    note_edizione       VARCHAR(256),

    -- Vincolo: Un utente può valutare lo stesso libro una sola volta
    CONSTRAINT unique_valutazione_utente_libro UNIQUE (userid, libro_id),

    CONSTRAINT fk_valutazione_utente
        FOREIGN KEY (userid) REFERENCES utenti(userid)
            ON DELETE CASCADE,

    CONSTRAINT fk_valutazione_libro
        FOREIGN KEY (libro_id) REFERENCES libri(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- 7. Creazione Tabella consigli
-- Gestisce i suggerimenti: Utente U suggerisce Libro B dato il Libro A.
-- Rif: Specifiche [cite: 148-152]
-- =============================================================================
CREATE TABLE consigli (
    id                      SERIAL PRIMARY KEY,
    userid                  VARCHAR(50) NOT NULL,
    libro_sorgente_id       INTEGER NOT NULL, -- Il libro che si sta visualizzando
    libro_consigliato_id    INTEGER NOT NULL, -- Il libro suggerito

    -- Vincolo: Non si può consigliare lo stesso libro come correlato di se stesso
    CONSTRAINT check_libri_diversi CHECK (libro_sorgente_id <> libro_consigliato_id),

    CONSTRAINT fk_consiglio_utente
        FOREIGN KEY (userid) REFERENCES utenti(userid)
            ON DELETE CASCADE,

    CONSTRAINT fk_libro_sorgente
        FOREIGN KEY (libro_sorgente_id) REFERENCES libri(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_libro_consigliato
        FOREIGN KEY (libro_consigliato_id) REFERENCES libri(id)
            ON DELETE CASCADE
);

-- Note finali:
-- Il vincolo "max 3 libri suggeriti per libro corrente" [cite: 37] è complesso da
-- implementare in SQL standard (richiederebbe Trigger).
-- Si consiglia di gestire questo controllo lato applicazione Java.