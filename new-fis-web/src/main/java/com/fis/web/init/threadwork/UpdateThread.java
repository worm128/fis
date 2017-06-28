package com.fis.web.init.threadwork;

import com.fis.web.modules.songmain.cache.SongMainCache;
import com.fis.web.tools.CTime;
import com.fis.web.tools.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class UpdateThread {
    private static Thread _updateThread = null, MonitorThread = null;
    private static List<TheardData> _cacheStatusList = null;
    private static boolean _isRun = true, UpdateLock = false;
    // 锁对象
    private final static ReentrantLock lock = new ReentrantLock();

    @Resource
    private SongMainCache songMainCache;

    @PostConstruct
    public void Start() {
        _isRun = true;
        if (_cacheStatusList == null) {
            log.info("#################### 网站启动 ####################");
            _cacheStatusList = new ArrayList<TheardData>();

            // 加载SongMain任务
            TheardData theardData = new TheardData();
            theardData.TheardType = AppTheardType.SongMain;
            theardData.Value = "Load";
            theardData.Action = AppAction.Load;
            Add(theardData);
        }

        // 启动加载(根据指令加载数据)
        _updateThread = new Thread() {
            public void run() {
                try {
                    ThreadUpdateProc();
                } catch (Exception e) {
                    log.error(e.toString());
                }
            }
        };
        _updateThread.setName("LoadCacheThread-Core");
        _updateThread.start();

        // 监控缓存更新线程(根据指令加载数据)
        MonitorThread = new Thread() {
            public void run() {
                try {
                    MonitorProc();
                } catch (Exception e) {
                    log.error(e.toString());
                }
            }
        };
        MonitorThread.setName("monitorThread-Core");
        MonitorThread.start();
    }

    // / <summary>
    // / 监控丢失缓存线程(丢失重新加载)
    // / </summary>
    private void MonitorProc() throws InterruptedException {
        log.info("监控丢失缓存线程(丢失重新加载)，休息3秒后开始工作...");

        Thread.sleep(3000); // 休息3秒
        while (_isRun) {
            try {
                if (UpdateLock == false)
                    AsyncForceAllList(AppTheardType.SongMain, AppAction.Check, "Check");

                Thread.sleep(6000);
                //log.debug("(监控丢失缓存线程)监控中...");
            } catch (Exception ex) {
                log.error("", ex);
            }
        }
    }

    // / <summary>
    // / 监控缓存更新线程(根据指令加载数据)
    // / </summary>
    private void ThreadUpdateProc() throws InterruptedException {
        log.info("监控缓存更新线程(根据指令加载数据)，休息3秒后开始工作...");

        Thread.sleep(3000); // 休息3秒
        while (_isRun) {
            try {
                // 定点更新缓存
                ChkUpdateCache();

                // 执行需更新的数据
                TheardData theardData = Get();
                if (theardData == null) {
                    // 解锁“监控游戏列表线程”
                    if (UpdateLock)
                        UpdateLock = false;

                    // 数据没有数据，就休息5秒
                    Thread.sleep(1000);
                } else {
                    // 上锁,不再执行“监控软件列表线程”
                    if (UpdateLock == false)
                        UpdateLock = true;

                    // 执行异步更新
                    Run(theardData);
                }
                //log.debug("(监控“缓存更新”线程)监控中...");
            } catch (Exception ex) {
                log.error("", ex);
            }
        }
    }

    // / <summary>
    // / 所有的数据运行方法
    // / </summary>
    // / <param name="theardData"></param>
    private void Run(TheardData theardData) {
        try {

            if (AppTheardType.SongMain == theardData.TheardType) {
                songMainCache.AsyncDeal(theardData.Action, theardData.Value);
                //log.error("更新SongMain缓存");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // / <summary>
    // / 获取队列中的数据
    // / </summary>
    // / <returns></returns>
    private TheardData Get() {
        // 上锁
        lock.lock();
        TheardData theardData = null;
        try {
            if (_cacheStatusList.size() > 0) {
                theardData = _cacheStatusList.get(0);
                _cacheStatusList.remove(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();// 释放锁
        }

        return theardData;
    }

    // / <summary>
    // / 给队列中增加数据
    // / </summary>
    // / <param name="theardData"></param>
    public void Add(TheardData theardData) {
        // 上锁
        lock.lock();
        try {

            // 如果不存在的时候，才增加
            if (!_cacheStatusList.contains(theardData))
                _cacheStatusList.add(theardData);
            // else
            //log.error("theardData已经存在" + theardData.Value);

        } catch (Exception e) {

        } finally {

            lock.unlock();// 释放锁

        }

    }

    // / <summary>
    // / (异步)"无条件重新"加载SoftBindList整表（List）
    // / </summary>
    public void AsyncForceAllList(AppTheardType thread, AppAction ac,
                                  String val) {
        TheardData theardData = new TheardData();
        theardData.TheardType = thread;
        theardData.Value = val;
        theardData.Action = ac;

        Add(theardData);
    }

    // / <summary>
    // / 定时更新列表缓存
    // / </summary>
    private void ChkUpdateCache() {
        if (IsReadToUpdate()) {
            log.info("定点更新SongMain任务加载");
            // 加载核心游戏数据表
            AsyncForceAllList(AppTheardType.SongMain, AppAction.All, "All");
        }
    }

    static SimpleDateFormat alltime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    static String dateYear = CTime.GetDate();
    static DateTime[] updateStime = new DateTime[]{
            new DateTime(dateYear + " 1:30:00"),
            new DateTime(dateYear + " 8:30:00"),
            new DateTime(dateYear + " 12:30:00"),
            new DateTime(dateYear + " 18:13:00")};

    private static boolean IsReadToUpdate() {

        for (DateTime utime : updateStime) {
            if (DateTime.GetTimeDiff(utime) == 0) {
                log.info("******************加载定点更新******************");
                log.info("更新时间:" + alltime.format(DateTime.getNowDate())
                        + ", 设置时间:" + utime);
                log.info("************************************************");
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        // DateTime aa = new DateTime("1:30:00");
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String year = sf.format(now);
        System.out.println(year);

        DateTime dt = new DateTime("2014-06-17 18:13:00");
        DateTime[] updateStime = new DateTime[]{dt};
        Integer ccc = DateTime.GetTimeDiff(dt);
        System.out.println(ccc);
    }

}
