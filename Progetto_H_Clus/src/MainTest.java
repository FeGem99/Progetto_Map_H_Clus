import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

public class MainTest {

	/**
	 * @param args
	 * @throws InvalidDepthException 
	 */
	public static void main(String[] args) throws InvalidDepthException {
		
		
		Data data =new Data();
		System.out.println(data);
		int k=5;
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
