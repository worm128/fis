package com.fis.web.modules.songmain.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class SongMain implements Serializable {
	
	
	public Integer SongID;
	
	public Integer SpecID;
	
	public Integer SingerID;
	
	public String SongName;
	
	public String DownUrl;
	
	public Date Stime;
	
	public Integer SortNum;
	
	public String KeyWord;
	
	public Integer IdRank;

}
