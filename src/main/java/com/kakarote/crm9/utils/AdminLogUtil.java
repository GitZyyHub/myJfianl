package com.kakarote.crm9.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.common.AdminModelEnum;
import com.kakarote.crm9.erp.admin.entity.AdminSystemLog;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import sun.net.util.IPAddressUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class AdminLogUtil {
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(1, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2048), new ThreadPoolExecutor.AbortPolicy());

    public static class SystemLogTask implements Runnable {
        private AdminSystemLog systemLog;
        private static final Integer batchNumber = 5;
        private static final String SAVE_RECORD_TEMPLATE = "INSERT INTO `72crm_admin_system_log` (`create_user_id`, `create_time`, `ip_address`, `types`, `behavior`, `object`, `detail`) VALUES ('{}', '{}', '{}', '{}', '{}', '{}', '{}')";
        private static final List<String> sqlList = new CopyOnWriteArrayList<>();

        SystemLogTask(AdminSystemLog systemLog) {
            this.systemLog = systemLog;
        }

        @Override
        public void run() {
            if (ObjectUtil.isEmpty(systemLog)) {
                return;
            }
            if (sqlList.size() >= batchNumber) {
                saveActionRecord();
            }
            sqlList.add(StrUtil.format(SAVE_RECORD_TEMPLATE, systemLog.getCreateUserId(), DateUtil.formatDateTime(systemLog.getCreateTime()), systemLog.getIpAddress(), systemLog.getTypes(), systemLog.getBehavior(), systemLog.getObject(), systemLog.getDetail()));
        }

        static void saveActionRecord() {
            List<String> list = new ArrayList<>(sqlList);
            //底层已经做过size为0的判断，此处不再限制
            try {
                Db.batch(list, batchNumber);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqlList.clear();
            }
        }
    }

    public static String getIpCityInfo(String ip) {

        //db
        String dbPath = PathKit.getRootClassPath() + "/config/ip2region.db";

        File file = new File(dbPath);
        if (!file.exists() || !IPAddressUtil.isIPv4LiteralAddress(ip)) {
            return null;
        }


        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);

            //选择算法
            Method method = searcher.getClass().getMethod("btreeSearch", String.class);

            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);

            return dataBlock.getRegion();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
