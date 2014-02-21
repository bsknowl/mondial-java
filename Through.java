
import javax.xml.bind.annotation.XmlAttribute;

public class Through {

	// Just one attribute which goes through lake
	
	String lake;
	
	@XmlAttribute
	public String getLake(){
		return lake;
	}
	public void setLake(String lake){
		this.lake = lake;
	}
}
