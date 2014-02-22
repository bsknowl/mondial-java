import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Estuary {
	
	// elements
	String latitude;
	String longitude;
	String country; // code
	
	// element located
	ArrayList<Located> located;
	
	@XmlElement
	public String getLatitude(){
		return latitude;
	}
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
	
	@XmlElement
	public String getLongitude(){
		return longitude;
	}
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
	
	@XmlElement
	public ArrayList<Located> getLocated(){
		return located;
	}
	public void setLocated(ArrayList<Located> located){
		this.located = located;
	}
	
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
}
