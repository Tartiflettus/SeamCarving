import java.util.Iterator;

public class Vertex{
		public int u;
		public Iterator<Edge> adjs;
		
		public Vertex(int v, Iterator<Edge> adjs){
			this.u = v;
			this.adjs = adjs;
		}
	}
