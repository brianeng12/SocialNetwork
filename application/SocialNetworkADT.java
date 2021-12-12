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
 * Social Network ADT
 */

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SocialNetworkADT {
    
    public boolean addFriends(String s1, String s2);

    public boolean removeFriends(String s1, String s2);

    public boolean addUser(String s);

    public boolean removeUser(String s);

    public Set<Person> getFriends(String s);

    public Set<Person> getMutualFriends(String s1, String s2);

    public List<Person> getShortestPath(String s1, String s2);

    public Set<Graph> getConnectedComponents();

    public void loadFromFile(File f) throws FileNotFoundException;

    public void saveToFile(File f) throws IOException;
}
