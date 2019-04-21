package edu.metroState.ics372GroupProject3.dataCollector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author: kekeli
 * Created: 4/21/2019
 **/
public class ItemBuilder {
    private Item myItem;
    private String siteID;

    private String readingType;

    private String readingID;

    private double readingValue;

    private String readingUnit = "";

    private long readingDate;

    public ItemBuilder(){myItem = new Item();}

    public ItemBuilder setSiteID(String id){
        siteID = id;
        return this;
    }
    public ItemBuilder setReadingType(String type){
        readingType = type;
        return this;
    }
    public ItemBuilder setReadingId(String rID){
        readingID = rID;
        return this;
    }
    public ItemBuilder setReadingValue(double value){
        readingValue = value;
        return this;
    }
    public ItemBuilder setReadingUnit(String unit){
        readingUnit = unit;
        return this;
    }
    public ItemBuilder setReadingDate(long date){
        readingDate = date;
        return this;
    }
}
