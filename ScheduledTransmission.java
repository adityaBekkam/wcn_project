import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class ScheduledTransmission {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	
	static Graph graph ;
	static int L ;
	static ArrayList<Path> paths ;
	static int maxTime ;
	static int[] mAlloc ;
	static float[] qValues ;
	static int bCapacity = 2;
	static int wCapacity = 4;
	static int zCapacity = 3;
	static ArrayList<Transmission>[] transfers ;
	
	public static void printPendingTransmissions(int timeStamp,ArrayList<Transmission>[] transfers){
		System.out.println("##############");
		System.out.println("Current timestamp: "+timeStamp);
		for( int i=0 ; i<maxTime+1; i++ ){
			System.out.println("Time = "+i+": ");
			for( int j=0 ; j<transfers[i].size() ; j++ ){
				transfers[i].get(j).printTransmission() ;
			}
			System.out.println("-----------------");
		}
		System.out.println("##############");
	}
	
	public static void changeTechnology(int time, int j, int tech) {
		// TODO Auto-generated method stub
		Transmission t = transfers[time].get(j) ;
		int oldTime = t.getRequiredTime() ;
		t.technology = tech ;
		//System.out.println("Entered changeTech: "+oldTime+"--"+t.getRequiredTime());
		if( t.getRequiredTime()>oldTime ){
			reschedule(t.id,time+1,t.getRequiredTime()-oldTime) ;
		}
		transfers[time].set(j, t);
		//transfers[time].get(j).printTransmission();
	}


	public static void reschedule(int id, int timeStamp,int time ) {
		// TODO Auto-generated method stub
		// Should be given a more technical value.
		int i = 200 ;
		while( i>=timeStamp ){
			if( transfers[i].size()!=0 ){
				for( int j=0 ; j<transfers[i].size() ; j++ ){
					if( transfers[i].get(j).id==id ){
						Transmission t = transfers[i].get(j) ;
						t.timeStamp += time ;
						transfers[t.timeStamp].add(t);
						transfers[i].remove(j);
						j-- ;
					}
				}
			}
			i-- ;
		}
	}
	
	public static boolean checkIfTechCanBeChanged(ArrayList<Transmission> completedTr,Transmission tempTr,int tempRange,int newTech){
		boolean flag = false ;
		Transmission tempTr1 ;
		for( int p=0 ; p<completedTr.size() ; p++ ){
			tempTr1 = completedTr.get(p) ;
			if( tempTr1.technology==newTech && graph.isPresentInInterferenceRange(tempTr.src, tempTr.dest, tempTr1.src, tempTr1.dest, tempRange) ){
				return false ;
			}
			else if( p==completedTr.size()-1 ){
				flag = true ;
			}
		}
		
		return flag ;
	}
	
	public static int getCapacity(int tech){
		if( tech==0 )
			return bCapacity ;
		else if( tech==1 )
			return wCapacity ;
		else
			return zCapacity ;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		// args[0] --> Network
		File inp = new File(args[0]);
		Scanner scanner = new Scanner(inp);
		
		// Constructing the network using classes: Graph & Node .
		graph = new Graph();
		String line ;
		String[] str ;
		Node n ;
		ArrayList<Integer> tempLst ;
		
		// Node technologies information
		scanner.nextLine();
		while( scanner.hasNext() ){
			line = scanner.nextLine() ;
			if( line.startsWith("#") ){
				break ;
			}
			str = line.split(" ");
			tempLst = new ArrayList<Integer>();
			tempLst.add( Integer.parseInt(str[1]) );
			tempLst.add( Integer.parseInt(str[2]) );
			tempLst.add( Integer.parseInt(str[3]) );
			n = new Node(Integer.parseInt(str[0]),tempLst) ;
			graph.addNode(n) ;
		}
		
		//tempArr1 --> adjacency matrix of the network.
		//tempArr2 --> tells whether there is an edge between a pair of nodes.
		boolean[][] tempArr1 = new boolean[ graph.nodes.size() ][ graph.nodes.size() ] ;
		int[][] tempArr2 = new int[ graph.nodes.size() ][ graph.nodes.size() ] ;
		int count = 0; 
		while( scanner.hasNext() ){
			line = scanner.nextLine() ;
			if( line.startsWith("#") ){
				break ;
			}
			str = line.split(" ") ;
			for( int i=0 ; i<str.length ; i++ ){
				if( Integer.parseInt(str[i])==1 )
					tempArr1[count][i] = true ;
				else
					tempArr1[count][i] = false ;
			}
			count++ ;
		}
		count = 0; 
		while( scanner.hasNext() ){
			line = scanner.nextLine() ;
			if( line.startsWith("#") ){
				break ;
			}
			str = line.split(" ") ;
			for( int i=0 ; i<str.length ; i++ ){
				tempArr2[count][i] = Integer.parseInt(str[i]);
			}
			count++ ;
		}
		graph.setGraphArrays(tempArr2, tempArr1);
		
		// args[1] --> contains 'l' value & 'k' disjoint paths from source to destination.
		inp = new File(args[1]);
		scanner = new Scanner(inp);
		scanner.nextLine();
		int L = scanner.nextInt() ;
		scanner.nextLine();
		paths = new ArrayList<Path>() ;
		Path tempPath ;
		scanner.nextLine() ;
		while( scanner.hasNext() ){
			line = scanner.nextLine() ;
			if( line.startsWith("#") ){
				break ;
			}
			tempPath = new Path();
			str = line.split(" ") ;
			for( int i=0 ; i<str.length ; i++ ){
				tempPath.addNodeToRoute( Integer.parseInt(str[i]) ) ;
			}
			paths.add(tempPath) ;
		}
		
		//Reading values of L-array and Q-values.
		mAlloc = new int[L] ;
		qValues = new float[paths.size()] ;
//		Random rand = new Random();
//		for( int i=0 ; i<L; i++ )
//			mAlloc[i] = rand.nextInt(5) + 1; 
//		for( int i=0 ; i<paths.size() ; i++ )
//			qValues[i] = ( (float)rand.nextInt(i+1)/(float)paths.size() ); 
		line = scanner.nextLine();
		str = line.split(" ");
		for( int i=0 ; i<L ; i++ ){
			mAlloc[i] = Integer.parseInt(str[i]);
		}
		scanner.nextLine();
		line = scanner.nextLine();
		str = line.split(" ");
		for( int i=0 ; i<paths.size() ; i++ ){
			qValues[i] = Float.parseFloat(str[i]);
		}
		scanner.nextLine();
		
		
		//Printing graph and all the 'k' paths.
		graph.printGraph();
		for( int i=0 ; i<paths.size(); i++ ){
			paths.get(i).printPath() ;
		}
		System.out.println("------------");
		System.out.println(Arrays.toString(mAlloc));
		System.out.println(Arrays.toString(qValues));
		
		maxTime = 0 ;
		transfers = new ArrayList[1000] ;
		for( int i=0 ; i<1000; i++ ){
			transfers[i] = new ArrayList<Transmission>() ;
		}

		// Scheduling tranmission of the L paths.
		int lValue = L ;
		for( int i=0 ; (i<lValue && i<paths.size()) ; i++ ){
			if( !paths.get(i).isActive ){
				//paths.remove(i);
				//i-- ;
				lValue++ ;
			}
			else{
				int curTime = 0;
				for( int j=0 ; j<paths.get(i).pathSize()-1 ; j++ ){
					Transmission t ;
					//Priority order: Wifi > Zigbee > Bluetooth
					if( graph.nodes.get( paths.get(i).route.get(j)).techs.get(1)==1 && 1==graph.nodes.get( paths.get(i).route.get(j+1)).techs.get(1) ){
						t = new Transmission(i,paths.get(i).route.get(j),paths.get(i).route.get(j+1),curTime,1,mAlloc[i]) ;
						transfers[curTime].add(t);
						curTime += t.getRequiredTime() ;
					}
					else if( graph.nodes.get( paths.get(i).route.get(j)).techs.get(2)==1 && 1==graph.nodes.get( paths.get(i).route.get(j+1)).techs.get(2) ){
						t = new Transmission(i,paths.get(i).route.get(j),paths.get(i).route.get(j+1),curTime,2,mAlloc[i]) ;
						transfers[curTime].add(t);
						curTime += t.getRequiredTime() ;
					}
					else{
						t = new Transmission(i,paths.get(i).route.get(j),paths.get(i).route.get(j+1),curTime,0,mAlloc[i]) ;
						transfers[curTime].add(t);
						curTime += t.getRequiredTime() ;
					}
					if( maxTime<curTime )
						maxTime = curTime ;
					if( j==0 )
						graph.nodes.get( paths.get(i).route.get(j) ).sBuffer = mAlloc[i] ;
				}
				if( Math.random()<= qValues[i] )
					paths.get(i).isActive = false; 
			}
		}
		
		// Transmitting data
		boolean isFinished = false ;
		int time = 0,numOfTimes=0;
		while( !isFinished ){
			printPendingTransmissions(0,transfers);
			if( transfers[time].size()==0 ){
				numOfTimes++ ;
				//Should be handled more carefully and technically
				if( numOfTimes>10 ){
					isFinished = true ;
				}
			}
			else{
				numOfTimes = 0;
				Transmission t ; 
				// Keeps track of all transmission that took place in current timestamp
				ArrayList<Transmission> completedTr = new ArrayList<Transmission>() ;
				for( int j=0 ; j<transfers[time].size() ; j++ ){
					t = transfers[time].get(j) ;
					if( t.technology==1 ){
						int interferenceRange = 2*graph.adj[t.src][t.dest] ;
						Transmission tempTr ;
						for( int k=j+1 ; k<transfers[time].size() ; k++ ){
							tempTr = transfers[time].get(k) ;
							if( tempTr.technology==1 && graph.isPresentInInterferenceRange(t.src, t.dest, tempTr.src, tempTr.dest, interferenceRange) ){
								//System.out.println("Interference found bwtween "+t.src+"-->"+t.dest+", "+tempTr.src+"-->"+tempTr.dest);
								int tempRange = 2*graph.adj[tempTr.src][tempTr.dest] ;
								boolean canTechChange = checkIfTechCanBeChanged(completedTr, tempTr, tempRange,2) ;
								if( canTechChange ){
									changeTechnology(time,k,2);
									//System.out.println("Technology of "+tempTr.id+" changed to zigbee");
								}
								else if( graph.nodes.get(tempTr.src).techs.get(0)==1 && graph.nodes.get(tempTr.dest).techs.get(0)==1 ){
									changeTechnology(time,k,0);
									//System.out.println("Technology of "+tempTr.id+" changed to bluetooth");
								}
								else{
									reschedule(tempTr.id, time, 1);
									//System.out.println("Timestamp of "+tempTr.id+" increased by 1");
								}
							}
						}
					}
					else if( t.technology==2 ){
						int interferenceRange = 2*graph.adj[t.src][t.dest] ;
						Transmission tempTr ;
						for( int k=j+1 ; k<transfers[time].size() ; k++ ){
							tempTr = transfers[time].get(k) ;
							if( tempTr.technology==2 && graph.isPresentInInterferenceRange(t.src, t.dest, tempTr.src, tempTr.dest, interferenceRange) ){
								//System.out.println("Interference found bwtween "+t.src+"-->"+t.dest+", "+tempTr.src+"-->"+tempTr.dest);
								if( graph.nodes.get(tempTr.src).techs.get(0)==1 && graph.nodes.get(tempTr.dest).techs.get(0)==1 ){
									changeTechnology(time,k,0);
									//System.out.println("Technology of "+tempTr.id+" changed to bluetooth");
								}
								else{
									reschedule(tempTr.id, time, 1);
									//System.out.println("Timestamp of "+tempTr.id+" increased by 1");
								}
							}
						}
					}
					if( t.getRequiredTime()<=1 ){
						graph.nodes.get(t.src).sBuffer -= t.numFrags ;
						graph.nodes.get(t.dest).rBuffer += t.numFrags ;
						graph.nodes.get(t.dest).sBuffer = graph.nodes.get(t.dest).rBuffer ; 
						System.out.println("Number of fragments at "+t.dest+"(receiver): "+graph.nodes.get(t.dest).rBuffer);
						transfers[time].remove(j);
						j-- ;
					}
					else{
						t.numFrags -= getCapacity(t.technology) ;
						graph.nodes.get(t.src).sBuffer -= t.numFrags ;
						graph.nodes.get(t.dest).rBuffer += getCapacity(t.technology) ;
						t.timeStamp++ ;
						transfers[time+1].add(t);
						transfers[time].remove(j);
						j-- ;
					}
					completedTr.add(t);
				}
			}
			time++ ;
		}
		
	}

}
