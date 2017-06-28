package com.fis.web.modules.songmain.cache;

import com.fis.web.constant.SortDirection;
import com.fis.web.init.threadwork.AppAction;
import com.fis.web.modules.songmain.db.impl.MyBatisDaoImpl;
import com.fis.web.modules.songmain.model.SongMain;
import com.fis.web.modules.songmain.model.SongMainGroup;
import com.fis.web.tools.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class SongMainCache {

    private static List<SongMain> SongMainList = new ArrayList<SongMain>();

    private static HashMap<String, Integer[]> _listIndex = new HashMap<String, Integer[]>();
    private static HashMap<String, Integer> _listRows = new HashMap<String, Integer>();

    // 锁对象
    private final static ReentrantLock lock = new ReentrantLock();

    @Resource
    private MyBatisDaoImpl myBatisDaoImpl;

    public SongMainCache() {

    }

    // / <summary>
    // / 队列操作静态缓存
    // / </summary>
    // / <param name="action"></param>
    // / <param name="value"></param>
    public void AsyncDeal(AppAction action, String value) {
        try {
            if (action == AppAction.Load) {
                LoadAllListCache();
                log.info("========起步加载SongMain整表完成========");
            } else if (action == AppAction.All) {
                UpdateAllListCache();
                log.info("========后台定点或监控更新SongMain整表缓存完成========");
            } else if (action == AppAction.Check) {
                LoadAllListCache();
                log.info("========检查SongMain加载情况========");
            } else if (action == AppAction.Remove) {
            }
        } catch (Exception ex) {
            log.error("SongMain.AsyncDeal()" + ex);
        }
    }

    public List<SongMain> GetAllListCache() {
        // 判断整表是否有数据
        if (SongMainList != null && SongMainList.size() > 0)
            return SongMainList;

        return new ArrayList<SongMain>();
    }

    public void killCache() {
        SongMainList.clear();
        _listRows.clear();
        _listIndex.clear();
        System.gc();
    }

    // / <summary>
    // / 普通列表调用（入口）
    // / </summary>
    // / <returns></returns>
    public SongMainGroup GetAllCache(int pageNow, int pageSize,
                                     String SongName, String sortDirection, String sortExpression) {
        Integer[] index = null;
        Integer rows = 0;

        // 整表没有数据
        if (SongMainList == null || SongMainList.size() <= 0) {
            SongMainGroup smg = new SongMainGroup();
            smg.SongMainList = new ArrayList<SongMain>();
            return smg;
        }

        String indexKey = "indexKey_SongMainList_index_" + pageSize + "_"
                + SongName + "_" + sortDirection + "_" + sortExpression;
        String rowsKey = "rowsKey_SongMainList_index_" + pageSize + "_"
                + SongName + "_" + sortDirection + "_" + sortExpression;

        // 获取缓存索引
        index = _listIndex.get(indexKey);
        if (index != null && index.length > 0) {
            rows = _listRows.get(rowsKey);
        } else {
            index = GetListIndex(SongMainList, SongName, sortDirection,
                    sortExpression);
            rows = index.length;
            if (rows > 0) {
                _listIndex.put(indexKey, index);
                _listRows.put(rowsKey, rows);
            }
        }

        // 逻辑计算当页索引范围
        int endNum = 0, startNum = 0, realcount = 0;
        startNum = (pageNow - 1) * pageSize;
        endNum = (pageNow * pageSize) - 1;
        if (endNum >= rows)
            endNum = rows;
        realcount = endNum - startNum + 1;

        //有数据
        if (endNum >= startNum && realcount > 0) {
            // 加载list
            List<SongMain> list = new ArrayList<SongMain>(realcount);
            int count = index.length;
            for (int i = startNum; i <= endNum && i < count; i++) {
                list.add(SongMainList.get(index[i]));
            }

            // 释放
            index = null;
            System.gc();

            SongMainGroup GL = new SongMainGroup();
            GL.SongMainList = list;
            GL.TotalRows = rows;

            return GL;
        }

        // 释放
        index = null;
        System.gc();

        SongMainGroup smg = new SongMainGroup();
        smg.SongMainList = new ArrayList<SongMain>();
        return smg;
    }

    // / <summary>
    // / 获取列表索引
    // / </summary>
    private Integer[] GetListIndex(List<SongMain> SongMainList,
                                   String SongName, String sortDirection, String sortExpression) {
        Integer[] index = null;
        Integer rows = 0;
        if (SongMainList == null || SongMainList.size() <= 0) {
            return new Integer[0];
        }
        try {
            List<SongMain> _SongMainList = new ArrayList<SongMain>();

            // 分配一个新内存来操作
            _SongMainList.addAll(SongMainList);

            // 关键字搜索
            if (StringUtils.hasText(SongName)) {
                // 分词搜索
                _SongMainList = GetSearchList(_SongMainList, SongName);
            } else {
                // 排序
                _SongMainList = GetSort(_SongMainList, sortDirection,
                        sortExpression);
            }

            // 获取数据条数
            rows = _SongMainList.size();

            // 加载list列表
            if (rows > 0) {
                int w = 0;
                index = new Integer[rows];
                // 循环添加索引
                for (SongMain songmod : _SongMainList) {
                    // _log.Info("XuanFtypeList.Bll.AddIndex_IDRank_"+gameMod.IDRank);
                    index[w] = songmod.IDRank;
                    w++;
                }
            } else {
                return new Integer[0];
            }

            // 释放
            _SongMainList = null;

        } catch (Exception ex) {
            log.error("App2012.Bll.AppDetailBll.Get_SongMainListIndex():" + ex);
            return new Integer[0];
        }
        return index;
    }

    // / <summary>
    // / 排序
    // / </summary>
    protected List<SongMain> GetSort(List<SongMain> SortList,
                                     final String sortDirection, String sortExpression) {
        sortExpression = sortExpression.toLowerCase();
        //排序规则:优先sortnum > stime

        if (sortExpression.contains("stime")) {
            // 发布时间
            Collections.sort(SortList, new Comparator<SongMain>() {
                @Override
                public int compare(SongMain object1, SongMain object2) {
                    Date time1 = object1.Stime;
                    Date time2 = object2.Stime;

                    if (SortDirection.Desc.getCode().equals(sortDirection))
                        return time2.compareTo(time1);
                    else
                        return time1.compareTo(time2);
                }
            });
        }

        if (sortExpression.contains("sortnum")) {
            // 自定义顺序
            Collections.sort(SortList, new Comparator<SongMain>() {
                @Override
                public int compare(SongMain object1, SongMain object2) {
                    if (SortDirection.Desc.getCode().equals(sortDirection))
                        return object2.SortNum.compareTo(object1.SortNum);
                    else
                        return object1.SortNum.compareTo(object2.SortNum);
                }
            });
        }

        return SortList;
    }

    // / <summary>
    // / 分词搜索(用于搜索)
    // / </summary>
    protected List<SongMain> GetSearchList(List<SongMain> SearchList,
                                           String kw) {
        try {
            List<SongMain> myList = new ArrayList<SongMain>();
            kw = kw.trim().toLowerCase();

            // 完全匹配
            for (SongMain s : SearchList) {
                if (s.SongName.equals(kw)
                        || ("," + s.KeyWord + ",").equals(("," + kw + ",")))
                    myList.add(s);
            }

            // 模糊匹配
            for (SongMain s : SearchList) {
                if (s.SongName.contains(kw) || s.KeyWord.contains(kw))
                    myList.add(s);
            }

            return myList;
        } catch (Exception ex) {
            log.error("", ex);
            return SearchList;
        }
    }

    static boolean isUserDoing = false;

    // / <summary>
    // / 返回所有列表
    // / </summary>
    // / <returns></returns>
    private void LoadAllListCache() {
        boolean isFirst = false;

        // 判断数量
        if (SongMainList == null || SongMainList.size() <= 0) {
            isFirst = true;
        }

        // 用户如果在操作数据，请等待
        while (isUserDoing && isFirst) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }

        // 如果没有加载缓存，则加载
        if (isFirst) {
            isUserDoing = true;
            try {
                lock.lock();

                //清空数据
                SongMainList.clear();
                _listRows.clear();
                _listIndex.clear();

                //加载数据
                SongMainList.addAll(myBatisDaoImpl.getSongMainList());
                log.info("加载SongMain整表完成!");
            } catch (Exception ex) {
                log.error("加载SongMain整表:", ex);
            } finally {
                isUserDoing = false;
                //回收内存
                System.gc();
                lock.unlock();
            }
        }

    }

    // / <summary>
    // / 返回所有列表
    // / </summary>
    // / <returns></returns>
    private void UpdateAllListCache() {
        boolean isFirst = true;

        // 用户如果在操作数据，请等待
        while (isUserDoing && isFirst) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("加载SongMain整表:", e);
            }
        }

        if (isFirst) {
            isUserDoing = true;
            try {
                lock.lock();

                //清空数据
                SongMainList.clear();
                _listRows.clear();
                _listIndex.clear();

                //加载数据
                SongMainList.addAll(myBatisDaoImpl.getSongMainList());

            } catch (Exception ex) {
                log.error("加载SongMain整表:", ex);
            } finally {
                isUserDoing = false;
                //回收内存
                System.gc();
                lock.unlock();
            }
        }

    }
}
