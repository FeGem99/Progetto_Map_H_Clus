import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

    /**
     * La porta sulla quale il server ascolterà le connessioni.
     */
    private int port;

    /**
     * Costruttore della classe `MultiServer`, inizializza la porta del server.
     * 
     * @param port La porta sulla quale il server deve ascoltare le connessioni.
     */
    public MultiServer(int port) {
        this.port = port;
    }

    /**
     * Avvia il server in ascolto sulla porta specificata. Quando un client si connette,
     * viene avviato un nuovo thread per gestire la comunicazione con il client.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("MultiServer avviato sulla porta " + port);

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
     * Il punto di ingresso principale dell'applicazione. Avvia il server sulla porta specificata.
     * 
     * @param args Argomenti della riga di comando (non utilizzati in questo caso).
     */
    public static void main(String[] args) {
        int port = 8080; // Puoi cambiare la porta
        MultiServer server = new MultiServer(port);
        server.start();
    }
}
