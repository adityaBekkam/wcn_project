import java.util.ArrayList;


public class Path {
	ArrayList<Integer> route ;
	boolean isActive ;
	
	public Path(){
		this.route = new ArrayList<Integer>() ;
		this.isActive = true ;
	}
	
	public void setPath( ArrayList<Integer> r ){
		this.route = r ;
		this.isActive = true ;
	}
	
	public void addNodeToRoute( int a ){
		this.route.add(a) ;
	}
	
	public int pathSize(){
		return this.route.size() ;
	}
	
	public void printPath(){
		if( !this.isActive ){
			System.out.println("Path inactive");
			return ;
		}
		for( int i=0 ; i<this.route.size()-1; i++ ){
			System.out.print(this.route.get(i)+"-->");
		}
		System.out.println(this.route.get(this.route.size()-1));
	}
	
}
