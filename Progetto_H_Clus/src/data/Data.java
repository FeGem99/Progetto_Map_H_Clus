package data;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Data {
	private List<Example> data= new ArrayList<>(); //rappresenta il dataset
	private int numberOfExamples; //numero di esempi nel dataset
	
	
	public Data(){
			inizializeData();
	}
		//inizializza il dataset predefinito
		private void inizializeData(){
		
		Example e=new Example();
		e.add(1.0);
		e.add(2.0);
		e.add(0.0);
		data.add(e);
		
		e=new Example();
		e.add(0.0);
		e.add(1.0);
		e.add(-1.0);
		data.add(e);
		
		e=new Example();
		e.add(1.0);
		e.add(3.0);
		e.add(5.0);
		data.add(e);
		
		
		e=new Example();
		e.add(1.0);
		e.add(3.0);
		e.add(4.0);
		data.add(e);
		
		e=new Example();
		e.add(2.0);
		e.add(2.0);
		e.add(0.0);
		data.add(e);
		}
		

	public int getNumberOfExample(){
		return data.size();
	}
	
	public Example getExample(int exampleIndex){
		if(exampleIndex>=0 && exampleIndex<data.size()){
			return data.get(exampleIndex);
		} else {
			throw new IndexOutOfBoundsException("indice non valido");
		}
		}
		
		public double [][] distance() {
			double [][] distanceMatrix = new double[numberOfExamples][numberOfExamples];
			for (int i = 0; i < numberOfExamples; i++) {
				for (int j = i + 1; j < numberOfExamples; j++) {
					double dist = getExample(i).distance(getExample(j));
					distanceMatrix[i][j] = dist; 
					distanceMatrix[j][i] = dist;
					
				}
			}
			return distanceMatrix;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			Iterator<Example> iter = data.iterator();
			int index = 0;
			while (iter.hasNext()) {
				sb.append("Example ").append(index).append(": ").append(iter.next().toString()).append("\n");
				index++;
			}
			return sb.toString();
		}
	

	public static void main(String args[]){
		Data trainingSet=new Data();
		System.out.println(trainingSet);
		
		double [][] distancematrix=trainingSet.distance();
		System.out.println("Distance matrix:\n");
		for(int i=0;i<distancematrix.length;i++) {
			for(int j=0;j<distancematrix.length;j++)
				System.out.print(distancematrix[i][j]+"\t");
			System.out.println("");
		}
	
	}
}