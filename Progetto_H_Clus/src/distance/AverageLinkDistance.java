package distance;
import clustering.Cluster;
import data.Data;
import data.Example;


public class AverageLinkDistance implements ClusterDistance {
@Override
public double distance(Cluster c1, Cluster c2, Data d) {
    double totalDistance = 0.0;
    int numDistance = 0;

    for (Integer id1: c1) {
        Example e1 = d.getExample(id1);

        for(Integer id2 : c2){
            Example e2 = d.getExample(id2);
            totalDistance += e1.distance(e2);
            numDistance++;
        }
    }
    return totalDistance/numDistance;
}
}
