package com.davidhaitch.crdroid;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class StreamStatistics 
{
	@SerializedName("CURRENTLISTENERS")
	public int CurrentListeners;
	
	@SerializedName("PEAKLISTENERS")
	public int PeakListeners;
	
	@SerializedName("MAXLISTENERS")
	public int MaxListeners;
	
	@SerializedName("SERVERURL")
	public String ServerUrl;
	
	@SerializedName("SERVERTITLE")
	public String ServerTitle;
	
	@SerializedName("SONGHISTORY")
	public List<Song> SongHistory;
}
