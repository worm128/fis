package com.fis.web.modules.songmain.service;

import com.fis.web.modules.songmain.cache.SongMainCache;
import com.fis.web.modules.songmain.model.SongMainGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GetDataList {

    @Resource
    private SongMainCache songMainCache;

    public SongMainGroup getSongList(int pg, int pgsize, String SongName, String sortDirection, String sortExpression) {
        SongMainGroup GL = songMainCache.GetAllCache(pg, pgsize, SongName, sortDirection, sortExpression);
        return GL;
    }

    public void killCache() {
        songMainCache.killCache();
    }
}
