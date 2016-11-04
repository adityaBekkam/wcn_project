from collections import defaultdict
import random

#### function which computes disjoint bfs paths from start to goal
def bfs_paths(graph, start, goal):
    queue = [(start, [start])]
    done_paths = []
    while queue:
        (vertex, path) = queue.pop(0)
        if vertex not in done_paths:
            l = list((  set(graph[vertex]) - set(done_paths) )- set(path))
            for next in l:
                if next == goal:
                    if len(set(done_paths) & (set(path) - set([start]))) == 0:
                        done_paths = list(set(done_paths + path ))
                        yield path + [next]
                else:
                    queue.append((next, path + [next]))

#### Scanning the input required from file
fragments_n = input("Enter num of fragments")
infile=open("graph.txt")
indata=infile.readlines()
interfaces = []
infile.close()
distances=[]
edges=[]
nodes_n=0
stage=0

#inputs 
start = 2
goal = 5
top_l=2
######
indata = indata[:-1]

#### Preparing the 3 matrices - adjacency, distance, interface matrices from data read 
for i in indata:
	if 'Bluetooth' in i:
		stage=0
	elif 'Edge' in i:
		stage =1
	elif 'Distances' in i:
		stage =2
	
	else:
		if stage == 0:
			l =i.split(' ')
			l = [int(j) for j in l if (j != '' and j !='\n')]
			interfaces.append(l[1:])
			nodes_n = nodes_n + 1
		elif stage == 1:
			l =i.split(" ")
			l = [int(j) for j in l if (j != '' and j !='\n')]
			edges.append(l)
		elif stage == 2:
			l =i.split(" ")
			l = [int(j) for j in l if (j != '' and j !='\n')]
			distances.append(l)
			
graph = defaultdict(lambda:[])
for i in range(0,nodes_n):
	for j in range(0,nodes_n):
		if edges[i][j] != 0:
			graph[i].append(j)
			
			
#0-bluetooth
#1-wifi
#2-zigbee

###### Getting all paths by caaling bfs function
all_paths = list(bfs_paths(graph, start, goal))
top_k = len(all_paths)
interface2=[]
for i in interfaces:
	interface2.append([j for j in range(0,3) if i[j] == 1])	


#### getting value for each paths, to filter top l paths
C_l = []
C_k = []
data_rate = {0:1,1:54,2:0.25}
for path in all_paths:
	dist=0
	min_interface=2
	inter = []
	for i in range(0,len(path)-1):
		#dist = dist + distances[path[i]][path[i+1]]
		dist = dist + 1
		comm = set(interface2[path[i]]) & set(interface2 [path[i+1]])
		if 1 in comm:
			inter.append(1)
		elif 0 in comm:
			inter.append(0)
		else:
			inter.append(2)
		if data_rate[inter[-1]] < min_interface:
			min_interface = data_rate[inter[-1]]
			
	c = min_interface/float(dist)   			#cap/dist
	C_k.append([path,c,inter])

##### sorting and filtering l paths
C_k.sort(key = lambda d:d[1])
only_paths_sorted = [item[0] for item in C_k]
all_paths_interfaces = [item[2] for item in C_k]
C_l = C_k[0:top_l]
print ("########## L value")
print top_l


########  m allocation #########
for i in range(0,top_k):
	C_k[i].append(random.uniform(0,1) ) 		#q
	C_k[i].append(0)							#m
	if i < top_l:
		C_l[i].append(random.uniform(0,1) ) 	#q
		C_l[i].append(0)						#m
	
C_l.sort(key = lambda d:d[3],reverse=True)
rate = 0.25 * 8  								#0.25 Mbps
#fragments_n = 10
frag_size = 0.25								#0.25 Mb

counter = fragments_n
while(counter != 0):
	for i in range(0,top_l):
		if counter!=0:
			assigned_n = min(int((C_l[i][3]*rate)/frag_size) , counter)
			C_l[i][4] =  C_l[i][4] + assigned_n
			counter = counter - assigned_n
		else:
			break


#####  code for printing all the outputs #########		
print ("########## k paths")
K_paths = [item[0] for item in C_k]	
L_paths = [item[0] for item in C_l]	
for i in L_paths:
	for j in i:
		print str(j),
	print ""
	
for i in range(top_l,top_k):
	for j in K_paths[i]:
		print str(j),
	print ""


L_fragments = [item[4] for item in C_l]	
q_values_l = [item[3] for item in C_l]	
q_values_k = [item[3] for item in C_k]	
print ("######### L-fragment allocated array")
for i in L_fragments:
	print str(i),
print ""	
print "######### Q-values of k-paths"
for i in q_values_l:
	print str(i),
	
for i in range(top_l,top_k):
	print str(q_values_k[i]),
print ""
print ("#########")

 
	














			
	
