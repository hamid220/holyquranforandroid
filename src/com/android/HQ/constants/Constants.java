package com.android.HQ.constants;

import com.android.HQ.vo.Reciter;

public class Constants {
	
	//database columns
	public static String INDEX = "ind";
	public static String TNAME = "tname";
	public static String REV_ORDER = "rev_order";
	public static String AYAS = "ayas";
	
	//entire sura url
	public static String SURA_URL = "http://server5.mp3quran.net/";
	//verse by verse url
	public static String VERSE_URL = "http://www.everyayah.com/data/Ghamadi_40kbps/";
	//sdcard file location
	public static String SD_CARD_DIR = "/sdcard/";
	
	//public static String SURA_TEXT_URL = "http://guidancesoftware.info/text1.xml";
	public static String SURA_TEXT_URL = "http://everyayah.com/data/xml/";
	
	//reciters
	public static String GHAMADI = "Ghamadi";
	public static String JIBREEL = "Jibreel";
	public static String SHATRREE = "Shatree";
	public static String ALAFASY = "Alafasy";
	public static String HUSARI = "Husari";
	public static String MINSHAWI = "Minshawi";
	
	//transalators
	public static String ENGLISH_ALHILALI="English_AlHilali";
	public static String ENGLISH_SHAKIR="English_Shakir";
	public static String FRENCH_MH="mohammad_hamidullah_french";
	
	public static String[] reciterList = 
	{			
		"Abdul Basit"
		,"Abu Bakr Ash-Shatri"
		,"Saad Al-Ghamdi"
		,"Mahmud Khaleel Al-Hussary"
		,"Muhammad Minshaawi"
		,"Mahmoud Al-Banna"
		,"Mashary Alfasi"
		,"Mustafa Ismael"		
	};
	
	public static String[] translationList = 
	{			
		"English_AlHilali"
		,"English_Shakir"
		,"French_Hamidullah"
	};
	
	public static String[] transliterationList = 
	{			
		"Do not show Transliteration"
		,"English_Transliteration"
	};
	
	public static Reciter[] reciterPath =
	{
		new Reciter(reciterList[0],"http://server8.mp3quran.net/basit_mjwd/")
		,new Reciter(reciterList[1],"http://server8.mp3quran.net/shatri/")
		,new Reciter(reciterList[2], "http://server5.mp3quran.net/s_gmd/")
		,new Reciter(reciterList[3],"http://server7.mp3quran.net/husr_mjwd/")
		,new Reciter(reciterList[4],"http://server7.mp3quran.net/minsh/")
		,new Reciter(reciterList[5],"http://server7.mp3quran.net/bna_mjwd/")
		,new Reciter(reciterList[6],"http://server5.mp3quran.net/afs/")
		,new Reciter(reciterList[7],"http://server6.mp3quran.net/mustafa/")
	};
	
	public static String[] preferencesList =
	{		
		"Choose Reciter"
		,"Choose Translation"
	};
}
