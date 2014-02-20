import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;





public class Indep_Date {

	
	String date;
	
	String from;
	
	public String getDate(){
	return date;
	}
	@XmlValue
	public void setDate(String date){
	this.date = date;
	}

	public String getFrom(){
		return from;
	}
	@XmlAttribute
	public void setFrom(String from){
		this.from = from;
	}
	

}
