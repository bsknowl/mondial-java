import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;


import com.sun.xml.internal.txw2.annotation.XmlElement;

public class City {

	String cityId, countryCap, code, name, year, longitude, latitude, population;
	private String is_state_cap;
	
	private ArrayList<Located_At> located_at;
	private ArrayList<Located_On> located_on;
	
	
	public City(){
		
	}
	
	// gets the city Id
	@XmlAttribute(name="id")
	public String getCityId(){
		return cityId;
	}
	
	public void setCityId(String cityId){
		this.cityId = cityId;
	}
	
	// gets a yes or no if city is country capital
	@XmlAttribute(name="is_country_cap")
	public String getCountryCap(){
		return countryCap;
	}
	
	public void setCountryCap(String countryCap){
		this.countryCap = countryCap;
	}
	
	@XmlAttribute(name="is_state_cap")
	public String getIsStateCap(){
		return is_state_cap;
	}
	public void setIsStateCap(String is_state_cap){
		this.is_state_cap = is_state_cap;
	}
	
	// gets country code
	// redundant?
	@XmlAttribute(name="country")
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	
	/* XmlElements */
	// name of city
	@XmlElement
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	// longitude
	@XmlElement
	public String getLongitude(){
		return longitude;
	}
	
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
	
	// longitude
	@XmlElement
	public String getLatitude(){
		return latitude;
	}
	
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
	
	// year at population
	@XmlAttribute(name="year")
	public String getYear(){
		return year;
	}
	public void setYear(String year){
		this.year=year;
	}
	
	// city population
	@XmlElement
	public String getPopulation(){
		return population;
	}
	
	public void setPopulation(String population){
		this.population = population;
	}
	
	
	public ArrayList<Located_At> getlocated_at(){
		return located_at;
	}
	@XmlElement
	public void setlocated_at(ArrayList<Located_At> located_at){
		this.located_at = located_at;
	}
	
	public ArrayList<Located_On> getlocated_on(){
		return located_on;
	}
	@XmlElement
	public void setlocated_on(ArrayList<Located_On> located_on){
		this.located_on = located_on;
	}
	
}
