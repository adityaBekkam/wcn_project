# wcn_project

//First part of the code

python generator.py > new_graph.txt     // new_graph.txt is created 
6                                      // No.of nodes
2                                      // connectivity

//Second part of the code

python code.py > paths.txt   // graph.txt should be inp file. This code handles running BFS to find disjoin paths frm src to des, and allocating fragments to it based on the proposed algo

//Last part of the code:

This part handles all the fragment allocations and scheduling of transmissions

For execution, include all the .java (Graph.java,Node.java,Path.java,ScheduledTransmission.java,Transmission.java) files at one place.
ScheduledTransmission.java is the main class.

For compiling do:
  javac ScheduledTransmission 
  
For executing on previous part outputs:
  java ScheduledTransmission graph.txt paths.txt
