import javax.xml.bind.annotation.XmlAttribute;

import com.sun.xml.internal.txw2.annotation.XmlElement;

public class City {

	String cityId, countryCap, code, name, year, longitude, latitude, population;
	
	
	/* NEEDS */
	// Elevation
	// located_at (zero or more) array
	// located_on (zero or more) array
	/* attributes needed */
	// is_state_cap
	// province (idref)
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
	
	// figure out located_at
	// e.g. watertype="sea"
	
	//figure out located_on
	//e.g. island="..."
}
