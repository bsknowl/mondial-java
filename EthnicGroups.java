import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


public class EthnicGroups {

	String name;
	String percentage;
	
	public String getName(){
		return name;
	}
	@XmlValue
	public void setName(String name){
		this.name = name;
	}
	
	public String getPercent(){
		return percentage;
	}
	@XmlAttribute(name="percentage")
	public void setPercent(String percentage){
		this.percentage = percentage;
	}
}
