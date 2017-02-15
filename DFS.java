import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

class DFS
{
	
    
    public static void botched_dfs1(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	visited[s] = true;	    	
	while (!stack.empty()){
	    int u = stack.pop();
	    System.out.println(u);
	    for (Edge e: g.next(u))
		if (!visited[e.to])
		    {
			visited[e.to] = true;
			stack.push(e.to);
		    }
	}
    }

    public static void botched_dfs2(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	System.out.println(s);
	visited[s] = true;	    	
	while (!stack.empty()){
	    int u = stack.pop();
	    for (Edge e: g.next(u))
		if (!visited[e.to])
		    {
			System.out.println(e.to);			
			visited[e.to] = true;
			stack.push(e.to);
		    }
	}
    }
    
    public static void botched_dfs3(Graph g, int s){
	Stack<Integer> stack = new Stack<Integer>();
	boolean visited[] = new boolean[g.vertices()];
	stack.push(s);
	while (!stack.empty()){
	    int u = stack.pop();
	    if (!visited[u]){
		visited[u] = true;
		System.out.println(u);
		for (Edge e: g.next(u))
		    if (!visited[e.to])
			   stack.push(e.to);
		
	    }
	}
    }

    
    public static ArrayList<Integer> tritopo_save(Graph g, int s){
	    ArrayList<Integer> res = new ArrayList<Integer>(g.vertices());
	    	
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[g.vertices()];
		stack.push(s);
		visited[s] = true;
		//System.out.println(s);
		while (!stack.empty()){
		    boolean end = true;
		    /* (a) Soit u le sommet en haut de la pile */
		    /* (b) Si u a un voisin non visité, alors */
		    /*     (c) on le visite et on l'ajoute sur la pile */
		    /* Sinon */
		    /*     (d) on enlève u de la pile */
		   
		    /* (a) */
		    int u = stack.peek();
		    for (Edge e: g.next(u))
			if (!visited[e.to]) /* (b) */
			    {
				visited[e.to] = true;
				//System.out.println(e.to);			
				stack.push(e.to); /*(c) */
				end = false;
				break;
			    }
		    if (end) /*(d)*/{
		    	res.add(stack.peek());
				stack.pop();
		    }
		}
		
		
		res.trimToSize();
		for(int i=0; i < res.size()/2; i++){
			int tmp = res.get(i);
			res.set(i, res.get(res.size()-1-i));
			res.set(res.size()-1-i, tmp);
		}
		return res;
    }


    public static ArrayList<Integer> tritopo(Graph g, int s){
    	ArrayList<Integer> res = new ArrayList<Integer>(g.vertices());
    	
    	Stack<Vertex> stack = new Stack<Vertex>();
    	stack.add(new Vertex(s, g.next(s).iterator()) );
    	boolean[] visited = new boolean[g.vertices()];
    	for(int i=0; i < visited.length; i++){
    		visited[i] = false;
    	}
    	visited[s] = true;
    	
    	while(!stack.isEmpty()){
    		Vertex peek = stack.peek();
    		if(peek.adjs.hasNext()){
    			//u has a neighbor
    			int v = peek.adjs.next().to;
    			if(visited[v]){
    				//already visited
    				continue; //return to the loop's begin
    			}else{
    				//not visited yet
    				visited[v] = true;
    				stack.push(new Vertex(v, g.next(v).iterator()));
    			}
    		}else{
    			//u has no neighbor => finished visiting => add to suffix order
    			stack.pop();
    			res.add(peek.u);
    		}
    	}
    	
    	
    	for(int i=0; i < res.size()/2; i++){
			int tmp = res.get(i);
			res.set(i, res.get(res.size()-1-i));
			res.set(res.size()-1-i, tmp);
		}
    	
    	return res;
    }

    
    
    public static void testGraph()
    {
		GraphArrayList g = new GraphArrayList(6);
		g.addEdge(new Edge(4, 0, 0));
		g.addEdge(new Edge(4, 1, 0));
		g.addEdge(new Edge(0, 2, 0));
		g.addEdge(new Edge(0, 3, 0));
		g.addEdge(new Edge(1, 2, 0));
		g.addEdge(new Edge(1, 3, 0));
		g.addEdge(new Edge(2, 5, 0));
		g.addEdge(new Edge(3, 5, 0));
	
	
		List<Integer> res = tritopo(g, g.vertices()-2);
		System.out.println(res);

    }
    
    public static void main(String[] args)
    {
	testGraph();
    }
}
