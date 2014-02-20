import javax.xml.bind.annotation.XmlAttribute;

public class Encompassed {

	String continent;
	String percentage;
	
	public String getContinent(){
		return continent;
	}
	@XmlAttribute
	public void setContinent(String continent){
		this.continent = continent;
	}
	
	public String getPercentage(){
		return percentage;
	}
	@XmlAttribute
	public void setPercentage(String percentage){
		this.percentage = percentage;
	}
	
}
