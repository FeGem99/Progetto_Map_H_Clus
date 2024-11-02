import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

public class ServerOneClient implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerOneClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Inizializza i flussi di input e output
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // Gestisce la comunicazione col client
            handleClient();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore di connessione con il client: " + e.getMessage());
        } catch (InvalidDepthException e) {
            e.printStackTrace();
        } finally {
            closeConnection();  // Chiamata alla nuova funzione di chiusura
        }
    }
    
    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Connessione chiusa correttamente.");
        } catch (IOException e) {
            System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
        }
    }

    private void handleClient() throws IOException, ClassNotFoundException, InvalidDepthException {
        boolean exit = false;

        while (!exit) {
            Object obj = in.readObject();  // Legge l'oggetto dal client
            int option;

            if (obj instanceof Integer) {
                option = (Integer) obj;  // Converte l'oggetto in Integer se possibile
            } else {
                out.writeObject("Errore: Tipo di dato non valido per l'opzione.");  // Invia errore al client
                continue;
            }

            switch (option) {
                case 0: // Carica il nome della tabella dal client
                    String tableName = (String) in.readObject();
                    boolean success = loadTableFromDatabase(tableName);
                    out.writeObject(success ? "OK" : "Tabella non trovata.");
                    break;

                case 1: // Apprende il dendrogramma dal database
                    int depth = (Integer) in.readObject();
                    int distanceType = (Integer) in.readObject();
                    String learnedDendrogram = learnDendrogramFromDatabase(depth, distanceType);
                
                    if (learnedDendrogram.startsWith("Errore")) {
                        out.writeObject(learnedDendrogram);  
                    } else {
                        out.writeObject("OK");
                        out.writeObject(learnedDendrogram);  
                    }
                    break;
                
                case 2: // Carica il dendrogramma da file
                    String fileName = (String) in.readObject();
                    String fileContent = loadTextDendrogramFromFile(fileName);
                    if (fileContent != null) {
                        out.writeObject("OK");
                        out.writeObject(fileContent);
                    } else {
                        out.writeObject("Errore: File non trovato.");
                    }
                    break;

                case 3: // Salva il dendrogramma su file
                boolean salvataggioRiuscito = false;
                String directoryPath = "Saved_Object";
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs();  // Crea la cartella se non esiste
                }
            
                // Ricevi il nome del file
                String saveFileName = (String) in.readObject();
                System.out.println("Nome file ricevuto: " + saveFileName);  // Log per confermare il nome file
            
                if (!saveFileName.endsWith(".txt")) {
                    saveFileName += ".txt";  // Aggiungi l'estensione se non presente
                }
                String fullPath = directoryPath + "\\" + saveFileName;
            
                // Ricevi il dendrogramma dal client come stringa
                String dendrogramData = (String) in.readObject();
                System.out.println("Dati dendrogramma ricevuti: " + dendrogramData);  // Log per confermare i dati del dendrogramma
            
                    // Salva il dendrogramma nel file
                    saveAsFormattedString(dendrogramData, fullPath);
                    System.out.println("Dendrogramma salvato correttamente in " + fullPath);
                    salvataggioRiuscito = true;
            
                // Invia la risposta al client
                if (salvataggioRiuscito) {
                    out.writeObject("Dendrogramma salvato con successo: " + fullPath);
                } else {
                    out.writeObject("Errore durante il salvataggio del file.");
                }
                break;

                case -1: // Fine della connessione
                    exit = true;
                    out.writeObject("Chiusura della connessione.");
                    break;

                default:
                    out.writeObject("Opzione non valida.");
                    break;
            }
        }
    }

    public void saveAsFormattedString(String dendrogramData, String fullPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writer.write(dendrogramData);
            System.out.println("Dendrogramma salvato correttamente in " + fullPath);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio del file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private boolean loadTableFromDatabase(String tableName) {
        return "exampletab".equals(tableName);
    }

    private String loadTextDendrogramFromFile(String fileName) {
        String directoryPath = "Saved_Object";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fullPath = directoryPath + "/" + fileName;
        File file = new File(fullPath);

        if (!file.exists()) {
            System.out.println("Errore: File non trovato in " + fullPath);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento del file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String learnDendrogramFromDatabase(int depth, int distanceType) throws IOException, InvalidDepthException {
        Data data;
        try {
            data = new Data("exampletab");
        } catch (Exception e) {
            return "Errore nella creazione dell'oggetto Data: " + e.getMessage();
        }

        if (depth < 1 || depth > data.getNumberOfExample()) {
            return "Profondità del dendrogramma non valida.";
        }

        ClusterDistance distance = null;
        switch (distanceType) {
            case 1:
                distance = new SingleLinkDistance();
                break;
            case 2:
                distance = new AverageLinkDistance();
                break;
            default:
                return "Tipo di distanza non valido.";
        }

        HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
        clustering.mine(data, distance);

        // Restituisce il dendrogramma come stringa per essere visualizzato dal client
        return clustering.toString(data);
    }
}
