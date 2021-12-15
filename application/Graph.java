/**
 * @author Bill Johnson
 * CS400 010
 * Project:	Final Project
 * Team: A12
 * Collaborators:
 * 		Chris Lansford
 * 		Ben Hutchison
 * 		Brian Eng 
 * 
 * Social Network Graph
 */
package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


 /**
 * PROJECT: Final Project
 * NAME: William Johnson
 * EMAIL: wjohnson24@wisc.edu
 * LECTURE NUMBER: Lec 010
 * DESCRIPTION: This is a graph for storing package information
*/

public class Graph implements GraphADT {
	
     /** List of vertices
      * The graph uses a list of lists to store data. The outer list represents
      * vertices, and the inner lists represent edges. Presence of vertices can
      * be determined by presence in outer list.
      */
     private List<List<Person>> adjacencyList;

	/*
	 * Default no-argument constructor
	 */ 
	public Graph() {
		adjacencyList = new ArrayList<List<Person>>();
	}

     public boolean vertexExists(Person p) {
          for (List<Person> nodes : adjacencyList) {
               if (nodes.get(0).equals(p)) {return true;}
          }
          return false;
     }

	/** addNode
     * Add new node to the graph.
     *
     * If node is null or already exists,
     * method ends without adding a node or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. node is non-null
     * 2. node is not already in the graph 
	 * @return 
     */
	public boolean addNode(Person p) {
		if (p == null) {return false;}
          if (vertexExists(p)) {return false;}
          adjacencyList.add(new ArrayList<Person>());
          adjacencyList.get(adjacencyList.size()-1).add(p);
          return true;
	}

	/**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
	 * @return 
     */
	public boolean removeNode(Person p) {
          boolean success = false;
          if (p == null) {return false;}
          if (!vertexExists(p)) {return false;}
          int i = 0;
          for (List<Person> nodes : adjacencyList) {
               if (nodes.get(0).equals(p)) {
                    adjacencyList.remove(i);
                    success = true;
               }
               if (nodes.contains(p)) {
                    nodes.remove(p);
                    success = true;
               }
               i++;
          }
          return success;
	}

	/**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
	 * @return 
	 */
	public boolean addEdge(Person p1, Person p2) {
          boolean success = false;
		if (p1 == null || p2 == null) {return false;}
          if (p1.equals(p2)) {return false;} //don't add edge if vertices are the same.
          if (!vertexExists(p1)) {addNode(p1);}
          if (!vertexExists(p2)) {addNode(p2);}
          for (List<Person> nodes : adjacencyList) {
               if (nodes.get(0).equals(p1)) {
                    if (nodes.contains(p2)) {continue;}
                    nodes.add(p2);
                    success = true;
               }
               if (nodes.get(0).equals(p2)) {
                    if (nodes.contains(p1)) {continue;}
                    nodes.add(p1);
                    success = true;
               }
          }
          return success;
	}
	
	/**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
	 * @return 
     */
	public boolean removeEdge(Person p1, Person p2) {
          boolean success = false;
          if (p1 == null || p2 == null) {return false;}
          if (p1.equals(p2)) {return false;}
          if (!(vertexExists(p1) && vertexExists(p2))) {return false;}
          for (List<Person> nodes : adjacencyList) {
               if (nodes.get(0).equals(p1)) {
                    if (!nodes.contains(p2)) {continue;}
                    nodes.remove(p2);
                    success = true;
               }
               if (nodes.get(0).equals(p2)) {
                    if (!nodes.contains(p1)) {continue;}
                    nodes.remove(p1);
                    success = true;
               }
          }
          return success;
	}	

	/**
     * Returns a Set that contains all the vertices
     * 
	 */
	public Set<Person> getAllNodes() {
		Set<Person> nodeSet = new HashSet<>();
          adjacencyList.forEach((n) -> nodeSet.add(n.get(0)));
          return nodeSet;
	}

	/**
     * Get all the neighbor (adjacent) vertices of a vertex - aka children
     *
	 */
	public Set<Person> getNeighbors(Person p) {
          if (!vertexExists(p)) {return null;}
          Set<Person> neighbors = new HashSet<>();
          for (List<Person> nodes : adjacencyList) {
               if (nodes.get(0).equals(p)) {
                    neighbors.addAll(nodes); //pull stored edges
                    neighbors.remove(p); //get rid of vertex key
                    break;
               }
          }
		return neighbors;
	}
	
	/**
     * Returns the number of edges in this graph.
     */
    public int size() {
          int[]  count = {0};
          adjacencyList.forEach((n) -> {count[0] += (n.size()-1);} );
          return count[0];
    }

	/**
     * Returns the number of vertices in this graph.
     */
	public int order() {
        return adjacencyList.size();
    }

     @Override
     public Person getNode(String s) {
          for (List<Person> nodes : adjacencyList) {
               if (nodes.get(0).Name.equals(s)) {return nodes.get(0);}
          }
          return null;
     }
}
