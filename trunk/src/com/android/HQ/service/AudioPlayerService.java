package com.android.HQ.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.HQ.R;
import com.android.HQ.activities.VerseList;
import com.android.HQ.constants.Constants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class AudioPlayerService extends Service {

	private MediaPlayer mp = new MediaPlayer();
	private List<String> songs = new ArrayList<String>();
	
	private int currentPosition;

	private NotificationManager nm;
	private static final int NOTIFY_ID = R.layout.verselist;

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
	}

	@Override
	public void onDestroy() {
		mp.stop();
		mp.release();
		//nm.cancel(NOTIFY_ID);
	}

	public IBinder getBinder() {
		return mBinder;
	}

	private void playSong(String file) {
		try {
			Notification notice = new Notification(R.drawable.playbackstart, "sdsadfsadf", System.currentTimeMillis());							
			//Notification notification = new Notification(
				//	R.drawable.playbackstart, file, null, file, null);
			//nm.notify(NOTIFY_ID, notice);

			mp.reset();
			mp.setDataSource(file);
			mp.prepare();
			mp.start();

			mp.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer arg0) {
					nextSong();
				}
			});

		} catch (IOException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		}
	}

	private void nextSong() {
		// Check if last song or not
		if (++currentPosition >= songs.size()) {
			currentPosition = 0;
			//nm.cancel(NOTIFY_ID);
		} else {
			playSong(Constants.SURA_URL+ songs.get(currentPosition));
		}
	}

	private void prevSong() {
		if (mp.getCurrentPosition() < 3000 && currentPosition >= 1) {
			playSong(Constants.SURA_URL+ songs.get(--currentPosition));
		} else {
			playSong(Constants.SURA_URL+ songs.get(currentPosition));
		}
	}

	private final AudioPlayerInterface.Stub mBinder = new AudioPlayerInterface.Stub() {

		public void playFile(int position) throws DeadObjectException {
			try {
				currentPosition = position;
				playSong(songs.get(position));

			} catch (IndexOutOfBoundsException e) {
				Log.e(getString(R.string.app_name), e.getMessage());
			}
		}

		public void addSongPlaylist(String song) throws DeadObjectException {
			songs.add(song);
		}

		public void clearPlaylist() throws DeadObjectException {
			songs.clear();
		}

		public void skipBack() throws DeadObjectException {
			prevSong();

		}

		public void skipForward() throws DeadObjectException {
			nextSong();
		}

		public void pause() throws DeadObjectException {
			//Notification notification = new Notification(
				//	R.drawable.playbackpause, null, null, null, null);
			//nm.notify(NOTIFY_ID, notification);
			mp.pause();
		}

		public void stop() throws DeadObjectException {
		//	nm.cancel(NOTIFY_ID);
			mp.stop();
		}

		@Override
		public void playFile(String file) throws RemoteException {
			playSong(file);
		}

		@Override
		public void playFileInPlaylist(int position) throws RemoteException {
			try {
				currentPosition = position;
				playSong(songs.get(position));

			} catch (IndexOutOfBoundsException e) {
				Log.e(getString(R.string.app_name), e.getMessage());
			}		
		}

		@Override
		public boolean isPlaying() throws RemoteException {
			return mp.isPlaying();
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;		
	}
}