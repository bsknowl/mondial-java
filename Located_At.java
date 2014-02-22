import javax.xml.bind.annotation.XmlAttribute;

public class Located_At {

	private String watertype;
	private String river;
	private String sea;
	private String lake;
	
	@XmlAttribute
	public String getWaterType(){
		return watertype;
	}
	public void setWaterType(String watertype){
		this.watertype = watertype;
	}
	
	@XmlAttribute
	public String getRiver(){
		return river;
	}
	public void setRiver(String river){
		this.river = river;
	}
	
	@XmlAttribute
	public String getSea(){
		return sea;
	}
	public void setSea(String sea){
		this.sea = sea;
	}
	
	
	@XmlAttribute
	public String getLake(){
		return lake;
	}
	public void setLake(String lake){
		this.lake = lake;
	}
}
