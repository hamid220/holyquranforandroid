package com.android.HQ.data;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.android.HQ.R;


public class DataParser {
	
	private static DataParser instance = null;
	protected DataParser() {}
	
	public static DataParser getInstance(){
		if (instance == null)
		{
			instance = new DataParser();
		}
		return instance;
	}
	
	
	private QDbAdapter qDb = null;
	
	
	
	/*public void parseSura(Context ctx, int suraNum) throws XmlPullParserException, IOException{
		qDb = new QDbAdapter(ctx);
				
		int verseNum = 0;
		String verse = null;
		String xmlSuraNum = null;
		boolean matchFound = false;
		boolean textSet = false;
		XmlPullParser xml = ctx.getResources().getXml(R.xml.text);
		qDb.open();
		int eventType = xml.getEventType();
		
		while(eventType != XmlPullParser.END_DOCUMENT)
		{
			matchFound = false;
			textSet = false;
			if (eventType == XmlPullParser.START_TAG)
			{
				if (xml.getName().equals("verse")){
					for (int i=0; i<xml.getAttributeCount(); i++)
	            	{
	            		  if (xml.getAttributeName(i).equals("surah")){	            		
	            			  xmlSuraNum = xml.getAttributeValue(i);
	            			  if (xmlSuraNum.equals(Integer.toString(suraNum)))
	            			  {	            				  
	            				 matchFound =true;
	            				 verseNum = Integer.parseInt(xml.getAttributeValue(1));
	            				 break;
	            				 
	            			  }
	            		  }	            		  	            		  
	            	}					
				}				
			}
			if( xmlSuraNum != null && eventType == XmlPullParser.TEXT && xmlSuraNum.equals(Integer.toString(suraNum))) {
				textSet = true;
				verse = xml.getText();
			}
			if (xmlSuraNum != null && xmlSuraNum.equals(Integer.toString(suraNum)) && textSet)
				qDb.insertSura(suraNum, verseNum, verse);
			eventType = xml.next();
		}
		
	}*/
	public void parseMetaData(Context ctx) throws XmlPullParserException, IOException{
		String index = null;
		String ayas = null;
		String aName =  null;
		String tName = null;
		String order = null;
			
		XmlPullParser xml = ctx.getResources().getXml(R.xml.data);		
		qDb = new QDbAdapter(ctx);
		qDb.open();
		int eventType = xml.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT)
		{
			if(eventType == XmlPullParser.START_TAG) {	             
	              if (xml.getName().equals("sura"))
	              {
	            	  for (int i=0; i<xml.getAttributeCount(); i++)
	            	  {
	            		  if (xml.getAttributeName(i).equals("index"))	            		
	            			  index = xml.getAttributeValue(i);
	            		  if (xml.getAttributeName(i).equals("ayas"))
	            			  ayas = xml.getAttributeValue(i);
	            		  if (xml.getAttributeName(i).equals("name"))	            			
	            	  		  aName = xml.getAttributeValue(i);
	            		  if (xml.getAttributeName(i).equals("tname"))	            			
	            	  		  tName = xml.getAttributeValue(i);
	            		  if (xml.getAttributeName(i).equals("order"))	            			
	            	  		  order = xml.getAttributeValue(i);
	            	  }
	            	  qDb.insert(Integer.parseInt(index), Integer.parseInt(ayas), aName, tName, Integer.parseInt(order));	            	  
	              }	              	              
			}
	         eventType = xml.next();
		}
		qDb.close();								
	}
}
