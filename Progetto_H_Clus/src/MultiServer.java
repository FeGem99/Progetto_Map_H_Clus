import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
    private int port;

    public MultiServer(int port) {
        this.port = port;
    }

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

    public static void main(String[] args) {
        int port = 8080; // Puoi cambiare la porta
        MultiServer server = new MultiServer(port);
        server.start();
    }
}
