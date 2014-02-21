
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

public class Desert extends Elements{

	private ArrayList<Located> located;
	
	private String id;
	private String country;
	private String type;
	
	public Desert(){
		super();
	}
	
	public ArrayList<Located> getLocated(){
		return located;
	}
	public void setLocated(ArrayList<Located> located){
		this.located = located;
	}
	
	@XmlAttribute
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	@XmlAttribute
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	@XmlAttribute
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
}
