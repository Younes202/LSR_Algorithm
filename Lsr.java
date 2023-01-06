//JavaClient

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

//java Lsr A 2000 config.txt


//Packet: Vertex, Edge, Seq




public class Lsr
{
    //private int[][] edges; // adjacency matrix
    //private Object[] labels;

    
    private static final int AVERAGE_DELAY = 100;
    public static final int[] prevcount = new int[100];


    public static class SimpleThreads extends Thread 
    {
        String str;
	int[] count;
	int[] prevcount;
	WeightedGraph Graph;
	//int initialize = 0;
	int initialize = 0;
	int i;
	//Timer timer = new Timer();
	//timer.schedule(SimpleThreads(Graph,count), 1000);	

	public SimpleThreads(WeightedGraph Gra, int[] cou) {
	    Graph = Gra;
	    count = cou;
	    prevcount = new int[count.length];
	    //prevcount[0] = count[0];
	}


	public void run()
	{

	    
	    TimerTask TiTask = new TimerTask(){
		    public void run()
		    {
			i++;



			if(initialize == 0)
			    {
				System.out.println("Timer is running...");
				for(int m = 0;m < count.length;m++)
				    {
					prevcount[m] = count[m];
				    }
				//TimerTask tasknew = new SimpleThreads(Graph,count);
				initialize = 1;
			    }
			else
			    {

				System.out.println("Timer is running...");

				
				for(int q=0;q<5;q++)
				    {
					//System.out.println("This Count: " + count[q]);
					
					if(count[q] == prevcount[q] && count[q]!=0 && prevcount[q]!=0 )
					    {
						//System.out.println(count[q]);
						//System.out.println(prevcount[q]);						
						System.out.println(Graph.getLabel(q) + " no information");
					    }
					
				    }
				for(int m = 0;m < count.length;m++)
				    {
					prevcount[m] = count[m];
				    }
			    
			    }
			

			
			
		    }

		};

	    System.out.println("thread is running...");
	    Timer NewTimer = new Timer();
	    NewTimer.scheduleAtFixedRate(TiTask,15000,10000);
	    //NewTimer.scheduleAtFixedRate(TiTask,10000,4000);
	    

			/*try {
			    Thread.sleep(100000);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}*/
			//prevcount = count;
	}

    }





    //Main
    public static void main(String args[])throws Exception
    {
	
	
	
    final WeightedGraph Graph = new WeightedGraph(100);
        
    
    
    //Initializaion
	//int i = 0;	
	//String ServerName = args[0];
	//String Node = args[0];
    String ServerName = "127.0.0.1";
    InetAddress Address = InetAddress.getByName(ServerName);
    String MainNode = args[0];
    int MainPort = Integer.parseInt(args[1]);	
    String ConfigFileName = args[2];
    System.out.println(ConfigFileName);	
    
    Graph.setLabel(0, MainNode);
    
    
    

       
    //readConfig(ConfigFileName); // Read the configfile and get the information.	
    File ConfigFile = new File(ConfigFileName);
    BufferedReader reader = null;
    reader = new BufferedReader(new FileReader(ConfigFile));
    String line = null;
    line = reader.readLine();
    int nNum = Integer.parseInt(line);
    System.out.println("nNum is: " + nNum);
    

    LinkedList<String> NodeQueue1 = new LinkedList<String>();
    
    

    LinkedList<String> NodeQueue2 = new LinkedList<String>();
    
    int[] Port = new int[nNum];
    
    //String[] test = new String[nNum];

 
    LinkedList<Integer> cost = new LinkedList<Integer>();
    

    /*
    int[] visited = new int[nNum];
    
    for(int j=0;j<visited.length;j++)
	    {
		visited[j] = 0;
	    }
    */



    //int[] Dist = new int[nNum];
    int i = 0;

    while((line = reader.readLine())!= null)
	{
	    //	    
	    //test[i] = line;
	    //
	    String[] temp;
	    System.out.println("line" + ":" + line);		 
	    temp = line.split(" ");
	    
	    //NodeQueue2[i] = temp[0];
	    NodeQueue2.add(temp[0]);
	    //NodeQueue1[i] = MainNode;
	    NodeQueue1.add(MainNode);
	    
	    System.out.println(NodeQueue2.getLast() + "!");
	    
	    //cost[i] = Integer.parseInt(temp[1]);
	    cost.add(Integer.parseInt(temp[1]));
	    
	    System.out.println(cost.getLast() + "!!");
	    
	    Port[i] = Integer.parseInt(temp[2]);
	    System.out.println(Port[i] + "!!!");
	    
	    
	    //Graph.setLabel(i+1, Neighbour[i]);
	    Graph.setLabel(i+1, NodeQueue2.getLast());
	    
	    //Graph.addEdge(0,i+1,cost[i]);
	    Graph.addEdge(0,i+1,Integer.parseInt(temp[1]));
	    
	    i++;
	    
	}
    reader.close();
    
    Graph.print();
    
    int GraphNum = i;
    int ReceiveTime = 0;
    double[] Time = new double[100];


    //int GTest = i;
    int[] count = new int[100];
    int[] precount = new int[100];
    for(int n=0;n<count.length;n++)
	{
	    count[n] = 0;
	}
 	
    //Send the StatePacket
    DatagramSocket MainSocket = new DatagramSocket(MainPort);
    
    
    int Seq = 0;
    
    //for(i=0;i<nNum;i++)
	while(true)  
	    {
		//try{
		    //MainSocket.setSoTimeout(1000);
		    for(i=0;i<nNum;i++)
			{			    
			    //PacketCreate
			    byte[] buf = new byte[102400];
			    String Str = "";
			    String All = "";
			    for(int j=0;j<NodeQueue2.size();j++)
				{
				    Str = Str + "/" + NodeQueue1.get(j) + "," + NodeQueue2.get(j) + "," + cost.get(j);

				}
			    //System.out.println(Str);
			    
			    
			    All = MainNode + ">" + Seq + ">" + Str;
			    buf = All.getBytes();
			    
			    DatagramPacket StatePacket = new DatagramPacket(buf,buf.length,Address,Port[i]);
			    MainSocket.send(StatePacket);
			    


			    // System.out.println("******************************");
			    //System.out.println(Str.length());
			    //System.out.println("******************************");
			    //System.out.println(Str);
			    ;
			}
		    Seq++;
		    
		    System.out.println("Send!");
		    //try{
		    //timeout
		    //PingClient.setSoTimeout(1000);
		    DatagramPacket ReceivePacket = new DatagramPacket(new byte[102400],102400);
		    MainSocket.receive(ReceivePacket);
		    
		    ReceiveTime++;
		    

		    String GetStr = new String(ReceivePacket.getData());
;
		    
		    //System.out.println(GetStr);
		    //System.out.println("000000");
		    String[] WholeString;
		    WholeString = GetStr.split(">");

		    //System.out.println("SourceNode: "+ WholeString[0]);
		    //System.out.println("Seq: "+ WholeString[1]);
		    String SourceNode = WholeString[0];
		    String SourceSeq = WholeString[1];

		    String[] temp1;		 
		    temp1 = WholeString[2].split("/");
		    String newStr = "";
		    
		    

		    //Check Wheather the node has failed
		    
		    int SourceVertex = Graph.GetVertex(Graph,SourceNode);
		    //System.out.println("SourceVertex: " + SourceVertex);

		    count[SourceVertex]++;
		    //System.out.println("Count: " + count[SourceVertex]);
		    //System.out.println("ReceiveTime: " + ReceiveTime);

		    //Time[SourceVertex] = System.nanoTime();
		    //double NowTime =  System.nanoTime();

		    
		    SimpleThreads t = new SimpleThreads(Graph,count);

		    t.start();
			


		    //if(ReceiveTime % 6 == 5 )
			{
			    //	    t.cancel();
			}
		    /*
		    for(int q=0;q<GraphNum && ReceiveTime >(GraphNum * 4);q++)
			{
			    System.out.println("This Count: " + count[q]);
			    System.out.println("ReceiveTime: " + ReceiveTime);
			    if(NowTime - Time[q] >100000*100000*100000*100000 && count[q]< ReceiveTime/(2*GraphNum))
				{
				    //System.out.println("count  :" + count[q]);
				    System.out.println(Graph.getLabel(q) + " Failed!!!");
				}
			}
		    */

		    //Add new edge into the graph
		    for(i = 1;i < temp1.length;i++)
		    {
			newStr = newStr + temp1[i];
			
			String[] temp2;
			temp2 = temp1[i].split(",");
			//for(j = 0;j<temp2.length;j++)
			//  {

			

				String Node1 = temp2[0];
				String Node2 = temp2[1];
				String Edgecost = temp2[2];
				int Ecost = Integer.parseInt(Edgecost.trim());	

				//Use LinkedList to store the value
				if((NodeQueue1.indexOf(Node1) != NodeQueue2.indexOf(Node2)) ||(NodeQueue1.indexOf(Node1) == -1 &&  NodeQueue2.indexOf(Node2) == -1))
				    {
					NodeQueue1.add(Node1);
					NodeQueue2.add(Node2);
					cost.add(Ecost);
				    }


				int Vertex1 = GraphNum+1;
				int Vertex2 = GraphNum+1;

				
				if(Graph.CheckEdge(Graph,Node1,Node2)!=-1)
				    {
					//Graph.addEdge(Node1,Node2,Edgecost);
					//System.out.println("aaaaaaaaaaa");
					//Graph.print();
				    }

				if(Graph.GetVertex(Graph,Node1) == -1)
				    {
					Graph.setLabel(GraphNum+1, Node1);
					GraphNum++;
					//System.out.println("bbbbbbbb");
				    }
				else
				    {
					Vertex1 = Graph.GetVertex(Graph,Node1);
				    }
				if(Graph.GetVertex(Graph,Node2) == -1)
				    {
					Graph.setLabel(GraphNum+1, Node2);
					GraphNum++;
					//System.out.println("ccccccc");					
				    }
				else
				    {
					Vertex2 = Graph.GetVertex(Graph,Node2);
				    }

				Ecost = Integer.parseInt(Edgecost.trim());

				Graph.addEdge(Vertex1,Vertex2,Ecost);
				Graph.addEdge(Vertex2,Vertex1,Ecost);


		    }

		    Graph.print();

		    /*
		    System.out.println("test!!!!!!!!!!!!!!");
		    for(int k=0;k<test.length;k++)
			{
			    System.out.println(test[k]);
			}
		    System.out.println(GTest);
		    */


		    System.out.println("");

		    
		    final int[] pred = Dijkstra.dijkstra(Graph, 0);
		    System.out.println("");
		    System.out.println("Route:");

		    for (int n = 0; n < GraphNum; n++) 
			{
			    System.out.printf("From %s to %s: ", Graph.getLabel(0),Graph.getLabel(n)); 
			    System.out.println("");
			    Dijkstra.printPath(Graph, pred, 0, n);
			}
		    System.out.println("");
		    

		    //}
		/*
		catch(IOException e){
		    //e.printStackTrace();
		    System.out.println("Timeout for Packet" + i);
		}
		*/
	}
	
    }
 
    /*
    // Read ConfigFile and create the map
    private static void readConfig(String ConfigFileName) throws Exception
    {

	
	  File ConfigFile = new File(ConfigFileName);
	  BufferedReader reader = null;
	  reader = new BufferedReader(new FileReader(ConfigFile));
	  String line = null;
	  line = reader.readLine();
	  int nNum = Integer.parseInt(line);
	  System.out.println("nNum is: " + nNum);
	  //String STR;
	  String[] Neighbour = new String[nNum];
	  int[] Port = new int[nNum];
	  int[] cost = new int[nNum];
	  int i = 0;
	  while((line = reader.readLine())!= null)
	      {
		  String[] temp;
		  System.out.println("line" + ":" + line);		 
		  temp = line.split(" ");
		  Neighbour[i] = temp[0];
		  System.out.println(Neighbour[i] + "!");
		  cost[i] = Integer.parseInt(temp[1]);
		  System.out.println(cost[i] + "!!");
		  Port[i] = Integer.parseInt(temp[2]);
		  System.out.println(Port[i] + "!!!");
		  //cost[i] = ;
		  //Port[i] = ;
		      i++;

	      }
	  reader.close();
	 
    }
    */

}







//Each node send the StatePacket, (interval 1s).

//Each node should wait for a Dijkstra's alorithm.

//Once a router s running Dijkstra's algorithm. it should print out the terminal, the leastcost path to each destination node(excluding itself) along with the cost this path. ROUTE_UPDATE_INTERVAL(the default value is 30 seconds).

//program execute forever(as a loop).






class WeightedGraph 
{
    
    private int[][] edges; // adjacency matrix
    private Object[] labels;
    
    public WeightedGraph(int n) 
    {
	edges = new int[n][n];
	labels = new Object[n];
    }

    public int size() 
    {
	return labels.length;
    }
    
    public void setLabel(int vertex, Object label) 
    {
	labels[vertex] = label;
    }
    
    public Object getLabel(int vertex) 
    {
	return labels[vertex];
    }
    
    //    
    public int CheckEdge(WeightedGraph Graph, String Node1, String Node2)
    {
	int i;
	int j;
	for(i = 0;i<Graph.edges.length;i++)
	    {
		String N1 = Graph.labels[i].toString();
		for(j = 0;j<Graph.edges[i].length;j++)
		    {    
			String N2 = Graph.labels[j].toString();
			if((N1.compareTo(Node1)==0 && N2.compareTo(Node2)==0) || edges[i][j]>0)		      
			    return 0;
		    }
	    }
	return -1;
    }

    public int GetVertex(WeightedGraph Graph, String Node)
    {
	int i;
	for(i = 0;(i<Graph.labels.length )&&(Graph.labels[i]!=null) ;i++)
	    {
		String N1 = Graph.labels[i].toString();	
		if(N1.compareTo(Node)==0)
		    {
			return i;
		    }
	    }
	return -1;
    }




    //

    public void addEdge(int source, int target, int w) 
    {
		edges[source][target] = w;
    }
    
    public boolean isEdge(int source, int target) 
    {
	return edges[source][target] > 0;
    }
    
    public void removeEdge(int source, int target)
    {
	edges[source][target] = 0;
    }
    
    public int getWeight(int source, int target)
    {
	return edges[source][target];
    }
    
    public int[] neighbors(int vertex) 
    {
	int count = 0;
	for (int i = 0; i < edges[vertex].length; i++) 
	    {
		if (edges[vertex][i] > 0)
		    count++;
	    }
	final int[] answer = new int[count];
	count = 0;
	for (int i = 0; i < edges[vertex].length; i++) 
	    {
		if (edges[vertex][i] > 0)
		    answer[count++] = i;
	    }
	return answer;
    }
    
    public void print() 
    {
	for (int j = 0; j < edges.length; j++) 
	    {
		if(labels[j]!=null)
		    System.out.print(labels[j] + ": ");

		for (int i = 0; i < edges[j].length; i++) 
		    {
			if (edges[j][i] > 0 && labels[i]!= null && labels[j]!= null)
			    System.out.print(labels[i] + ":" + edges[j][i] + " ");
		    }
		if(labels[j]!=null)
		    System.out.println();
	    }
    }
}





class Dijkstra 
{

	// Dijkstra's algorithm to find shortest path from s to all other nodes
    public static int[] dijkstra(WeightedGraph G, int s) 
    {
	final int[] dist = new int[G.size()]; // shortest known distance from "s"
	
	final int[] pred = new int[G.size()]; // preceeding node in path
	final boolean[] visited = new boolean[G.size()+1024]; // all false initially
	
	for (int i = 0; i < dist.length; i++) 
	    {
		//dist[i] = Integer.MAX_VALUE;
		dist[i]= 2147483647;
	    }
	dist[s] = 0;
	
	
	for (int i = 0; i < dist.length; i++) 
	    {
		final int next = minVertex(dist, visited);
		if(next == -1)
		    break;

		
		visited[next] = true;
		
		// The shortest path to next is dist[next] and via pred[next].
		
		final int[] n = G.neighbors(next);
		for (int j = 0; j < n.length; j++) 
		    {
			final int v = n[j];
			final int d = dist[next] + G.getWeight(next, v);
			if (dist[v] > d) 
			    {
				dist[v] = d;
				pred[v] = next;
			    }
				
		    }
		System.out.println("least-cost path to node " + G.getLabel(i) + ":" + " Distance: "+ dist[i]);
		
	    }
	
	
	//System.out.println(pred);
	//System.out.println(dist);
	return pred; // (ignore pred[s]==0!)
    }

    private static int minVertex(int[] dist, boolean[] v) 
    {
	//int x = Integer.MAX_VALUE;
	int x= 2147483647;
	int y = -1; // graph not connected, or no unvisited vertices
	for (int m = 0; m < dist.length; m++) 
	    {		
		if (!v[m] && (dist[m] < x)) 
		    {
			y = m;
			x = dist[m];
		    }
	    }
	return y;
    }
    
    public static void printPath(WeightedGraph G, int[] pred, int s, int e) 
    {
	final java.util.ArrayList path = new java.util.ArrayList();
	int x = e;
	while (x != s) 
	    {
		path.add(0, G.getLabel(x));
		x = pred[x];
	    }
	path.add(0, G.getLabel(s));
	System.out.println(path);
	
    }
    
}



