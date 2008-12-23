package com.android.HQ.model;

public class HQModel {

	private static HQModel model = null;
	public static HQModel getInstance(){
		if (model == null)
		{
			model = new HQModel();			
		}
		return model;
	}
	
	public String selectedVerseReciter = "";
	public String selectedChapterReciter = "";
	public String selectedTranslator = "";
	
	public int numPlayEachFile = 1;
	public int numPlayPlayList = 1;
}
