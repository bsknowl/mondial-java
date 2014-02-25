import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class Country {
	
	//declarations for xml
	
	String code, capital, memberships, 
			name, area, government, population,
			relpercent, percentage, popGrowth,
			from, continent, infant, gdpInd, gdpServ,
			gdpTotal, gdpAgri, inflation;
    
	 
	 /* NEEDS STILL */
	// Dependent date?
	 String date;
	 
    
     Indep_Date indep_date;
     Dependent dependent;
     ArrayList<Encompassed> encompassed;
	 ArrayList<EthnicGroups> ethnicgroups;
	 ArrayList<Religions> religions;
	 ArrayList<Languages> languages;
	 ArrayList<Border> border;
	 
	//create arraylist for city and province
	 // either 1 or more city
	 // or 1 or more province
	 ArrayList<City> city;
	 ArrayList<Province> province;
	 public Country(){
		 
	 }
   
    //country name
    @XmlAttribute(name="car_code")
    public String getCode(){
            return code;
    }

    public void setCode(String code){
            this.code = code;
    }
    
    //country area
    @XmlAttribute
    public String getArea(){
       return area;
    }

    public void setArea(String area){
        this.area = area;
    }
    
    //capital
    @XmlAttribute
    public String getCapital(){
            return capital;
    }

    public void setCapital(String capital){
            this.capital = capital;
    }
    
    //memberships
    @XmlAttribute
    public String getMemberships(){
    	return memberships;
    }
    
    public void setMemberships(String memberships){
    	this.memberships = memberships;
    }
    
    //name
    @XmlElement
    public String getName(){
            return name;
    }

    public void setName(String name){
            this.name = name;
    }

    //population
    @XmlElement
    public String getPopulation(){
        return population;
    }

    public void setPopulation(String population){
        this.population = population;
    }
    
    //population growth
    @XmlElement(name="population_growth")
    public String getPopulationGrowth(){
		return popGrowth;
    }
    
    public void setPopulationGrowth(String popGrowth){
    	this.popGrowth = popGrowth;
    }
    
    //infant mortality
    @XmlElement(name="infant_mortality")
    public String getInfant(){
    	return infant;
    }
    
    public void setInfant(String infant){
    	this.infant = infant;
    }
    
    // total gdp
    @XmlElement(name="gdp_total")
    public String getGdpTot(){
    	return gdpTotal;
    }
    
    public void setGdpTot(String gdpTotal){
    	this.gdpTotal = gdpTotal;
    }
    
    //gdp agri
    @XmlElement(name="gdp_agri")
    public String getGdpAgri(){
    	return gdpAgri;
    }
    
    public void setGdpAgri(String gdpAgri){
    	this.gdpAgri = gdpAgri;
    }
    
    @XmlElement(name="gdp_ind")
    public String getGdpInd(){
    	return gdpInd;
    }
    public void setGdpInd(String gdpInd){
    	this.gdpInd = gdpInd;
    }
    
    @XmlElement(name="gdp_serv")
    public String getGdpServ(){
    	return gdpServ;
    }
    public void setGdpServ(String gdpServ){
    	this.gdpServ = gdpServ;
    }
    // inflation
    @XmlElement
    public String getInflation(){
    	return inflation;
    }
    
    public void setInflation(String inflation){
    	this.inflation = inflation;
    }
    
    // Indep_date
    @XmlElement(name="indep_date")
    public Indep_Date getIndepDate(){
    	return indep_date;
    }
    
    public void setIndepDate(Indep_Date indep_date){
    	this.indep_date = indep_date;
    }
    
    @XmlElement(name="dependent")
    public Dependent getDependent(){
    	return dependent;
    }
    public void setDependent(Dependent dependent){
    	this.dependent = dependent;
    }
    
    // government
    @XmlElement(name="government")
    public String getGov(){
    	return government;
    }
    
    public void setGov(String government){
    	this.government = government;
    }
    
    /* Figure how to do encompassed */
    // encompassed element, only attr. continent and percentage
    @XmlElement(name="encompassed")
    public ArrayList<Encompassed> getEncompassed(){
    	return encompassed;
    }
    public void setEncompassed(ArrayList<Encompassed> encompassed){
    	this.encompassed = encompassed;
    }
 
    // EthnicGroups is arraylist that gets the percentage attribute and the name of group
    @XmlElement(name="ethnicgroups")
    public ArrayList<EthnicGroups> getEthnicGroups(){
		return ethnicgroups;
    }
    
    public void setEthnicGroups(ArrayList<EthnicGroups> ethnicgroups){
    	this.ethnicgroups = ethnicgroups;
    }

    // Religions, same as ethnicgroups. need name and attribute 
    // as an arraylist
    @XmlElement(name="religions")
    public ArrayList<Religions> getReligions(){
    	return religions;
    }
    
    public void setReligions(ArrayList<Religions> religions){
    	this.religions = religions;
    }
    
    @XmlElement(name="languages")
    public ArrayList<Languages> getLanguages(){
    	return languages;
    }
    public void setLanguages(ArrayList<Languages> languages){
    	this.languages = languages;
    }
    
    
    // border is an arraylist of attributes
    // create class and get the attributes
    @XmlElement(name="border")
    public ArrayList<Border> getBorder(){
    	return border;
    }
    public void setBorder(ArrayList<Border> border){
    	this.border = border;
    }
    
    
    // call province class for city classes
    
    public ArrayList<Province> getProvince(){
        return province;
    }
    @XmlElement
    public void setProvince(ArrayList<Province> province){
        this.province = province;
    }
    
    // City wrapper, create new class with arraylist
    @XmlElement
    public ArrayList<City> getCity(){
    	return city;
    }
    public void setCity(ArrayList<City> city){
    	this.city = city;
    }
  
    
}
