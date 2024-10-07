public class Example {
   
    private Double [] example; //attributo della classe Example
    // Costruttore di example di lunghezza lenght
    public Example (int length){
        example = new Double[length];
    }

    public void set(int index, Double v){
        if (index>=0 && index< example.length){
            example[index]= v;
        }
        else {
            throw new IndexOutOfBoundsException("indice non valido");
        }
    }

    public Double get(int index){
        if (index>=0 && index<example.length){
            return example[index];
        }
        else {
            throw new IndexOutOfBoundsException("indice non valido");
        }

    }

    public double distance (Example newE){
        Double sum=0.0; 
        if (this.example.length == newE.example.length){
            for(int i=0;i<this.example.length; i++){
                Double diff= this.example[i]-newE.example[i];
                sum += diff * diff;
            }
            return sum; 
        } else {
            throw new IllegalArgumentException("i vettori devono avere la stessa lunghezza");
        }
    }

   @Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (int i = 0; i < example.length; i++) {
	        sb.append(example[i]);
	        if (i < example.length - 1) {
	            sb.append(", ");
	        }
	    }
	    sb.append("]");
	    return sb.toString();
	}
}
