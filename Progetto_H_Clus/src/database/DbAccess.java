package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce l'accesso al database per la lettura dei dati di training.
 * Inizializza e fornisce la connessione al database, gestisce eventuali errori di connessione,
 * e chiude la connessione quando non è più necessaria.
 */
public class DbAccess {

    private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private String DBMS = "jdbc:mysql";
    private String SERVER = "localhost";
    private String DATABASE = "MapDB";
    private int PORT = 3306;
    private String USER_ID = "root";
    private String PASSWORD = "123456";

    private Connection conn;

    /**
     * Inizializza la connessione al database.
     * 
     * Questo metodo tenta di caricare il driver JDBC per il database MySQL
     * e stabilisce una connessione utilizzando i parametri di configurazione.
     * 
     * @throws DatabaseConnectionException Eccezione lanciata se la connessione al database fallisce.
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            System.out.println("[!] Driver non trovato: " + e.getMessage());
            throw new DatabaseConnectionException(e.toString());
        }

        String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.toString());
        }
    }

    /**
     * Restituisce la connessione al database.
     * 
     * Se la connessione non è ancora stata inizializzata, questo metodo la crea.
     * 
     * @return La connessione al database.
     * @throws DatabaseConnectionException Eccezione lanciata se la connessione al database fallisce.
     */
    public Connection getConnection() throws DatabaseConnectionException {
        this.initConnection();
        return conn;
    }

    /**
     * Chiude la connessione al database.
     * 
     * @throws SQLException Eccezione lanciata se si verifica un errore durante la chiusura della connessione.
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
