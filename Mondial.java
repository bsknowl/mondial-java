import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mondial {
	ArrayList<Country> country;
	ArrayList<Continent> continent;
	ArrayList<Organization>	organization;
	ArrayList<Sea> sea;
	ArrayList<River> river;
	ArrayList<Lake> lake;
	ArrayList<Island> island;
	ArrayList<Mountain> mountain;
	ArrayList<Desert> desert;
	
	public Mondial(){
		
	}
	
	@XmlElement
    public ArrayList<Country> getCountry(){
    	return country;
    }
    public void setCountry(ArrayList<Country> country){
    	this.country = country;
    }
    
    @XmlElement
    public ArrayList<Continent> getContinent(){
    	return continent;
    }
    public void setContinent(ArrayList<Continent> continent){
    	this.continent = continent;
    }
    
    @XmlElement
    public ArrayList<Organization> getOrganization(){
    	return organization;
    }
    public void setOrganization(ArrayList<Organization> organization){
    	this.organization = organization;
    }
    
    @XmlElement
    public ArrayList<Sea> getSea(){
    	return sea;
    }
    public void setSea(ArrayList<Sea> sea){
    	this.sea = sea;
    }
    
    @XmlElement
    public ArrayList<River> getRiver(){
    	return river;
    }
    public void setRiver(ArrayList<River> river){
    	this.river = river;
    }
    
    @XmlElement
    public ArrayList<Lake> getLake(){
    	return lake;
    }
    public void setLake(ArrayList<Lake> lake){
    	this.lake = lake;
    }
    
    @XmlElement
    public ArrayList<Island> getIsland(){
    	return island;
    }
    public void setIsland(ArrayList<Island> island){
    	this.island = island;
    }

    @XmlElement
    public ArrayList<Mountain> getMountain(){
    	return mountain;
    }
    public void setMountain(ArrayList<Mountain> mountain){
    	this.mountain = mountain;
    }
	
    public ArrayList<Desert> getDesert(){
    	return desert;
    }
    public void setDesert(ArrayList<Desert> desert){
    	this.desert = desert;
    }
	
}
