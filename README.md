### Generazione javadoc
Comando: `mvn clean install javadoc:aggregate`

### Per far partire la gui
Utilizza il comando `mvn clean javafx:run` direttamente nel terminale del progetto.
Ora anche tramite file .sh /.bat.

### Per configurare il progetto
Utilizzo Github CLI:
1. gh auth login (seguo i passaggi a schermo)
2. gh repo clone proprietario/nome-repository (clono la repo su intellij o qualsiasi ide)

### TO-DO Lorenzo
- [ ] Fare javadoc parte client (in attesa di sergio)
- [ ] Scrivere in latex manuale utente una volta che Nash lo ha terminato

### TODO Lyan
- [ ] (Opzionale, se ho tempo) i metodi di get non ritornano nessun enum di errore, implementarlo
- [ ] (Opzionale, se ho tempo) nella creazione dei DTO con un ResultSet, il messaggio di errore non stampa bene i ResultSet.
Cerca come stampare un ResultSet e crea un metodo apposito.
