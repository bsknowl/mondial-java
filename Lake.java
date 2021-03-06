import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Lake extends Sea{

	
	// Elements
	
	String elevation;
	
	// Attributes
	String id;
	String country;
	String island;
	String type;
	
	ArrayList<Located> located;
	To to;
	
	public Lake(){
		super();
	}
	// Element get and set
		
		
		@XmlElement
		public String getElevation(){
			return elevation;
		}
		public void setElevation(String elevation){
			this.elevation = elevation;
		}
		
		
		
	// Attributes get and set

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

}
