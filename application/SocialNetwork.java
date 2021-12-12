package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SocialNetwork implements SocialNetworkADT{
	
	Graph graph = new Graph();
	Person selectedUser;

	/**
	 * Takes two users and makes them friends. 
	 * 
	 * @param user1 
	 * @param user2 
	 * @return true if friendship is created, false otherwise
	 */
	public boolean addFriends(String user1, String user2) {
	
		Person p1 = graph.getNode(user1);
		Person p2 = graph.getNode(user2);

		// checks are handled by Graph
		// if (!graph.getAllNodes().contains(p1) || 
		// 		!graph.getAllNodes().contains(p2)) {
		// 	return false;
		// }
		
		return graph.addEdge(p1, p2);
	}
	
	/**
	 * Removes a friendship between two users.
	 * 
	 * @param user1 
	 * @param user2 
	 * @return true - if friendship is removed, false otherwise
	 */
	public boolean removeFriends(String user1, String user2) {
		
		Person p1 = graph.getNode(user1);
		Person p2 = graph.getNode(user2);

		// if (!graph.getAllNodes().contains(p1) || 
		// 		!graph.getAllNodes().contains(p2)) {
		// 	return false;
		// }
		
		return graph.removeEdge(p1, p2);
	}
	
	/**
	 * Adds a user to Social network
	 * 
	 * @param user 
	 * @return true if user is added to Social network, false otherwise
	 */
	public boolean addUser(String user) {
		
		Person p = graph.getNode(user);
			
		return graph.addNode(p);
	}
	
	/**
	 * Removes a user from Social network. 
	 * 
	 * @param user 
	 * @return true if user is removed from Social network, false otherwise
	 */
	public boolean removeUser(String user) {

		Person p = graph.getNode(user);
	
		return graph.removeNode(p);
	}
	
	/**
	 * A set of user's friends
	 * 
	 * @param user 
	 * @return set of user's friends
	 */
	public Set<Person> getFriends(String user) {
		
		Person p = graph.getNode(user);
	
		return graph.getNeighbors(p);
		
	}
	
	
	/**
	 * 
	 * Gets all of the friends between two different friends.
	 * 
	 * @param user1
	 * @param user2
	 * @return - the mutual friends between friend1 and friend2. 
	 */
	public Set<Person> getMutualFriends(String user1, String user2){
		
		Person p1 = graph.getNode(user1);
		Person p2 = graph.getNode(user2);
		
		//Initialize Sets
		Set<Person> p1Friends = graph.getNeighbors(p1);
	    Set<Person> p2Friends = graph.getNeighbors(p2);
		
	    //Retain common friends 
		p1Friends.retainAll(p2Friends);
		return p1Friends;
	}
	
	/**
	 * Returns the shortest path between two users. 
	 * 
	 * @param user1  
	 * @param user2
	 * @return - shortest list between two users. 
	 */
	public List<Person> getShortestPath(String user1, String user2){
		
		Person p1 = graph.getNode(user1);
		Person p2 = graph.getNode(user2);	
		
		HashMap<Person,Person> path = new HashMap<Person,Person>();
		
		shortestBFS(p1, p2, path);
		
		ArrayList<Person> shortestPath = new ArrayList<Person>();
		
		Person p = p2;
		while (path.get(p) !=null)
		{
			shortestPath.add(path.get(p));
			p = path.get(p);
		}
		
		return shortestPath;
	}
	
	/**
	 * Gets shortest path using a BFS. 
	 * 
	 * @param p1
	 * @param p2
	 * @param shortestPath
	 */
	private void shortestBFS(Person p1, Person p2, HashMap<Person,Person> shortestPath) {
		
		LinkedList<Person> queue = new LinkedList<Person>();
		
		HashMap<Person, Boolean> visitedBool = new HashMap<Person, Boolean>(graph.order());
		for (Person p : graph.getAllNodes())
		{
			visitedBool.put(p, false);
			shortestPath.put(p, null);
		}
	
		visitedBool.put(p1, true);
		queue.add(p1);
		
		while(!queue.isEmpty())
		{
			Person p = queue.remove();
			ArrayList<Person> adjList= new ArrayList<Person>();
			adjList.addAll(graph.getNeighbors(p));
			for (int i = 0 ;i < adjList.size(); i++)
			{
				
				if (visitedBool.get(adjList.get(i)) ==  false)
				{
					visitedBool.put(adjList.get(i), true);
					shortestPath.put(adjList.get(i), p);
					queue.add(adjList.get(i));
					
					
					if (adjList.get(i) == p2)
						{return;}
				}
			}
		}
		
		
		return;
	}
	
	/**
	 *  
	 */
	public Set<Graph> getConnectedComponents() {
		return null;
		
	}

	@Override
	public void loadFromFile(File f) throws FileNotFoundException {
		Scanner input = new Scanner(f);

		while (input.hasNextLine()) {
			String nextLine = input.nextLine();
			String[] lineArray = nextLine.split(" ",3);
			switch(lineArray[0]) {
				case "a":
					if (lineArray.length > 2) {addFriends(lineArray[1], lineArray[2]);}
					else {addUser(lineArray[1]);}
					break;
				case "r":
					if (lineArray.length > 2) {removeFriends(lineArray[1], lineArray[2]);}
					else {removeUser(lineArray[1]);}
					break;
				case "s":
					selectedUser = graph.getNode(lineArray[1]);
					break;
				default:

			}
		}
		
		input.close();
	}

	@Override
	public void saveToFile(File f) throws IOException {
		FileWriter output = new FileWriter(f);
		//TODO: in-order, add users, edges, and set current user

		output.close();
	}
	
}
