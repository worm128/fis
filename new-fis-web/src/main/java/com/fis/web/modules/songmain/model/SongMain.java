package com.fis.web.modules.songmain.model;

import java.util.Date;


public class SongMain{
	
	
	public Integer SongID;
	
	public Integer SpecID;
	
	public Integer SingerID;
	
	public String SongName;
	
	public String DownUrl;
	
	public Date Stime;
	
	public Integer SortNum;
	
	public String KeyWord;
	
	public Integer IDRank;

	
	public Integer getSongID() {
		return SongID;
	}
	public void setSongID(Integer songID) {
		SongID = songID;
	}
	public Integer getSpecID() {
		return SpecID;
	}
	public void setSpecID(Integer specID) {
		SpecID = specID;
	}
	public Integer getSingerID() {
		return SingerID;
	}
	public void setSingerID(Integer singerID) {
		SingerID = singerID;
	}
	public String getSongName() {
		return SongName;
	}
	public void setSongName(String songName) {
		SongName = songName;
	}
	public String getDownUrl() {
		return DownUrl;
	}
	public void setDownUrl(String downUrl) {
		DownUrl = downUrl;
	}
	public Date getStime() {
		return Stime;
	}
	public void setStime(Date stime) {
		Stime = stime;
	}
	public Integer getSortNum() {
		return SortNum;
	}
	public void setSortNum(Integer sortNum) {
		SortNum = sortNum;
	}
	public String getKeyWord() {
		return KeyWord;
	}
	public void setKeyWord(String keyWord) {
		KeyWord = keyWord;
	}
	public Integer getIDRank() {
		return IDRank;
	}
	public void setIDRank(Integer iDRank) {
		IDRank = iDRank;
	}
	
	
}
