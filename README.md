# wcn_project

//First part of the code

// new_graph.txt is created 

python generator.py > new_graph.txt
No. of nodes and connectivity are taken as inputs for this part.


//Second part of the code

// graph.txt should be inp file. This code handles running BFS to find disjoin paths frm src to des, and allocating fragments to it based on the proposed algo

python code.py > new_paths.txt   
Inputs to this part of the code are number of fragments and L value.

//Last part of the code:

This part handles all the fragment allocations and scheduling of transmissions

For execution, include all the .java (Graph.java,Node.java,Path.java,ScheduledTransmission.java,Transmission.java) files at one place.
ScheduledTransmission.java is the main class.

For compiling do:
  javac ScheduledTransmission 
  
For executing on previous part outputs:
  java ScheduledTransmission graph.txt paths.txt
