package com.stringsai.feedmanagement.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7802181938803392051L;
	private String username;
	private String name;
	private String companyName;
	private Set<String> following;
	
	public User() {
		this.following = new HashSet<>();
	}
	
	public User(String username, String name,String company, Set<String> following) {
		this.username = username;
		this.name = name;
		this.companyName = company;
		this.following = following;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Set<String> getFollowing() {
		return following;
	}

	public void setFollowing(Set<String> following) {
		this.following = following;
	}
	
	public void addFollowing(String user) {
		this.following.add(user);
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", name=" + name + ", companyName=" + companyName + "]";
	}

}
