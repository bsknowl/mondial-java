
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class Source extends Elements{
	// Single Elements
	
	String mountains; // from mountains
	String elevation;
	
	// Attributes
	String country; // code
	
	// class elements 
	ArrayList<Located> located;
	ArrayList<From> from;

	
	// set and get sing tag element
	
	public Source(){
		super();
	}
	
	@XmlElement
	public String getElevation(){
		return elevation;
	}
	public void setElevation(String elevation){
		this.elevation = elevation;
	}
	
	@XmlElement
	public String getMountains(){
		return mountains;
	}
	public void setMountains(String mountains){
		this.mountains = mountains;
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
