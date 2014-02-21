
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
// Let's just inherit some of Sea's stuff!
public class River extends Sea{
	// Element River contained in Mondial Root
	
	// just attribute
	String length; // just data
	
	Estuary estuary;
	
	//multiple elemenst, need array or new class?
	To to;
	Source source; // contains multiple elements with country attr.
	ArrayList<Through> through; // empty data, just attr.
	
	
	// constructor
	public River(){
		super();
	}
	
	@XmlElement
	public Source getSource(){
		return source;
	}
	public void setSource(Source source){
		this.source = source;
	}
	
	@XmlElement
	public To getTo(){
		return to;
	}
	public void setTo(To to){
		this.to = to;
	}
	
	@XmlElement
	public String getLength(){
		return length;
	}
	public void setLength(String length){
		this.length = length;
	}
	
	// get and set Class Elements
	@XmlElement
	public Estuary getEstuary(){
		return estuary;
	}
	public void setEstuary(Estuary estuary){
		this.estuary = estuary;
	}
	
	
	
	@XmlElement
	public ArrayList<Through> getThrough(){
		return through;
	}
	public void setThrough(ArrayList<Through> through){
		this.through = through;
	}
	
}
