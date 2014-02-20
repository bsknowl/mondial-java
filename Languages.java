import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


public class Languages {

	String language;
	String percentage;
	
	public String getLanguage(){
		return language;
	}
	@XmlValue
	public void setLanguage(String language){
		this.language = language;
	}
	
	public String getPercentage(){
		return percentage;
	}
	@XmlAttribute
	public void setPercentage(String percentage){
		this.percentage = percentage;
	}
}
