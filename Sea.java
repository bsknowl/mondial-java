import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Sea extends Elements{

	// Elements 
	String name;  // sea name
	String area;	// area of sea (optional)
	String depth;	// dpeth of sea (optional)
	
	// Attributes
	
	String id;	// ID of sea (required)
	String country;	// country codes for it (array type, needs parsing)
	String bordering; // borders other seas (implied) - array type, needs parsing (sea-NAME ..)
	
	/* multiple locateds with attributes create own class */
	ArrayList<Located> located;	
	
	/* Getter and setter for Element tage */
	
	public Sea(){
		super();
	}
	
	/* Getter and setter for attributes */
	@XmlAttribute
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	// Getter and setter for located class 
	@XmlAttribute
	public String getBordering(){
		return bordering;
	}
	public void setBordering(String bordering){
		this.bordering = bordering;
	}
	
	@XmlElement
	public ArrayList<Located> getLocated(){
		return located;
	}
	public void setLocated(ArrayList<Located> located){
		this.located = located;
	}

}
