package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.MissingNumberException;
import database.TableData;

/**
 * Rappresenta un dataset di esempi. Ogni esempio è una serie di valori numerici.
 * Questa classe gestisce il caricamento dei dati da una tabella di un database,
 * la gestione degli esempi, e il calcolo delle distanze tra gli esempi.
 */
public class Data {

    private List<Example> data = new ArrayList<>(); // Rappresenta il dataset
    private int numberOfExamples; // Numero di esempi nel dataset

    /**
     * Costruttore che carica i dati da una tabella del database specificata.
     * 
     * @param tableName il nome della tabella da cui caricare i dati.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     * @throws EmptyStackException se si verifica un errore durante l'elaborazione dei dati.
     * @throws NoDataExeption se la tabella è vuota.
     * @throws MissingNumberException se manca un numero nei dati.
     * @throws DatabaseConnectionException se si verifica un errore di connessione al database.
     * @throws EmptySetException se il set di dati è vuoto.
     */
    public Data(String tableName) throws SQLException, EmptyStackException, NoDataExeption, MissingNumberException, DatabaseConnectionException, EmptySetException {
        DbAccess dbAccess = new DbAccess();
        dbAccess.initConnection();

        try {
            TableData tableData = new TableData(dbAccess);
            List<Example> examples = tableData.getDistinctTransazioni(tableName);
            if (examples.isEmpty()) {
                throw new NoDataExeption("La tabella " + tableName + " è vuota.");
            }

            this.data.addAll(examples);
            this.numberOfExamples = data.size();

        } finally {
            dbAccess.closeConnection();
        }
    }

    /**
     * Inizializza un dataset predefinito con esempi di dati.
     */
    private void inizializeData() {
        Example e = new Example();
        e.add(1.0);
        e.add(2.0);
        e.add(0.0);
        data.add(e);

        e = new Example();
        e.add(0.0);
        e.add(1.0);
        e.add(-1.0);
        data.add(e);

        e = new Example();
        e.add(1.0);
        e.add(3.0);
        e.add(5.0);
        data.add(e);

        e = new Example();
        e.add(1.0);
        e.add(3.0);
        e.add(4.0);
        data.add(e);

        e = new Example();
        e.add(2.0);
        e.add(2.0);
        e.add(0.0);
        data.add(e);
    }

    /**
     * Restituisce il numero di esempi nel dataset.
     * 
     * @return il numero di esempi.
     */
    public int getNumberOfExample() {
        return data.size();
    }

    /**
     * Restituisce un esempio specifico in base all'indice.
     * 
     * @param exampleIndex l'indice dell'esempio.
     * @return l'esempio richiesto.
     * @throws IndexOutOfBoundsException se l'indice è fuori dai limiti.
     */
    public Example getExample(int exampleIndex) {
        if (exampleIndex >= 0 && exampleIndex < data.size()) {
            return data.get(exampleIndex);
        } else {
            throw new IndexOutOfBoundsException("indice non valido");
        }
    }

    /**
     * Calcola la matrice delle distanze tra tutti gli esempi nel dataset.
     * 
     * @return una matrice bidimensionale contenente le distanze tra gli esempi.
     */
    public double[][] distance() {
        double[][] distanceMatrix = new double[numberOfExamples][numberOfExamples];
        for (int i = 0; i < numberOfExamples; i++) {
            for (int j = i + 1; j < numberOfExamples; j++) {
                double dist = getExample(i).distance(getExample(j));
                distanceMatrix[i][j] = dist;
                distanceMatrix[j][i] = dist;
            }
        }
        return distanceMatrix;
    }

    /**
     * Restituisce una rappresentazione in formato stringa del dataset.
     * 
     * @return una stringa che rappresenta il dataset.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Example> iter = data.iterator();
        int index = 0;
        while (iter.hasNext()) {
            sb.append("Example ").append(index).append(": ").append(iter.next().toString()).append("\n");
            index++;
        }
        return sb.toString();
    }

    /**
     * Metodo principale per testare la classe Data.
     * 
     * @param args gli argomenti della linea di comando.
     */
    public static void main(String args[]) {
        try {
            // Modifico il nome della tabella secondo il nome del database
            Data trainingSet = new Data("exampleTab");
            System.out.println(trainingSet);

            double[][] distancematrix = trainingSet.distance();
            System.out.println("Distance matrix:\n");
            for (int i = 0; i < distancematrix.length; i++) {
                for (int j = 0; j < distancematrix.length; j++) {
                    System.out.print(distancematrix[i][j] + "\t");
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
