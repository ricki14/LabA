----REQUISITI DI SISTEMA----

Per compilare ed eseguire CineMax è necessario avere:

1) Java Development Kit (JDK) installato;
2) JDK 25 o versione compatibile;
3) un sistema operativo compatibile con Java.

Non sono richiesti database, connessioni Internet o altri servizi esterni.


----STRUTTURA DEL PROGETTO----

Il repository è organizzato nel seguente modo:

CineMax/
├── bin/
├── data/
├── doc/
├── lib/
├── src/
├── autori.txt
└── README.txt

La directory "src" contiene il codice sorgente Java.

La directory "bin" contiene il file JAR eseguibile del progetto.

La directory "data" contiene i file utilizzati dal programma per la
memorizzazione dei dati.

La directory "doc" contiene il manuale utente, il manuale tecnico e
la Javadoc generata.

La directory "lib" contiene eventuali librerie esterne utilizzate
dal progetto.


----INSTALLAZIONE DI JAVA----

Prima di compilare o eseguire CineMax è necessario installare un JDK.

Per verificare che Java sia correttamente installato, aprire un
terminale ed eseguire:

java -version

Per verificare anche la disponibilità del compilatore Java:

javac -version

I due comandi devono restituire la versione del JDK installato.


----INSTALLAZIONE DI CINEMAX----

1) Scaricare o copiare la cartella del progetto CineMax sul computer.
2) Estrarre l'archivio ZIP, se il progetto viene fornito in formato
   compresso.
3) Mantenere invariata la struttura delle directory del progetto.
4) Verificare che nella cartella del progetto siano presenti i file
   sorgente Java e la cartella contenente i dati utilizzati dal programma,
   tra cui il file delle proiezioni.
5) Aprire il progetto con un ambiente di sviluppo Java oppure
   utilizzare il terminale.


----AVVIO TRAMITE INTELLIJ IDEA----

Per avviare CineMax tramite IntelliJ IDEA:
1) Aprire il progetto in IntelliJ IDEA.
2) Attendere il caricamento del progetto.
3) Individuare la classe principale CineMax.
4) Eseguire il metodo main() tramite il comando Run.

In alternativa, da terminale è possibile compilare i file Java e
successivamente eseguire la classe principale.


PRIMO AVVIO

Al primo avvio:
1) Avviare CineMax.
2) Seguire le istruzioni visualizzate nel terminale.
3) È possibile utilizzare gli utenti e i dati già presenti nei file
   della directory "data" per verificare il funzionamento del sistema.

È possibile inoltre registrare nuovi utenti attraverso le
funzionalità messe a disposizione dal programma.


----RISOLUZIONE DEI PROBLEMI----

Se il programma non si avvia, verificare:
-che il JDK sia installato correttamente;
-che i comandi "java -version" e "javac -version" restituiscano
 una versione valida;
-che la compilazione non produca errori;
-che la struttura delle directory non sia stata modificata;
-che i file presenti nella directory "data" siano disponibili;
-che il file "bin/CineMax.jar" sia stato generato correttamente;
-che il programma venga eseguito dalla directory principale del
 progetto.

In caso di errori relativi ai file di dati, verificare che i file
CSV si trovino nella directory "data" e che i relativi nomi non siano
stati modificati.