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
                    // Caricamento di un oggetto salvato come stringa formattata
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

    public static void saveAsFormattedString(String dendrogramString, String filePath) throws IOException {
        Files.write(Paths.get(filePath), dendrogramString.getBytes());
    }

    public static String loadDendrogramAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
