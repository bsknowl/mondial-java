import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Island extends Elements{


	// class 
	ArrayList<Located> located;
	
	// Attributes
	String id;
	String sea;
	String lake;
    String river;
	String country;
	String type;
	
	// Elements
	String elevation;
	String islands;
	
	public Island(){
		super();
	}
	
	
	//attribute set and get
	@XmlAttribute
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	@XmlAttribute
	public String getSea(){
		return sea;
	}
	public void setSea(String sea){
		this.sea = sea;
	}
	
	@XmlAttribute
     public String getLake(){
        return lake;
    }
    public void setLake(String lake){
        this.lake = lake;
    }

    @XmlAttribute
    public String getRiver(){
        return river;
    }
    public void setRiver(String river){
        this.river = river;
    }
	
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	
	@XmlAttribute
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
		
	
	// elements get and set
	@XmlElement
	public String getElevation(){
		return elevation;
	}
	public void setElevation(String elevation){
		this.elevation = elevation;
	}
	
	@XmlElement
	public String getIslands(){
		return islands;
	}
	public void setIslands(String islands){
		this.islands = islands;
	}
	
	// get set for located
	@XmlElement
	public 	ArrayList<Located> getLocated(){
		return located;
	}
	public void setLocated(	ArrayList<Located> located){
		this.located = located;
	}

	
	
}
