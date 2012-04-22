package org.karibu.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Announcer implements Serializable {

	public String city;	

	public String country;

	@SerializedName("created_at")
	public String createdAt;

	public long id;

	public float latitude;	

	public float longitude;
	
	public String name;

	public String state;

	public String street;

	@SerializedName("updated_at")	
	public String updatedAt;

	public String zip;

}
