import numpy as np
from scipy.stats import poisson
import networkx as nx
import sys
from math import trunc

pkt_size = 128 #Constant packet size

#assigns distance and techology to edges
def details(a):
    b = np.zeros(len(a))
    answer = []
    for i in range(0,len(a)):
        b[i] = poisson.rvs(1)%3
    for i in range(0,len(a)):    
        if(b[i]==0):
            d = 1
            e = poisson.rvs(5)%11
        if(b[i]==1):
            d=54
            e=poisson.rvs(16)%33
        if(b[i]==2):
            d=0.25
            e=poisson.rvs(25)%51
        answer.append((b[i],d,e))       
    return answer

#generates k-connected graph
def poissongraph(n,mu):
    z= np.zeros(n) #n is number of nodes
    while(sum(z)==0 or sum(z)%2!=0):
        for i in range(n):
            z[i]=round(poisson.rvs(mu)) % n #mu is the expected value
            if(z[i]<mu):
                z[i] = int(mu)
    #print z
    x = []
    for i in range(n):
        x.append(int(z[i]))
    #z = [3,5,2,2,6]
    #print x
    G = nx.random_degree_sequence_graph(x)
    return G

#checks if the graph is k-connected
def check(a,n,k):
    degree = np.zeros(n) 
    for i in a:
        degree[i[0]] += 1
        degree[i[1]] += 1
    for i in degree:
        if(i<k):   
            return 0
    return 1


def main():
    nodeCount = eval(raw_input())
    connectivity = eval(raw_input())
    A = poissongraph(nodeCount,connectivity)
    #print A.edges()
    
    B = list(set(A.edges()))
    for i in B:
        if(i[0] == i[1]):
            B.remove(i)
    C = nx.empty_graph(nodeCount)    
    C.add_edges_from(B)
    D = details(C.edges())        
    
    #run the loop till k-connectivity is achieved
    while(check(C.edges(),nodeCount,connectivity)==0):
        A = poissongraph(nodeCount,connectivity)
        #print A.edges()
        B = list(set(A.edges()))
        for i in B:
            if(i[0] == i[1]):
                B.remove(i)
        C = nx.empty_graph(connectivity)    
        C.add_edges_from(B)
        D = details(C.edges())        
        #print C.edges()
    EdgeCount = len(C.edges())
    #print A.edges()
    node = []
    edge = []
    distance = []
    for i in range(0,nodeCount):
        z= np.zeros(3)
        node.append(z)
        x = np.zeros(nodeCount)
        edge.append(x)
        v = np.zeros(nodeCount)
        distance.append(v)
    for j in range(0,EdgeCount):
        node[C.edges()[j][0]][D[j][0]] = 1
        node[C.edges()[j][1]][D[j][0]] = 1
        edge[C.edges()[j][0]][C.edges()[j][1]] = 1
        edge[C.edges()[j][1]][C.edges()[j][0]] = 1      
    for i in range(0,nodeCount):
        for j in range(0,nodeCount):
            if(edge[i][j] == 1):
                if (i,j) in C.edges():
                    distance[i][j] = D[C.edges().index((i,j))][2]
                    distance[j][i] = D[C.edges().index((i,j))][2]
                else:
                    distance[i][j] = D[C.edges().index((j,i))][2]
                    distance[j][i] = D[C.edges().index((j,i))][2]
            else:
                distance[i][j] = round(poisson.rvs(20))
                distance[j][i] = distance[i][j]

    print("########## Node Bluetooth Wifi Zigbee")
    for i in node:
        for j in i:
            print '{:d}'.format(trunc(j)),
            print " ",
        print "\n",
    print("########## Edge Presense")    
    for i in edge:
        for j in i:
            print '{:d}'.format(trunc(j)),
            print " ",
        print "\n",
    print("######### Distances")
    for i in distance:
        for j in i:
            print '{:d}'.format(trunc(j)),
            print " ",
        print "\n",  
   

if __name__ == "__main__":
    main()

