package edu.metroState.ics372GroupProject3.dataCollector;

import android.app.Application;
import java.io.File;
import java.io.IOException;

/**
 * This class maintain the states of the current program execution:
 * the studies record, the current study, sites, and readings along
 * with imported file if one exist and readings from the imported file
 */
public class ProgramState extends Application {
	private Record currentProgramRecord;
	private Study currentProgramStudy = null;
	private Site currentProgramSite = null;
	private Item currentProgramItem = null;
	private File currentProgramInputFile = null;
	private Readings currentProgramReadings = null;
	private boolean isRecordEmpty;
	private XMLFile xmlfile;
	private JSONFile jsonFile;
	protected String inputFileName;
	protected String outputFileName;
	protected  String stateFileName;
	
	/**
	 * Constructor
	 */
	public ProgramState(){
	    xmlfile = new XMLFile();
	    jsonFile = new JSONFile();
	    currentProgramRecord = Record.getInstance();
	    isRecordEmpty = currentProgramRecord.isEmpty();
    }

	public ProgramState(String stateFileName) throws Exception{
		//return record from previous state if it exist
        this.stateFileName = stateFileName;
//		currentProgramRecord = JSONFile.loadState(stateFileName);
		isRecordEmpty = currentProgramRecord.isEmpty();
		xmlfile = new XMLFile();
		jsonFile = new JSONFile();
	}
	
	/**
	 * Only create a study if it hasn't already been created
	 */
	public boolean createNewStudy(String studyID, String studyName) {
		if(isRecordEmpty) {
			setCurrentProgramStudy(new Study(studyID, studyName));
		}else if(currentProgramRecord.getStudy(studyID, studyName) == null) {
            setCurrentProgramStudy(new Study(studyID, studyName));
		}else {
            setCurrentProgramStudy(currentProgramRecord.getStudy(studyID, studyName));
		}
		return currentProgramRecord.addStudy(currentProgramStudy);
	}

	/**
	 * Creates a new site only if it doesn't already exist
	 */
	public boolean createSite(String siteID) {
		if(currentProgramSite == null) {
			currentProgramSite = new Site(siteID);
		}else if(currentProgramStudy.getSiteByID(siteID) == null) {
			currentProgramSite = new Site(siteID);
		}
		return currentProgramStudy.addSite(currentProgramSite);
	}
	
	/**
	 * Creates a new reading
	 */
	public void createReading(
			String siteID, 
			String readingType, 
			String unit, 
			String readingID, 
			Double readingValue, 
			Long readingDate) {
        try {
            if (currentProgramSite != null) {
                currentProgramSite.setRecording(true);
                currentProgramSite.addItem(
                        new Item(siteID,
                                readingType,
                                unit,
                                readingID,
                                readingValue,
                                readingDate)
                        );
            }else {
                throw new Exception("Cannot add to Null Site");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Takes a file of the user choice and reads the file
	 *
	 */
	public void readFile(File readingFile)throws Exception {
		final String JSON = ".json";
		final String XML = ".xml";
		String extension = getExtension(readingFile.getName());
		if(extension.equals(JSON)) {
			currentProgramReadings  = jsonFile.read(readingFile);
			currentProgramStudy.setSiteForReading(currentProgramReadings);
		}else if(extension.equals(XML)) {
			currentProgramReadings = xmlfile.read(readingFile);
			if (currentProgramReadings != null) {
				currentProgramStudy = xmlfile.getStudy();
				currentProgramStudy.setSiteForReading(currentProgramReadings);
			}
		}
	}
	
	/**
	 * Take a file name and return the file extension
	 */
	private String getExtension(String filename) {
		return filename.substring(filename.lastIndexOf("."));
	}

	/**
	 * export the record to a file of the user's choice
	 */
	public void exportStudies(File outputFile) throws IOException {
		jsonFile.writeToFile(currentProgramRecord, outputFile);
	}

	

	public Record getCurrentProgramRecord() {
		return currentProgramRecord;
	}


	public void setCurrentProgramRecord(File stateFile)throws Exception {
		this.currentProgramRecord = jsonFile.loadState(stateFile);
        isRecordEmpty = currentProgramRecord.isEmpty();
	}


	public Study getCurrentProgramStudy() {
		return currentProgramStudy;
	}

	/**
	 * Check for study in record if null create new
	 */
	public void setCurrentProgramStudy(Study activeStudy) {
	    currentProgramStudy = activeStudy;
	}


	public Site getCurrentProgramSite() {
		return currentProgramSite;
	}


	public void setCurrentProgramSite(String siteID) {
		if (currentProgramStudy != null) {
			if(currentProgramStudy.getSiteByID(siteID) != null) {
				currentProgramSite = currentProgramStudy.getSiteByID(siteID);
			}else {
				currentProgramSite = null;
			}
		}
	}
	
	/**
	 * call to start collecting for current site
	 */
	public void startCollectingReadings() {
		if(currentProgramSite != null) {
			currentProgramSite.setRecording(true);
			if(currentProgramReadings != null) {
				currentProgramSite.addReadings(currentProgramReadings);
			}
		}
	}
	

	public void endCollectingReadings() {
		if(currentProgramSite != null) {
			currentProgramSite.setRecording(false);
		}
	}


	public Item getCurrentProgramItem() {
		return currentProgramItem;
	}


	public void setCurrentProgramItem(Item currentProgramItem) {
		this.currentProgramItem = currentProgramItem;
	}

	public File getCurrentProgramInputFile() {
		return currentProgramInputFile;
	}


	public void setCurrentProgramInputFile(File currentProgramInputFile) {
		this.currentProgramInputFile = currentProgramInputFile;
	}

	public Readings getCurrentProgramReadings() {
		return currentProgramReadings;
	}

	public String getStateFileName() {
		return stateFileName;
	}

	public void setStateFileName(String stateFileName) {
		this.stateFileName = stateFileName;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
}
