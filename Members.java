import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

public class Members {
	// Members arraylist of String
	// must create an array to get each country
	
	ArrayList<String> type;
	ArrayList<String> country;
	
	public ArrayList<String> getType(){
		return type;
	}
	@XmlAttribute
	public void setType(ArrayList<String> type){
		this.type = type;
	}
	
	public ArrayList<String> getCountry(){
		return country;
	}
	@XmlAttribute
	public void setCountry(ArrayList<String> country){
		this.country = country;
	}
}
