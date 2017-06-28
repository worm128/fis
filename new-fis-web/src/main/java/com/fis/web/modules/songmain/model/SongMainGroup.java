package com.fis.web.modules.songmain.model;

import java.io.Serializable;
import java.util.List;

public class SongMainGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<SongMain> SongMainList;
	public Integer TotalRows = 0; // 总条数

	public List<SongMain> getSongMainList() {
		return SongMainList;
	}

	public void setSongMainList(List<SongMain> songMainList) {
		SongMainList = songMainList;
	}

	public Integer getTotalRows() {
		return TotalRows;
	}

	public void setTotalRows(Integer totalRows) {
		TotalRows = totalRows;
	}

}
