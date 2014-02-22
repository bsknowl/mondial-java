import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;



public class Province {

	String id;
	String country;
	String capital;
	String name;
	String area;
	String population;
	
	ArrayList<City> city;
	
	
	public Province(){
		
	}
	// province name
	@XmlElement
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	// province area
	@XmlElement
	public String getArea(){
		return area;
	}
	public void setArea(String area){
		this.area = area;
	}
	
	//province population
	@XmlElement
	public String getPopulation(){
		return population;
	}
	public void setPopulation(String population){
		this.population = population;
	}
	// City wrapper, create new class with arraylist
    @XmlElement
    public ArrayList<City> getCity(){
    	return city;
    }
    public void setCity(ArrayList<City> city){
    	this.city = city;
    }
    
    @XmlAttribute
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id=id;
	}
	
	//country element
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	// capital
	@XmlAttribute
	public String getCapital(){
		return capital;
	}
	public void setCapital(String capital){
		this.capital = capital;
	}
	
}
