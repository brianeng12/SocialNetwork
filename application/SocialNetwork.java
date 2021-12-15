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
	ArrayList<String> commands = new ArrayList<String>();
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
		if (p1 == null) {p1 = new Person(user1);}
		Person p2 = graph.getNode(user2);
		if (p2 == null) {p2 = new Person(user2);}

		// checks are handled by Graph
		// if (!graph.getAllNodes().contains(p1) || 
		// 		!graph.getAllNodes().contains(p2)) {
		// 	return false;
		// }
		boolean result = graph.addEdge(p1, p2);
		if (result) {commands.add("a "+user1+" "+user2);}
		return result;
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

		boolean result = graph.removeEdge(p1, p2);
		if (result) {commands.add("r "+user1+" "+user2);}
		return result;
	}
	
	/**
	 * Adds a user to Social network
	 * 
	 * @param user 
	 * @return true if user is added to Social network, false otherwise
	 */
	public boolean addUser(String user) {
		
		if (user == null) {return false;}
		Person p = new Person(user);
			
		boolean result = graph.addNode(p);
		if (result) {commands.add("a "+user);}
		return result;
	}
	
	/**
	 * Removes a user from Social network. 
	 * 
	 * @param user 
	 * @return true if user is removed from Social network, false otherwise
	 */
	public boolean removeUser(String user) {

		if (user == null) {return false;}
		Person p = graph.getNode(user);
		if (p == null) {return false;}

		boolean result = graph.removeNode(p);
		if (result) {commands.add("r "+user);}
		return result;
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
	 * Groups in the social network
	 * 
	 * @return - Number of connected components (groups) in the social network
	 */
	public int getConnectedComponents() {
		ArrayList<ArrayList<Person>> connectedComponents = new ArrayList<ArrayList<Person>>();
		
		Set<Person> people = graph.getAllNodes();
		
		HashMap<Person, Boolean> visitedPerson = new HashMap<Person, Boolean>(graph.order());
		for (Person p : people)
		{
			visitedPerson.put(p, false);
		}
		
		for(Person p : people)
		{
			if (!visitedPerson.get(p))
			{
				ArrayList<Person> components = new ArrayList<Person>();
				DFS(visitedPerson, components, p);
				connectedComponents.add(components);
			}
		}
	
		return connectedComponents.size();	
	}
	
	/***
	 * DFS algorithm 
	 * 
	 * @param visitedPerson - track visited persons
	 * @param components - groups
	 * @param p1 - person to begin DFS
	 */
	private void DFS(HashMap <Person, Boolean> visitedPerson, ArrayList<Person> components, Person p1) {
		visitedPerson.put(p1, true);
		components.add(p1);
		for (Person p2 : graph.getNeighbors(p1)) {
			if (!visitedPerson.get(p2)) {
				DFS(visitedPerson,components,p2);
			}
		}
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
					System.out.println("Invalid command.");
			}
		}

		input.close();
	}

	@Override
	public void saveToFile(File f) throws IOException {
		FileWriter output = new FileWriter(f);
		for (int i = 0 ; i < commands.size(); i++) {
	    	output.write(commands.get(i) + "\n");
	   	}
		output.write("s " + selectedUser);
		output.close();
	}
	
	public boolean personExists(String user) {
		return graph.getNode(user) != null ? true : false;
	}
	
}
