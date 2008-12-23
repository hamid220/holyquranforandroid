package com.android.HQ.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.android.HQ.R;
import com.android.HQ.data.QDbAdapter;

public class PreferencesActivity extends PreferenceActivity {
	
	
	private QDbAdapter qdb;
	@Override
	public void onCreate(Bundle savedInstance)
	{	
		super.onCreate(savedInstance);
		addPreferencesFromResource(R.xml.preferences);		
		
	}
	
		
		
}
