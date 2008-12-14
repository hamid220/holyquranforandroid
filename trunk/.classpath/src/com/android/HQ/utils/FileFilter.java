package com.android.HQ.utils;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {

	private String suraNum;
	public FileFilter(String suraNum)
	{
		this.suraNum = suraNum;
	}
	@Override
	public boolean accept(File dir, String filename) {
		if(filename.startsWith(suraNum) && filename.endsWith(".mp3"))
			return true;
		return false;
	}

}
