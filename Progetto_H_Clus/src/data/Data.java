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
 * Rappresenta un dataset di esempi. Gestisce l'inizializzazione del dataset, 
 * il calcolo delle distanze tra gli esempi e la gestione delle eccezioni relative ai dati.
 */
public class Data {
    
    /** Lista che contiene gli esempi del dataset. */
    private List<Example> data = new ArrayList<>();
    
    /** Numero di esempi nel dataset. */
    private int numberOfExamples;
    
    /**
     * Costruttore che inizializza un oggetto Data utilizzando i dati di una tabella.
     * Estrae i dati dalla tabella tramite l'oggetto DbAccess e li carica nel dataset.
     * 
     * @param tableName il nome della tabella da cui caricare i dati.
     * @throws SQLException se si verifica un errore durante la connessione al database.
     * @throws EmptyStackException se si verifica un errore nell'elaborazione della pila.
     * @throws NoDataExeption se la tabella non contiene dati.
     * @throws MissingNumberException se mancano numeri nei dati.
     * @throws DatabaseConnectionException se si verifica un errore nella connessione al database.
     * @throws EmptySetException se il dataset risulta vuoto.
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
     * Inizializza il dataset con valori predefiniti.
     * Usato per creare un esempio di dataset in fase di test.
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
     * Restituisce un esempio specificato dal suo indice.
     * 
     * @param exampleIndex l'indice dell'esempio da recuperare.
     * @return l'esempio corrispondente all'indice.
     * @throws IndexOutOfBoundsException se l'indice fornito è invalido.
     */
    public Example getExample(int exampleIndex) {
        if (exampleIndex >= 0 && exampleIndex < data.size()) {
            return data.get(exampleIndex);
        } else {
            throw new IndexOutOfBoundsException("Indice non valido");
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
     * @return una stringa che rappresenta gli esempi del dataset.
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
     * Metodo principale per testare la creazione e il calcolo delle distanze nel dataset.
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            // Modifica il nome della tabella secondo il nome del database
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
