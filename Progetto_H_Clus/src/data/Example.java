package data;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Example implements Iterable<Double>{
    public List<Double> example;
    public Example ()
    {
    example=new LinkedList<>();
    }
    public Iterator<Double> iterator() {
    return example.iterator();
    }
    public void add(Double v) {
    example.add(v);
    }
    Double get(int index) {
    return example.get(index);
    }

    public double distance (Example newE){
        Double sum=0.0; 
        Iterator<Double> thisIter = this.iterator();
        Iterator<Double> newIter = newE.iterator();
        
   while (thisIter.hasNext() && newIter.hasNext()){
                Double diff= thisIter.next()-newIter.next();
                sum += diff * diff;
            }
            return sum;
    }

   @Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (int i = 0; i < example.size(); i++) {
	        sb.append(example.get(i));
	        if (i < example.size() - 1) {
	            sb.append(", ");
	        }
	    }
	    sb.append("]");
	    return sb.toString();
	}
}
