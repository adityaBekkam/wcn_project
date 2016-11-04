import java.util.ArrayList;
import java.util.Random;


public class ScheduledTransmission_without_self_healing {

	public double getAveragePDR_nonSelfHealed(int L,int numOfRuns,int sentFragments,int[] mAlloc,double[] qValues,ArrayList<Path> tempPaths){
		double avg = 0;
		for( int i=0 ; i<numOfRuns ; i++ ){
			avg += getPDR_nonSelfHealed(L,numOfRuns,sentFragments,mAlloc,qValues,tempPaths);
		}
		return avg/numOfRuns;
	}
	
	public double getPDR_nonSelfHealed(int L,int numOfRuns,int sentFragments,int[] mAlloc,double[] qValues,ArrayList<Path> tempPaths){
		int received = 0;
		for( int i=0 ; (i<L && i<tempPaths.size()) ; i++ ){
			if( tempPaths.get(i).isActive ){
				Random rand = new Random();
				double randNum = rand.nextDouble() ;
				//System.out.println(randNum);
				if( randNum>qValues[i]  )
					tempPaths.get(i).isActive = false;
				else
					received += mAlloc[i] ;
			}
		}
		//System.out.println(received);
		return 1 - ((double)received/(double)sentFragments) ;
	}
}
