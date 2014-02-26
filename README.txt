======================
** Group Project #2 **
======================

* Knowlton, Brian       997670242   Figured out JAXB stuff and wrote a lot of the insert functions
* Waggoner, Prescott    996614827   Helped write many of the insert functions and other helper functions
* Vargas, Alix          815167168   Helped write many of the insert functions and classes

We created a Java program using JAXB to parse the XML file and populate classes we created to hold that data. We then
wrote functions for each of the tables we had to insert and went through our data structures pulling out the necessary
data. When generating output we compared our output.sql file to http://www.dbis.informatik.uni-goettingen.de/Mondial/mondial-inputs.sql
which was the Oracle version of the sql file. This file matched the data of the XML file better than the other mondial-inputs sql files.
Even so we had some slight differences in values contained in some of the insert statements due to different formatting
or new data. Refer to the table at the bottom for the slight differences in counts between what we got and what the
mondial-inputs.sql file had.

Build Instructions:
tar xvf XMLtoSQL.tar
/usr/java/jdk1.7.0_45/bin/javac *.java  #Used full path to get it to run on CSIF. Normally just javac *.java will work
java Main

Requirements: Needs the java compiler and runtime
Running: When our program is run it reads the mondialXML.xml file from the working directory and writes to output.sql


--Table with counts in relations--
reltuples is the count found in the file count-output.csv
output.sql is the count given by our program
mondial-inputs.sql are the counts found in the Oracle version that we were comparing our results to

relname,            reltuples,  output.sql, mondial-inputs.sql
ismember,           8008,       8012,	    8012
city,               3111,       3267,   	3263
province,           1450,       1700,	    1700
located,            857,        1161,   	1160
geo_river,          851,        934,	    934
geo_sea,            735,        912,    	910
ethnicgroup,        540,        628,	    628
religion,           454,        539,    	539
locatedon,          434,        465,	    465
geo_island,         418,        465,    	465
islandin,           349,        373,	    373
borders,            320,        324,    	325
geo_mountain,       297,        312,	    314
island,             276,        285,    	285
geo_estuary,        265,        269,	    285
geo_lake,           253,        277,    	277
encompasses,        242,        246,	    246
mountain,           240,        246,    	246
country,            238,        241,	    241
population,         238,        241,	    241
economy,            238,        241,	    241
politics,           238,        241,	    241
geo_source,         219,        231,	    236
river,              218,        226,	    226
geo_desert,         154,        179,	    179
organization,       153,        152,	    152
language,           144,        272,	    272
lake,               130,        138,	    138
mountainonisland,   69,         70,         70
desert,             63,         62, 	    62
mergeswith,         54,         63,         63
sea,                34,         40,         5
continent,          5,          5,          5
