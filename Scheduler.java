import java.util.ArrayList;


public class Scheduler {
	ArrayList<ArrayList<Transmission>> transfers ;
	
	public Scheduler(){
		this.transfers = new ArrayList<ArrayList<Transmission>>() ;
	}
	
	public Scheduler(ArrayList<ArrayList<Transmission>> l){
		this.transfers = l;
	}
	
	public void addTransfer(ArrayList<Transmission> tr){
		this.transfers.add(tr) ;
	}
	
	public void reScheduleTransmission(int time, Transmission t){
		ArrayList<Transmission> temp = this.transfers.get(time) ;
		temp.remove(temp.indexOf(t));
		this.transfers.set(time, temp);
		temp = this.transfers.get(time+1);
		t.timeStamp++ ;
		if( this.transfers.size()==time ){
			temp = new ArrayList<Transmission>() ;
			temp.add(t);
			this.transfers.add(temp);
		}
		else{
			temp = this.transfers.get(time+1);
			temp.add(t);
			this.transfers.set(time+1, temp);
		}
		//return this.transfers ;
	}
	
	public void reSchedulePath( int id, int time){
		ArrayList<Transmission> temp ;
		for( int i=this.transfers.size()-1 ; i>=time ; i-- ){
			temp = this.transfers.get(i) ;
			for( int j=0 ; j<temp.size() ; j++ ){
				if( temp.get(j).id==id ){
					this.reScheduleTransmission(time, temp.get(j)) ;
					//break ;
				}
			}
		}
		//return this.transfers ;
	}
}
