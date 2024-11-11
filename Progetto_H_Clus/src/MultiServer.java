import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe MultiServer rappresenta un server che gestisce connessioni multiple da parte dei client.
 * Il server accetta le connessioni sulla porta specificata e avvia un thread separato per ciascun client connesso.
 */
public class MultiServer {
    
    private int port;

    /**
     * Costruttore della classe MultiServer.
     * 
     * @param port La porta sulla quale il server ascolterà le connessioni in entrata.
     */
    public MultiServer(int port) {
        this.port = port;
    }

    /**
     * Avvia il server, che inizia ad accettare connessioni dai client sulla porta specificata.
     * Per ogni connessione, viene avviato un nuovo thread per gestire il client.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("MultiServer avviato sulla porta " + port);

            // Ciclo infinito per accettare connessioni dai client
            while (true) {
                // Accetta una nuova connessione dal client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connessione accettata: " + clientSocket);

                // Avvia un nuovo thread per gestire il client
                new Thread(new ServerOneClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Errore nell'avvio del server: " + e.getMessage());
        }
    }

    /**
     * Metodo principale per avviare il server. Imposta la porta e avvia l'istanza del server.
     * 
     * @param args Gli argomenti della riga di comando (non utilizzati in questa implementazione).
     */
    public static void main(String[] args) {
        int port = 8080; // Puoi cambiare la porta
        MultiServer server = new MultiServer(port);
        server.start();
    }
}
