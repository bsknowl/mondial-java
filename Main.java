import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class Main {

	private BufferedWriter output;
	private double d;
	public static void main(String[] args) {
		try{
			Main m = new Main();
            File file = new File("mondialXML.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Mondial.class);
            
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Mondial mondial = (Mondial) jaxbUnmarshaller.unmarshal(file);
            
            
            ArrayList<Country> country = mondial.country;
            ArrayList<Continent> continent = mondial.continent;
            ArrayList<Organization> organization = mondial.organization;
            ArrayList<Sea> sea = mondial.sea;
            ArrayList<River> river = mondial.river;
            ArrayList<Mountain> mountain = mondial.mountain;
            
            System.out.println("Name: " + country.get(0).name);
            System.out.println("Code: " + country.get(0).code);
            System.out.println("Area: " + country.get(0).area);
            System.out.println("Capital: " + country.get(0).capital);
            System.out.println("Memberships: " + country.get(0).memberships);
            System.out.println("Population: " + country.get(0).population);
            System.out.println("popGrowth: " + country.get(0).popGrowth);
            System.out.println("inf: " + country.get(0).infant);
            System.out.println("gdptot: " + country.get(0).gdpTotal);
            System.out.println("gdpagr: " + country.get(0).gdpAgri);
            System.out.println("inflation: " + country.get(0).inflation);
            System.out.println("ind. date: " + country.get(0).indep_date.date);
            System.out.println("indep. from: " + country.get(0).indep_date.from);
            System.out.println("indep. from: " + country.get(0).government);
            System.out.println("Encompassed cont: " + country.get(0).encompassed.continent);
            System.out.println("Encompassed perc: " + country.get(0).encompassed.percentage);
            System.out.println("Ethnic Groups: " + country.get(0).ethnicgroups.get(0).name);
            System.out.println("Ethnic Groups: " + country.get(0).ethnicgroups.get(0).percentage);
            System.out.println("Religions: " + country.get(0).religions.get(0).name);
            System.out.println("Religion percent: " + country.get(0).religions.get(0).percentage);
            System.out.println("Greece's languages: " + country.get(1).languages.get(0).language);
            System.out.println("Greece's perc: " + country.get(1).languages.get(0).percentage);
            System.out.println("Border: " + country.get(0).border.get(0).country);
            System.out.println("Border length: " + country.get(0).border.get(0).length);
            System.out.println("Province w/ cities(Greece): " + country.get(1).province.get(0).name + "\n"
            		+ country.get(1).getProvince().get(0).getCity().get(0).name);
         
            // Insert Statements
            File f = new File("countries.sql");
            // If file exists just write over it, else create new file and write to it.
            try {
                f.createNewFile();
                BufferedWriter output = new BufferedWriter(new FileWriter(f));
                output.write("\nALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD';\n");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
            m.insertCountry(country); //done
            m.insertPopulation(country); // done
            m.insertPolitics(country); // done
            m.insertEconomy(country); // done
            m.insertLanguage(country); // done
            m.insertEthnicGroup(country); // done
            m.insertReligion(country); // done
            m.insertBorders(country); /* done, but has duplicates. i.e. (Al, GR) (GR, AL)? */
            m.insertContinent(continent); // done
            m.insertEncompasses(country); // done
            m.insertCity(country);		// done
            m.insertProvince(country); // done
            m.insertOrganization(organization);
            m.insertIsMember(organization); //done
            m.insertSea(sea); 	// done
            m.insertRiver(river);// done
            m.insertRiverThrough(river); // done
            // lake
            // island
            // mountain 
            // desert
            // geo_sea?
            // to be continued
            // ...
            
            
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
		/** THIS INSERTS INTO riverThrough(River, Lake) **/
		private void insertRiverThrough(ArrayList<River> river) {
			File f = new File("countries.sql");
			// does file exist? append if yes, else print no
			if(f.exists()){
				try{
					output = new BufferedWriter(new FileWriter(f, true));
					// insert riverThrough values
					
					for(River r : river){
						if(r.through != null){
							for (Through t : r.through){
								output.write("INSERT INTO riverthrough VALUES ("
										+ stringOrNull(r.name) + ","
										 + stringOrNull(t.lake) +
												");\n" );
							}
						}
								
							
							
						
					}
					output.write("\nCOMMIT;\n\n\n");
					output.close();
					System.out.println("Appended riverThrough table successfully");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else{
				System.out.println("Could not append river table to file.");
			}
		
	}

		/*** THIS INSERTS INTO river TABLE river(name, river, lake, sea, length, sourceGeoCoord, mountains
	 * source elevation, estuaryGeoCoord)
	 * @param river
	 */
	private void insertRiver(ArrayList<River> river){
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert river values
				
				for(River r : river){
						String flows_to_r = flowsTo(r.to, "river");
						String flows_to_l = flowsTo(r.to, "lake");
						String flows_to_s = flowsTo(r.to, "sea");
						
							output.write("INSERT INTO river VALUES ("
									+ stringOrNull(r.name) + "," + stringOrNull(flows_to_r) + ","
									+ stringOrNull(flows_to_l) + "," + stringOrNull(flows_to_s) + ","
									+ numOrNull(r.length) + "," 
									+ "GeoCoord(" + r.source.longitude + "," 
									+ r.source.latitude + "),"
									+ stringOrNull(r.source.mountains) +  "," 
									+ numOrNull(r.source.elevation) + ","
									+ "GeoCoord(" + r.estuary.longitude + "," + r.estuary.latitude 
									 + "));\n" );
						
						
					
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended river table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append river table to file.");
		}
	}
	
	
	private String flowsTo(To to, String dest) {
		try{
			//System.out.println(to.waterType);
			if(dest.equals(to.watertype)){
				String delims = "-";
				String[] tokens = to.water.split(delims);
				return tokens[1];
			}else{
			return null;
			}
		} catch(Exception e){
			
		}
		return null;
		
	}

	/*** THIS INSERTS INTO sea TABLE Sea(Name, depth) ***/
	private void insertSea(ArrayList<Sea> sea){
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert sea values
				
				for(Sea s : sea){
					
							output.write("INSERT INTO sea VALUES ("
									+ stringOrNull(s.name) + ","
									+ numOrNull(s.depth)
									 + ");\n" );
						
					}
				
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended sea table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append sea table to file.");
		}
	}
	private void insertIsMember(ArrayList<Organization> organization) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert isMember values
				// get member values from string (as array)
				// default member type = "member"
			//	String member_type = "member";
				
				for(Organization o : organization){
					if(o.members != null){
						for (Members m : o.members){
							String[] members = getMembers(m.country);
							for(String s : members){
							output.write("INSERT INTO isMember VALUES ("
									+ stringOrNull(s) + ","
									+ stringOrNull(o.abbrev) + ","
									+ stringOrNull(m.type)
									 + ");\n" );
							}
						
						}
					}
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended isMember table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append isMember table to file.");
		}
	}

	// Gets the members out of organization
	private String[] getMembers(String country) {
		String delims = " ";
		String[] tokens = country.split(delims);
		/* Debug
		for(String s: tokens){
			System.out.println(s + " ");
		}
		*/
		return tokens;
	}
	
	private void insertEncompasses(ArrayList<Country> m) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert encompasses values
				
				for(int i = 0; i < m.size(); i++){
					if(m.get(i).encompassed != null){
							/* Insert code, continent, percent */
							output.write("INSERT INTO encompasses VALUES ("
									+ stringOrNull(m.get(i).code) + "," 
									+ stringOrNull(m.get(i).encompassed.continent) + ","
									+ numOrNull(m.get(i).encompassed.percentage) + ");\n" );
							
					}
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended encompasses table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append encompasses table to file.");
		}
	}


	private void insertBorders(ArrayList<Country> m) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert borders values
				
				for(int i = 0; i < m.size(); i++){
					if(m.get(i).border != null){
						for(int j = 0; j < m.get(i).border.size(); j++){
							/* Insert code, border_code, length */
							output.write("INSERT INTO borders VALUES ("
									+ stringOrNull(m.get(i).code) + "," 
									+ stringOrNull(m.get(i).border.get(j).country) + ","
									+ numOrNull(m.get(i).border.get(j).length) + ");\n" );
						}
							
					}
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended borders table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append borders table to file.");
		}
	}



	// Insert into Country Table, open sql file and write to it.
	public void insertCountry(ArrayList<Country> m){
		File f = new File("countries.sql");
		String country_cap = "";
		// new Buffered output and loop for the Insert statements
		try {
			output = new BufferedWriter(new FileWriter(f, true));
			for(int i = 0; i < m.size(); i++){
				//enter if a province exists
				
				if(m.get(i).getProvince() != null){
					
					/*** NEED TO CHECK FOR IS_COUNTRY_CAP == yes through province, then assign it
					 * the name
					 */
					for(int k = 0; k < m.get(i).province.size(); k++){
						if(m.get(i).province.get(k).city != null){
							for(int j = 0; j < m.get(i).province.get(k).city.size(); j++){
								if(m.get(i).province.get(k).city.get(j).countryCap != null){
									country_cap = m.get(i).province.get(k).name;
								}
							}
						}
					}
					
					output.write("INSERT INTO country VALUES (" + stringOrNull(m.get(i).name)
							+ "," + stringOrNull(m.get(i).code) + "," + stringOrNull(m.get(i).capital) 
							+ "," + stringOrNull(country_cap) + "," + numOrNull(m.get(i).area) 
							+ "," + numOrNull(m.get(i).population) + ");\n");
				} else{
					//enter null if no province exists
					output.write("INSERT INTO country VALUES (" + "" + stringOrNull(m.get(i).name)
							+ "," + stringOrNull(m.get(i).code) + "," + stringOrNull(m.get(i).capital) 
							+ "," + stringOrNull(m.get(i).name) + "," + numOrNull(m.get(i).area) 
							+ "," + numOrNull(m.get(i).population) + ");\n");
				}
			}
			output.write("\n\nCOMMIT;\n\n\n");
			output.close();
			System.out.println("File written to successfully\n" + "Appended countries table");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// insert into the city Array, just append to file
	public void insertCity(ArrayList<Country> m){
		File f = new File("countries.sql");
		if(f.exists()){
			
			try {
				output = new BufferedWriter(new FileWriter(f, true));
				
				// get the cities for the given country and loop through each country
				// then loop through the city array to grab cities
				for(int i = 0; i < m.size(); i++){
					// if province null, just insert null
					if(m.get(i).province == null){
					// check if array is not null then proceed (avoids null pointer exception)
						if(m.get(i).getCity() != null){
						
						// debug
						//System.out.println(m.size() + " " + m.get(i).name + " " + m.get(i).city.size());
						for(int j = 0; j < m.get(i).getCity().size(); j++){
							//create Insert into city table
							// if province exists, insert, else insert null
							
								output.write("INSERT INTO City VALUES (" + stringOrNull(m.get(i).city.get(j).name) 
									+ "," + stringOrNull(m.get(i).city.get(j).code) + "," + stringOrNull(m.get(i).name)
									+ "," + numOrNull(m.get(i).city.get(j).population) + "," 
									+ numOrNull(m.get(i).city.get(j).longitude)
									+ "," + numOrNull(m.get(i).city.get(j).latitude) + ",NULL);\n");
							//debugging
							//System.out.println(m.get(i).getCity().get(j).name);
							}
						}
					} else {
						// debug
						
						// now inserting cities with non-null provinces
						for(int j = 0; j < m.get(i).province.size(); j++){
							
							//System.out.println("debug, country.province not null " + m.get(i).province.get(j).name);
							if(m.get(i).province.get(j).getCity() != null){
								
								
								for(int k = 0; k < m.get(i).province.get(j).city.size(); k++){
									// debug
									/*	System.out.println("debug, city not null" + m.get(i).province.size()
											+ " " + m.get(i).province.get(j).getCity().get(k).name);
									 */	
									output.write("INSERT INTO City VALUES (" + stringOrNull(m.get(i).province.get(j).city.get(k).name) 
											+ "," + stringOrNull(m.get(i).province.get(j).city.get(k).code) + "," 
											+ stringOrNull(m.get(i).province.get(j).name)
											+ "," + numOrNull(m.get(i).province.get(j).city.get(k).population) + "," 
											+ numOrNull(m.get(i).province.get(j).city.get(k).longitude)
											+ "," + numOrNull(m.get(i).province.get(j).city.get(k).latitude) + ",NULL);\n");
								}
							}
						}
					}
				}
				output.write("\nCOMMIT;\n\n");
				output.close();
				System.out.println("Appended cities successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else
			System.out.println("Could not append to file");
	}
	
	// insert into population table
	public void insertPopulation(ArrayList<Country> m){

		File f = new File("countries.sql");
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert population here
				for(int i = 0; i < m.size(); i++){	
					output.write("INSERT INTO population VALUES (" + stringOrNull(m.get(i).code) + ","
							+ numOrNull(m.get(i).popGrowth) + "," + numOrNull(m.get(i).infant) +");\n");
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended population table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append population to file.");
		}
	}
	
	// politics table
	public void insertPolitics(ArrayList<Country> m){
		File f = new File("countries.sql");
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert politics values here
			
				for(int i = 0; i < m.size(); i++){	
					String dependent, date, from;
					// check to see if dependent is null;
					if (m.get(i).dependent != null){
						dependent = m.get(i).dependent.getDependent();
					}else{
						dependent = null;
					}
					
					// check to see if date is null
					if(m.get(i).indep_date != null){
						date = m.get(i).indep_date.date;
						from = m.get(i).indep_date.from;
					}else{date = null; from = null;}
					
					
					
					/* INSERT INTO POLITICS( code, date, independent_from, dependent, gov) */
					output.write("INSERT INTO politics VALUES (" + stringOrNull(m.get(i).code) + ","
							+ stringOrNull(date) + "," + stringOrNull(from) + ","
							+ stringOrNull(dependent) + "," + stringOrNull(m.get(i).government)
							+");\n");
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended politics table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append politics to file.");
		}
	}

	//economy table insert
	public void insertEconomy(ArrayList<Country> m){
		File f = new File("countries.sql");
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert economy values here
				
				for(int i = 0; i < m.size(); i++){	
					output.write("INSERT INTO economy VALUES (" + stringOrNull(m.get(i).code) + "," + numOrNull(m.get(i).gdpTotal) 
							+ "," + numOrNull(m.get(i).gdpAgri) + "," + numOrNull(m.get(i).gdpInd) + "," + numOrNull(m.get(i).gdpServ) 
							+ "," + numOrNull(m.get(i).inflation) + ");\n");
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended economy table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append economy to file.");
		}
	}
	
	// Insert language table
	public void insertLanguage(ArrayList<Country> m){
		File f = new File("countries.sql");
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert language values here
				
				for(int i = 0; i < m.size(); i++){	
					// insert language 
					
					if(m.get(i).languages != null){
						for(int j = 0; j < m.get(i).languages.size(); j++){
							output.write("INSERT INTO language VALUES (" + stringOrNull(m.get(i).code) + "," + stringOrNull(m.get(i).languages.get(j).language) + "," + numOrNull(m.get(i).languages.get(j).percentage) + ");\n");

						}
					}
					
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended language table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append language table to file.");
		}
	}
	
	// Insert Ethnic Group
	public void insertEthnicGroup(ArrayList<Country> m){
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert ethnicgroup values
				
				for(int i = 0; i < m.size(); i++){	
					 
						// need percentage of ethnic group
					if(m.get(i).ethnicgroups != null){
						for(int j = 0; j < m.get(i).ethnicgroups.size(); j++){
							output.write("INSERT INTO ethnicGroup VALUES (" + stringOrNull(m.get(i).code) 
								+ "," + stringOrNull(m.get(i).ethnicgroups.get(j).name) + "," + numOrNull(m.get(i).ethnicgroups.get(j).percentage) + ");\n");
						}
					}
				}
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended ethnicgroup table successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append ethnicgroup table to file.");
		}
	}
	
	// Insert Religion 
    public void insertReligion(ArrayList<Country> m){
        File f = new File("countries.sql");
        // does file exist? append if yes, else print no
        if(f.exists()){
            try{
                output = new BufferedWriter(new FileWriter(f, true));
                // insert Religion values

                for(int i = 0; i < m.size(); i++){
                     // need percentage of religions
                    if(m.get(i).religions !=null){
                        for(int j = 0; j < m.get(i).religions.size(); j++){
                            output.write("INSERT INTO religion VALUES (" + stringOrNull(m.get(i).code)
                                + "," + stringOrNull(m.get(i).religions.get(j).name) + "," + numOrNull(m.get(i).religions.get(j).percentage) + ");\n");
                        }
                    }
                }
                output.write("\nCOMMIT;\n\n\n");
                output.close();
                System.out.println("Appended religion table successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Could not append religion table to file.");
        }
    }
		
		
    // Insert continent
    public void insertContinent(ArrayList<Continent> m){
        File f = new File("countries.sql");
        // does file exist? append if yes, else print no
        if(f.exists()){
            try{
                output = new BufferedWriter(new FileWriter(f, true));
                // insert continent values

                for(int i = 0; i < m.size(); i++){
                            output.write("INSERT INTO continent VALUES (" + stringOrNull(m.get(i).name) + "," + numOrNull(m.get(i).area) +");\n" );
                        }
                output.write("\nCOMMIT;\n\n\n");
                output.close();
                System.out.println("Appended continent table successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Could not append continent table to file.");
        }
    }
				
	public void insertProvince(ArrayList<Country> m){
		File f = new File("countries.sql");
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert Province here
				for(Country country : m){
					if(country.province != null){
						//debug
						/* Insert(name, country(code), population, area, capital, capProv) */
						for(int i = 0; i < country.province.size(); i++){
							/* DEBUG */
							/*  System.out.println("province: " + country.province.get(i).country + "  " 
							 
									+ country.province.get(i).name + "  "
									+ country.province.get(i).capital + " "
									+ country.province.get(i).area +  " " 
									+ country.province.get(i).population + " " 
									+ country.province.get(i).id);
									*/
							
							/* Get the capital of the province, must check if city is null
							 * for null pointer exception
							 */
							String capital = null;
							if(country.province.get(i).city != null){
							//	System.out.println(country.province.get(i).city.get(0).name);
								capital = country.province.get(i).city.get(0).name;
							}
						
							/* Insert(province name, code, pop, area, capital, name) */
							output.write("INSERT INTO province VALUES (" + stringOrNull(country.province.get(i).name) + ","
									+ stringOrNull(country.province.get(i).country) + ","
									+ numOrNull(country.province.get(i).population) + ","
									+ numOrNull(country.province.get(i).area) + ","
									+ stringOrNull(capital) + "," 
									+ stringOrNull(country.province.get(i).name) + ");\n");
						}
					} else{ 
						//else need to insert countries with no province and have city as cap...etc.
						// enter the capital city of country, check if null for np exception
						// capital usually is the first city
							if(country.city != null){
								/* Insert(country, code, pop, area, capital, name) */
								output.write("INSERT INTO province VALUES (" + stringOrNull(country.name) + ","
										+ stringOrNull(country.code) + "," 
										+ numOrNull(country.population) + ","
										+ numOrNull(country.area) + ","
										+ stringOrNull(country.city.get(0).name) + "," + stringOrNull(country.name) + ");\n");
							}	
						}
					}


                output.write("\nCOMMIT;\n\n\n");
				System.out.println("Successfuly appended province table to file");
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append table province to file.");
		}
		
	}

    // Insert organization
    public void insertOrganization(ArrayList<Organization> m){
        File f = new File("countries.sql");
        // does file exist? append if yes, else print no
        if(f.exists()){
            try{
                output = new BufferedWriter(new FileWriter(f, true));
                // insert Organization values

                for(int i = 0; i < m.size(); i++){
                    output.write("INSERT INTO organization VALUES (" + stringOrNull(m.get(i).abbrev)
                            + "," + stringOrNull(m.get(i).name)
                            + "," + stringOrNull(m.get(i).headq)
                            + "," + "NULL"
                            + "," + "NULL"
                            + "," + stringOrNull(m.get(i).established) +");\n" );
                }
                output.write("\nCOMMIT;\n\n\n");
                output.close();
                System.out.println("Appended organization table successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Could not append organization table to file.");
        }
    }


	/* This checks if there is a String or if it contains NULL
	 * if it is a String, we put ' surrounding it, else change
	 * it to NULL
	 */
	private String stringOrNull(String o){
		String t;
		if(o == null){
			//System.out.println("NULL");
			t = "NULL";
		}else{
			t = "'" + o.concat("'");
		}
		//System.out.println(t);
		return t;
	}
	
	/* This checks to see if the String does in fact contain
	 * a number. If so, return just the number, else return NULL
	 * No " ' " needed for this.
	 */
	private String numOrNull(String o){
		String t;
		try{
			if (o == null){
				return "NULL";
			}
		d = Double.parseDouble(o);
		t = o;
		} catch(NumberFormatException nfe){
			return "NULL";
		}
		return t;
	}

	
}
