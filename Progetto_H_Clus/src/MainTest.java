import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.*;
import java.io.File;

import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;


/**
 * La classe `MainTest` gestisce il flusso principale dell'applicazione di clustering gerarchico.
 * Consente all'utente di scegliere se caricare un oggetto `HierachicalClusterMiner` esistente
 * da un file, o crearne uno nuovo eseguendo un'analisi di clustering su un dataset.
 * L'utente può scegliere il tipo di distanza da utilizzare tra i cluster e la profondità del dendrogramma.
 * I risultati del clustering vengono poi salvati su un file in formato `.dat`.
 *
 * La classe gestisce anche la visualizzazione del dendrogramma in una finestra grafica e la gestione di errori
 * relativi all'input dell'utente, come la scelta della profondità del dendrogramma e il tipo di distanza tra i cluster.
 */
public class MainTest {

    /**
     * Oggetto scanner utilizzato per leggere l'input dell'utente.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Oggetto per eseguire l'analisi di clustering gerarchico.
     */
    private static HierachicalClusterMiner clustering;

    /**
     * Percorso della directory in cui vengono salvati gli oggetti.
     */
    private static final String directoryPath = "Saved_Object";

    /**
     * Metodo principale per eseguire l'applicazione.
     * Permette all'utente di scegliere se caricare un oggetto `HierachicalClusterMiner`
     * da un file esistente o crearne uno nuovo, eseguire il clustering e salvare i risultati.
     * 
     * @param args Argomenti della riga di comando (non utilizzati).
     * @throws InvalidDepthException Se la profondità del dendrogramma è invalida.
     */
    public static void main(String[] args) throws InvalidDepthException {
        FileUtils.createDirectoryIfNotExists(directoryPath);

        boolean sceltaValida = false;

        // Ciclo principale per gestire la selezione dell'utente
        while (!sceltaValida) {
            try {
                // Presenta il menu all'utente
                System.out.println("Benvenuto! Vuoi caricare un oggetto HierachicalClusterMiner esistente o crearne uno nuovo?");
                System.out.println("1. Carica da file");
                System.out.println("2. Crea un nuovo HierachicalClusterMiner");
                String scelta = scanner.nextLine();

                if (scelta.equals("1")) {
                    // Caricamento di un oggetto salvato
                    System.out.print("Inserisci il nome del file da caricare (da Saved_Object): ");
                    String fileName = scanner.nextLine();
                    if (!fileName.endsWith(".dat")) {
                        fileName += ".dat";
                    }
                    String fullPath = directoryPath + "/" + fileName;

                    try {
                        String dendrogrammaStringa = loadDendrogramAsString(fullPath);
                        mostraDendrogramma(dendrogrammaStringa, "Dendrogramma caricato");
                        System.out.println("Dendrogramma caricato correttamente da " + fullPath);
                    } catch (IOException e) {
                        System.out.println("Errore durante il caricamento del file: " + e.getMessage());
                    }
                } else if (scelta.equals("2")) {  
                    // Creazione di un nuovo oggetto Data
                    Data data = null;
                    while (data == null) {
                        try {
                            System.out.print("Inserisci il nome della tabella: ");
                            String tableName = scanner.nextLine();
                            data = new Data(tableName);
                            System.out.println("Oggetto Data creato correttamente!");
                            System.out.println(data);
                        } catch (Exception e) {
                            System.out.println("Errore durante la creazione dell'oggetto Data: " + e.getMessage());
                            System.out.println("Riprova con un nome di tabella valido.");
                        }
                    }

                    // Gestione della profondità del dendrogramma
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
                            scanner.next();
                        }
                    } while (true);
                    scanner.nextLine();
                    clustering = new HierachicalClusterMiner(depth);

                    // Selezione del tipo di distanza tra cluster
                    ClusterDistance distance = null;
                    do {
                        try {
                            System.out.println("Scegli il tipo di distanza tra cluster:");
                            System.out.println("1. Single link distance");
                            System.out.println("2. Average link distance");
                            String input = scanner.next();
                            int sceltaDist = Integer.parseInt(input);

                            switch (sceltaDist) {
                                case 1 -> distance = new SingleLinkDistance();
                                case 2 -> distance = new AverageLinkDistance();
                                default -> System.out.println("Scelta non valida. Riprova.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Errore: inserire un numero valido (1 o 2)");
                        }
                    } while (distance == null);

                    // Calcolo della matrice delle distanze
                    double[][] distancematrix = data.distance();
                    System.out.println("Matrice delle distanze:\n");
                    for (double[] row : distancematrix) {
                        for (double value : row) {
                            System.out.print(value + "\t");
                        }
                        System.out.println();
                    }
                    clustering.mine(data, distance);
                    System.out.println(clustering);
                    System.out.println(clustering.toString(data));

                    // Salvataggio dell'oggetto
                    boolean salvataggioRiuscito = false;
                    while (!salvataggioRiuscito) {
                        scanner.nextLine();
                        System.out.print("Inserisci il nome del file: ");
                        String fileName = scanner.nextLine();
                        if (!fileName.endsWith(".dat")) {
                            fileName += ".dat";
                        }
                        String fullPath = directoryPath + "/" + fileName;

                        try {
                            saveAsFormattedString(clustering.toString(), fullPath);
                            System.out.println("Oggetto HierachicalClusterMiner salvato correttamente in " + fullPath);
                            mostraDendrogramma(clustering.toString(), "Dendrogramma creato e salvato");
                            salvataggioRiuscito = true;
                        } catch (IOException e) {
                            System.out.println("Errore durante il salvataggio del file: " + e.getMessage());
                        }
                    }
                    sceltaValida = true;
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                }
            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Mostra un dendrogramma in una finestra grafica.
     * 
     * @param dendrogramma Stringa contenente la rappresentazione del dendrogramma.
     * @param titolo Titolo della finestra.
     */
    public static void mostraDendrogramma(String dendrogramma, String titolo) {
        JFrame frame = new JFrame(titolo);
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setText(dendrogramma);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Salva un dendrogramma come stringa formattata in un file.
     * 
     * @param dendrogramString La stringa che rappresenta il dendrogramma.
     * @param filePath Il percorso del file in cui salvare il dendrogramma.
     * @throws IOException Se si verifica un errore durante il salvataggio del file.
     */
    public static void saveAsFormattedString(String dendrogramString, String filePath) throws IOException {
        Files.write(Paths.get(filePath), dendrogramString.getBytes());
    }

    /**
     * Carica un dendrogramma da un file come stringa.
     * 
     * @param filePath Il percorso del file da cui caricare il dendrogramma.
     * @return La stringa che rappresenta il dendrogramma.
     * @throws IOException Se si verifica un errore durante il caricamento del file.
     */
    public static String loadDendrogramAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
