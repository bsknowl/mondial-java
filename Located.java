
import javax.xml.bind.annotation.XmlAttribute;

public class Located {

	
	String country;
	String province;
	
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	@XmlAttribute
	public String getProvince(){
		return province;
	}
	public void setProvince(String province){
		this.province = province;
	}
	
	
	
}
