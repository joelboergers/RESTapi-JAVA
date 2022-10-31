package com.user;

import java.text.MessageFormat;


public class User {
	private String firstname; 
	private String lastname;
	
	public User(){
	}
	
	
	/**
	 * Constructor User
	 * 
	 * @param firstname
	 * @param lastname
	 * 
	 */
	public User(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	/**
	 * @param firstname
	 * methode to set this user firstname
	 */
	public void setfirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * @param lastname
	 *  methode to set this user lastname
	 */
	public void setlastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * 
	 * @return this user firstname
	 */
	public String getfirstname() {
		return firstname;
	}
	
	/**
	 * 
	 * @return this user lastname
	 */
	public String getlastname() {
		return lastname;
	}
	
	
	/**
	 * 
	 * @return an string with this user firstname and lastname
	 */
	public String toString(){
		return firstname + " " + lastname;
	}
	
	
	/**
	 * 
	 * @return an String in json fromat with this user firstname and lastname
	 */
	public String toJSON() {
        return MessageFormat.format("'{'\"firstname\":\"{0}\",\"lastname\":\"{1}\"'}'", firstname, lastname);

    }  
	
}
