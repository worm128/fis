package com.fis.web.modules.songmain.cache;

import com.fis.web.constant.SortDirection;
import com.fis.web.init.threadwork.AppAction;
import com.fis.web.modules.songmain.db.impl.MyBatisDaoImpl;
import com.fis.web.modules.songmain.model.SongMain;
import com.fis.web.modules.songmain.model.SongMainGroup;
import com.fis.web.redis.base.JedisExecService;
import com.fis.web.redis.util.JsonSerializer;
import com.fis.web.tools.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class SongMainCache {

    private static int cacheType = 1;  //1.本地缓存 2.redis缓存
    private static String songMainkey = "SongMain";
    private static List<SongMain> SongMainList = new ArrayList<SongMain>();

    private static HashMap<String, Integer[]> _listIndex = new HashMap<String, Integer[]>();
    private static HashMap<String, Integer> _listRows = new HashMap<String, Integer>();

    // 锁对象
    private final static ReentrantLock lock = new ReentrantLock();

    @Autowired
    private MyBatisDaoImpl myBatisDaoImpl;

    @Autowired
    private JedisExecService jedisExecService;

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
        if (cacheType == 1) {
            //清空本地缓存
            SongMainList.clear();
        } else {
            //清空redis缓存
            jedisExecService.rpush(songMainkey, null);
        }

        //本地索引缓存
        _listRows.clear();
        _listIndex.clear();
        //gc回收
        System.gc();
    }

    // / <summary>
    // / 普通列表调用（入口）
    // / </summary>
    // / <returns></returns>
    public SongMainGroup GetAllCache(int pageNow, int pageSize,
                                     String SongName, String sortDirection, String sortExpression) {

        //如果有数据在加载，则需要等待数据加载完毕，数据不完整不能读取制作索引
        if (isUserDoing) {
            SongMainGroup smg = new SongMainGroup();
            smg.SongMainList = new ArrayList<SongMain>();
            return smg;
        }

        Integer[] index = null;
        Integer rows = 0;
        Boolean isExists = false;

        if (cacheType == 1) {
            //本地静态变量
            if (SongMainList != null && SongMainList.size() > 0) {
                isExists = true;
            }
        } else {
            //redis缓存
            isExists = jedisExecService.exists(songMainkey);
        }

        // 整表没有数据
        if (!isExists) {
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
            List<SongMain> SongMainList_Cache = null;
            if (cacheType == 1) {
                //本地静态变量
                SongMainList_Cache = SongMainList;
            } else {
                //SongMainList_Cache = (List<SongMain>) jedisExecService.findInfoByIdForObj(songMainkey, ArrayList.class, SongMain.class);
                //redis缓存
                List<String> rangeList = jedisExecService.lrange(songMainkey, 0, 99999);
                JsonSerializer jsonSerializer = new JsonSerializer();
                SongMainList_Cache = new ArrayList<SongMain>();
                for (String m : rangeList) {
                    SongMain songMain = jsonSerializer.deserializeForObject(m, SongMain.class);
                    SongMainList_Cache.add(songMain);
                }
            }

            //算出缓存索引位置
            index = GetListIndex(SongMainList_Cache, SongName, sortDirection,
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
                if (cacheType == 1) {
                    //静态变量
                    list.add(SongMainList.get(index[i]));
                } else {
                    //redis
                    list.add((SongMain) jedisExecService.lindex(songMainkey, index[i], SongMain.class));
                }
            }

            // 释放
            index = null;
            //gc回收
            System.gc();

            SongMainGroup GL = new SongMainGroup();
            GL.SongMainList = list;
            GL.TotalRows = rows;

            return GL;
        }

        // 释放
        index = null;
        //gc回收
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
                    // _log.Info("XuanFtypeList.Bll.AddIndex_IdRank_"+gameMod.IdRank);
                    index[w] = songmod.IdRank;
                    w++;
                }
            } else {
                return new Integer[0];
            }

            // 释放
            _SongMainList = null;

        } catch (Exception ex) {
            log.error("GetListIndex():" + ex);
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
                    try {
                        Date time1 = object1.Stime;
                        Date time2 = object2.Stime;

                        if (SortDirection.Desc.getCode().equals(sortDirection))
                            return time2.compareTo(time1);
                        else
                            return time1.compareTo(time2);
                    } catch (Exception e) {
                        log.error("对比时间错误", e);
                    }

                    return 0;
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
    // / 存在不更新缓存
    // / </summary>
    // / <returns></returns>
    private void LoadAllListCache() {
        boolean isFirst = false;
        boolean isExists = false;

        if (cacheType == 1) {
            //本地静态变量
            if (SongMainList != null && SongMainList.size() > 0) {
                isExists = true;
            }
        } else {
            //redis缓存
            isExists = jedisExecService.exists(songMainkey);
        }

        // 判断是否首次加载
        if (!isExists) {
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

                //本地索引缓存
                _listRows.clear();
                _listIndex.clear();

                //读取数据库
                List<SongMain> list = myBatisDaoImpl.getSongMainList();

                if (cacheType == 1) {
                    //清空本地缓存
                    SongMainList.clear();
                    //加载本地缓存
                    boolean isLocalSave = SongMainList.addAll(list);
                } else {
                    //清空redis缓存
                    jedisExecService.rpush(songMainkey, null);
                    //加载redis缓存
                    int rn = 0;
                    for (SongMain m : list) {
                        boolean isRedisSave = jedisExecService.rpush(songMainkey, m);
                        log.info("设置到redis的行:{},实体索引,{}", rn, m.getIdRank());
                        rn++;
                    }
                }

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
    // / 强制更新缓存
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

                //本地索引缓存
                _listRows.clear();
                _listIndex.clear();

                //读取数据库
                List<SongMain> list = myBatisDaoImpl.getSongMainList();

                if (cacheType == 1) {
                    //清空本地缓存
                    SongMainList.clear();
                    //加载本地缓存
                    boolean isLocalSave = SongMainList.addAll(list);
                } else {
                    //清空redis缓存
                    jedisExecService.rpush(songMainkey, null);
                    //加载redis缓存
                    int rn = 0;
                    for (SongMain m : list) {
                        boolean isRedisSave = jedisExecService.rpush(songMainkey, m);
                        log.info("设置到redis的行:{},实体索引,{}", rn, m.getIdRank());
                        rn++;
                    }
                }

                log.info("强制加载SongMain整表完成!");

            } catch (Exception ex) {
                log.error("强制加载SongMain整表:", ex);
            } finally {
                isUserDoing = false;
                //回收内存
                System.gc();
                lock.unlock();
            }
        }

    }
}
