package com.android.HQ.utils;

import com.android.HQ.constants.Constants;
import com.android.HQ.vo.Reciter;

import android.content.SharedPreferences;

public class HQUtils {

	public static String getRecitorPath(String selectedReciter){
		
		for (int i=0; i<Constants.reciterList.length; i++)
		{
			Reciter reciter = (Reciter)Constants.reciterPath[i];
			if (selectedReciter.equals(reciter.getName()))
			{
				return reciter.getUrl();
			}
		}
		return "s_gmd/";				
	}
}
