package org.karibu.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Announcement implements Serializable {	

	@SerializedName("announcer_id")	
	public long announcerId;	

	public String city;	

	public String country;

	@SerializedName("created_at")	
	public String createdAt;	

	public String details;	

	@SerializedName("distance")
	public double distanceFromUser;

	public boolean gmaps;	

	public long id;	

	public double latitude;	

	public double longitude;	

	public String overview;	

	public float range;	

	public String state;

	public String street;

	@SerializedName("updated_at")	
	public String updatedAt;

	public String zip;
	
	public Announcer announcer;
	
	public List<Category> categories;

}
