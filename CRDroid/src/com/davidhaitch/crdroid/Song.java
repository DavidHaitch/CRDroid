package com.davidhaitch.crdroid;

import com.google.gson.annotations.SerializedName;

public class Song
{
	@SerializedName("TITLE")
	public String Title;
	
	@SerializedName("ARTIST")
	public String Artist;
	
	@SerializedName("ARTISTID")
	public int ArtistId;
	
	@SerializedName("SONG")
	public String SongName;
	
	@SerializedName("SONGID")
	public int SongId;
}