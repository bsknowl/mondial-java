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
            ArrayList<Lake> lake = mondial.lake;
            ArrayList<Island> island = mondial.island;
            ArrayList<Desert> desert = mondial.desert;
            
         
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
           
            m.insertCountry(country); //done count exact
            m.insertPopulation(country); // done count exact
            m.insertPolitics(country); // done count exact
            m.insertEconomy(country); // done count exact
            m.insertLanguage(country); // done count exact
            m.insertEthnicGroup(country); // done count exact
            m.insertReligion(country); // done count exact
            m.insertBorders(country); // done, off by 1 count, but the xml and sql not same 
            m.insertContinent(continent); // done count exact (better be!)
            m.insertEncompasses(country); // done count exact
            m.insertCity(country);		// done off by 4...too many to try and look at
            m.insertProvince(country); // done exact count
            m.insertOrganization(organization, country); // done exact count
            m.insertIsMember(organization); //done exact count!!!!
            m.insertSea(sea); 	// done exact count
            m.insertRiver(river);// done exact count
            m.insertRiverThrough(river); // done exact count
            m.insertLake(lake); // done exact count 
            m.insertIsland(island); // done exact count
            m.insertMountain(mountain); // done exact count
            m.insertDesert(desert); // done exact count
            
            // could have made one method for the geo, but oh well
            
            m.insertGeoSea(sea, country); // done over by 2 entries (prob good)
            m.insertGeoRiver(river, country); // done exact count
            m.insertGeoSource(river, country); // done off by count of ~5 entries (idk yet)
            m.insertGeoEstuary(river, country); // done off by count ~16 entries (some not in xml)
            m.insertGeoLake(lake, country); // done exact count
            m.insertGeoIsland(island, country); // done exact count
            m.insertGeoMountain(mountain, country); // done off by count ~2 (under)
            m.insertGeoDesert(desert, country); // done exact count
            m.mergesWith(sea);/* Done, count good, but because symmetric it some are off */
            m.located(country, river, lake, sea); // done, off by 1
            m.locatedOn(country, island);// done exact count
            m.insertIslandIn(island, sea, lake, river); // done counts match
            m.insertMountainOnIsland(mountain, island); // done? counts match, looks good
            // DONE!
            
            
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	private void locatedOn(ArrayList<Country> country, ArrayList<Island> island){
		
		File f = new File("countries.sql");
		
		
		
		if(f.exists()){
			try{
				
				output = new BufferedWriter(new FileWriter(f, true));
				
				// loop over country and check if there is a province.
				// loop into province then cities or cities if no province exists
				// get the located_on element
				for( Country coun : country){
					
					// does province exist?
					if(coun.getProvince() != null){
						
						//loop down into province
						for(Province prov : coun.getProvince()){
							
							// is city inside province not null?
							if(prov.getCity() != null){
								
								// loop over city into located_on
								for(City city : prov.getCity()){
									//System.out.println("HELLO WORLD");
									// is there a located_on in city?
									if(city.getlocated_on() != null){
										// loop on located_on
										
										for(Located_On loc : city.getlocated_on()){
											// get id of island
											String island_id = loc.getIsland();
											
											
											// loop through island and get values to insert into table
											for(Island isle : island){
												// if they match, insert into table
												if(island_id.compareToIgnoreCase(isle.getId()) == 0){
													output.write("INSERT INTO locatedOn VALUES ("
															+ stringOrNull(city.getName()) + ","
															+ stringOrNull(prov.getName()) + ","
															+ stringOrNull(coun.getCode()) + ","
															+ stringOrNull(isle.getName())
															+ ");\n");
												}
											}
										}
									}
								}
							}
						}
					} else{
						// loop through city instead of province
						for(City city : coun.getCity()){
							
							// loop on locatedOn if not null
							if(city.getlocated_on() != null){
								
								for(Located_On loc : city.getlocated_on()){
									
									// get id of island located_on and match on island
									String island_id = loc.getIsland();
									
									for(Island isle : island){
										//insert into table if ids match
										if(island_id.compareToIgnoreCase(isle.getId()) == 0){
											output.write("INSERT INTO locatedOn VALUES ("
													+ stringOrNull(city.getName()) + ","
													+ stringOrNull(coun.getName()) + ","
													+ stringOrNull(coun.getCode()) + ","
													+ stringOrNull(isle.getName())
													+ ");\n");
										}
									}
								}
							}
						}
					}
				}
					
				
				
				
				output.close();
				commit("locatedAt and located");
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else{
			System.out.println("Could not append locatedOn and located table to file.");

		}
		
		
	}
	private void located(ArrayList<Country> country, ArrayList<River> river2, ArrayList<Lake> lake2, ArrayList<Sea> sea2) {
		
		String river= null, river_name = null;
		String sea = null, sea_name = null;
		String lake = null, lake_name = null;
		boolean river_flag = false;
		boolean sea_flag = false;
		boolean lake_flag = false;
						// loop over countries then cities/province
						// check for located_at
						// get the id from located_at
						
						for(Country coun : country){
							//debug
							
							if(coun.getProvince() != null){
								// debug
								// System.out.print("country: " + coun.getName() + " " + coun.getCode()+ "\t");
								for(Province pro : coun.getProvince()){
									
									if(pro.getCity() != null){
										//debug
										//System.out.print("Province: " + pro.getName() + "\n");
										for(City cit : pro.getCity()){
											
											//System.out.print("City: " + cit.getName() + "\n");
											if(cit.getlocated_at() != null){
												int count = 0;
												for(Located_At loc : cit.getlocated_at()){
													count++;
													
													/* We need to get the river | sea | lake id and compare to get the name
													 *  if we get a repeated one, insert into the table the previous 
													 * and retrieve the next name. if we reach to where we don't get repeats,
													 * insert into table and move on to the next city/province
													 */
													// iterate over river, lake, sea
													river = loc.getRiver();
													sea = loc.getSea();
													lake = loc.getLake();
														if(river != null){
															if(river_flag == true)
																insert(cit.getName(), pro.getName(), coun.getCode(), river_name, lake_name, sea_name);
																//river_flag = false;
																river_name = null;
																lake_name = null;
																sea_name = null;
															
															for(River riv : river2){
																if( riv.getId().compareToIgnoreCase(river) == 0){
																	river_name = riv.getName();
																	river_flag = true;
																	}
															}
														//	System.out.println("River_name: " + river_name);
															
														} else if(sea != null){
															if(sea_flag == true)
																insert(cit.getName(), pro.getName(), coun.getCode(), river_name, lake_name, sea_name);
																
																river_name =null;
																lake_name = null;
																sea_name = null;
															
															for(Sea s : sea2){
																if( s.getId().compareToIgnoreCase(sea) == 0){
																	sea_name = s.getName();
																	sea_flag = true;
																}
															}
														} else if(lake != null){
															if(lake_flag == true)
																insert(cit.getName(), pro.getName(), coun.getCode(), river_name, lake_name, sea_name);
																
																river_name = null;
																lake_name = null;
																sea_name = null;
															
															for(Lake lak : lake2){
																if( lak.getId().compareToIgnoreCase(lake) == 0){
																	lake_name = lak.getName();
																	lake_flag = true;
																}
															}
														}
														if(count == cit.getlocated_at().size()){
															insert(cit.getName(), pro.getName(), coun.getCode(), river_name, lake_name, sea_name);
															river_flag =false;
															river_name = null;
															sea_flag = false;
															sea_name = null;
															lake_flag = false;
															lake_name = null;
														}
												}
											}
										}
									}
								}
							}else{
								// no province exists
							//	System.out.println("country: " + coun.getName());
								// make sure city exists
								if(coun.getCity() != null){
									
									for(City cit : coun.getCity()){
									//	System.out.println("city: " + ci.getName());
										
										// located_at exists
										if(cit.getlocated_at() != null){
										int count = 0;
											for(Located_At loc : cit.getlocated_at()){
												count++;
													// iterate over river, lake, sea
												/* We need to get the river | sea | lake id and compare to get the name
												 *  if we get a repeated one, insert into the table the previous 
												 * and retrieve the next name. if we reach to where we don't get repeats,
												 * insert into table and move on to the next city...
												 */
													river = loc.getRiver();
													sea = loc.getSea();
													lake = loc.getLake();
													
													if(river != null){
															if(river_flag == true)
																insert(cit.getName(), coun.getName(), coun.getCode(), river_name, lake_name, sea_name);
																river_name = null;
																lake_name = null;
																sea_name = null;
															
															for(River riv : river2){
																if( riv.getId().compareToIgnoreCase(river) == 0){
																	river_name = riv.getName();
																	river_flag = true;
																	}
															}
															
														} else if(sea != null){
															if(sea_flag == true)
																insert(cit.getName(), coun.getName(), coun.getCode(), river_name, lake_name, sea_name);
																
																river_name =null;
																lake_name = null;
																sea_name = null;
															
															for(Sea s : sea2){
																if( s.getId().compareToIgnoreCase(sea) == 0){
																	sea_name = s.getName();
																	sea_flag = true;
																}
															}
														} else if(lake != null){
															if(lake_flag == true)
																insert(cit.getName(), coun.getName(), coun.getCode(), river_name, lake_name, sea_name);
																
																river_name = null;
																lake_name = null;
																sea_name = null;
															
															for(Lake lak : lake2){
																if( lak.getId().compareToIgnoreCase(lake) == 0){
																	lake_name = lak.getName();
																	lake_flag = true;
																}
															}
														}
													
													// when get_located reaches end of block, insert into the table 
														if(count == cit.getlocated_at().size()){
															insert(cit.getName(), coun.getName(), coun.getCode(), river_name, lake_name, sea_name);
															river_flag =false;
															river_name = null;
															sea_flag = false;
															sea_name = null;
															lake_flag = false;
															lake_name = null;
														}
														
												
												
									}
								}
							}
							
								}
							}
						}
						
							
						
				//		commit("located");			
		
	}
	
	
	private void insert(String name, String name2, String code, String name3,
			String string, String string2) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
				if(f.exists()){
					try{
						
						output = new BufferedWriter(new FileWriter(f, true));
						output.write("INSERT INTO located VALUES ("
								+ stringOrNull(name) + "," + stringOrNull(name2) + ","
								+ stringOrNull(code) + ","
								+ stringOrNull(name3) + "," + stringOrNull(string) + ","
								+ stringOrNull(string2) + ");\n");
						output.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
	
				} else{
					System.out.println("Could not append located table to file.");
	
				}
		
	}

	/* Insert into mergesWith(sea1, sea2) */
	private void mergesWith(ArrayList<Sea> sea) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
				if(f.exists()){
					try{
						
						output = new BufferedWriter(new FileWriter(f, true));
						
						// create array of booleans to flag to check
						// for symmetry
						boolean[] b = new boolean[sea.size()];
						for(Sea s : sea){
							String[] temp = getStrings(s.getBordering());
							
							
							for(int i = 0; i < temp.length; i++){
								for(Sea s1 : sea){
									if(temp[i].compareToIgnoreCase(s1.getId()) == 0){
										// this checks if the sea has already been seen
										// don't want to go over it twice
										if(b[sea.indexOf(s1)] == false){
										output.write("INSERT INTO mergesWith VALUES ("
												+ stringOrNull(s.getName()) + ","
												+ stringOrNull(s1.getName()) +
												");\n");
										}
										
									}
								}
								
									
								}
							b[sea.indexOf(s)] = true;
							}
					
						output.close();
						commit("mergesWith");
					} catch (IOException e) {
						e.printStackTrace();
					}
	
				} else{
					System.out.println("Could not append mergesWith table to file.");
	
				}
						
	}

	private void insertGeoDesert(ArrayList<Desert> desert, ArrayList<Country> country) {
		String name = null;
		String prov = null;
		boolean flag = false;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_estuary values
				
				// This inserts if no province exists
				for(Desert des : desert){
					if(des.getLocated() == null){
				//		System.out.println("null: " + r.getName());
						name = des.getName();
						String[] coun_temp = getStrings(des.getCountry());
						for(int i = 0; i < coun_temp.length; i++){
							for(Country c : country){
								if (c.getCode().compareToIgnoreCase(coun_temp[i]) == 0){
									prov = c.getName();
									output.write("INSERT INTO geo_desert VALUES ("
											+ stringOrNull(name) + ","
											+ stringOrNull(coun_temp[i]) + ","
											+ stringOrNull(prov)
											+ ");\n" );
								}
								
							}
						}
						
					}else{
						// inserts geo_lake if province exists
						
							name = des.getName();
							String[] countries = getStrings(des.getCountry());
							for(int i = 0; i < countries.length; i++){
								for(Located loc : des.getLocated()){
									if(countries[i].compareToIgnoreCase(loc.getCountry()) == 0){
										
										flag = true;
										String[] provs = getStrings(loc.getProvince());
										
										for(int j = 0; j < provs.length; j++){
											
											prov = compareProv(provs[j], country);
											output.write("INSERT INTO geo_desert VALUES ("
													+ stringOrNull(des.getName()) + ","
													+ stringOrNull(loc.getCountry()) + ","
													+ stringOrNull(prov)
													+ ");\n" );
											
										}
										break;
									} else {flag = false; }			
								}
								// if flag is false, country doesn't have a province and get name 
								// of country instead
								if(flag == false){
									for(Country c : country){
										if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
										
											prov = c.getName();
											output.write("INSERT INTO geo_desert VALUES ("
													+ stringOrNull(des.getName()) + ","
													+ stringOrNull(countries[i]) + ","
													+ stringOrNull(prov)
													+ ");\n" );
										}
									}
									
								}

									}	
								}
							}
								
			
				output.close();
				commit("geo_sea and geo_river and geo_source \nand geo_estuary and geo_lake and geo_island"
						+ "\nand geo_mountain and geo_desert");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_desert table to file.");
		}
		
	}

	private void insertGeoMountain(ArrayList<Mountain> mountain, ArrayList<Country> country) {
		String name = null;
		String prov = null;
		Boolean flag = false;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_estuary values
				
				// This inserts if no province exists
				for(Mountain mount : mountain){
					if(mount.getLocated() == null){
				//		System.out.println("null: " + r.getName());
						name = mount.getName();
						String[] coun_temp = getStrings(mount.getCountry());
						for(int i = 0; i < coun_temp.length; i++){
							for(Country c : country){
								if (c.getCode().compareToIgnoreCase(coun_temp[i]) == 0){
									prov = c.getName();
									output.write("INSERT INTO geo_mountain VALUES ("
											+ stringOrNull(name) + ","
											+ stringOrNull(coun_temp[i]) + ","
											+ stringOrNull(prov)
											+ ");\n" );
								}
								
							}
						}
						
					}else{
						// inserts geo_lake if province exists
						
							name = mount.getName();
							String[] countries = getStrings(mount.getCountry());
							for(int i = 0; i < countries.length; i++){
								for(Located loc : mount.getLocated()){
									if(countries[i].compareToIgnoreCase(loc.getCountry()) == 0){
										
										flag = true;
										String[] provs = getStrings(loc.getProvince());
										
										for(int j = 0; j < provs.length; j++){
											
											prov = compareProv(provs[j], country);
											output.write("INSERT INTO geo_mountain VALUES ("
													+ stringOrNull(mount.getName()) + ","
													+ stringOrNull(loc.getCountry()) + ","
													+ stringOrNull(prov)
													+ ");\n" );
											
										}
										break;
									} else {flag = false; }			
								}
								// if flag is false, country doesn't have a province and get name 
								// of country instead
								if(flag == false){
									for(Country c : country){
										if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
										
											prov = c.getName();
											output.write("INSERT INTO geo_mountain VALUES ("
													+ stringOrNull(mount.getName()) + ","
													+ stringOrNull(countries[i]) + ","
													+ stringOrNull(prov)
													+ ");\n" );
										}
									}
									
								}

									}	
								}
							}
								
			
				output.close();
			//	commit("geo_sea and geo_river and geo_source \nand geo_estuary and geo_lake and geo_island"
			//			+ "\nand geo_mountain");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_mountain table to file.");
		}
		
	}

	private void insertGeoIsland(ArrayList<Island> island, ArrayList<Country> country) {
		String name = null;
		String prov = null;
		Boolean flag = false;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_estuary values
				
				// This inserts if no province exists
				for(Island isle : island){
					if(isle.getLocated() == null){
				//		System.out.println("null: " + r.getName());
						name = isle.getName();
						String[] coun_temp = getStrings(isle.getCountry());
						for(int i = 0; i < coun_temp.length; i++){
							for(Country c : country){
								if (c.getCode().compareToIgnoreCase(coun_temp[i]) == 0){
									prov = c.getName();
									output.write("INSERT INTO geo_island VALUES ("
											+ stringOrNull(name) + ","
											+ stringOrNull(coun_temp[i]) + ","
											+ stringOrNull(prov)
											+ ");\n" );
								}
								
							}
						}
						
					}else{
						// inserts geo_lake if province exists
						
							name = isle.getName();
							String[] countries = getStrings(isle.getCountry());
							for(int i = 0; i < countries.length; i++){
								for(Located loc : isle.getLocated()){
									if(countries[i].compareToIgnoreCase(loc.getCountry()) == 0){
										
										flag = true;
										String[] provs = getStrings(loc.getProvince());
										
										for(int j = 0; j < provs.length; j++){
											
											prov = compareProv(provs[j], country);
											output.write("INSERT INTO geo_island VALUES ("
													+ stringOrNull(isle.getName()) + ","
													+ stringOrNull(loc.getCountry()) + ","
													+ stringOrNull(prov)
													+ ");\n" );
											
										}
										break;
									} else {flag = false; }			
								}
								// if flag is false, country doesn't have a province and get name 
								// of country instead
								if(flag == false){
									for(Country c : country){
										if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
										
											prov = c.getName();
											output.write("INSERT INTO geo_island VALUES ("
													+ stringOrNull(isle.getName()) + ","
													+ stringOrNull(countries[i]) + ","
													+ stringOrNull(prov)
													+ ");\n" );
										}
									}
									
								}

									}	
								}
							}
								
			
				output.close();
				//commit("geo_sea and geo_river and geo_source and geo_estuary and geo_lake and geo_island");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_island table to file.");
		}
		
	}

	/* Insert into geo_lake(Lake, country_code, province) */
	private void insertGeoLake(ArrayList<Lake> lake, ArrayList<Country> country) {
		String name = null;
		String prov = null;
		Boolean flag = false;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_estuary values
				
				// This inserts if no province exists
				for(Lake l : lake){
					if(l.getLocated() == null){
				//		System.out.println("null: " + r.getName());
						name = l.getName();
						String[] coun_temp = getStrings(l.getCountry());
						for(int i = 0; i < coun_temp.length; i++){
							for(Country c : country){
								if (c.getCode().compareToIgnoreCase(coun_temp[i]) == 0){
									prov = c.getName();
									output.write("INSERT INTO geo_lake VALUES ("
											+ stringOrNull(name) + ","
											+ stringOrNull(coun_temp[i]) + ","
											+ stringOrNull(prov)
											+ ");\n" );
								}
								
							}
						}
						
					}else{
						// inserts geo_lake if province exists
						
							name = l.getName();
							String[] countries = getStrings(l.getCountry());
							for(int i = 0; i < countries.length; i++){
								for(Located loc : l.getLocated()){
									if(countries[i].compareToIgnoreCase(loc.getCountry()) == 0){
										
										flag = true;
										String[] provs = getStrings(loc.getProvince());
										
										for(int j = 0; j < provs.length; j++){
											
											prov = compareProv(provs[j], country);
											output.write("INSERT INTO geo_lake VALUES ("
													+ stringOrNull(l.getName()) + ","
													+ stringOrNull(loc.getCountry()) + ","
													+ stringOrNull(prov)
													+ ");\n" );
											
										}
										break;
									} else {flag = false; }			
								}
								// if flag is false, country doesn't have a province and get name 
								// of country instead
								if(flag == false){
									for(Country c : country){
										if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
										
											prov = c.getName();
											output.write("INSERT INTO geo_lake VALUES ("
													+ stringOrNull(l.getName()) + ","
													+ stringOrNull(countries[i]) + ","
													+ stringOrNull(prov)
													+ ");\n" );
										}
									}
									
								}

									}	
								}
							}
								
			
				output.close();
				//commit("geo_sea and geo_river and geo_source and geo_estuary and geo_lake");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_lake table to file.");
		}
		
	}

	/* Insert into geo_estuary(River, country, province) */
	private void insertGeoEstuary(ArrayList<River> river, ArrayList<Country> country) {
		String name = null;
		String coun = null;
		String prov = null;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_estuary values
				
				// This inserts if no province exists
				for(River r : river){
					if(r.getEstuary().getLocated() == null){
				//		System.out.println("null: " + r.getName());
						name = r.getName();
						coun = r.getEstuary().getCountry();
						for(Country c : country){
							if (c.getCode().compareToIgnoreCase(coun) == 0){
								prov = c.getName();
								output.write("INSERT INTO geo_estuary VALUES ("
										+ stringOrNull(name) + ","
										+ stringOrNull(coun) + ","
										+ stringOrNull(prov)
										+ ");\n" );
							}
							
						}
					}else{
						// inserts geo_estuary if province exists
						for(Located l : r.getEstuary().getLocated()){
							name = r.getName();
							coun = l.getCountry();
							String[] provs = getStrings(l.getProvince());
							// loops on length of provinces in located
							// then country to province
							
							for(int i = 0; i < provs.length; i++){
								for(Country c : country){
									if(c.getProvince() != null){
										for(Province p : c.getProvince()){
											if(provs[i].compareToIgnoreCase(p.getId()) == 0){
												prov = p.getName();
												output.write("INSERT INTO geo_estuary VALUES ("
														+ stringOrNull(name) + ","
														+ stringOrNull(coun) + ","
														+ stringOrNull(prov)
														+ ");\n" );
											}
										}
									}
								}
							}
						}
					}
							
				}
				output.close();
			//	commit("geo_sea and geo_river and geo_source and geo_estuary");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_estuary table to file.");
		}
		
	}

	/* Insert into geo_source(river_name, country, province) */
	private void insertGeoSource(ArrayList<River> river,ArrayList<Country> country) {
		String name = null;
		String coun = null;
		String prov = null;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_source values
				
				// This inserts if no province exists
				for(River r : river){
					if(r.getSource().getLocated() == null){
					//	System.out.println("null: " + r.getName());
						name = r.getName();
						coun = r.getSource().getCountry();
						for(Country c : country){
							if (c.getCode().compareToIgnoreCase(coun) == 0){
								prov = c.getName();
								output.write("INSERT INTO geo_source VALUES ("
										+ stringOrNull(name) + ","
										+ stringOrNull(coun) + ","
										+ stringOrNull(prov)
										+ ");\n" );
							}
							
						}
					}else{
						// inserts geo_source if province exists
						for(Located l : r.getSource().getLocated()){
							name = r.getName();
							coun = l.getCountry();
							String[] provs = getStrings(l.getProvince());
							// loops on length of provinces in located
							// then country to province
							
							for(int i = 0; i < provs.length; i++){
								for(Country c : country){
									if(c.getProvince() != null){
										for(Province p : c.getProvince()){
											if(provs[i].compareToIgnoreCase(p.getId()) == 0){
												prov = p.getName();
												output.write("INSERT INTO geo_source VALUES ("
														+ stringOrNull(name) + ","
														+ stringOrNull(coun) + ","
														+ stringOrNull(prov)
														+ ");\n" );
											}
										}
									}
								}
							}
						}
					}
							
				}
				output.close();
			//	commit("geo_sea and geo_river and geo_source");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_source table to file.");
		}
		
	}

	/* Insert into geo_river(River, country, province) */
	private void insertGeoRiver(ArrayList<River> river, ArrayList<Country> country) {
		Boolean flag = false;
		String prov = null;
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_river values
				
				// loop through 
				for(River s : river){
					if(s.getLocated() != null){
						/* get the country code from the sea country attr
						 * loop through each code to get province name 
						 * or country name
						 */
						String river_country = s.getCountry();
						String[] countries = getStrings(river_country);
						
						for(int i = 0; i < countries.length; i++){
							
							for(Located l : s.getLocated()){
								
								if(countries[i].compareToIgnoreCase(l.getCountry()) == 0){
									
									flag = true;
									// get the provinces from located
									String[] loc_prov = getStrings(l.getProvince());
									// loop through province name and get province name that match
									for(int j = 0; j < loc_prov.length; j++){
										// get the province name 
										prov = compareProv(loc_prov[j], country);
										output.write("INSERT INTO geo_river VALUES ("
												+ stringOrNull(s.getName()) + ","
												+ stringOrNull(l.getCountry()) + ","
												+ stringOrNull(prov)
												+ ");\n" );
										
									}
									break;
								} else {flag = false; }			
							}
							// if flag is false, country doesn't have a province and get name 
							// of country instead
							if(flag == false){
								for(Country c : country){
									if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
									
										prov = c.getName();
										output.write("INSERT INTO geo_river VALUES ("
												+ stringOrNull(s.getName()) + ","
												+ stringOrNull(countries[i]) + ","
												+ stringOrNull(prov)
												+ ");\n" );
									}
								}
								
							}
							
						}
					}else{
						String[] countries = getStrings(s.getCountry());
						
						for(int i = 0; i < countries.length; i++){
							for(Country c : country){
								if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
									
									output.write("INSERT INTO geo_river VALUES ("
											+ stringOrNull(s.getName()) + ","
											+ stringOrNull(countries[i]) + ","
											+ stringOrNull(c.getName())
											+ ");\n" );
								}
							}
							
						}
					
					}
				}
				output.close();
			//	commit("geo_sea and geo_river");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_river table to file.");
		}
		
		
	}

	
	/* Insert into geo_sea(sea_name, country_code, province/country name) */
	private void insertGeoSea(ArrayList<Sea> sea, ArrayList<Country> country) {

		String prov = "";
		Boolean flag = false; // flag is needed if country is not in located.province
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert geo_sea values
				
				// loop through 
				for(Sea s : sea){
					if(s.getLocated() != null){
						/* get the country code from the sea country attr
						 * loop through each code to get province name 
						 * or country name
						 */
						String sea_country = s.getCountry();
						String[] countries = getStrings(sea_country);
						
						for(int i = 0; i < countries.length; i++){
							
							for(Located l : s.getLocated()){
								
								if(countries[i].compareToIgnoreCase(l.getCountry()) == 0){
									
									flag = true;
									// get the provinces from located
									String[] loc_prov = getStrings(l.getProvince());
									// loop through province name and get province name that match
									for(int j = 0; j < loc_prov.length; j++){
										// get the province name 
										prov = compareProv(loc_prov[j], country);
										output.write("INSERT INTO geo_sea VALUES ("
												+ stringOrNull(s.getName()) + ","
												+ stringOrNull(l.getCountry()) + ","
												+ stringOrNull(prov)
												+ ");\n" );
										
									}
									break;
								} else {flag = false; }			
							}
							// if flag is false, country doesn't have a province and get name 
							// of country instead
							if(flag == false){
								for(Country c : country){
									if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
									
										prov = c.getName();
										output.write("INSERT INTO geo_sea VALUES ("
												+ stringOrNull(s.getName()) + ","
												+ stringOrNull(countries[i]) + ","
												+ stringOrNull(prov)
												+ ");\n" );
									}
								}
								
							}
							
						}
					}else{
						String[] countries = getStrings(s.getCountry());
						
						for(int i = 0; i < countries.length; i++){
							for(Country c : country){
								if(countries[i].compareToIgnoreCase(c.getCode()) == 0){
									
									output.write("INSERT INTO geo_sea VALUES ("
											+ stringOrNull(s.getName()) + ","
											+ stringOrNull(countries[i]) + ","
											+ stringOrNull(c.getName())
											+ ");\n" );
								}
							}
							
						}
					
					}
				}
				output.close();
				//commit("geo_sea");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append geo_sea table to file.");
		}
		
	}



	/* This compares the id of the province with the province from located */
	/* If matches, return the name of the province */
	private String compareProv(String string, ArrayList<Country> country) {
			for(Country c : country){
				// if there is no province, continue to the next country
				if(c.getProvince() == null){
					continue;
				}
				
					for(Province p : c.getProvince()){
						
						if(p.getId().compareTo(string) == 0){
							// Debug
						//	System.out.println(p.getId() + " " + string + " " + p.getName());
							return p.getName();
						}
					}
			}
			
		
		return null;
	}

	/* Insert into desert(name, area, geoCoord) */
	private void insertDesert(ArrayList<Desert> desert) {
		
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert desert values
				
				for(Desert d : desert){
					
							output.write("INSERT INTO desert VALUES ("
									+ stringOrNull(d.getName()) + ","
									+ numOrNull(d.getArea())
									+ ",GeoCoord(" + numOrNull(d.getLongitude()) + "," 
									+ numOrNull(d.getLatitude())
									+ ")"
									+ ");\n" );
				}
				output.close();
				commit("desert");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append desert table to file.");
		}
		
	}
	
	/* Insert into mountains(name, mountains, elevation, type, geoCoord) */
	private void insertMountain(ArrayList<Mountain> mountain) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert mountain values
				
				for(Mountain m : mountain){
					
							output.write("INSERT INTO mountain VALUES ("
									+ stringOrNull(m.getName()) + ","
									+ stringOrNull(m.getMountains()) + ","
									+ numOrNull(m.getElevation()) + ","
									+ stringOrNull(m.getType()) + ",GeoCoord(" 
									+ numOrNull(m.getLongitude()) + "," + numOrNull(m.getLatitude())
									+ ")"
									+ ");\n" );
				}
				output.close();
				commit("mountain");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append mountain table to file.");
		}
		
	}
	/* Insert into island(name, islands, area, elevation, type, geoCoordinates) */
	private void insertIsland(ArrayList<Island> island) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert island values
				
				for(Island i : island){
					
							output.write("INSERT INTO island VALUES ("
									+ stringOrNull(i.getName()) + ","
									+ stringOrNull(i.getIslands()) + ","
									+ numOrNull(i.getArea()) + ","
									+ numOrNull(i.getElevation()) + ","
									+ stringOrNull(i.getType()) + ",GeoCoord(" 
									+ numOrNull(i.getLongitude()) + "," + numOrNull(i.getLatitude())
									+ ")"
									+ ");\n" );
				}
				output.close();
				commit("island");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append island table to file.");
		}
	}

	
	private void insertLake(ArrayList<Lake> lake) {
		File f = new File("countries.sql");
		// does file exist? append if yes, else print no
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				// insert lake values
				
				for(Lake l : lake){
					String flows = flowsTo(l.to, "river");
							output.write("INSERT INTO lake VALUES ("
									+ stringOrNull(l.getName()) + ","
									+ numOrNull(l.getArea()) + ","
									+ numOrNull(l.getDepth()) + ","
									+ numOrNull(l.elevation) + ","
									+ stringOrNull(l.type) + ","
									+ stringOrNull(flows) + ","
                                    + "GeoCoord(" + numOrNull(l.getLongitude()) + ","
                                    + numOrNull(l.getLatitude()) + ")" +
											");\n" );
				}
				output.close();
				commit("riverthrough and lake");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append river table to file.");
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
										+ stringOrNull(r.getName()) + ","
										 + stringOrNull(t.lake) +
												");\n" );
							}
						}
					}
					output.close();
					// gets done after lake commit("riverthrough");
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
									+ stringOrNull(r.getName()) + "," + stringOrNull(flows_to_r) + ","
									+ stringOrNull(flows_to_l) + "," + stringOrNull(flows_to_s) + ","
									+ numOrNull(r.length) + "," 
									+ "GeoCoord(" + r.source.getLongitude() + "," 
									+ r.source.getLatitude() + "),"
									+ stringOrNull(r.source.mountains) +  "," 
									+ numOrNull(r.source.elevation) + ","
									+ "GeoCoord(" + r.estuary.longitude + "," + r.estuary.latitude 
									 + "));\n" );
						
						
					
				}
				
				output.close();
				commit("river");
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
									+ stringOrNull(s.getName()) + ","
									+ numOrNull(s.getDepth())
									 + ");\n" );
						
					}
				
				output.close();
				commit("sea");
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
				
				output.close();
				commit("isMember");
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
				
				for(Country country : m){
			//	for(int i = 0; i < m.size(); i++){
					if(country.getEncompassed() != null){
						for(Encompassed enc : country.getEncompassed()){
							/* Insert code, continent, percent */
							output.write("INSERT INTO encompasses VALUES ("
									+ stringOrNull(country.getCode()) + "," 
									+ stringOrNull(enc.getContinent()) + ","
									+ numOrNull(enc.getPercentage()) + ");\n" );
						}
							
					}
				}
				
				output.close();
				commit("encompasses");
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
				boolean[] flag = new boolean[m.size()]; /* boolean array to check for symmetric relation */
				for(Country country : m){
			//	for(int i = 0; i < m.size(); i++){
					
					if(country.getBorder() != null){
						
						// loop through borders of the current country
						for(Border border : country.getBorder()){
								// need to loop through country to check for
								// symmetric relation
							for(Country c : m){
								
								/* if the codes match and the flag in the country position is not set yet, insert
								 * else do not insert and continue on.
								 */
								if(border.getCountry().compareToIgnoreCase(c.getCode()) == 0){
									if(flag[m.indexOf(c)] == false){
										/* Insert code, border_code, length */
										output.write("INSERT INTO borders VALUES ("
												+ stringOrNull(country.getCode()) + "," 
												+ stringOrNull(border.getCountry()) + ","
												+ numOrNull(border.getLength()) + ");\n" );
									
									}
								}
							}
							
						}
							
					}
					flag[m.indexOf(country)] = true;
				}
				output.close();
				commit("borders");
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
		// new Buffered output and loop for the Insert statements
		try {
			output = new BufferedWriter(new FileWriter(f, true));
			for(Country country : m){
				//enter if a province exists
				
				if(country.getProvince() != null){
					
					/* Get capital and capitalProvince values by finding the city id that matches country.getCapital() */
                    CityProvinceCountry capital = findCityById(m, country.getCapital());
					
					output.write("INSERT INTO country VALUES ("
                            + stringOrNull(country.name) + ","
                            + stringOrNull(country.code) + ","
                            + stringOrNull(capital.getCity()) + ","
                            + stringOrNull(capital.getProvince()) + ","
                            + numOrNull(country.area) + ","
                            + numOrNull(country.population) + ");\n");
				} else{
                    /* Get capital values by finding the city id that matches country.getCapital() */
                    CityProvinceCountry capital = findCityById(m, country.getCapital());

					//enter null if no province exists
					output.write("INSERT INTO country VALUES ("
                            + stringOrNull(country.name) + ","
                            + stringOrNull(country.code) + ","
                            + stringOrNull(capital.getCity()) + ","
                            + stringOrNull(capital.getCountry()) + ","
                            + numOrNull(country.area) + ","
                            + numOrNull(country.population) + ");\n");
				}
			}
			output.close();
			System.out.println("File written to successfully\n");
			commit("countries");
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
				for(Country coun : m){
					// if province null, just insert null
					if(coun.getProvince() == null){
					// check if array is not null then proceed (avoids null pointer exception)
						if(coun.getCity() != null){
						
						// debug
						//System.out.println(m.size() + " " + m.get(i).name + " " + m.get(i).city.size());
						for(City c : coun.getCity()){
							//create Insert into city table
							// if province exists, insert, else insert null
							
								output.write("INSERT INTO City VALUES ("
                                    + stringOrNull(c.getName()) + ","
									+ stringOrNull(c.getCode())+ ","
                                    + stringOrNull(coun.getName())+ ","
									+ numOrNull(c.getPopulation()) + ","
									+ numOrNull(c.getLongitude())+ ","
									+ numOrNull(c.getLatitude()) + ","
									+ numOrNull(c.getElevation()) + ");\n");
							//debugging
							//System.out.println(m.get(i).getCity().get(j).name);
							}
						}
					} else {
						
						
						// now inserting cities with non-null provinces
						for(Province prov : coun.getProvince()){
							
							//System.out.println("debug, country.province not null " + m.get(i).province.get(j).name);
							if(prov.getCity() != null){
								
								
								for(City ci : prov.getCity()){
									// debug
									/*	System.out.println("debug, city not null" + m.get(i).province.size()
											+ " " + m.get(i).province.get(j).getCity().get(k).name);
									 */	
									output.write("INSERT INTO City VALUES (" + stringOrNull(ci.getName()) 
											+ "," + stringOrNull(ci.getCode()) + "," 
											+ stringOrNull(prov.getName())
											+ "," + numOrNull(ci.getPopulation()) + "," 
											+ numOrNull(ci.getLongitude())
											+ "," + numOrNull(ci.getLatitude()) + ","
											+ numOrNull(ci.getElevation()) + ");\n");
								}
							}
						}
					}
				}
				output.close();
				commit("cities");
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
				output.close();
				commit("population");
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
				output.close();
				commit("politics");
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
				output.close();
				commit("economy");
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
				output.close();
				commit("language");
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
				output.close();
				commit("ethnicgroup");
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
                output.close();
                commit("religion");
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
                output.close();
                commit("continent");
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
                                capital = country.province.get(i).getCapital();
                                for(City city : country.getProvince().get(i).getCity()){
                                    if(city.cityId.equals(capital)){
                                        capital = city.getName();
                                        break;
                                    }
                                }
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
                                /* Get the capital of the province, must check if city is null
							 * for null pointer exception
							 */
                                String capital = null;
                                if(country.getCapital() != null){
                                    capital = country.getCapital();
                                    for(City city : country.getCity()){
                                        if(city.cityId.equals(capital)){
                                            capital = city.getName();
                                            break;
                                        }
                                    }
                                }

								/* Insert(country, code, pop, area, capital, name) */
								output.write("INSERT INTO province VALUES (" + stringOrNull(country.name) + ","
										+ stringOrNull(country.code) + "," 
										+ numOrNull(country.population) + ","
										+ numOrNull(country.area) + ","
										+ stringOrNull(capital) + "," + stringOrNull(country.name) + ");\n");
							}	
						}
					}


				output.close();
				commit("province");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not append table province to file.");
		}
		
	}

    // Insert organization
    public void insertOrganization(ArrayList<Organization> m, ArrayList<Country> countries){
        File f = new File("countries.sql");
        // does file exist? append if yes, else print no
        if(f.exists()){
            try{
                output = new BufferedWriter(new FileWriter(f, true));
                // insert Organization values

                for(int i = 0; i < m.size(); i++){
                    CityProvinceCountry cityProvince = findCityById(countries, m.get(i).headq);
                    output.write("INSERT INTO organization VALUES ("
                            + stringOrNull(m.get(i).abbrev) + ","
                            + stringOrNull(m.get(i).name) + ","
                            + stringOrNull(cityProvince.getCity()) + ","
                            + stringOrNull(cityProvince.getCountryCode()) + ","
                            + stringOrNull(cityProvince.getProvince()) + ","
                            + stringOrNull(m.get(i).established) +");\n" );
                }
                output.close();
                commit("organization");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Could not append organization table to file.");
        }
    }

    private void insertIslandIn(ArrayList<Island> island, ArrayList<Sea> sea, 
    		ArrayList<Lake> lake,
    		ArrayList<River> river){
    	
    	File f = new File("countries.sql");
        // does file exist? append if yes, else print no
        if(f.exists()){
            try{
                output = new BufferedWriter(new FileWriter(f, true));
                
                // loop through islands
                for(Island isle : island){
                	// get the sea, lake, & river id's
                	String sea_string = isle.getSea();
                	String lake_string = isle.getLake();
                	String river_string = isle.getRiver();
                	String[] seas;
                	String[] lakes;
                	String[] rivers;
                	
                	// if the strings are not null, parse and return to insert into table
                	if(sea_string != null){
                		// get array of seas id
                		seas = getStrings(sea_string);
                		// loop through the array string and seas to match id, insert into table
                		for(int i = 0; i < seas.length;i++){
                			for(Sea s : sea){
                    			if(seas[i].compareToIgnoreCase(s.getId()) == 0){
                    				output.write("INSERT INTO islandIn VALUES ("
											+ stringOrNull(isle.getName()) + ","
											+ stringOrNull(s.getName()) + ","
											+ stringOrNull(null) + ","
											+ stringOrNull(null)
											+ ");\n");
                    			}
                    		}
                		}
                		
                	}
                	if(lake_string != null){
                		// get array of lakes
                		lakes = getStrings(lake_string);
                		// loop through array and lakes then insert into table when ids match
                		for(int i = 0; i < lakes.length; i++){
                			for(Lake la : lake){
                				if(lakes[i].compareToIgnoreCase(la.getId()) == 0){
                					output.write("INSERT INTO islandIn VALUES ("
											+ stringOrNull(isle.getName()) + ","
											+ stringOrNull(null) + ","
											+ stringOrNull(la.getName()) + ","
											+ stringOrNull(null)
											+ ");\n");
                				}
                			}
                		}
                		
                	}
                	if(river_string != null){
                		rivers = getStrings(river_string);
                		// get arry of river id, compare to ids of river insert into table
                		for(int i = 0; i < rivers.length; i++){
                			for(River r : river){
                				if(rivers[i].compareToIgnoreCase(r.getId()) == 0){
                					output.write("INSERT INTO islandIn VALUES ("
											+ stringOrNull(isle.getName()) + ","
											+ stringOrNull(null) + ","
											+ stringOrNull(null) + ","
											+ stringOrNull(r.getName())
											+ ");\n");
                				}
                			}
                		}
                	}
                	
                }
                
                
                output.close();
                commit("islandIn");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Could not append islandIn table to file.");
        }

    	
    }


    // Insert mountain
    public void insertMountainOnIsland(ArrayList<Mountain> m, ArrayList<Island> i){
        File f = new File("countries.sql");
        // does file exist? append if yes, else print no
        if(f.exists()){
            try{
                output = new BufferedWriter(new FileWriter(f, true));

                // insert mountainOnIsland values
                for(Mountain mountain : m){
                    if(mountain.getIsland() != null){
                        String outputIsland = mountain.getIsland();
                        for(Island island : i){
                            if(outputIsland.equals(island.getId())){
                                outputIsland = island.getName();
                                break;
                            }
                        }
                        output.write("INSERT INTO mountainOnIsland VALUES (" + stringOrNull(mountain.getName())
                                + "," + stringOrNull(outputIsland) +");\n" );
                    }
                }
                output.write("\nCOMMIT;\n\n\n");
                output.close();
                System.out.println("Appended mountainOnIsland table successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("Could not append mountainOnIsland table to file.");
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

	/* Appends Commit when wanting to
	 * Make sure to put in the tables as a string 
	 * for what you want to be output onto screen
	 */
	private void commit(String s){
		File f = new File("countries.sql");
		if(f.exists()){
			try{
				output = new BufferedWriter(new FileWriter(f, true));
				output.write("\nCOMMIT;\n\n\n");
				output.close();
				System.out.println("Appended " + s + " table successfully.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			System.out.println("Could not add commit to file");
		}
				
	}
	

	/** This tokenizes a string based on a space delimiter and returns back array of String */
	private String[] getStrings(String sea_country) {
		String delims = " ";
		return sea_country.split(delims);
		
	}

    public CityProvinceCountry findCityById(ArrayList<Country> m, String cityId){
        for(Country country : m){
            //enter if a province exists
            if(country.getProvince() != null){
                for(Province province : country.getProvince()){
                    if(province.getCity() != null){
                        for(City city : province.getCity()){
                            if(city.cityId.equals(cityId)){
                                return new CityProvinceCountry(city, province, country);
                            }
                        }
                    }
                }
            }else{
                for(City city : country.getCity()){
                    if(city.cityId.equals(cityId)){
                        return new CityProvinceCountry(city, null, country);
                    }
                }
            }
        }
        return new CityProvinceCountry(null, null, null);
    }

}