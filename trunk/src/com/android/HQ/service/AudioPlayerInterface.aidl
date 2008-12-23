package com.android.HQ.service;

interface AudioPlayerInterface {
	void clearPlaylist();
	void addSongPlaylist(in String song ); 
	void playFileInPlaylist(in int position );
	void playFile(in String file);
	boolean isPlaying();

	void pause();
	void stop();
	void skipForward();
	void skipBack(); 
} 