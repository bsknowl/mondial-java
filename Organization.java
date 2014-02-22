import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Organization {
	/* Organization is an element list from Mondial root*/
	
	// Elements
	String name;
	String abbrev;
	String established;
	
	// Attributes for Organization element
	String id;
	String headq;
	
	// multiple element with attribute
	/* members is zero or more, but within each the attribute, must be parsed to
	 * get each country so create new class.
	 */
	
	ArrayList<Members> members;
	
	// Elements
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getAbbrev(){
		return abbrev;
	}
	
	public void setAbbrev(String abbrev){
		this.abbrev = abbrev;
	}
	
	public String getEstablished(){
		return established;
	}
	
	public void setEstablished(String established){
		this.established = established;
	}
	
	// attributes id & headq
	public String getId(){
		return id;
	}
	@XmlAttribute
	public void setString(String id){
		this.id = id;
	}
	
	public String getHeadq(){
		return headq;
	}
	@XmlAttribute
	public void setHeadq(String headq){
		this.headq = headq;
	}
	// multiple element members
	public ArrayList<Members> getMembers(){
		return members;
	}
	@XmlElement(name="members")
	public void setMembers(ArrayList<Members> members){
		this.members = members;
	}
	
}
