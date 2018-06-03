package com.stringsai.feedmanagement.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="users")
public class SignRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7105560717126989797L;
	private String username;
	private String name;
	private String companyName;
	private byte[] password;
	private Set<String> following;
	
	
	public SignRequest() {
		this.following = new HashSet<>();
	}
	
	@Field("_id")
	public String getUsername() {
		return username;
	}
	
	@Field("_id")
	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
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

	public User convertToUser() {
		return new User(this.username, this.name, this.companyName, this.following);
	}

}
