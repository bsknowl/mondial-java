import javax.xml.bind.annotation.XmlAttribute;

public class Dependent {
	
	String country;
	
	public Dependent(){
		
	}
	
	
	public String getDependent(){
		return country;
	}
	@XmlAttribute(name="country")
	public void setDependent(String country){
		this.country = country;
	}
}
