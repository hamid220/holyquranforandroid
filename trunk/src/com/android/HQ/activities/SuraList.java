package com.android.HQ.activities;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.HQ.R;
import com.android.HQ.constants.Constants;
import com.android.HQ.data.DataParser;
import com.android.HQ.data.QDbAdapter;

public class SuraList extends ListActivity {
	
	private Cursor qCursor;
	private QDbAdapter dbAdapter;
	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       DataParser parser =DataParser.getInstance();
	       setContentView(R.layout.main);	              
	       dbAdapter = new QDbAdapter(this);
	       dbAdapter.open();	
	       if (dbAdapter.suraCount() != 114)
	       {
	    	   try {
	    		   dbAdapter.deleteAll();
	    		   dbAdapter.close();
	    		   parser.parseMetaData(this);	    		   
	    	   } catch (XmlPullParserException e) {	    		   
	    		   e.printStackTrace();
	    	   } catch (IOException e) {	
	    		   e.printStackTrace();
	    	   }
	    	   finally{
	    		   //dbAdapter.close();
	    	   }
	       }
		fillData();		
	   }
	   
	   protected void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        Cursor c = qCursor;
	        startManagingCursor(c);	  
	        c.moveToPosition(position);	        
	        Intent i = new Intent(this, VerseList.class);
	        i.putExtra(Constants.INDEX, c.getInt(1));
	        i.putExtra(Constants.AYAS, c.getString(2));
	        i.putExtra(Constants.TNAME, c.getString(4));
	        i.putExtra(Constants.REV_ORDER, c.getString(5));	
	        startActivity(i);	        	    	
	        //not sure if I need to do this
	        //c.close();
	        //c.deactivate();
	        //qCursor.close();
	        //qCursor.deactivate();
	    }
	   
	   public boolean onCreateOptionsMenu(Menu menu) {
	    	boolean result = super.onCreateOptionsMenu(menu);	    		    	
	    	menu.add("About");	    	
	        return result;
	    }
	        
	    private void fillData() {
	    	dbAdapter.open();
	    	Cursor c = dbAdapter.fetchAllMetadata();
	    	startManagingCursor(c);	    	
	    	String [] to = new String[] {"ind",QDbAdapter.TNAME};
	    	int [] from = {R.id.txt_index, R.id.txt_tname};
	    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.surarow, c, to, from);	    	  	
	        setListAdapter(adapter);	   	  	    
	        qCursor = c;
	        //dbAdapter.close();
	        //c.close();	        
	    }	   
}
