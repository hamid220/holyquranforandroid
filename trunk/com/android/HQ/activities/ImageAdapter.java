package com.android.HQ.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class ImageAdapter extends SimpleCursorAdapter {

	private Activity ctx;
	
	public ImageAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);	
		this.ctx = (Activity) context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return super.getView(position, convertView, parent);
		/*LayoutInflater inflater=ctx.getLayoutInflater();
		View row = inflater.inflate(R.layout.verserow,null);
		
		ImageView img = (ImageView)row.findViewById(R.id.icon);
		img.setImageResource(R.drawable.a1_1);
		
		TextView verseText = (TextView)row.findViewById(R.id.txt_verse_num);
		verseText.setText(getCursor().getString(2));
		
		TextView verseText2 = (TextView)row.findViewById(R.id.txt_verse_txt);
		verseText2.setText(getCursor().getString(3));
		return row;*/
	}
	
	

}
