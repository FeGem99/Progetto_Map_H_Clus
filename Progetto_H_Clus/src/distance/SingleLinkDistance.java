package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

public class SingleLinkDistance implements ClusterDistance {
	public double distance(Cluster c1, Cluster c2, Data d) {
		
		double min=Double.MAX_VALUE;
		
		for (Integer id1 : c1)
		{
			Example e1=d.getExample(id1);
			for(Integer id2 : c2) {
				double distance=e1.distance(d.getExample(id2));
				if (distance<min)				
					min=distance;
			}
		}
		return min;
	}
}
