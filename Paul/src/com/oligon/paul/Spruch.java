package com.oligon.paul;

public class Spruch {
	
	int _id;
	String _spruch;
	String _land;
	
	public Spruch(){
		
	}
	
	public Spruch(int id, String spruch, String land){
		this._id= id;
		this._spruch= spruch;
		this._land= land;
		
	}
	
	public Spruch(String spruch, String land){
		this._spruch= spruch;
		this._land= land;
	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public String getSpruch() {
		return this._spruch;
	}

	public void setSpruch(String spruch) {
		this._spruch = spruch;
	}

	public String getLand() {
		return this._land;
	}

	public void setLand(String land) {
		this._land = land;
	}
	
	
	
	
	
}
