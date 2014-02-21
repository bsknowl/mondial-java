import javax.xml.bind.annotation.XmlAttribute;

public class Members {
	// Members arraylist of String
	// must create an array to get each country
	
	String type;
	String country;
	
	public String getType(){
		return type;
	}
	@XmlAttribute
	public void setType(String type){
		this.type = type;
	}
	
	public String getCountry(){
		return country;
	}
	@XmlAttribute
	public void setCountry(String country){
		this.country = country;
	}
}
