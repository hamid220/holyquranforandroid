package com.android.HQ.activities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

public class DismissListener implements OnDismissListener {

	public VerseList ctx;
	public int suraNum;
	@Override
	public void onDismiss(DialogInterface dialog) {
		ctx.fillData(suraNum);

	}

}
