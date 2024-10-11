import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;
import java.util.Scanner;
public class MainTest {

	/**
	 * @param args
	 * @throws InvalidDepthException 
	 */
	public static void main(String[] args) throws InvalidDepthException {
		Scanner scanner = new Scanner(System.in);
		
		Data data =new Data();
		System.out.println(data);
		int depth;
        do {
			try{
			System.out.print("Inserisci la profondità del dendrogramma (deve essere >= 1): ");
            depth = scanner.nextInt();
            if (depth < 1) {
                throw new InvalidDepthException("Profondità non valida, selezionare numero >=1, riprova");
            } else if (depth>data.getNumberOfExample()) {
				throw new InvalidDepthException("profondità del dendrogramma è superiore al numero di esempi memorizzati nel dataset, riprova");
			}
         // Se non ci sono eccezioni, esci dal ciclo
		 break;

		} catch (InvalidDepthException e) {
			// Stampa il messaggio di errore e riprova
			System.out.println("Errore: " + e.getMessage());
		} catch (Exception e) {
			// Gestione di eventuali errori di input non validi
			System.out.println("Errore di input: inserire un numero valido.");
			scanner.next(); // Pulisce il buffer dello scanner per evitare loop infiniti
		}
	} while (true);

		HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
		// Creazione dell'oggetto ClusterDistance in base alla scelta dell'utente
		ClusterDistance distance = null;
       // Scelta del tipo di distanza tra cluster
	   do {
		   System.out.println("Scegli il tipo di distanza tra cluster:");
		   System.out.println("1. Single link distance");
		   System.out.println("2. Average link distance");
		   int scelta = scanner.nextInt();
		   switch (scelta) {
			   case 1 -> distance = new SingleLinkDistance();
			   case 2 -> distance = new AverageLinkDistance();
			   default -> System.out.println("Scelta non valida. Riprova.");
		   }
	   } while (distance == null);
	
		
		double [][] distancematrix=data.distance();
		System.out.println("Distance matrix:\n");
		for(int i=0;i<distancematrix.length;i++) {
			for(int j=0;j<distancematrix.length;j++)
				System.out.print(distancematrix[i][j]+"\t");
			System.out.println("");
		}
		clustering.mine(data,distance);
		System.out.println(clustering);
		System.out.println(clustering.toString(data));
		
		
		System.out.println("Average link distance");
		distance=new AverageLinkDistance();
		clustering.mine(data,distance);
		System.out.println(clustering);
		System.out.println(clustering.toString(data));
		
	}

}
