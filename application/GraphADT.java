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
 * Graph ADT
 */
package application;

import java.util.Set;

/**
 * Filename:   GraphADT.java
 * Project:    Social Network
 */
public interface GraphADT {

 public boolean addEdge(Person p1, Person p2);

 public boolean removeEdge(Person p1, Person p2);

 public boolean addNode(Person p);

 public boolean removeNode(Person p);

 public Set<Person> getNeighbors(Person p);

 public Person getNode(String s);

 public Set<Person> getAllNodes();

}
