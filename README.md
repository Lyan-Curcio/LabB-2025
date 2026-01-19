### To Do
1. bisogna far partire prima server (controllare maven) (controllare aservermain)
2. prima lavoriamo su server: verificare connessione db (databasemanager), scriver equery (bookrepositoryimpl)
3. common: aggiungere classi mancanti, ovvero libreria e consigli
4. poi su client: bisogna capire che pagine dell'interfaccia mancano (informazioni libro) e implementare
5. migliorare interfaccia grafica e renderla unificata


### Generazione javadoc
Comando: `mvn clean install javadoc:aggregate`


















### 1. La divisione tra "Main" e "Test" (`src/main` vs `src/test`)
Questa è la distinzione più importante.
* **`src/main`**: Qui ci va il codice che finirà nel programma finale (quello che dai al cliente).
* **`src/test`**: Qui ci vanno i test automatici (codice che serve solo a te per controllare che il programma funzioni).

**Perché?** Quando Maven costruisce il file finale (il `.jar`), prende *solo* il contenuto di `main` e ignora `test`. Se mettessi tutto insieme, consegneresti al cliente un programma pieno di codice di test inutile e pesante.

### 2. La divisione tra "Java" e "Resources"
All'interno di `main` trovi spesso due sottocartelle:
* **`java`**: Solo file `.java` (che devono essere compilati in `.class`).
* **`resources`**: Immagini, file di configurazione, file XML.

**Perché?** Il compilatore Java non sa leggere le immagini o i file di testo. Maven sa che tutto ciò che è in `java` va passato al compilatore, mentre tutto ciò che è in `resources` va semplicemente copiato così com'è nella cartella finale.

### 3. Le cartelle "Matrioska" (`com/example/mio/progetto`)
Questa è la parte che confonde di più. Perché `src/main/java/com/example/App.java` invece di `src/main/java/App.java`?

In Java, i nomi delle classi devono essere **univoci in tutto il mondo**.
Immagina se tu creassi una classe chiamata `Utente` e importassi una libreria esterna che ha anch'essa una classe `Utente`. Il computer impazzirebbe.

Per evitare questo, si usano i **Package** (che seguono la struttura delle cartelle):
* Si usa il dominio del sito web al contrario (es. `com.google` o `it.miaazienda`).
* Questo garantisce che la tua classe `com.miaazienda.Utente` sia diversa da `com.google.Utente`.

Maven ti costringe a riflettere questa struttura nelle cartelle per evitare errori.

### 4. La cartella "Target" (Il cestino)
Noterai una cartella `target` che appare e scompare.
Quella è la "zona di lavoro" di Maven. Lì dentro finiscono i file compilati (`.class`).
Il bello di Maven è che puoi cancellare l'intera cartella `target` in qualsiasi momento (`mvn clean`) senza perdere nulla del tuo lavoro, perché il tuo codice sorgente è al sicuro in `src`.

---

### L'analogia della Cucina del Ristorante

Immagina un progetto Maven come la cucina di un ristorante stellato:

1.  **`src/main/java`**: È il bancone dove gli chef cucinano le pietanze (il codice).
2.  **`src/main/resources`**: È la dispensa con le spezie e i piatti (configurazioni/immagini).
3.  **`src/test`**: È il tavolo dell'assaggiatore. Il cliente non deve vedere l'assaggiatore, quindi sta in una stanza separata.
4.  **`target`**: È il pass dove vengono messi i piatti finiti pronti per uscire. A fine serata, si pulisce tutto il pass (`mvn clean`), ma la cucina e la dispensa (`src`) restano intatte per il giorno dopo.
5.  **`pom.xml`**: È il menù e la lista della spesa. Dice cosa serve per cucinare (le dipendenze).

### Il vantaggio per te
Poiché questa struttura è **standard** in tutto il mondo:
* Se scarichi un progetto open source fatto da un giapponese o un americano, sai *esattamente* dove cercare il codice.
* **VS Code lo sa.** Appena vede questa struttura, capisce automaticamente dove sono i file sorgenti e come compilarli, senza che tu debba configurare nulla (a meno che la struttura non sia rotta, come succedeva prima!).

### Per far partire la gui
Utilizza il comando `mvn clean javafx:run` direttamente nel terminale del progetto.

### Per configurare il progetto
1. Utilizza il comando `git clone https://github.com/Lyan-Curcio/LabB-2025.git`;
2. Utilizza poi `git init`;
3. Poi `git config --global user.name "il tuo nome di github"` e `git config --global user.email "la tua mail github"`;
4. Poi non ricordo (da aggiungere)


