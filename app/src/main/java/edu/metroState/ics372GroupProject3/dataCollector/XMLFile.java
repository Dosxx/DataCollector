package edu.metroState.ics372GroupProject3.dataCollector;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * 
 * This class responsibility is to read an XML file
 * It has a default constructor
 *
 */

public class XMLFile implements IReader{
	private SAXParserFactory saxParserFactory;
	private Study myStudy; //variable to hold the input study from XML
	
	public XMLFile() {
		saxParserFactory = SAXParserFactory.newInstance();
		myStudy = null;
	}
	/**
	 * Read the XML file
	 */
	public Readings read(File file) {
		Readings readings = new Readings();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XMLSAXParserHandler handler = new XMLSAXParserHandler();
			saxParser.parse(file, handler);
			//Get Item List
			myStudy = handler.getStudy();
			readings.setReadings(handler.getItemList());
			for(Item i: readings.getReadings()) {
				//correction to date and unit in the imported readings
				i.validateDate();
				i.ValidateUnit();
			}
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
		    e.printStackTrace();
		}
		return readings;
	}

//	public Readings readXMLFile(File file) {
//		Readings readings = new Readings();
//		try {
//			SAXParser saxParser = saxParserFactory.newSAXParser();
//			XMLSAXParserHandler handler = new XMLSAXParserHandler();
//			saxParser.parse(file, handler);
//			//Get Item List
//			myStudy = handler.getStudy();
//			readings.setReadings(handler.getItemList());
//			for(Item i: readings.getReadings()) {
//				//correction to date and unit in the imported readings
//				i.validateDate();
//				i.ValidateUnit();
//			}
//		}
//		catch (ParserConfigurationException | SAXException | IOException e) {
//			e.printStackTrace();
//		}
//		return readings;
//	}
	
	//This method return the study imported from the input file
	public Study getStudy() {
		return myStudy;
	}
}
