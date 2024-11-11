package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.Example;
import database.TableSchema.Column;

/**
 * Classe che rappresenta un'operazione su una tabella di un database.
 * Fornisce metodi per recuperare i dati da una tabella e verificarne la validità.
 */
public class TableData {

    private DbAccess db;

    /**
     * Costruttore per inizializzare un oggetto TableData con una connessione al database.
     * 
     * @param db L'oggetto DbAccess che fornisce l'accesso al database.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Recupera le transazioni distinte da una tabella del database e le restituisce come lista di esempi.
     * Verifica che tutti gli attributi della tabella siano numerici prima di eseguire la query.
     * 
     * @param table Il nome della tabella da cui recuperare i dati.
     * @return Una lista di oggetti {@link Example} che rappresentano le transazioni distinte nella tabella.
     * @throws SQLException Se si verifica un errore nella connessione al database o nell'esecuzione della query.
     * @throws EmptySetException Se la tabella è vuota.
     * @throws MissingNumberException Se uno degli attributi della tabella non è numerico.
     * @throws DatabaseConnectionException Se si verifica un errore durante la connessione al database.
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException, 
                                                                     MissingNumberException, DatabaseConnectionException {
        List<Example> transactions = new ArrayList<>();

        // Ottieni lo schema della tabella
        TableSchema schema = new TableSchema(db, table);

        // Verifica se ci sono attributi non numerici
        for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
            Column column = schema.getColumn(i);
            if (!column.isNumber()) {
                throw new MissingNumberException("Attributo non numerico trovato: " + column.getColumnName());
            }
        }

        // Connessione al database
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT DISTINCT * FROM " + table;
        ResultSet resultSet = statement.executeQuery(query);

        // Verifica se la tabella è vuota
        if (!resultSet.next()) {
            throw new EmptySetException("La tabella " + table + " è vuota");
        }

        // Torna al primo record
        resultSet.beforeFirst();

        // Recupera i dati dalla tabella
        while (resultSet.next()) {
            Example example = new Example();
            for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                double value = resultSet.getDouble(i + 1);
                example.add(value);
            }
            transactions.add(example);
        }

        // Chiusura delle risorse
        resultSet.close();
        statement.close();
        connection.close();

        return transactions;
    }
}
