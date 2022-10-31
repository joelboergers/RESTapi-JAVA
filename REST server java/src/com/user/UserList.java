
package com.user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;

import com.server.Server;
import com.sun.net.httpserver.HttpExchange;

public class UserList
{
    private static final int     MAX_USER = 100;
    private           User[]     userTab;
    
    /**
     * UserList constructor
     * @param UserListGroesse
     */
	 public UserList(int UserListGroesse){
	   this.userTab = new User[MAX_USER];
	 }
	 
	 public UserList(){
	     this(MAX_USER);
	 }
     
     /**
      * This method add user into the userList
      * @param user
      */
    public void addUser(User user){
       for(int i = 0; i < userTab.length; i++){
           if(userTab[i] == null){
        	  userTab[i] = user; 
              break;
            }
        }
    }
    
    /**
     * This method find and user and change his fistname or lastname
     * @param key
     * @param keyValue
     * @param newValue
     */
    public void editUser(String key, String keyValue, String newValue) {
    	if(key.equals("firstname")) {
    		getUserWithFirstname(keyValue).setfirstname(newValue);
    	}else {
    		getUserWithLastname(keyValue).setlastname(newValue);
    	}
    	addToFile();
    	toString();
    }
    
    /**
     * Method to find an user an delete him
     * @param key
     * @param keyValue
     */
    public void deleteUser(String key, String keyValue) {
    	int j = 0;
    	for(int i = 0; i < userTab.length; i++){
    		if(key.equals("firstname")) {
    			if(userTab[i].getfirstname().equals(keyValue)){
                    for(j = i; j < userTab.length - 1; j++) {
                    	userTab[j] = userTab[j+1];
                    }
                    userTab[j] = null;
                    break;
                 }
    		}else {
    			if(userTab[i].getlastname().equals(keyValue)){
                    for(j = i; j < userTab.length - 1; j++) {
                    	userTab[j] = userTab[j+1];
                    }
                    userTab[j] = null;
                    break;
                 }
    		}
         }
    	addToFile();
    }
    
    /**
     * This method copy th euser list into the file.
     */
    public void addToFile() {
		try {
		      FileWriter myWriter = new FileWriter("user.json", false);
		        for(int i = 0; i < MAX_USER; i++){
		            if(userTab[i] != null){
		            	myWriter.write(userTab[i].toJSON() + "\n");
		            } else {
		                break;
		            }
		        }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		}
    
    /**
     * this method add all user store in the "user.json" file into the userList
     * this Method is call befor anything else when the server start
     * @throws IOException
     */
    public void getFromFile() throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader("user.json"));
    	String fileLine = br.readLine();
    	while(fileLine != null) {
    		Object[] hash = fromJSON(fileLine);
            User user = new User();
            user.setfirstname(hash[1].toString());
            user.setlastname(hash[3].toString());
            addUser(user);
    		fileLine = br.readLine();
    	}
		br.close();
    }
    
    /**
     * 
     * @param json
     * @return Object[] with all key and value from the JSON string
     */
    public Object[] fromJSON(String json) {
        MessageFormat mf = new MessageFormat("'{'\"{0}\":\"{1}\",\"{2}\":\"{3}\"'}'");
        Object[] hash = null;
        try {
            hash = mf.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    /**
     * Convert an user to a string JSON format 
     * @return String in a JSON format
	 */
    public String toJSON() {
    	 User user = getUser(0);
         String print = "";
         if (user == null){
             print = "No user store!";
             
         }else {
         	for(int i = 0; i < MAX_USER; i++){
 	            if(userTab[i] != null){
 	            	print += MessageFormat.format("'{'\"firstname\":\"{0}\",\"lastname\":\"{1}\"'}'\r\n", userTab[i].getfirstname(),  userTab[i].getlastname());
 	            } else {
 	                break;
 	            }
             }
         }
         
         return print;
    }
    
    /**
     * 
     * @param json
     * @return Object[] with the key and value from the JSON string
     */
    public Object[] fromJSONWithOnePrams(String json) {
        MessageFormat mf= new MessageFormat("'{'\"{0}\":\"{1}\"'}'");
        Object[] hash = null;
        try {
            hash = mf.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    /**
     * Clear the userList
     */
    public void clearUserTab() {
    	for(int i = 0; i <userTab.length; i++) {
    		userTab[i] = null;
    	}
    }
    
    /**
     * Get an user with an index from the userList
     * @param index
     * @return
     */
    public User getUser(int index){
        for(int i = 0; i < userTab.length; i++){
            if(i == index){
                return userTab[i];
             }
         }
        return null;
      }
    
    /**
     * Find a user with an firstname in the userList
     * @param firstname
     * @return User
     */
    public User getUserWithFirstname(String firstname) {
    	for(int i = 0; i < userTab.length; i++){
    		if(userTab[i] == null ) return null;
            if(userTab[i].getfirstname().equals(firstname)){
                return userTab[i];
             }
         }
		return null;
    }
    
    /**
     * Find a user with an lastname in the userList
     * @param firstname
     * @return User
     */
    public User getUserWithLastname(String lastname) {
    	if(lastname.isEmpty()) {
    		throw new IllegalArgumentException("lastname empty");
    	}
	for(int i = 0; i < userTab.length; i++){
		if(userTab[i] == null ) return null;
        if(userTab[i].getlastname().equals(lastname)){
            return userTab[i];
         }
     }
	return null;
    }
    
    
    /**
     * Method toString give all user back
     */
    public String toString(){
        return "Firstname Lastname \r\n" + printAllUser();
     }
     /**	
      * 
      * @return String with all user from the userList
      */
    public String printAllUser(){
    	
        User user = getUser(0);
        String print = "";
        if (user == null){
            print = "No user store!";
            
        }else {
        	for(int i = 0; i < MAX_USER; i++){
	            if(userTab[i] != null){
	            	print += userTab[i] + "\r\n";	
	            } else {
	                break;
	            }
            }
        }
        
        return print;
    }
        
}