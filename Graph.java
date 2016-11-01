import java.util.ArrayList;


public class Graph {
	ArrayList<Node> nodes ;
	int[][] adj ;
	boolean[][] isEdgePresent ;
	
	//Empty Constructor
	public Graph(){
		this.nodes = new ArrayList<Node>();
	}
	
	//Fills the adjacency array and edges array
	public void setGraphArrays( int[][] arr1, boolean[][] arr2 ){
		this.adj = new int[arr1.length][arr1[0].length] ;
		this.isEdgePresent = new boolean[arr2.length][arr2[0].length] ;
		this.adj = arr1 ;
		this.isEdgePresent = arr2 ;
	}

	//Add a node to the given graph
	public void addNode(Node n){
		this.nodes.add(n);
	}
	
	public boolean isPresentInInterferenceRange( int id1, int id2, int id3, int range ){
		if( adj[id1][id3]<=range || adj[id2][id3]<=range ){
			return true ;
		}
		return false ;
	}
	
	public void printGraph(){
		System.out.println("Printing nodes: ");
		for( int i=0 ; i<this.nodes.size(); i++ ){
			this.nodes.get(i).printNode() ;
		}
		System.out.println("-----------------");
		System.out.println("Printing adjacency matrix: ");
		for( int i=0 ; i<this.adj.length; i++ ){
			for( int j=0 ; j<this.adj[0].length; j++ ){
				System.out.print(this.adj[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("-----------------");
		System.out.println("Printing boolean matrix: ");
		for( int i=0 ; i<this.isEdgePresent.length; i++ ){
			for( int j=0 ; j<this.isEdgePresent[0].length; j++ ){
				if( this.isEdgePresent[i][j] )
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
		System.out.println("-----------------");
	}
	
}
