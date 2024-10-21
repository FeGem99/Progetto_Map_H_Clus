package clustering;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;



public class Cluster implements Iterable<Integer>, Cloneable, Serializable{
	//attributo
	private Set<Integer> clusteredData=new TreeSet<>();
	
        @Override
	public Iterator<Integer> iterator(){
		return clusteredData.iterator();
	}
		
	//add the index of a sample to the cluster
	void addData(int id){
		clusteredData.add(id);		
	}
		
	
	public int getSize() {
		return clusteredData.size();
	}
	

	
	public Object clone() {
		Cluster copyC = new Cluster();
		copyC.clusteredData.addAll(this.clusteredData);
		return copyC;
	}

	
	Cluster mergeCluster (Cluster c)
	{
		Cluster newC=new Cluster();
			newC.clusteredData.addAll(this.clusteredData);
			newC.clusteredData.addAll(c.clusteredData);
		return newC;
		
	}
	
	
	public String toString() {		
		StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = clusteredData.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
	}
	
	String toString(Data data){
		StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = clusteredData.iterator();
        while (iterator.hasNext()) {
            sb.append("<").append(data.getExample(iterator.next())).append(">");
        }
        return sb.toString();
	}
		
	}
