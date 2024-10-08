

public class AverageLinkDistance implements ClusterDistance{
@Override
public double distance(Cluster c1, Cluster c2, Data d) {
    double totalDistance = 0.0;
    int numDistance = 0;

    for (int i=0; i<c1.getSize(); i++){
        Example e1 = d.getExample(c1.getElement(i));

        for(int j=0; j<c2.getSize(); j++){
            Example e2 = d.getExample(c2.getElement(j))
            totalDistance += e1.distance(e2);
            numDistance++;
        }
    }
    return totalDistance/numDistance;
}
}