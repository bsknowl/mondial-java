import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Source {
	// Single Elements
	
	String mountains; // from mountains
	String elevation;
	String longitude;
	String latitude;
	
	// Attributes
	String country; // code
	
	// class elements 
	ArrayList<Located> located;
	ArrayList<From> from;
	
	public Source(){
	}
	// set and get sing tag element
	@XmlElement
	public String geMountains(){
		return mountains;
	}
	public void setMountains(String mountains){
		this.mountains = mountains;
	}
	
	@XmlElement
	public String getElevation(){
		return elevation;
	}
	public void setElevation(String elevation){
		this.elevation = elevation;
	}
	
	@XmlElement
	public String getLongitude(){
		return longitude;
	}
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
	
	@XmlElement
	public String getLatitude(){
		return latitude;
	}
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
	
	// set attribute
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	// set and get classes Element
	@XmlElement
	public ArrayList<Located> getLocated(){
		return located;
	}
	public void setLocated(ArrayList<Located> located){
		this.located = located;
	}
	
	@XmlElement
	public ArrayList<From> getFrom(){
		return from;
	}
	public void setFrom(ArrayList<From> from){
		this.from = from;
	}
}
