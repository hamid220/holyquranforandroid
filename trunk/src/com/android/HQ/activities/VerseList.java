package com.android.HQ.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.HQ.R;
import com.android.HQ.constants.Constants;
import com.android.HQ.data.HQDataHandler;
import com.android.HQ.data.QDbAdapter;
import com.android.HQ.utils.FileFilter;
import com.android.HQ.utils.MediaPlayerCompletionListener;

public class VerseList extends ListActivity {

	private QDbAdapter qdb = new QDbAdapter(this);	
	public  ProgressDialog mProgress;    
    private int suraNum;
    public int pos;
    private final Context ctx = this;
    private MediaPlayer mp = new MediaPlayer();
    private Menu menu;
    private String tName;    
    private String curentFileName;
    
	public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);	       
	       setContentView(R.layout.verselist);	  
	       populateTextViews();
	       
	       suraNum = getIntent().getExtras().getInt(Constants.INDEX);
	       tName = getIntent().getExtras().getString(Constants.TNAME);
	       qdb = new QDbAdapter(this);
	       qdb.open();		     	       
	    	if (qdb.isSuraStored(Integer.toString(suraNum))){
	    		fillData(suraNum);
		     }
	    	else{
		      downloadSuraText();
	    	}
	    	qdb.close();
	}
	
	public void downloadSuraText()
	{
		 mProgress = ProgressDialog.show(this,"Downloading Sura", "This surah will download the first time you read it", true, true);
	       Thread loadThread = new Thread(){
	    	   public void run(){
	    		   HQDataHandler dataHandler = new HQDataHandler();
	    		   dataHandler.parseSura(ctx, suraNum);
	    		   mProgress.dismiss();
	    	   }
	       };
	       loadThread.start();
	       DismissListener onDismiss = new DismissListener();
	       onDismiss.ctx = this;
	       onDismiss.suraNum = suraNum;
	       mProgress.setOnDismissListener(onDismiss);
	}
	
	public void refreshSura()
	{
		try{
			qdb.open();
			qdb.deleteSura(suraNum);
			deleteAudioFiles();
			downloadSuraText();
			qdb.close();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	public void deleteAudioFiles()
	{		
		File f;			
		try{				
			f = new File(Constants.SD_CARD_DIR);
			FileFilter filter = new FileFilter(getSuraFileName(suraNum)); 
			String [] listOfFiles = f.list(filter);				
			for (int i=0; i<listOfFiles.length;i++)
			{
				f = new File(Constants.SD_CARD_DIR+listOfFiles[i]);	
				boolean isDeleted = f.delete();
			}
		}catch(Exception e)
			{
				e.printStackTrace();
			}						
	}
	protected void onPause()
	{
		super.onPause();
//		mp.stop();
		mp.reset();
	}
	
	protected void onDestroy()
	{
		super.onDestroy();
	//	mp.stop();
		mp.reset();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);        
        String fileName = getSuraFileName(suraNum)+ getSuraFileName(position+1)+".mp3";
        curentFileName = fileName;
        pos = position+1;
        /*if (isAudioDownloaded(fileName))
        {
        	playAudio(fileName, true);        	
        }
        else*/{
        	mProgress = ProgressDialog.show(this,"Downloading Audio...", "Downloading verse "+Integer.toString(position+1)+ " from Sura "+ tName, true, true);
        	Thread loadThread = new Thread(){
   	    	 public void run(){	 
   	    		String fileName = getSuraFileName(suraNum)+ getSuraFileName(pos)+".mp3";
   	    		downloadAudio(Constants.VERSE_URL, fileName);
   	    		playAudio(fileName, true);
   	    		 mProgress.dismiss();
   	    	   }
   	       };
   	     loadThread.start();	        	        	
        }        
	}
	
	public void fillData(int suraNum) {
		qdb.open();
    	Cursor c = qdb.fetchSura(Integer.toString(suraNum));
    	startManagingCursor(c);	    	    	
    	String [] to = new String[] {"aya_num","verse_txt"};
    	int [] from = {R.id.txt_verse_num, R.id.txt_verse_txt};  
    	try{    		
    		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.verserow, c, to, from);	    	    	
    		setListAdapter(adapter);    		
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    	finally{
    		qdb.close();
    		//c.close();
    	}
    }
	
	private String getSuraFileName(int suraNum){
		String strSuraNum = Integer.toString(suraNum);
		String fileName = "";
		if (strSuraNum.length() ==1)
			fileName = "00"+strSuraNum;
		else if (strSuraNum.length()==2)
			fileName = "0"+strSuraNum;
		else
			fileName = strSuraNum;
		return fileName;
	}
	
	private void downloadAudio(String strUrl, String fileName)
	{
		URL url;
		OutputStream out = null;
		InputStream  in = null;		
		Date startTime = new Date();
		try {
			
			url = new URL(strUrl+fileName);										
			out = new BufferedOutputStream(new FileOutputStream(Constants.SD_CARD_DIR+fileName, false));
			URLConnection c = url.openConnection();					
			in = c.getInputStream();				
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}					
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}				
				Date endDate = new Date();
				System.out.println("Download TIME");
				System.out.println((endDate.getTime() - startTime.getTime())/1000);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	private void initiateDownloadAndPlay(final String url)	
	{	
		 mProgress = ProgressDialog.show(this,"Downloading Audio...", "Downloading an audio recitation of Sura "+tName, true, true);
	     Thread loadThread = new Thread(){
	    	 public void run(){	 
	    		 String suraFileName = getSuraFileName(suraNum)+".mp3";
	    		 downloadAudio(url, suraFileName);
	    		 playAudio(suraFileName, false);
	    		 mProgress.dismiss();
	    	   }
	       };
	     loadThread.start();				
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		String title = (String) item.getTitle();
		if (title.equals("Refresh Sura"))
		{
			refreshSura();
		}
		if (title.equals("Play Audio")){
			String suraFileName = getSuraFileName(suraNum)+".mp3";
			if (!isAudioDownloaded(suraFileName))
			{	new AlertDialog.Builder(this).setTitle("Download Sura").setMessage("Do you want to download Sura " + tName + " to your SD Card?")
					.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dlg, int v){							
							initiateDownloadAndPlay(Constants.SURA_URL);
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dlg, int v){							
						}
					})					
					.show();									           				
			}
			else{
				playAudio(suraFileName, false);
			}
			menu.findItem(0).setTitle("Stop Audio");
			return true;
		}
		else if (title.equals("Stop Audio")){
			mp.stop();
			release();
			menu.findItem(0).setTitle("Play Audio");
			return true;
		}
        return super.onOptionsItemSelected(item);
    }
	
	private void release(){
		//mp.release();	
		
		try{
			if (menu != null)
			{
				MenuItem mi = menu.findItem(0);
				if (mi != null)
					mi.setTitle("Play Audio");
			}
		}catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
	}
	
	private boolean isAudioDownloaded(String suraFileName)
	{				
		File f = new File(Constants.SD_CARD_DIR+suraFileName);
		boolean doesExist = f.exists();		
		return doesExist;
	}
	private void playAudio(String suraFileName, boolean isVerse){
		try {	
					
		mp = new MediaPlayer();									
		mp.setDataSource(Constants.SD_CARD_DIR+suraFileName);
		mp.prepare();
		//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.start();	
		mp.setOnCompletionListener(new MediaPlayerCompletionListener(Constants.SD_CARD_DIR+suraFileName, isVerse));
		} catch (Exception e) {
			mp.release();
			deleteAudioFile(Constants.SD_CARD_DIR+suraFileName);
			e.printStackTrace();
		} 		
	}	
	public void deleteAudioFile(String fileName)
	{
		try
		{
			File f = new File(fileName);
			f.delete();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	 public boolean onCreateOptionsMenu(Menu menu) {
	    	boolean result = super.onCreateOptionsMenu(menu);
	    	if (!mp.isPlaying())
	    		menu.add("Play Audio");
	    	else 
	    		menu.add("Stop Audio");	    	
	    	menu.add("Refresh Sura");
	    	this.menu = menu;
	        return result;
	    }

	private void populateTextViews() {
		Bundle extras = getIntent().getExtras();
		TextView txtSuraName = (TextView) findViewById(R.id.txt_suraName);
		txtSuraName.setText(extras.getString(Constants.TNAME));
		TextView txtVerses = (TextView)findViewById(R.id.txt_num_verses);
		txtVerses.setText(extras.getString(Constants.AYAS));
		TextView txtRevOrder = (TextView)findViewById(R.id.txt_rev_order);
		txtRevOrder.setText(extras.getString(Constants.REV_ORDER));		
	}
}
