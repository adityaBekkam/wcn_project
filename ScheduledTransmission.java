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
	
	static int[] mAlloc ;
	static float[] qValues ;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		// args[0] --> Network
		File inp = new File(args[0]);
		Scanner scanner = new Scanner(inp);
		
		// Constructing the network using classes: Graph & Node .
		Graph graph = new Graph();
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
		ArrayList<Path> paths = new ArrayList<Path>() ;
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
		
		//Printing graph and all the 'k' paths.
		graph.printGraph();
		for( int i=0 ; i<paths.size(); i++ ){
			paths.get(i).printPath() ;
		}
		System.out.println("------------");
		
		//Array of m-values and q-values.
		//Assigning statically as of now.
		mAlloc = new int[L] ;
		Random rand = new Random();
		for( int i=0 ; i<L; i++ ){
			mAlloc[i] = rand.nextInt(5) + 1; 
		}
		qValues = new float[paths.size()] ;
		for( int i=0 ; i<paths.size() ; i++ ){
			qValues[i] = ( (float)rand.nextInt(i+1)/(float)paths.size() ); 
		}
		System.out.println(Arrays.toString(mAlloc));
		System.out.println(Arrays.toString(qValues));
		int maxTime = 0 ;
		int bCapacity = 2;
		int wCapacity = 4;
		int zCapacity = 3;
		ArrayList<Transmission>[] transfers = new ArrayList[1000] ;
		for( int i=0 ; i<1000; i++ ){
			transfers[i] = new ArrayList<Transmission>() ;
		}
		

		// Scheduling tranmission of the L paths.
		for( int i=0 ; i<L ; i++ ){
			if( !paths.get(i).isActive ){
				paths.remove(i);
				i-- ;
			}
			else{
				int curTime = 0;
				for( int j=0 ; j<paths.get(i).pathSize()-1 ; j++ ){
					if( graph.nodes.get( paths.get(i).route.get(j)).techs.get(0)==1 && 1==graph.nodes.get( paths.get(i).route.get(j+1)).techs.get(0) ){
						transfers[curTime].add(new Transmission(i,paths.get(i).route.get(j),paths.get(i).route.get(j+1),curTime,0,mAlloc[i]) );
						if( mAlloc[i]<bCapacity ){
							curTime++ ;
						}
						else{
							if( mAlloc[i]%bCapacity==0 )
								curTime += (mAlloc[i]/bCapacity) ;
							else
								curTime += (mAlloc[i]/bCapacity + 1) ;
						}
					}
					else if( graph.nodes.get( paths.get(i).route.get(j)).techs.get(1)==1 && 1==graph.nodes.get( paths.get(i).route.get(j+1)).techs.get(1) ){
						transfers[i].add(new Transmission(i,paths.get(i).route.get(j),paths.get(i).route.get(j+1),curTime,1,mAlloc[i]) );
						if( mAlloc[i]<wCapacity ){
							curTime++ ;
						}
						else{
							if( mAlloc[i]%wCapacity==0 )
								curTime += (mAlloc[i]/wCapacity) ;
							else
								curTime += (mAlloc[i]/wCapacity + 1) ;
						}
					}
					else{
						transfers[i].add(new Transmission(i,paths.get(i).route.get(j),paths.get(i).route.get(j+1),curTime,2,mAlloc[i]) );
						if( mAlloc[i]<zCapacity ){
							curTime++ ;
						}
						else{
							if( mAlloc[i]%zCapacity==0 )
								curTime += (mAlloc[i]/zCapacity) ;
							else
								curTime += (mAlloc[i]/zCapacity + 1) ;
						}
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
		
		for( int i=0 ; i<maxTime+1; i++ ){
			System.out.println("Time = "+i+": ");
			for( int j=0 ; j<transfers[i].size() ; j++ ){
				transfers[i].get(j).printTransmission() ;
			}
			System.out.println("-----------------");
		}
		
		
		// Transmit data.
//		for( int i=0 ; i<sch.transfers.size() ; i++ ){
//			tempTransfer = sch.transfers.get(i) ;
//			for( int j=0 ; j<tempTransfer.size() ; j++ ){
//				if( tempTransfer.get(j).technology!=0 ){
//					//Code for wi-fi/zigbee transmission
//					int tempSrc = tempTransfer.get(j).src ;
//					int tempDest = tempTransfer.get(j).dest ;
//					for( int k=j+1 ; k<tempTransfer.size() ; k++ ){
//						if( tempTransfer.get(j).technology==tempTransfer.get(k).technology ){
//							if( graph.isPresentInInterferenceRange(tempSrc, tempDest, tempTransfer.get(k).src, 2*graph.adj[tempSrc][tempDest])){
//								sch.reSchedulePath(tempTransfer.get(k).id, i);
//							}
//						}
//					}
//				}
//				if( tempTransfer.get(j).timeReq<=1 ){
//					graph.nodes.get( tempTransfer.get(j).src ).sBuffer-- ;
//					graph.nodes.get( tempTransfer.get(j).dest ).rBuffer++ ;
//					graph.nodes.get( tempTransfer.get(j).src ).sBuffer = 0;
//				}
//				else{
//					graph.nodes.get( tempTransfer.get(j).src ).sBuffer-- ;
//					graph.nodes.get( tempTransfer.get(j).dest ).rBuffer++ ;
//					tempTransfer.get(j).timeReq-- ;
//					tempTransfer.get(j).isStarted = true ;
//					sch.reScheduleTransmission( i,tempTransfer.get(j) ) ;
//					j-- ;
//				}
//			}
//		}
	}

}
