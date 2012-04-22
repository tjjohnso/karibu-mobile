package org.karibu.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Category implements Serializable {
	@SerializedName("created_at")	
	public String createdAt;
	
	public int id;
	
	public String name;

	@SerializedName("updated_at")	
	public String updatedAt;
}
