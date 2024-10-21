import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;


public class MainTest {

    public static void main(String[] args) throws InvalidDepthException {
        Scanner scanner = new Scanner(System.in);
        HierachicalClusterMiner clustering = null;
        String directoryPath = "Saved_Object";
        FileUtils.createDirectoryIfNotExists(directoryPath);

        boolean sceltaValida = false;

        while (!sceltaValida) {
            try {
                // Presenta il menu all'utente
                System.out.println("Benvenuto! Vuoi caricare un oggetto HierachicalClusterMiner esistente o crearne uno nuovo?");
                System.out.println("1. Carica da file");
                System.out.println("2. Crea un nuovo HierachicalClusterMiner");
                String scelta = scanner.nextLine();

                if (scelta.equals("1")) {
                    // Caricamento di un oggetto serializzato
                    System.out.print("Inserisci il nome del file da caricare (da Saved_Object): ");
                    String fileName = scanner.nextLine();
                    String fullPath = directoryPath + "/" + fileName;  // Percorso completo
                    
                    try {
                        clustering = HierachicalClusterMiner.loadHierachicalClusterMiner(fullPath);
                        System.out.println("Oggetto HierachicalClusterMiner caricato correttamente da " + fullPath);
                    } catch (FileNotFoundException e) {
                        System.out.println("Errore: il file non è stato trovato. Riprova.");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Errore durante il caricamento del file: " + e.getMessage());
                    }
                } else if (scelta.equals("2")) {  //metodo per usare il nuovo costruttore di Data(commento da sistemare)
                    // Ciclo per la creazione di un oggetto Data valido
                    Data data = null;
                    while (data == null) {
                        try {
                            System.out.print("Inserisci il nome della tabella: ");
                            String tableName = scanner.nextLine();
                            data = new Data(tableName); // Creazione del nuovo oggetto Data
                            System.out.println("Oggetto Data creato correttamente!");
                            System.out.println(data);
                        } catch (Exception e) {
                            System.out.println("Errore durante la creazione dell'oggetto Data: " + e.getMessage());
                            System.out.println("Riprova con un nome di tabella valido.");
                        }
                    }

                    int depth;

                    do {
                        try {
                            System.out.print("Inserisci la profondità del dendrogramma (deve essere >= 1): ");
                            depth = scanner.nextInt();
                            if (depth < 1) {
                                throw new InvalidDepthException("Profondità non valida, selezionare numero >=1, riprova");
                            } else if (depth > data.getNumberOfExample()) {
                                throw new InvalidDepthException("Profondità del dendrogramma è superiore al numero di esempi memorizzati nel dataset, riprova");
                            }
                            break;
                        } catch (InvalidDepthException e) {
                            System.out.println("Errore: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Errore di input: inserire un numero valido.");
                            scanner.next(); // Pulisce il buffer dello scanner per evitare loop infiniti
                        }
                    } while (true);
                    scanner.nextLine();
                    clustering = new HierachicalClusterMiner(depth);

                    // Creazione dell'oggetto ClusterDistance in base alla scelta dell'utente
                    ClusterDistance distance = null;
                    do {
                        try {
                            System.out.println("Scegli il tipo di distanza tra cluster:");
                            System.out.println("1. Single link distance");
                            System.out.println("2. Average link distance");
                            String input = scanner.next(); // Legge l'input come stringa
                            int sceltaDist = Integer.parseInt(input); // Prova a convertire l'input in un numero

                            switch (sceltaDist) {
                                case 1 -> distance = new SingleLinkDistance();
                                case 2 -> distance = new AverageLinkDistance();
                                default -> System.out.println("Scelta non valida. Riprova.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Errore: inserire un numero valido (1 o 2)");
                        }
                    } while (distance == null);

                    double[][] distancematrix = data.distance();
                    System.out.println("Matrice delle distanze:\n");
                    for (int i = 0; i < distancematrix.length; i++) {
                        for (int j = 0; j < distancematrix.length; j++) {
                            System.out.print(distancematrix[i][j] + "\t");
                        }
                        System.out.println("");
                    }
                    clustering.mine(data, distance);
                    System.out.println(clustering);
                    System.out.println(clustering.toString(data));

                    // Ciclo per il salvataggio con gestione eccezioni
                    boolean salvataggioRiuscito = false;
            while (!salvataggioRiuscito) {
                try {
                    scanner.nextLine();
                    System.out.print("Inserisci il nome del file: ");
                    String fileName = scanner.nextLine();  // Usa scanner.nextLine() per leggere il nome del file
                    String fullPath = "Saved_Object/" + fileName;  // Salva nella directory Saved_Object
                    clustering.salva(fullPath);  // Salva il file nel percorso specificato
                    System.out.println("Oggetto HierachicalClusterMiner salvato correttamente in " + fullPath);
                    salvataggioRiuscito = true;  // Esci dal ciclo se il salvataggio ha successo
    } catch (IOException e) {
        System.out.println("Errore durante il salvataggio: " + e.getMessage());
        System.out.println("Riprova con un percorso valido.");
    }
}

                    sceltaValida = true; // Uscita dal ciclo perché tutte le operazioni sono andate a buon fine
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                }
            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
            }
        }

        // Fine dell'elaborazione
        scanner.close();
    }

    }

