package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SocialNetworkManagerADT {
	/**
	 * Construct the social network with 
	 * the input file which contains the
	 * instructions like
	 * a deb brian
	 * r brian 
	 * s deb
	 * 
	 * @param input file of insturctions
	 * @throws Exception 
	 * @throws IllegalCharacterException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void constructNetwork(File file) throws FileNotFoundException, IOException, IllegalCharacterException, Exception;
	
	/**
	 * Set the specific person as the 
	 * center of the network so that
	 * we can base him/her to display
	 * different information 
	 * 
	 * 
	 * @param username to set the central person
	 */
	public void centralize(String person);
	

	/**
	 * get the central person
	 * 
	 * @return user name of central person
	 */
	public String getCentralPerson();
	
	
	/**
	 * Add a user into the social network
	 * If successful, return true
	 * If the user already exists in the network
	 * Keep the existed user, no changes of the network
	 * If the user is null
	 * There won't be any changes of the network
	 * 
	 * @param username to add the person into newtowk
	 * @throws IllegalCharacterException 
	 */
    public void addPerson(String person) throws IllegalCharacterException;
	
	/**
	 * Set friendship between user1 and user2
	 * If any user name is null
	 * Do nothing
	 * If user1 and user2 are friends already
	 * Do nothing
	 * 
	 * @param username to add the person into network
	 * @throws IllegalCharacterException 
	 *
	 **/
    public void setFriendship(String person1, String person2) throws IllegalCharacterException;
     
     
	/**
	 * Remove a user from the social network
	 * If user is null
	 * do nothing
	 * If the user was not in the network
	 * do nothing
	 * 
	 * @param username to remove it from the network
	 * 
	 */
 	public void removePerson(String person);
 	
	/**
	 * Remove friendship between user1 and user2
	 * If any of user is null
	 * do nothing
	 * Else if user1 and user2 are not friends
	 * do nothing
     * Else
     * remove the friendship between user1 and user2
	 * 
	 * @param username1
	 * @param username2
	 */ 
 	public void removeFriendship(String person1, String person2);
 	
	/**
	 * Get a set of person who are friends of the user
	 * If the user is NOT in the social network
	 * Return NULL
	 * Else
	 * Return the friend list of the user
	 * 
	 * @param  username to get the friends of the user
	 * @return List<String> friend list of the user
	 */
 	public List<String> getPersonalNetwork(String person);
 	
	/**
	 * Get a set of person who are friends of the user
	 * If the user is NOT in the social network
	 * Return NULL
	 * Else
	 * Return the friend list of the user
	 * 
	 * @return a set of all users in the network
	 */
 	public Set<String> getAllUsers();
 	
	/**
	 * Get a list of the mutual friends between
	 * user1 and user2
	 * If the user is NOT in the social network
	 * Return NULL
	 * Else
	 * Return the list of the mutual friends of 
	 * user1 and user2
	 * 
	 * @param username1
	 * @param username2
	 * @return a set of all users in the network
	 * @throws IllegalCharacterException 
	 */
 	public List<String> mutualFriends(String person1, String person2) throws IllegalCharacterException;
 	
	/**
	 * Find the short path to from user1 and user2
	 * If any user is NOT in the social network
	 * Return NULL 
	 * else
	 * return the path
	 * 
	 * @param username1
	 * @param username2
	 * @return a list of persons in the path with order 
	 * @throws IllegalCharacterException 
	 */
 	public List<String> shortestPath(String person1, String person2) throws IllegalCharacterException;
 	
	/**
	 * Save the history operations of the network
	 * to the indicated file
	 * 
	 * @param file
	 * @throws IOException 
	 * 
	 */
    public void saveLog(File file) throws IOException;
}