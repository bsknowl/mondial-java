
import javax.xml.bind.annotation.XmlAttribute;




public class Border {

	String country;
	String length;
	
	
	public String getCountry(){
		return country;
	}
	@XmlAttribute
	public void setCountry(String country){
		this.country = country;
	}
	
	public String getLength(){
		return length;
	}
	
	@XmlAttribute
	public void setLength(String length){
		this.length = length;
	}
}
