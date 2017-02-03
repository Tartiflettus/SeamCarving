import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{
	
	private static int inputDepth = 65535;

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
		   inputDepth = maxVal;
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
		ow.println(inputDepth); //max intensity
		
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
			   int src = i*itr[0].length + j;
			   int targetBase = src + itr[0].length;
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
	   for(int i=0; i < nbCols; i++){
		   g.addEdge(new Edge((nbLines-1)*nbCols + i, g.vertices()-1, itr[nbLines-1][i]));
	   }
	   
	   return g;
   }
   
   
   public static ArrayList<Integer> Bellman(Graph g, int s, int t, ArrayList<Integer> order){
	   ArrayList<Integer> res = new ArrayList<Integer>();
	   int T[] = new int[g.vertices()];
	   int history[] = new int[g.vertices()];
	   for(int i=0; i < T.length; i++){
		   T[i] = 70000;
	   }
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
	   //System.out.println("t: "+t);
	   //System.out.println("history[t]: " + history[t]);
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
   
   
   
   public static int[][] suppressColumn(int[][] image, ArrayList<Integer> column){	   
	   final int width = image[0].length;
	   final int height = image.length;
	   int[][] nouvelleImage = new int[height][width-1];
	   //System.out.println("image dimension: " + width + " ; " + height);
	   //System.out.println("selected column : " + column);

	   
	   for(int j=1; j < column.size()-1; j++){
		   //final int pix = column.get(j);
		   
		   //correct the line
		   final int colPixSuppr = column.get(j) % width;
		   int i=0;
		   
		   for(i=0; i != colPixSuppr; i++){ //before pix
			   nouvelleImage[j-1][i] = image[j-1][i];
		   }
		   for( ; i < width-1; i++){ //after pix
			   nouvelleImage[j-1][i] = image[j-1][i+1];
		   }
		   
		   
	   }
	   return nouvelleImage;
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
	   g.writeFile("output.pgm");
	   ArrayList<Integer> order = (ArrayList<Integer>) DFS.tritopo(g, g.vertices()-2);
	   //System.out.println(order);
	   List<Integer> path = Bellman(g, g.vertices()-2, g.vertices()-1, order);
	   
	 //  System.out.println(path);
   }
   
   
   public static void testInterestInput(){
	   int[][] image = readpgm("input.pgm");
	   int[][] interest = interest(image);
	   
	  /* for(int i=0; i < image.length; i++){
		   for(int j=0; j < image[i].length; j++){
			   System.out.print(image[i][j]+ " ");
		   }
		   System.out.println();
	   }*/
   }
   
   /*public static void maleficCode(int [][] image){
	   int temp;
	   for(int i=1; i < image.length; i++){
		   for(int k=0; k < i; k++){
			   temp = image[i][0];
			   for(int j=1; j < image[0].length; j++){
				   image[i][j-1] = image[i][j];
			   }
			   image[i][image[0].length-1] = temp; 
		   }
	   }
   }*/
   
   public static void main(String args[]){
	   
	   if(args.length < 2){
		   System.err.println("usage : java SeamCarving <filename> <col number>\n");
		   return;
	   }
	   
	   
	   System.out.println("Vous pouvez aller chercher un cafe ;)");
	   
	   
	   int[][] image = readpgm(args[0]);
	//   System.out.println("dimensions avant opération : " + image.length + " ; " + image[0].length);
	   
	   if(image.length <= Integer.parseInt(args[1]) || image[0].length == 0){
		   System.err.println("Image trop petite par rapport a l'argument");
	   }
	   
	   for(int i = 0; i < Integer.parseInt(args[1]); i++){
		   int[][] interest = interest(image);
	   		Graph graph = tograph(interest);
	   		ArrayList<Integer> order = DFS.tritopo(graph, graph.vertices()-2);
	   		ArrayList<Integer> selectedColumn = Bellman(graph, graph.vertices()-2, graph.vertices()-1, order);
	   		image = suppressColumn(image, selectedColumn);
	   		System.out.println((i+1)+"/"+args[1]);
	   }
	  // System.out.println("dimensions après opération : " + image.length + " ; " + image[0].length);
	   //maleficCode(image);
	   writepgm(image, "output.pgm");
	   
	   System.out.println("Merci de votre patience !!!! :D");
	   
   }

   
}
