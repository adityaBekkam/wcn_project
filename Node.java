import java.util.ArrayList;


public class Node {
	int id ;
	int sBuffer ;					//sending buffer
	int rBuffer ;					//receiving buffer
	ArrayList<Integer> techs ;		//0: Bluetooth, 1: Wi-fi, 2:Zigbee
	
	
	public Node(){
		this.techs = new ArrayList<Integer>();
		this.sBuffer = 0 ;
		this.rBuffer = 0 ;
	}
	
	
	public Node( int a, ArrayList<Integer> l){
		this.id = a ;
		this.techs = l ;
		this.sBuffer = 0 ;
		this.rBuffer = 0 ;
	}
	
	public void printNode(){
		System.out.print(this.id+" "+this.sBuffer+" "+this.rBuffer+" ");
		if( this.techs.get(0)==1 )
			System.out.print("Bluetooth ");
		if( this.techs.get(1)==1 )
			System.out.print("Wifi ");
		if( this.techs.get(2)==1 )
			System.out.print("Zigbee ");
		System.out.println();
	}
	
}
