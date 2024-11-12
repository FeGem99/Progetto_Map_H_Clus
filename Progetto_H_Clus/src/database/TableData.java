package database;

import data.Example;
import database.TableSchema.Column;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'accesso ai dati contenuti in una tabella di un database.
 * 
 * La classe consente di recuperare i dati da una tabella specifica, assicurandosi
 * che tutti gli attributi siano numerici. Gestisce anche il recupero delle transazioni
 * distinti dalla tabella, mappando ciascun record a un oggetto.
 */
public class TableData {

    private DbAccess db;

    /**
     * Costruttore della classe TableData.
     * 
     * @param db Oggetto DbAccess per interagire con il database.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Recupera le transazioni distinte dalla tabella specificata, assicurandosi che
     * tutti gli attributi siano numerici. Se la tabella è vuota o contiene attributi
     * non numerici, vengono sollevate le eccezioni appropriate.
     * 
     * @param table Il nome della tabella da cui recuperare i dati.
     * @return Una lista di oggetti {@link Example} contenenti i dati estratti dalla tabella.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws EmptySetException Se la tabella è vuota.
     * @throws MissingNumberException Se viene trovato un attributo non numerico.
     * @throws DatabaseConnectionException Se si verifica un errore di connessione al database.
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException,
            EmptySetException, MissingNumberException, DatabaseConnectionException {

        List<Example> transactions = new ArrayList<>();
        TableSchema schema = new TableSchema(db, table);

        // Verifica che tutti gli attributi siano numerici
        for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
            Column column = schema.getColumn(i);
            if (!column.isNumber()) {
                throw new MissingNumberException("Attributo non numerico trovato: " + column.getColumnName());
            }
        }

        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT DISTINCT * FROM " + table;
        ResultSet resultSet = statement.executeQuery(query);

        // Verifica che ci siano dati nella tabella
        if (!resultSet.next()) {
            throw new EmptySetException("La tabella " + table + " è vuota");
        }

        // Torna al primo record
        resultSet.beforeFirst();

        // Recupera tutti i record distinti e li mappa in oggetti Example
        while (resultSet.next()) {
            Example example = new Example();
            for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                double value = resultSet.getDouble(i + 1);
                example.add(value);
            }
            transactions.add(example);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return transactions;
    }
}
