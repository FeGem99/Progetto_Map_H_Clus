public class Dendrogram {
    private ClusterSet tree[]; //modella il dendrogramma

public Dendrogram (int depth) {
        tree =new ClusterSet[depth];
    }
void setClusterSet(ClusterSet c, int level){
    if (level>= 0 && level < tree.length){
        tree[level]=c;
    } else {
        throw new IllegalArgumentException("livello fuori dai limiti del dendrogramma");
    }
    }

ClusterSet getClusterSet(int level){
    if (level >= 0 && level < tree.length){
        return tree[level];
    } else {
        throw new IllegalArgumentException("livello fuori dai limiti del dendrogramma");
    }
  
}
    


}
