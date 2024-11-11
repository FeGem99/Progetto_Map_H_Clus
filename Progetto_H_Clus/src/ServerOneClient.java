import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

/**
 * Classe che gestisce la connessione con un singolo client.
 * Implementa l'interfaccia {@link Runnable} per poter essere eseguita in un thread separato.
 * Gestisce richieste di caricamento dati, mining di dendrogrammi, e operazioni di salvataggio/caricamento di file.
 */
public class ServerOneClient implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static final String DIRECTORY_PATH = "Saved_Object"; // Path della directory per salvare i dendrogrammi

    /**
     * Costruttore della classe ServerOneClient.
     * 
     * @param clientSocket Il socket di connessione con il client.
     * @throws IOException Se si verifica un errore nell'instaurare i flussi di I/O.
     */
    public ServerOneClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     * Metodo eseguito quando il thread viene avviato. Gestisce la ricezione dei comandi dal client
     * e l'esecuzione delle operazioni richieste.
     */
    @Override
    public void run() {
        try {
            while (true) {
                // Log per tracciare i dati ricevuti
                System.out.println("In attesa di un comando dal client...");

                Object requestObj = in.readObject();
                System.out.println("Ricevuto comando: " + requestObj);

                // Verifica se l'input è del tipo Integer, altrimenti continua con il messaggio di errore
                if (!(requestObj instanceof Integer)) {
                    out.writeObject("Errore: comando non riconosciuto. Assicurati di inviare un comando numerico.");
                    continue;
                }

                int request = (Integer) requestObj;

                // Condizione per terminare la connessione
                if (request == -1) {
                    System.out.println("Client ha chiuso la connessione.");
                    break;
                }

                // Esegui i vari comandi
                switch (request) {
                    case 0 -> loadData();
                    case 1 -> mineDendrogram();
                    case 2 -> loadDendrogramFromFile();
                    default -> {
                        // Comando non valido: log dettagliato
                        System.out.println("Ricevuto comando non valido: " + request);
                        out.writeObject("Errore: comando non riconosciuto.");
                    }
                }
            }
        } catch (EOFException eof) {
            System.out.println("Il client ha chiuso la connessione improvvisamente.");
        } catch (Exception e) {
            System.out.println("Errore durante l'elaborazione della richiesta del client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Carica i dati dalla tabella specificata e crea un oggetto {@link Data}.
     * Invia un messaggio di conferma o errore al client.
     * 
     * @throws IOException Se si verifica un errore di I/O durante la lettura dei dati.
     * @throws ClassNotFoundException Se la classe degli oggetti letti non è trovata.
     */
    private void loadData() throws IOException, ClassNotFoundException {
        String tableName = (String) in.readObject();
        try {
            Data data = new Data(tableName);
            out.writeObject("OK");
            System.out.println("Oggetto Data creato con successo per la tabella: " + tableName);
        } catch (Exception e) {
            out.writeObject("Errore durante il caricamento dei dati: " + e.getMessage());
        }
    }

    /**
     * Esegue il mining di un dendrogramma per la tabella specificata e con la profondità indicata.
     * Gestisce anche la scelta del tipo di distanza e il salvataggio del dendrogramma.
     * 
     * @throws IOException Se si verifica un errore di I/O durante il processo.
     * @throws ClassNotFoundException Se la classe degli oggetti letti non è trovata.
     * @throws InvalidDepthException Se la profondità richiesta è invalida.
     */
    private void mineDendrogram() throws IOException, ClassNotFoundException, InvalidDepthException {
        String tableName = null;
        Integer depth = null;
        Integer dType = null;

        // Continua a chiedere i dati finché non sono validi
        while (tableName == null || depth == null || dType == null) {
            // Controlla che il nome della tabella sia del tipo String
            Object tableNameObj = in.readObject();
            if (tableNameObj instanceof String) {
                tableName = (String) tableNameObj;
            } else {
                out.writeObject("Errore: il nome della tabella deve essere una stringa. Reinserire i dati.");
                continue;
            }

            // Controlla che la profondità sia del tipo Integer
            Object depthObj = in.readObject();
            if (depthObj instanceof Integer) {
                depth = (Integer) depthObj;
            } else {
                out.writeObject("Errore: la profondità deve essere un numero intero. Reinserire i dati.");
                continue;
            }

            // Controlla che il tipo di distanza sia del tipo Integer
            Object dTypeObj = in.readObject();
            if (dTypeObj instanceof Integer) {
                dType = (Integer) dTypeObj;
            } else {
                out.writeObject("Errore: il tipo di distanza deve essere un numero intero. Reinserire i dati.");
                continue;
            }
        }

        // Prosegue con il processo di mining del dendrogramma
        try {
            ClusterDistance distance = switch (dType) {
                case 1 -> new SingleLinkDistance();
                case 2 -> new AverageLinkDistance();
                default -> throw new IllegalArgumentException("Tipo di distanza non valido");
            };

            Data data = new Data(tableName);
            HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
            clustering.mine(data, distance);

            out.writeObject("OK");
            out.writeObject(clustering.toString(data)); // Manda il dendrogramma al client

            // Richiede il nome del file e lo salva
            Object fileNameObj = in.readObject();
            if (fileNameObj instanceof String) {
                String fileName = (String) fileNameObj;
                saveDendrogram(clustering.toString(), fileName);
                out.writeObject("Dendrogramma salvato correttamente.");
                System.out.println("Dendrogramma salvato, operazione completata.");
            } else {
                out.writeObject("Errore: il nome del file deve essere una stringa.");
            }

        } catch (Exception e) {
            out.writeObject("Errore durante l'apprendimento del dendrogramma: " + e.getMessage());
        }
    }

    /**
     * Carica un dendrogramma da un file e lo invia al client.
     * 
     * @throws IOException Se si verifica un errore durante la lettura del file.
     * @throws ClassNotFoundException Se la classe degli oggetti letti non è trovata.
     */
    private void loadDendrogramFromFile() throws IOException, ClassNotFoundException {
        String fileName = (String) in.readObject();
        String fullPath = DIRECTORY_PATH + "/" + fileName;

        try {
            String dendrogramData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fullPath)));
            out.writeObject("OK");
            out.writeObject(dendrogramData);
            System.out.println("Dendrogramma caricato e inviato al client.");
        } catch (IOException e) {
            out.writeObject("Errore durante il caricamento del file: " + e.getMessage());
        }
    }

    /**
     * Salva un dendrogramma su un file nel disco.
     * 
     * @param dendrogramString La rappresentazione in stringa del dendrogramma da salvare.
     * @param fileName Il nome del file dove salvare il dendrogramma.
     * @throws IOException Se si verifica un errore durante il salvataggio del file.
     */
    private void saveDendrogram(String dendrogramString, String fileName) throws IOException {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        String fullPath = DIRECTORY_PATH + "/" + fileName;
        java.nio.file.Files.write(java.nio.file.Paths.get(fullPath), dendrogramString.getBytes());
        System.out.println("Dendrogramma salvato in: " + fullPath);
    }

    /**
     * Chiude la connessione con il client, chiudendo i flussi di input/output e il socket.
     */
    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Connessione con il client chiusa.");
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }
}
