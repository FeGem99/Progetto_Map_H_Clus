package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che rappresenta lo schema di una tabella nel database.
 * Contiene informazioni sulle colonne della tabella, inclusi il nome e il tipo di dati.
 */
public class TableSchema {

    private DbAccess db;

    /**
     * Classe interna che rappresenta una colonna di una tabella del database.
     * Contiene il nome e il tipo di dati della colonna.
     */
    public class Column {
        private String name;
        private String type;

        /**
         * Costruttore della colonna.
         * 
         * @param name Il nome della colonna.
         * @param type Il tipo di dati della colonna.
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
         * Verifica se il tipo di dati della colonna è numerico.
         * 
         * @return True se il tipo di dati è numerico, altrimenti false.
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * Restituisce una rappresentazione in formato stringa della colonna.
         * 
         * @return La stringa rappresentante il nome e il tipo della colonna.
         */
        public String toString() {
            return name + ":" + type;
        }
    }

    private List<Column> tableSchema = new ArrayList<Column>();

    /**
     * Costruttore della classe TableSchema.
     * Recupera lo schema di una tabella dal database e lo memorizza.
     * 
     * @param db L'oggetto DbAccess per accedere al database.
     * @param tableName Il nome della tabella di cui ottenere lo schema.
     * @throws SQLException Se si verifica un errore nella connessione al database o nell'esecuzione della query.
     * @throws DatabaseConnectionException Se si verifica un errore nella connessione al database.
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException, DatabaseConnectionException {
        this.db = db;

        // Mappa dei tipi SQL ai tipi Java
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        // Connessione al database per ottenere informazioni sulla tabella
        Connection con = db.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {
            // Verifica il tipo di dati della colonna e lo mappa
            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))
                ));
        }

        res.close();
    }

    /**
     * Restituisce il numero di colonne nella tabella.
     * 
     * @return Il numero di colonne nella tabella.
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna specificata da un indice.
     * 
     * @param index L'indice della colonna da restituire.
     * @return L'oggetto Column corrispondente all'indice.
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }
}
