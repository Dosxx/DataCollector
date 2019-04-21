package edu.metroState.ics372GroupProject3.dataCollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

/**
 * JSONFile handles JSON file
 * It reads, write JSON file into java object
 */
public class JSONFile implements IReader {

	//Class members 
	private  File outputFile = null;
	private FileReader reader = null;
	
	/**
	 * @param
	 * input method takes a File as a parameter, reads the content into Java Objects
	 * and return list of sites objects
	 */	
	public Readings read(File input)throws IOException {
		//Instantiates a BufferReader object that takes the input file as an argument
		reader = new FileReader(input);
		Gson myGSon = new Gson(); //instance of GSon
		Readings myReadings = myGSon.fromJson(reader, Readings.class);
		reader.close();
		for(Item i : myReadings.getReadings()) {
			//correction to date and unit in the imported readings
			i.validateDate();
			i.ValidateUnit();
		}
		return myReadings;
	}

//	public static Readings readJSON(File input) throws Exception{
//		//Instantiates a BufferReader object that takes the input file as an argument
//		reader = new FileReader(input);
//		Gson myGson = new Gson(); //instance of GSON
//		Readings myReadings = new Readings();
//		myReadings = myGson.fromJson(reader, Readings.class);
//		reader.close();
//		for(Item i : myReadings.getReadings()) {
//			//correction to date and unit in the imported readings
//			i.validateDate();
//			i.ValidateUnit();
//		}
//		return myReadings;
//	}


	/**
	 *
	 *
	 */
	public Record loadState(File stateFile) throws IOException {
		//Instantiates a BufferReader object that takes the input file as an argument 
		reader = new FileReader(stateFile);
		Record myRecord = Record.getInstance();
		boolean hasState = false;
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(stateFile));
		//check if state file is empty
		if (br.readLine() != null) {
			hasState = true;
		}
		if(hasState) {
			Gson myGSon = new GsonBuilder().setLenient().create(); //instance of GSOon
			Study[] study = myGSon.fromJson(reader, Study[].class);
			for(int i = 0; i < study.length; i++) {
				myRecord.addStudy(study[i]);
			}
		}
		reader.close();
		return myRecord;
	}
	
	/*
	 * WriteToFile method takes as a parameters a list of sites
	 * and a file name. It write the sites in the list to a file on the disk
	 */
	public void writeToFile(Record studyRecord, File outputFile) throws IOException{
		//Instantiate a PrintWriter object
		FileWriter writer = new FileWriter(outputFile);
		//Write JSON object in pretty format
		Gson myGSon = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		//new instance of JSON Object that will contains iterations of study
		JsonArray jObject = new JsonArray();
		Iterator<Study> it = studyRecord.iterator();
		while(it.hasNext()) {
			jObject.add(myGSon.toJsonTree(it.next()));
		}
		
		String jsonString = myGSon.toJson(jObject);
		writer.write( jsonString);
		writer.close();
	}
}
