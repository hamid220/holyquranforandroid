package com.android.HQ.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.android.HQ.activities.VerseList;
import com.android.HQ.constants.Constants;

import android.content.Context;

public class HQDataHandler extends DefaultHandler {

	private int suraNum = 0;
	private QDbAdapter qdb;
	private int ayah = 0;
	private boolean inVerse = false;
	public VerseList ctx;
	public void startElement(String uri, String name, String qName,	Attributes atts) {
		this.ctx.mProgress.incrementProgressBy(5);
		if (name.equals("verse"))
		{						
			String xmlSuraNum = atts.getValue("surah");
			if (xmlSuraNum.equals(Integer.toString(suraNum)))
			{
				inVerse = true;
				ayah = Integer.parseInt(atts.getValue("ayah"));						
			}
		}
	}
	
	public void characters(char ch[], int start, int length) {
		String chars = (new String(ch).substring(start, start + length));
		if (inVerse && ayah != 0 ){
			qdb.insertSura(suraNum, ayah, chars);
			ayah = 0;
			inVerse = false;
		}
	}
	
	public void parseSura(Context ctx, int suraNum){
		this.suraNum = suraNum;
		qdb = new QDbAdapter(ctx);
		qdb.open();
		this.ctx = (VerseList) ctx;
		SAXParserFactory spf = SAXParserFactory.newInstance();		
		SAXParser sp = null;
		XMLReader xr = null;
		URL url = null;
		URLConnection c = null;
		try {
			sp = spf.newSAXParser();
			xr = sp.getXMLReader();
			xr.setContentHandler(this);			
			url = new URL(Constants.SURA_TEXT_URL);
			c = url.openConnection();
            c.setRequestProperty("User-Agent", "Android/SDK1");
            xr.parse(new InputSource(c.getInputStream()));            
            
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			qdb.close();
		}
		
		
	}
}
