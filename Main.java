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
            m.insertLake(lake); // done
            m.insertIsland(island); // done
            m.insertMountain(mountain); // done 
            m.insertDesert(desert); // done
            
            // could have made one method for the geo, but oh well
            
            m.insertGeoSea(sea, country); // done over by 2 entries (prob good)
            m.insertGeoRiver(river, country); // done
            m.insertGeoSource(river, country); // done off by count of ~5 entries (idk yet)
            m.insertGeoEstuary(river, country); // done off by count ~16 entries (some not in xml)
            m.insertGeoLake(lake, country); // done
            m.insertGeoIsland(island, country); // done
            m.insertGeoMountain(mountain, country); // done off by count ~2 (under)
            m.insertGeoDesert(desert, country); // done
            m.mergesWith(sea);/* Done, count good, but because symmetric it some are off */
        //    m.located(country, river, lake, sea); // located
            // locatedOn
            // islandIn
            // mountainOnIsland
            // DONE!
            
            
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private void located(ArrayList<Country> country, ArrayList<River> river2, ArrayList<Lake> lake2, ArrayList<Sea> sea2) {
		
		String river= null, river_name = null;
		String sea = null, sea_name = null;
		String lake = null, lake_name = null;
						// loop over countries then cities/province
						// check for located_at
						// get the id from located_at
						
						for(Country coun : country){
							//debug
							
							if(coun.getProvince() != null){
								for(Province pro : coun.getProvince()){
									if(pro.getCity() != null){
										for(City cit : pro.getCity()){
											if(cit.getlocated_at() != null){
												for(Located_At loc : cit.getlocated_at()){
													// iterate over river
													
													if(loc.getRiver() != null){
														
															river = loc.getRiver();
															for(River riv : river2){
																if( riv.getId().compareToIgnoreCase(river) == 0){
																	river_name = riv.getName();
																	
																}
																
															}
														
														
														break;
													}
													if(loc.getSea() != null){
														
															sea = loc.getSea();
															for(Sea s : sea2){
																if( s.getId().compareToIgnoreCase(sea) == 0){
																	sea_name = s.getName();
																	//	insert(cit.getName(), pro.getName(), coun.getCode(), null,null, s.getName());
																}
															}
															
														
													}
													if(loc.getLake() != null){
													
															lake = loc.getLake();
																for(Lake lak : lake2){
																	if( lak.getId().compareToIgnoreCase(lake) == 0){
																		lake_name = lak.getName();
																		//insert(cit.getName(), pro.getName(), coun.getCode(), null,lak.getName(), null);
																	}
																}
														
													} 
													
													insert(cit.getName(), pro.getName(), coun.getCode(), river_name, lake_name, sea_name);
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
									
									for(City ci : coun.getCity()){
									//	System.out.println("city: " + ci.getName());
										
										// located_at exists
										if(ci.getlocated_at() != null){
										
											for(Located_At loc : ci.getlocated_at()){
												// iterate over river
												//System.out.println("located_at: " + loc.getWaterType());
												
												// get river|lake|sea
												if(loc.getRiver() != null){
													river = loc.getRiver();
													for(River riv : river2){
														if( riv.getId().compareToIgnoreCase(river) == 0){
															insert(ci.getName(), coun.getName(), coun.getCode(), riv.getName(), null, null);
														}
													}
													
													
												}
												if(loc.getSea() != null){
													sea = loc.getSea();
													for(Sea s : sea2){
														if( s.getId().compareToIgnoreCase(sea) == 0){
															insert(ci.getName(), coun.getName(), coun.getCode(), null,null, s.getName());
														}
													}
												}
												if(loc.getLake() != null){
													lake = loc.getLake();
													for(Lake lak : lake2){
														if( lak.getId().compareToIgnoreCase(lake) == 0){
															insert(ci.getName(), coun.getName(), coun.getCode(), null,lak.getName(), null);
														}
													}
												}
									}
								}
							}
							
								}
							}
						}
						
							
						
						commit("located");			
		
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
		Boolean flag = false;
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
									+ stringOrNull(flows) + "GeoCoord(" + numOrNull(l.getLongitude())
									+ "," + numOrNull(l.getLatitude()) + ")" +
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
										+ stringOrNull(r.name) + ","
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
									+ stringOrNull(r.name) + "," + stringOrNull(flows_to_r) + ","
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
									+ stringOrNull(s.name) + ","
									+ numOrNull(s.depth)
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
				
				for(int i = 0; i < m.size(); i++){
					if(m.get(i).encompassed != null){
							/* Insert code, continent, percent */
							output.write("INSERT INTO encompasses VALUES ("
									+ stringOrNull(m.get(i).code) + "," 
									+ stringOrNull(m.get(i).encompassed.continent) + ","
									+ numOrNull(m.get(i).encompassed.percentage) + ");\n" );
							
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
                output.close();
                commit("organization");
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
}
