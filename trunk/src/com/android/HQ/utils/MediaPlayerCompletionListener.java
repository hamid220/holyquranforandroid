package com.android.HQ.utils;

import java.io.File;

import com.android.HQ.constants.Constants;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MediaPlayerCompletionListener implements OnCompletionListener {

	public String fileName ="";
	public boolean isVerse;
	
	
	public MediaPlayerCompletionListener(String fileName, boolean isVerse)
	{
		this.fileName = fileName;
		this.isVerse = isVerse;
	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.release();
		if(isVerse)
    		deleteAudioFile(this.fileName);		
	}
	
	public void deleteAudioFile(String fileName)
	{
		try
		{
			File f = new File(fileName);
			boolean isDeleted = f.delete();
			if (!isDeleted)
				throw new Exception();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
