import java.awt.HeadlessException;
import java.util.ArrayList;

class GraphImplicit implements Graph{
    private int N;
    private int[][] interest;
    private int w;
    private int h;
    
    
@SuppressWarnings("unchecked")    
    GraphImplicit(int interest[][], int w, int h){
		this.N = w*h + 2;
		this.w = w;
		this.h = h;
		this.interest = interest;
    }

    public int vertices(){
    	return N;
    }
    
    @SuppressWarnings("unchecked")    
    public Iterable<Edge> next(int v)
	{
	     ArrayList<Edge> edges = new ArrayList<Edge>();
	     
	     if(v == vertices()-1){
	    	 return edges;
	     }
	     
	     if(v == vertices()-2){
	    	 for(int i=0; i < w; i++){
	    		 edges.add(new Edge(v, i, 0));
	    	 }
	    	 return edges;
	     }
	     
	     if(v >= w*h - w){
	    	 edges.add(new Edge(v, vertices()-1, interest[v/w][v%w]));
	    	 return edges;
	     }
	     
	     edges.add(new Edge(v, v + w, interest[v/w][v%w]));
	     if(v%w != 0){
	    	 edges.add(new Edge(v, v+w-1, interest[v/w][v%w]));
	     }
	     if(v%w != w-1){
	    	 edges.add(new Edge(v, v+w+1, interest[v/w][v%w]));
	     }
	     return edges;
		      
	}



@SuppressWarnings("unchecked")
   public Iterable<Edge> prev(int v)
	 {
	     ArrayList<Edge> edges = new ArrayList();
	     for (int i = 0; i < v-1; i++)
		  edges.add(new Edge(i,v,v));
	     return edges;
		      
	 }
}
