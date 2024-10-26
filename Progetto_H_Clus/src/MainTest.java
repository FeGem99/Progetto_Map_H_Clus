import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.EmptyStackException;
import java.util.Scanner;
import javax.swing.*;

import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import data.NoDataExeption;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.MissingNumberException;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

public class MainTest {
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public static void main(String[] args) throws InvalidDepthException, EmptyStackException, SQLException, NoDataExeption, MissingNumberException, DatabaseConnectionException, EmptySetException {
        Scanner scanner = new Scanner(System.in);
        HierachicalClusterMiner clustering = null;
        String directoryPath = "Saved_Object";
        FileUtils.createDirectoryIfNotExists(directoryPath);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {  // Porta su cui ascolta il server
            System.out.println("Server in ascolto sulla porta 8080...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connessione accettata da " + clientSocket);

                    out = new ObjectOutputStream(clientSocket.getOutputStream());
                    in = new ObjectInputStream(clientSocket.getInputStream());

                    int scelta = (int) in.readObject();  // Legge la scelta dal client

                    if (scelta == 1) {
                        // Carica un dendrogramma dal file
                        String fileName = (String) in.readObject();
                        String fullPath = directoryPath + "/" + fileName;

                        try {
                            clustering = HierachicalClusterMiner.loadHierachicalClusterMiner(fullPath);
                            out.writeObject("OK");
                            out.writeObject(clustering.toString());  // Invia il dendrogramma caricato
                            mostraDendrogramma(clustering, "Dendrogramma caricato");
                        } catch (FileNotFoundException e) {
                            out.writeObject("Errore: file non trovato");
                        } catch (IOException | ClassNotFoundException e) {
                            out.writeObject("Errore durante il caricamento: " + e.getMessage());
                        }

                    } else if (scelta == 2) {
                        // Crea un nuovo dendrogramma
                        String tableName = (String) in.readObject();
                        Data data = new Data(tableName);
                        int depth = (int) in.readObject();
                        

                        if (depth < 1 || depth > data.getNumberOfExample()) {
                            out.writeObject("Profondità non valida, riprova");
                        } else {
                            ClusterDistance distance;
                            int distanzaTipo = (int) in.readObject();

                            distance = switch (distanzaTipo) {
                                case 1 -> new SingleLinkDistance();
                                case 2 -> new AverageLinkDistance();
                                default -> null;
                            };

                            if (distance != null) {
                                clustering = new HierachicalClusterMiner(depth);
                                clustering.mine(data, distance);
                                out.writeObject("OK");
                                out.writeObject(clustering.toString());
                                mostraDendrogramma(clustering, "Dendrogramma creato e salvato");

                                String fileName = (String) in.readObject();
                                String fullPath = directoryPath + "/" + fileName;
                                clustering.salva(fullPath);
                            } else {
                                out.writeObject("Tipo di distanza non valido");
                            }
                        }
                    } else {
                        out.writeObject("Scelta non valida");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Errore durante la connessione con il client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Errore di connessione del server: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void mostraDendrogramma(HierachicalClusterMiner clustering, String titolo) {
        JFrame frame = new JFrame(titolo);
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setText(clustering.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
