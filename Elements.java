
import javax.xml.bind.annotation.XmlElement;

public class Elements {

	private String name;
	private String area;
	private String depth;
	private String latitude;
	private String longitude;
	
	
	@XmlElement
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	@XmlElement
	public String getArea(){
		return area;
	}
	public void setArea(String area){
		this.area = area;
	}
	
	@XmlElement
	public String getDepth(){
		return depth;
	}
	public void setDepth(String depth){
		this.depth = depth;
	}
	
	public String getLongitude(){
		return longitude;
	}
	@XmlElement
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
	
	public String getLatitude(){
		return latitude;
	}
	@XmlElement
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
}
