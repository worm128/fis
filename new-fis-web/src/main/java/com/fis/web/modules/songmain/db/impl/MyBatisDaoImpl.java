package com.fis.web.modules.songmain.db.impl;

import com.fis.web.modules.songmain.db.mapping.SelectMapper;
import com.fis.web.modules.songmain.model.SongMain;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;



@Service
public class MyBatisDaoImpl {
	@Resource
	private SelectMapper selectMapper;

	public List<SongMain> getSongMainList() {
		List<SongMain> songlist = selectMapper.getSongMainList();
		return songlist;
	}

	public static void main(String[] args) {
		AbstractApplicationContext abstractApplicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		MyBatisDaoImpl servic = (MyBatisDaoImpl) abstractApplicationContext
				.getBean("myBatisDaoImpl");
		// servic.findPdfByContractId("SOA1000000000145");
		List<SongMain> aa = servic.getSongMainList();
	}
}
