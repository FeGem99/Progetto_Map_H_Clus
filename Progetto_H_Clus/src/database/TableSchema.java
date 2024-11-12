package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Rappresenta lo schema di una tabella nel database.
 * 
 * La classe recupera le informazioni sulle colonne della tabella specificata,
 * mappando i tipi di dati SQL nei tipi di dati Java. Permette di ottenere il
 * numero di colonne e i dettagli su ciascuna colonna.
 */
public class TableSchema {

    private DbAccess db;
    private List<Column> tableSchema = new ArrayList<>();

    /**
     * Rappresenta una colonna della tabella.
     */
    public class Column {
        private String name;
        private String type;

        /**
         * Costruttore per creare una colonna.
         * 
         * @param name Il nome della colonna.
         * @param type Il tipo di dato della colonna.
         */
        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Restituisce il nome della colonna.
         * 
         * @return Il nome della colonna.
         */
        public String getColumnName() {
            return name;
        }

        /**
         * Verifica se il tipo della colonna è numerico.
         * 
         * @return true se il tipo è numerico, altrimenti false.
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * Restituisce una rappresentazione testuale della colonna.
         * 
         * @return Una stringa con il nome e il tipo della colonna.
         */
        @Override
        public String toString() {
            return name + ":" + type;
        }
    }

    /**
     * Costruttore per creare lo schema di una tabella.
     * 
     * Recupera le informazioni sulle colonne della tabella dal database,
     * mappando i tipi di dato SQL nei tipi di dato Java.
     * 
     * @param db L'oggetto DbAccess per connettersi al database.
     * @param tableName Il nome della tabella di cui si vuole ottenere lo schema.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     * @throws DatabaseConnectionException Se si verifica un errore di connessione al database.
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException, DatabaseConnectionException {
        this.db = db;
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<>();
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        Connection con = db.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {
            String typeName = res.getString("TYPE_NAME");
            if (mapSQL_JAVATypes.containsKey(typeName)) {
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(typeName)
                ));
            }
        }
        res.close();
    }

    /**
     * Restituisce il numero di attributi (colonne) nella tabella.
     * 
     * @return Il numero di colonne della tabella.
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce una colonna specifica in base all'indice.
     * 
     * @param index L'indice della colonna da restituire.
     * @return L'oggetto {@link Column} che rappresenta la colonna.
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }
}
