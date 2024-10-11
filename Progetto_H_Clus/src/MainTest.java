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
		
		
		Data data =new Data();
		System.out.println(data);
		int k;
		try (Scanner scanner = new Scanner(System.in)){
			System.out.println("inserisci la profondità del Dendrogramma: ");
			k = scanner.nextInt();
			if(k<2){
				throw new InvalidDepthException("selezionare un numero maggiore di 2");
			}
		// Aggiungiamo il menu per selezionare il tipo di misura di distanza
		System.out.println("Seleziona il tipo di misura di distanza:");
		System.out.println("1. Single Link Distance");
		System.out.println("2. Average Link Distance");
		int scelta = scanner.nextInt();

		// Creazione dell'oggetto ClusterDistance in base alla scelta dell'utente
		ClusterDistance distance;
		if (scelta == 1) {
			distance = new SingleLinkDistance();
			System.out.println("Hai selezionato: Single Link Distance");
		} else if (scelta == 2) {
			distance = new AverageLinkDistance();
			System.out.println("Hai selezionato: Average Link Distance");
		} else {
			System.out.println("Scelta non valida. Verrà utilizzata di default la Single Link Distance.");
			distance = new SingleLinkDistance();
		}
		}
		
		
		
		HierachicalClusterMiner clustering=new HierachicalClusterMiner(k);
		
		System.out.println("Single link distance");
		ClusterDistance distance=new SingleLinkDistance();
		
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
