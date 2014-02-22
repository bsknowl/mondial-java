import javax.xml.bind.annotation.XmlAttribute;

public class Continent {
	// element continent from Mondial root
	/* Continent has attribute id and elements name and area */
	
	// elements
	String name, area;
	
	//attributes
	String id;
	public Continent(){
		
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getArea(){
		return area;
	}
	public void setArea(String area){
		this.area = area;
	}
	
	public String getId(){
		return id;
	}
	@XmlAttribute
	public void setId(String id){
		this.id = id;
	}
}
