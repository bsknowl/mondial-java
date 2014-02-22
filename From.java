import javax.xml.bind.annotation.XmlAttribute;

public class From {

		// just Attributes
		String watertype; // lake, etc.
		String water; // name of body of water

	@XmlAttribute(name="watertype")
	public String getWaterType(){
		return watertype;
	}
	public void setWaterType(String waterType){
		this.watertype = waterType;
	}
	
	@XmlAttribute
	public String getWater(){
		return water;
	}
	public void setWater(String water){
		this.water = water;
	}

}
