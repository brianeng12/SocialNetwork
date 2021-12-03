package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SocialNetwork {
	
	/**
	 * Takes two users and makes them friends. 
	 * 
	 * @param user1 
	 * @param user2 
	 * @return true if friendship is created, false otherwise
	 */
	public boolean addFriends(String user1, String user2) {
	
		if (!graph.getAllNodes().containsKey(user1) || 
				!graph.getAllNodes().containsKey(user2)) {
			return false;
		}
		
		Person p1 = graph.getNode(user1);
		Person p2 = graph.getNode(user2);
		
		graph.addEdge(p1, p2);
		return true;
	}
	
	/**
	 * Removes a friendship between two users.
	 * 
	 * @param user1 
	 * @param user2 
	 * @return true - if friendship is removed, false otherwise
	 */
	public boolean removeFriends(String user1, String user2) {
		
		if (!graph.getAllNodes().containsKey(user1) || 
				!graph.getAllNodes().containsKey(user2)) {
			return false;
		}
		
		Person p1 = graph.getNode(user1);
		Person p2 = graph.getNode(user2);
		
		graph.removeEdge(p1, p2);
		return true;
	}
	
	/**
	 * Adds a user to Social network
	 * 
	 * @param user 
	 * @return true if user is added to Social network, false otherwise
	 */
	public boolean addUser(String user) {
		
		
		if (graph.getAllNodes().containsKey(user)) {
			return false;
		}
		
		Person p = graph.getNode(user);
			
		graph.addNode(p);
		return true;
	}
	
	/**
	 * Removes a user from Social network. 
	 * 
	 * @param user 
	 * @return true if user is removed from Social network, false otherwise
	 */
	public boolean removeUser(String user) {
		 
		if (!graph.getAllNodes().containsKey(user)) {
			return false; 
		}
		
		Person p = graph.getNode(user);
	
		graph.removeNode(p);
		return true;
	}
	
	/**
	 * A set of user's friends
	 * 
	 * @param user 
	 * @return set of user's friends
	 */
	public Set<Person> getFriends(String user) {
		 
		if (!graph.getAllNodes().containsKey(user)) {
			return; 
		}
		
		Person p = graph.getNode(user);
	
		return graph.getNeighbors(p);
		
	}
	
	
	/**
	 * 
	 * Gets all of the friends between two different friends.
	 * 
	 * @param friend1
	 * @param friend2
	 * @return - the mutual friends between friend1 and friend2. 
	 */
	public Set<Person> getMutualFriends(String friend1, String friend2){
		
		Person p1 = graph.getNode(friend1);
		Person p2 = graph.getNode(friend2);
		
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
		
		Person p1 = graph.getNode(friend1);
		Person p2 = graph.getNode(friend2);	
		
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
	public Set<Graph> getNumConnectedComponents() {
		
	}
	
}
