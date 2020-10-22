package com.kakarote.crm9.erp.work.common;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.utils.BaseUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 编辑删除权限验证
 * @author zyy
 */
public class WorkPermissions {

    static StringBuffer stringBuffer = new StringBuffer();

    public static boolean isPermissions(String tableName,String tabkeId, Object id) {
        Long userId = BaseUtil.getUserId();
        Record byId = Db.findById(tableName,tabkeId, id);
        Long createUserId = byId.getLong("create_user_id");
        Integer deptId = Db.queryInt("SELECT dept_id FROM 72crm_admin_user WHERE user_id=?", createUserId);
        if(deptId == 0){
            return true;
        }
        String deptPid = isDeptPid(deptId);
        List<String> strings = Arrays.asList(deptPid.split(","));
        List<Record> records = Db.find(Db.getSqlPara("admin.user.queryUserByDeptIds", Kv.by("deptIds", strings)));
        boolean temp = false;
        for (Record record : records) {
            Long user_id = record.getLong("user_id");
            if (user_id == userId) {
                temp = true;
            }
        }
        if (temp || userId.equals(createUserId)) {
            return true;
        }
        return false;
    }

    public static String isDeptPid(Integer deptId) {
        Integer pid = Db.queryInt("SELECT pid FROM 72crm_admin_dept WHERE dept_id =?",deptId);
        stringBuffer.append(pid + ",");
        if (pid == 0) {
            return stringBuffer.toString();
        }
        return isDeptPid(pid);
    }
}
