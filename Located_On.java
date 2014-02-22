import javax.xml.bind.annotation.XmlAttribute;

public class Located_On {
	
	private String island;
	
	@XmlAttribute
	public String getIsland(){
		return island;
	}
	public void setIsland(String island){
		this.island = island;
	}
}
