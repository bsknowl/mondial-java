import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Lake {

	
	// Elements
	String name;
	String area;
	String elevation;
	String depth;
	String longitude;
	String latitude;
	
	// Attributes
	String id;
	String country;
	String island;
	String type;
	
	ArrayList<Located> located;
	To to;
	
	// Element get and set
		@XmlElement
		public String getName(){
			return name;
		}
		public void setName(String name){
			this.name = name;
		}
		
		@XmlElement
		public String getArea(){
			return area;
		}
		public void setArea(String area){
			this.area = area;
		}
		
		@XmlElement
		public String getElevation(){
			return elevation;
		}
		public void setElevation(String elevation){
			this.elevation = elevation;
		}
		
		@XmlElement
		public String getDepth(){
			return depth;
		}
		public void setDepth(String depth){
			this.depth = depth;
		}
	
		public String getLongitude(){
			return longitude;
		}
		@XmlElement
		public void setLongitude(String longitude){
			this.longitude = longitude;
		}
		
		public String getLatitude(){
			return latitude;
		}
		@XmlElement
		public void setLatitude(String latitude){
			this.latitude = latitude;
		}
		
		
	// Attributes get and set
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
	@XmlAttribute
	public String getIsland(){
		return island;
	}
	public void setIsland(String island){
		this.island = island;
	}
	@XmlAttribute
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	
	// Class get and set
	@XmlElement
	public To getTo(){
		return to;
	}
	public void setTo(To to){
		this.to = to;
	}
	@XmlElement
	public ArrayList<Located> getLocated(){
		return located;
	}
	public void setLocated(ArrayList<Located> located){
		this.located = located;
	}
	
}
