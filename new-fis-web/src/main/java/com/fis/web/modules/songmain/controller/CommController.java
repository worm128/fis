package com.fis.web.modules.songmain.controller;

import com.fis.web.constant.SortDirection;
import com.fis.web.modules.songmain.model.SongMain;
import com.fis.web.modules.songmain.model.SongMainGroup;
import com.fis.web.modules.songmain.service.GetDataList;
import com.fis.web.tools.NumberUtils;
import com.fis.web.tools.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("")
public class CommController {

    @Resource
    private GetDataList getDataList;

    @RequestMapping("index")
    public String getSongList(HttpServletRequest request, HttpServletResponse response) {
        log.info("===========访问index.do页面开始===========");

        //翻页
        Integer pg = NumberUtils.parseInt(request.getParameter("pg"));
        if (pg == 0)
            pg = 1;
        Integer pgsize = NumberUtils.parseInt(request.getParameter("pgsize"));
        if (pgsize == 0)
            pgsize = 12;

        //搜索名称
        String songName = request.getParameter("songName");
        //排序字段
        String sort = request.getParameter("sort");
        if (!StringUtils.hasText(sort))
            sort = "stime";
        //排序规则
        String dirct = request.getParameter("dirct");
        if (!StringUtils.hasText(dirct))
            dirct = "D";

        SongMainGroup GL = getDataList.getSongList(pg, pgsize, songName, dirct, sort);
        List<SongMain> list = GL.SongMainList;
        request.setAttribute("list", list);
        request.setAttribute("pg", pg);
        request.setAttribute("pgsize", pgsize);
        request.setAttribute("total", GL.getTotalRows());
        // int a = UserManage.Login("root", "123", "123", request);
        // request.setAttribute("a", a);

        return "web/showdata.jsp";
    }

    @RequestMapping("gc")
    public String getJVM(HttpServletRequest request, HttpServletResponse response) {
        return "web/gc.jsp";
    }

    @RequestMapping("kill")
    public String getkill(HttpServletRequest request, HttpServletResponse response) {
        getDataList.killCache();
        return "web/kill.jsp";
    }
}
