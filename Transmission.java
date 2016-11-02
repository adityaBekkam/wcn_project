
public class Transmission extends ScheduledTransmission {
	int id ;
	int src ;
	int dest ;
	int timeStamp ;
//	int timeReq ;
	int technology ;
	int numFrags ;
	boolean isStarted ;
	
	public Transmission( int _id,int a,int b,int c, int d,int e){
		this.id = _id ;
		this.src = a ;
		this.dest = b ;
		this.timeStamp = c ;
		this.technology = d;
		this.isStarted = false ;
		this.numFrags = e ;
	}
	
	public void printTransmission(){
		System.out.println("Printing transmission: ");
		System.out.print("ID:"+this.id+" ; Timestamp: "+this.timeStamp+" ; Technology: "+this.technology+"; ");
		System.out.println("Between :"+this.src+"-->"+this.dest+" ; Fragments: "+this.numFrags);
		//System.out.println("TimeReq:"+this.timeReq);
	}
	
	public int getRequiredTime(){
		int capacity = getCapacity(this.technology) ;
		if( this.numFrags<=capacity )
			return 1 ;
		else{
			if( this.numFrags%capacity==0 )
				return (this.numFrags/capacity);
			else
				return (this.numFrags/capacity + 1);
		}
	}
	
}
