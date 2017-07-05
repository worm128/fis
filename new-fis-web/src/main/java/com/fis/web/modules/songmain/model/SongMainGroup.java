package com.fis.web.modules.songmain.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
public class SongMainGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<SongMain> SongMainList;
	public Integer TotalRows = 0; // 总条数

}
