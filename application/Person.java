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
 * Social Network Person Class
 */
package application;

public class Person{
    public String Name;

    public Person(String userName) {
        this.Name = userName;
    }
    
    public String getName() {
    	return this.Name;
    }
}