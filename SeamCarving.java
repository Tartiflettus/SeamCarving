import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{

   public static int[][] readpgm(String fn)
	 {		
        try {
            //InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));
            String magic = d.readLine();
            String line = d.readLine();
		   while (line.startsWith("#")) {
			  line = d.readLine();
		   }
		   Scanner s = new Scanner(line);
		   int width = s.nextInt();
		   int height = s.nextInt();		   
		   line = d.readLine();
		   s = new Scanner(line);
		   int maxVal = s.nextInt();
		   int[][] im = new int[height][width];
		   s = new Scanner(d);
		   int count = 0;
		   while (count < height*width) {
			  im[count / width][count % width] = s.nextInt();
			  count++;
		   }
		   return im;
        }
		
        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
    }
   
   public static void writepgm(int[][] image, String filename){
	   try {
		   
		PrintWriter ow = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		ow.println("P2"); //magic number for PGM
		ow.print(image[0].length); //number of columns
		ow.print(" ");
		ow.println(image.length); //number of lines
		ow.println(65535); //max intensity
		
		for(int i=0; i < image.length; i++){
			for(int j=0; j < image[i].length; j++){
				ow.print(image[i][j] + " ");
			}
			ow.println();
		}
		
		ow.close();
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
   }
   
   public static int[][] interest(int[][] image){
	   int[][] res = new int[image.length][image[0].length];
	   
	   for(int i=0; i < image.length; i++){
		   for(int j=0; j < image[i].length; j++){
			   res[i][j] = fInterest(image, i, j);
		   }
	   }
	   
	   return res;
   }
   
   private static int fInterest(int[][] image, int i, int j){
	   if(j == 0){
		   return Math.abs(image[i][j] - image[i][1]); //no left neighbor
	   }
	   if(j == image[0].length-1){
		   return Math.abs(image[i][j] - image[i][image[0].length-2]); //no right neighbor
	   }
	   return Math.abs(image[i][j] - (image[i][j-1] + image[i][j+1])/2);
   }
   
   
   public static Graph tograph(int[][] itr){
	   GraphArrayList g = new GraphArrayList(itr.length*itr[0].length + 2/*A and P*/);
	   for(int i=0; i < itr.length-1; i++){
		   for(int j=0; j < itr[i].length; j++){
			   int src = i*itr.length + j;
			   int targetBase = src + itr.length;
			   g.addEdge(new Edge(src, targetBase, itr[i][j]));
			   
			   if(j != 0){
				   g.addEdge(new Edge(src, targetBase-1, itr[i][j]));
			   }
			   if(j != itr[i].length-1){
				   g.addEdge(new Edge(src, targetBase+1, itr[i][j]));
			   }
		   }
	   }
	   
	   //A edges
	   for(int i=0; i < itr[0].length; i++){
		   g.addEdge(new Edge(g.vertices()-2, i, 0));
	   }
	   
	   //P edges
	   final int nbLines = itr.length;
	   final int nbCols = itr[0].length;
	   for(int i=0; i < itr[0].length; i++){
		   g.addEdge(new Edge((nbLines-1)*nbCols + i, g.vertices()-1, itr[nbLines-1][i]));
	   }
	   
	   return g;
   }
   
   
   public static List<Integer> Bellman(Graph g, int s, int t, ArrayList<Integer> order){
	   List<Integer> res = new ArrayList<Integer>();
	   int T[] = new int[g.vertices()];
	   int history[] = new int[g.vertices()];
	   T[order.get(0)] = 0;
	   order.remove(0);
	   
	   boolean finished = false;
	   while(! finished){
		   int vertex = order.get(0);
		   //System.out.println("vertex : " + vertex);
		   order.remove(0);
		   finished = order.isEmpty();
		   
		   int min = 70000; //infinite
		   int vertexOfMin = vertex;
		   for(Edge neighbor : g.prev(vertex)){
			   int cost = neighbor.cost + T[neighbor.from];
			   if(cost < min){
				   min = cost;
				   vertexOfMin = neighbor.from;
			   }
		   }
		   T[vertex] = min < T[vertex] ? min : T[vertex];
		   history[vertex] = vertexOfMin;
	   }
	   
	   //get the best path
	   int prev = t;
	   res.add(t);
	   while(history[prev] != s){
		   res.add(history[prev]);
		   prev = history[prev];
	   }
	   res.add(s);
	   
	   //invert elements
	   for(int i=0; i < res.size()/2; i++){
		   int tmp = res.get(i);
		   res.set(i, res.get(res.size()-i-1));
		   res.set(res.size()-i-1, tmp);
	   }
	   
	   return res;
   }
   
   
   public static void testWrite(){
	   int[][] image = new int[2][2];
	   image[0][1] = 1;
	   image[1][1] = 6543;
	   SeamCarving.writepgm(image, "test.pgm");
   }
   
   public static void testRead(){
	   int[][] image = readpgm("test.pgm");
	   
	   StringBuilder sb = new StringBuilder();
	   for(int i=0; i < image.length; i++){
		   for(int j=0; j < image[i].length; j++){
			   sb.append(image[i][j] + " ");
		   }
		   sb.append("\n");
	   }
	   System.out.println("test de read:");
	   System.out.println(sb.toString());
   }
   
   public static void testInterest(){
	   int[][] image = new int[2][3];
	   image[0][0] = 1;
	   image[0][1] = 5;
	   image[0][2] = 1;
	   image[1][0] = 2;
	   image[1][1] = 2;
	   image[1][2] = 9;
	   int[][] res = SeamCarving.interest(image);
	   
	   System.out.println("test de interest:");
	   for(int i=0; i < res.length; i++){
		   for(int j=0; j < res[i].length; j++){
			   System.out.print(res[i][j] + " ");
		   }
		   System.out.println();
	   }
	   
   }
   
   public static void testGraph(){
	   int[][] itr = new int[2][2];
	   itr[1][1] = 45;
	   itr[0][0] = 7;
	   itr[0][1] = 6;
	   Graph g = tograph(itr);
	   g.writeFile("output.txt");
   }
   
   public static void testbellman(){
	   int[][] itr = new int[2][2];
	   itr[0][0] = 0;
	   itr[0][1] = 55;
	   itr[1][0] = 0;
	   itr[1][1] = 78;
	   
	   Graph g = tograph(itr);
	   g.writeFile("output.txt");
	   ArrayList<Integer> order = (ArrayList<Integer>) DFS.tritopo(g, g.vertices()-2);
	   System.out.println(order);
	   List<Integer> path = Bellman(g, g.vertices()-2, g.vertices()-1, order);
	   
	   System.out.println(path);
   }
   
   
   public static void main(String args[]){
	   //SeamCarving.testWrite();
	   //SeamCarving.testRead();
	   //SeamCarving.testInterest();
	   //SeamCarving.testGraph();
	   SeamCarving.testbellman();
	   /*ArrayList<Integer> b = new ArrayList<Integer>();
	   b.add(1);
	   b.add(2);
	   b.add(0);
	   System.out.println(b.get(0));
	   b.remove(0);
	   System.out.println(b);*/

   }

   
}
