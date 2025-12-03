# Schemi

### Schema ER

### Entità e Attributi

Ecco le entità richieste con i relativi campi obbligatori e opzionali derivati dalle specifiche:

- **Libri (Repository)**
    - Questa entità rappresenta il catalogo globale dei libri.
    - **Attributi:**
        - `ID` (Identificativo univoco, es. ISBN o ID autogenerato)
        - `Titolo`
        - `Autori`
        - `AnnoPubblicazione`
        - `Editore` (Opzionale)
        - `Categoria` (Opzionale)
- **UtentiRegistrati**
    - Rappresenta gli utenti che hanno effettuato l'iscrizione.
    - **Attributi:**
        - `UserID` (Chiave primaria/univoca per il login)
        - `Nome`
        - `Cognome`
        - `CodiceFiscale`
        - `Email`
        - `Password`
- **Librerie**
    - Rappresenta le raccolte personali create dagli utenti.
    - **Attributi:**
        - `ID` (Identificativo univoco della libreria)
        - `Nome` (Il nome dato dall'utente alla libreria)
- **ValutazioniLibri**
    - Contiene i voti e i commenti assegnati dagli utenti ai libri. Le specifiche richiedono criteri molto dettagliati.
    - **Attributi:**
        - `ID` (Identificativo della valutazione)
        - `Stile` (1-5) e `NoteStile` (max 256 char)
        - `Contenuto` (1-5) e `NoteContenuto` (max 256 char)
        - `Gradevolezza` (1-5) e `NoteGradevolezza` (max 256 char)
        - `Originalita` (1-5) e `NoteOriginalita` (max 256 char)
        - `Edizione` (1-5) e `NoteEdizione` (max 256 char)
        - `VotoFinale` (Media calcolata, 1-5)
- **ConsigliLibri**
    - Rappresenta i suggerimenti di libri correlati inseriti dagli utenti.
    - **Attributi:**
        - `ID` (Identificativo del consiglio)

---

### Relazioni (Associazioni)

Qui definiamo come le entità interagiscono tra loro:

1. **Utente - Libreria (1:N):**
    - Un `Utente` può creare molte `Librerie`
    - Una `Libreria` appartiene a un solo `Utente`
2. **Libreria - Libro (M:N):**
    - Una `Libreria` contiene un elenco di `Libri`
    - Un `Libro` può essere presente in molte `Librerie` diverse
    - *Nota di progettazione:* Questa relazione molti-a-molti richiederà una tabella di associazione nello schema logico (es. `ComposizioneLibreria`)
3. **Utente - Valutazione (1:N) e Libro - Valutazione (1:N):**
    - Un `Utente` inserisce una `Valutazione` per un `Libro`
    - La valutazione è legata indissolubilmente sia all'utente che al libro valutato.
    - *Vincolo:* L'utente può valutare solo libri presenti nelle proprie librerie
4. **Consiglio (Associazione Ricorsiva sui Libri):**
    - Un `Utente` suggerisce dei libri (fino a max 3) correlati a un libro corrente
    - Questa relazione collega un `Libro` (sorgente) a un altro `Libro` (suggerito), mediata dall'azione di un Utente

---

### Visualizzazione Concettuale

Per aiutarti a visualizzare la struttura, immagina il diagramma in questo modo:

- Al centro c'è l'entità **Libri** (il catalogo).
- **UtentiRegistrati** è collegata a **Librerie** (l'utente possiede la libreria).
- **Librerie** è collegata a **Libri** (la libreria contiene i libri).
- **ValutazioniLibri** è un'entità "debole" o associativa collegata sia a **UtentiRegistrati** che a **Libri**.
- **ConsigliLibri** collega **UtentiRegistrati** e fa da ponte tra due istanze di **Libri** (Libro A -> suggerisce -> Libro B).

### Schema Logico

### 1. Tabella `Libri`

Contiene il repository dei libri importati dal dataset.

- **`id`** (SERIAL, **PK**): Identificativo univoco autogenerato del libro
- **`titolo`** (VARCHAR): Titolo del libro
- **`autori`** (VARCHAR): Elenco degli autori (stringa, come da CSV originale)
- **`anno_pubblicazione`** (INTEGER): Anno di pubblicazione
- **`editore`** (VARCHAR): Nome dell'editore (opzionale)
- **`categoria`** (VARCHAR): Categoria del libro (opzionale)

### 2. Tabella `UtentiRegistrati`

Memorizza le informazioni degli utenti iscritti al servizio

- **`userid`** (VARCHAR, **PK**): Identificativo univoco scelto dall'utente per il login
- **`nome`** (VARCHAR): Nome dell'utente
- **`cognome`** (VARCHAR): Cognome dell'utente
- **`codice_fiscale`** (CHAR(16)): Codice fiscale
- **`email`** (VARCHAR): Indirizzo email
- **`password`** (VARCHAR): Password per l'accesso (suggerimento: da salvare come hash)

### 3. Tabella `Librerie`

Definisce le raccolte create dagli utenti

- **`id`** (SERIAL, **PK**): Identificativo univoco della libreria.
- **`nome`** (VARCHAR): Nome assegnato dall'utente alla libreria
- **`userid`** (VARCHAR, **FK**): Riferimento alla tabella `UtentiRegistrati`.

### 4. Tabella `ComposizioneLibrerie` (Tabella di Associazione)

*Nota:* Questa tabella non è esplicitamente nominata nelle specifiche, ma è **necessaria** nello schema relazionale per gestire la relazione "Molti a Molti" tra le Librerie e i Libri (una libreria contiene molti libri, un libro può stare in molte librerie)16.

- **`libreria_id`** (INTEGER, **FK**): Riferimento alla tabella `Librerie`.
- **`libro_id`** (INTEGER, **FK**): Riferimento alla tabella `Libri`.
- **PK composta:** (`libreria_id`, `libro_id`).

### 5. Tabella `ValutazioniLibri`

Contiene i voti dettagliati e le note per ogni criterio

- **`id`** (SERIAL, **PK**): Identificativo della singola scheda di valutazione.
- **`userid`** (VARCHAR, **FK**): L'utente che ha inserito la valutazione.
- **`libro_id`** (INTEGER, **FK**): Il libro valutato.
- **`stile`** (INTEGER): Punteggio 1-5
- **`note_stile`** (VARCHAR(256)): Commento opzionale
- **`contenuto`** (INTEGER): Punteggio 1-5.
- **`note_contenuto`** (VARCHAR(256)): Commento opzionale.
- **`gradevolezza`** (INTEGER): Punteggio 1-5.
- **`note_gradevolezza`** (VARCHAR(256)): Commento opzionale.
- **`originalita`** (INTEGER): Punteggio 1-5.
- **`note_originalita`** (VARCHAR(256)): Commento opzionale.
- **`edizione`** (INTEGER): Punteggio 1-5.
- **`note_edizione`** (VARCHAR(256)): Commento opzionale.
- **`voto_finale`** (INTEGER): Media arrotondata calcolata
- *Vincolo Unicità:* Un utente non dovrebbe poter inserire più di una scheda di valutazione per lo stesso libro (Unique constraint su `userid`, `libro_id`).

### 6. Tabella `ConsigliLibri`

Memorizza i suggerimenti (link tra libri) creati dagli utenti

- **`id`** (SERIAL, **PK**): Identificativo del consiglio.
- **`userid`** (VARCHAR, **FK**): L'utente che fornisce il consiglio.
- **`libro_sorgente_id`** (INTEGER, **FK**): Il libro che l'utente sta visualizzando/valutando.
- **`libro_consigliato_id`** (INTEGER, **FK**): Il libro suggerito come correlato.
- *Nota:* Il vincolo "max 3 libri suggeriti" dovrà essere gestito via software (logica Java) o trigger database, impedendo l'inserimento di una quarta riga per la stessa coppia (`userid`, `libro_sorgente_id`).

---

### Script SQL

```sql
-- =============================================================================
-- Script di creazione database Book Recommender (Lab B)
-- Target DBMS: PostgreSQL
-- =============================================================================

-- 1. Pulizia preliminare (opzionale: rimuove le tabelle se esistono già per ripartire da zero)
DROP TABLE IF EXISTS ConsigliLibri CASCADE;
DROP TABLE IF EXISTS ValutazioniLibri CASCADE;
DROP TABLE IF EXISTS ComposizioneLibrerie CASCADE;
DROP TABLE IF EXISTS Librerie CASCADE;
DROP TABLE IF EXISTS UtentiRegistrati CASCADE;
DROP TABLE IF EXISTS Libri CASCADE;

-- =============================================================================
-- 2. Creazione Tabella UtentiRegistrati
-- Contiene i dati degli utenti che si registrano all'applicazione.
-- Rif: Specifiche [cite: 106, 113]
-- =============================================================================
CREATE TABLE UtentiRegistrati (
    userid          VARCHAR(50) PRIMARY KEY, -- UserID scelto dall'utente
    nome            VARCHAR(50) NOT NULL,
    cognome         VARCHAR(50) NOT NULL,
    codice_fiscale  CHAR(16) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(256) NOT NULL -- Lunghezza sufficiente per hash (es. SHA-256)
);

-- =============================================================================
-- 3. Creazione Tabella Libri
-- Repository dei libri importati dal dataset CSV.
-- Rif: Specifiche [cite: 56-62]
-- =============================================================================
CREATE TABLE Libri (
    id                  SERIAL PRIMARY KEY, -- ID autogenerato
    titolo              VARCHAR(500) NOT NULL,
    autori              VARCHAR(500),       -- Lista autori come stringa dal CSV
    anno_pubblicazione  INTEGER,
    editore             VARCHAR(200),       -- Campo opzionale
    categoria           VARCHAR(200)        -- Campo opzionale
);

-- =============================================================================
-- 4. Creazione Tabella Librerie
-- Rappresenta le raccolte create dagli utenti.
-- Rif: Specifiche [cite: 122-127]
-- =============================================================================
CREATE TABLE Librerie (
    id      SERIAL PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL,
    userid  VARCHAR(50) NOT NULL,
    
    -- Vincolo: Se un utente viene cancellato, le sue librerie vengono cancellate
    CONSTRAINT fk_utente_libreria 
        FOREIGN KEY (userid) REFERENCES UtentiRegistrati(userid) 
        ON DELETE CASCADE
);

-- =============================================================================
-- 5. Creazione Tabella ComposizioneLibrerie (Relazione Molti-a-Molti)
-- Collega i Libri alle Librerie. Un libro può stare in più librerie.
-- =============================================================================
CREATE TABLE ComposizioneLibrerie (
    libreria_id INTEGER NOT NULL,
    libro_id    INTEGER NOT NULL,
    
    PRIMARY KEY (libreria_id, libro_id),
    
    CONSTRAINT fk_composizione_libreria 
        FOREIGN KEY (libreria_id) REFERENCES Librerie(id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_composizione_libro 
        FOREIGN KEY (libro_id) REFERENCES Libri(id) 
        ON DELETE CASCADE
);

-- =============================================================================
-- 6. Creazione Tabella ValutazioniLibri
-- Contiene i voti (1-5) e le note (max 256 char) per ogni criterio.
-- Rif: Specifiche [cite: 135-140]
-- =============================================================================
CREATE TABLE ValutazioniLibri (
    id SERIAL PRIMARY KEY,
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
        FOREIGN KEY (userid) REFERENCES UtentiRegistrati(userid) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_valutazione_libro 
        FOREIGN KEY (libro_id) REFERENCES Libri(id) 
        ON DELETE CASCADE
);

-- =============================================================================
-- 7. Creazione Tabella ConsigliLibri
-- Gestisce i suggerimenti: Utente U suggerisce Libro B dato il Libro A.
-- Rif: Specifiche [cite: 148-152]
-- =============================================================================
CREATE TABLE ConsigliLibri (
    id SERIAL PRIMARY KEY,
    userid                  VARCHAR(50) NOT NULL,
    libro_sorgente_id       INTEGER NOT NULL, -- Il libro che si sta visualizzando
    libro_consigliato_id    INTEGER NOT NULL, -- Il libro suggerito
    
    -- Vincolo: Non si può consigliare lo stesso libro come correlato di se stesso
    CONSTRAINT check_libri_diversi CHECK (libro_sorgente_id <> libro_consigliato_id),
    
    CONSTRAINT fk_consiglio_utente 
        FOREIGN KEY (userid) REFERENCES UtentiRegistrati(userid) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_libro_sorgente 
        FOREIGN KEY (libro_sorgente_id) REFERENCES Libri(id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_libro_consigliato 
        FOREIGN KEY (libro_consigliato_id) REFERENCES Libri(id) 
        ON DELETE CASCADE
);

-- Note finali:
-- Il vincolo "max 3 libri suggeriti per libro corrente" [cite: 37] è complesso da
-- implementare in SQL standard (richiederebbe Trigger). 
-- Si consiglia di gestire questo controllo lato applicazione Java.
```

---