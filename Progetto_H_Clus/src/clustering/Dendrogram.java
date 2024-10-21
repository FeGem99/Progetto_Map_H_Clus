package clustering;

import java.io.Serializable;

import data.Data;

public class Dendrogram implements Serializable{
    private static final long serialVersionUID = 1L;
    public ClusterSet tree[]; //modella il dendrogramma

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

public int getDepth() {
    return tree.length;
}

public String toString() {
    String v=
    "";
    for (int i=0;i<tree.length;i++)
    v+=("level"+i+":\n"+tree[i]+"\n");
    return v;
    }
    String toString(Data data) {
    String v=
    "";
    for (int i=0;i<tree.length;i++)
    v+=("level"+i+":\n"+tree[i].toString(data)+"\n");
    return v;
    }
    

}
