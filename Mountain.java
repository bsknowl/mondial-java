
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

public class Mountain {
    private String name;
    private String elevation;
    private String longitude;
    private String latitude;
    private String mountains;

    // Attributes for Organization element
    private String id;
    private String country;
    private String type;
    private String island;

    private ArrayList<Located> located;
    
    public ArrayList<Located> getLocated(){
    	return located;
    }
    public void setLocated(ArrayList<Located> located){
    	this.located = located;
    }
    
    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getMountains(){
    	return mountains;
    }
    public void setMountains(String mountains){
    	this.mountains = mountains;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }
}
