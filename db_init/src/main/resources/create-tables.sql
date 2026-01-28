-- =============================================================================
-- Script di creazione database Book Recommender (Lab B)
-- =============================================================================

-- Pulizia preliminare
DROP TABLE IF EXISTS consigli CASCADE;
DROP TABLE IF EXISTS valutazioni CASCADE;
DROP TABLE IF EXISTS libri_x_librerie CASCADE;
DROP TABLE IF EXISTS librerie CASCADE;
DROP TABLE IF EXISTS utenti CASCADE;
DROP TABLE IF EXISTS libri CASCADE;

DROP TABLE IF EXISTS "ConsigliLibri" CASCADE;
DROP TABLE IF EXISTS "ValutazioniLibri" CASCADE;
DROP TABLE IF EXISTS "LibriXLibrerie" CASCADE;
DROP TABLE IF EXISTS "Librerie" CASCADE;
DROP TABLE IF EXISTS "UtentiRegistrati" CASCADE;
DROP TABLE IF EXISTS "Libri" CASCADE;

-- =============================================================================
-- Tabella UtentiRegistrati
-- Contiene i dati degli utenti che si registrano all'applicazione.
-- =============================================================================
CREATE TABLE "UtentiRegistrati" (
    userid          VARCHAR(50) PRIMARY KEY, -- UserID scelto dall'utente
    nome            VARCHAR(50) NOT NULL,
    cognome         VARCHAR(50) NOT NULL,
    codice_fiscale  CHAR(16) NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(256) NOT NULL
);

-- =============================================================================
-- Tabella Libri
-- Repository dei libri importati dal dataset CSV.
-- =============================================================================
CREATE TABLE "Libri" (
    id                  SERIAL PRIMARY KEY,     -- ID autogenerato
    titolo              VARCHAR(500) NOT NULL,
    autori              VARCHAR(500) NOT NULL,  -- Lista autori come stringa dal CSV
    anno_pubblicazione  INTEGER NOT NULL,
    editore             VARCHAR(200),           -- Campo opzionale
    categorie           VARCHAR(200)            -- Campo opzionale, lista categorie come stringa dal CSV
);

-- =============================================================================
-- Tabella Librerie
-- Rappresenta le raccolte create dagli utenti.
-- =============================================================================
CREATE TABLE "Librerie" (
    id      SERIAL PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL,
    userid  VARCHAR(50) NOT NULL,

    -- Vincolo: Se un utente viene cancellato, le sue librerie vengono cancellate
    CONSTRAINT fk_utente_libreria
        FOREIGN KEY (userid) REFERENCES "UtentiRegistrati"(userid)
            ON DELETE CASCADE,

    -- Vincolo: Un utente non può avere due librerie con lo stesso nome
    CONSTRAINT unique_nome_userid UNIQUE(nome, userid)
);

-- =============================================================================
-- Tabella LibriXLibrerie (Relazione Molti-a-Molti)
-- Collega i Libri alle Librerie. Un libro può stare in più librerie.
-- =============================================================================
CREATE TABLE "LibriXLibrerie" (
    libreria_id INTEGER NOT NULL,
    libro_id    INTEGER NOT NULL,

    PRIMARY KEY (libreria_id, libro_id),

    CONSTRAINT fk_libreria
        FOREIGN KEY (libreria_id) REFERENCES "Librerie"(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_libro
        FOREIGN KEY (libro_id) REFERENCES "Libri"(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- Tabella ValutazioniLibri
-- Contiene i voti (1-5) e le note (max 256 char) per ogni criterio.
-- =============================================================================
CREATE TABLE "ValutazioniLibri" (
    id          SERIAL PRIMARY KEY,
    userid      VARCHAR(50) NOT NULL,
    libro_id    INTEGER NOT NULL,

    -- Punteggi (Scala 1-5)
    stile           INTEGER NOT NULL CHECK (stile BETWEEN 1 AND 5),
    contenuto       INTEGER NOT NULL CHECK (contenuto BETWEEN 1 AND 5),
    gradevolezza    INTEGER NOT NULL CHECK (gradevolezza BETWEEN 1 AND 5),
    originalita     INTEGER NOT NULL CHECK (originalita BETWEEN 1 AND 5),
    edizione        INTEGER NOT NULL CHECK (edizione BETWEEN 1 AND 5),

    -- Note opzionali
    note_stile          VARCHAR(256),
    note_contenuto      VARCHAR(256),
    note_gradevolezza   VARCHAR(256),
    note_originalita    VARCHAR(256),
    note_edizione       VARCHAR(256),
    note_finale         VARCHAR(256),

    -- Vincolo: Un utente può valutare lo stesso libro una sola volta
    CONSTRAINT unique_valutazione_utente_libro UNIQUE (userid, libro_id),

    CONSTRAINT fk_valutazione_utente
        FOREIGN KEY (userid) REFERENCES "UtentiRegistrati"(userid)
            ON DELETE CASCADE,

    CONSTRAINT fk_valutazione_libro
        FOREIGN KEY (libro_id) REFERENCES "Libri"(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- Tabella ConsigliLibri
-- Gestisce i suggerimenti: Utente U suggerisce Libro B dato il Libro A.
-- =============================================================================
CREATE TABLE "ConsigliLibri" (
    id                      SERIAL PRIMARY KEY,
    userid                  VARCHAR(50) NOT NULL,
    libro_sorgente_id       INTEGER NOT NULL, -- Il libro che si sta visualizzando
    libro_consigliato_id    INTEGER NOT NULL, -- Il libro suggerito

    CONSTRAINT unique_consiglio UNIQUE(userid, libro_sorgente_id, libro_consigliato_id),

    -- Questo check non è necessario perché è gestito da un trigger su questa tabella
    --CONSTRAINT check_libri_diversi CHECK (libro_sorgente_id <> libro_consigliato_id),

    CONSTRAINT fk_consiglio_utente
        FOREIGN KEY (userid) REFERENCES "UtentiRegistrati"(userid)
            ON DELETE CASCADE,

    CONSTRAINT fk_libro_sorgente
        FOREIGN KEY (libro_sorgente_id) REFERENCES "Libri"(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_libro_consigliato
        FOREIGN KEY (libro_consigliato_id) REFERENCES "Libri"(id)
            ON DELETE CASCADE
);


-- Vincolo "max 3 libri suggeriti per libro corrente e il libro deve essere in una tua libreria"
CREATE OR REPLACE FUNCTION public.check_consigli_libri()
    RETURNS trigger
    LANGUAGE plpgsql
AS $function$
BEGIN
    IF (
       SELECT COUNT(*) FROM "ConsigliLibri"
       WHERE userid = NEW.userid AND
           libro_sorgente_id = NEW.libro_sorgente_id
    ) >= 3
    -- Questa condizione fa rispettare anche il vincolo "libro_sorgente_id <> libro_consigliato_id"
    OR (
        SELECT COUNT(DISTINCT lxl.libro_id)
        FROM "Librerie" as l JOIN "LibriXLibrerie" AS lxl ON l.id = lxl.libreria_id
        WHERE l.userid = NEW.userid AND
            lxl.libro_id IN (NEW.libro_sorgente_id, NEW.libro_consigliato_id)
    ) < 2 THEN
        RAISE EXCEPTION 'Il check tabella ConsigliLibri ha fallito';
    END IF;
    RETURN NEW;
END;
$function$;

-- Crea il trigger per la funzione definita prima
CREATE TRIGGER check_table
    BEFORE INSERT OR UPDATE ON "ConsigliLibri"
    FOR EACH ROW
EXECUTE FUNCTION public.check_consigli_libri();


-- Funzione di utility che conta quante volte un libro appare nella libreria di un utente
CREATE OR REPLACE FUNCTION public.count_libri_in_librerie(uid character varying, bid integer)
    RETURNS bigint
    LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN (
        SELECT COUNT(lxl.libro_id)
        FROM "Librerie" as l JOIN "LibriXLibrerie" AS lxl ON l.id = lxl.libreria_id
        WHERE l.userid = uid AND
            lxl.libro_id = bid
    );
END;
$function$;


-- Evita di eliminare dalle librerie i libri che sono presenti in valutazioni o consigliati
CREATE OR REPLACE FUNCTION public.check_delete_libri_x_librerie()
    RETURNS trigger
    LANGUAGE plpgsql
AS $function$
    DECLARE uid varchar(50);
BEGIN
    SELECT userid INTO uid
    FROM "Librerie"
    WHERE id = OLD.libreria_id;

    IF count_libri_in_librerie(uid, OLD.libro_id) <= 1
    AND EXISTS(
        SELECT 1 FROM (
            SELECT libro_id AS l, userid AS u FROM "ValutazioniLibri" UNION
            SELECT libro_sorgente_id AS l, userid AS u FROM "ConsigliLibri" UNION
            SELECT libro_consigliato_id AS l, userid AS u FROM "ConsigliLibri"
        ) AS tmp
        WHERE l = OLD.libro_id AND u = uid
    ) THEN
        RAISE EXCEPTION 'Eliminazione non permessa in LibriXLibrerie';
    END IF;

    RETURN OLD;
END;
$function$;

-- Crea il trigger per la funzione definita prima
CREATE TRIGGER check_table
    BEFORE delete ON "LibriXLibrerie"
    FOR EACH ROW
EXECUTE FUNCTION public.check_delete_libri_x_librerie();


-- Vincolo "il libro deve essere in una tua libreria"
CREATE OR REPLACE FUNCTION public.check_valutazioni_libri()
    RETURNS trigger
    LANGUAGE plpgsql
AS $function$
BEGIN
    IF count_libri_in_librerie(NEW.userid, NEW.libro_id) < 1 THEN
        RAISE EXCEPTION 'Il check tabella ValutazioniLibri ha fallito';
    END IF;

    RETURN NEW;
END;
$function$;

-- Crea il trigger per la funzione definita prima
CREATE TRIGGER check_table
    BEFORE INSERT OR UPDATE ON "ValutazioniLibri"
    FOR EACH ROW
EXECUTE FUNCTION public.check_valutazioni_libri();