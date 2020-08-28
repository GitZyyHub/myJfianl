package com.kakarote.crm9.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminNumberSetting;
import com.kakarote.crm9.erp.crm.common.CrmEnum;

import java.util.Date;
import java.util.List;

/**
 * @author wyq
 */
public class ToolUtils {

    public String generateNumber(AdminConfig adminConfig, Date orderDate) {
        CrmEnum crmEnum = CrmEnum.parse(Integer.valueOf(adminConfig.getValue()));
        List<String> numberList = Db.query("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName());
        String result;
        List<AdminNumberSetting> settingList = AdminNumberSetting.dao.find("select * from 72crm_admin_number_setting where pid = ? order by sort", adminConfig.getSettingId());
        int i = 0;
        do {
            StringBuilder numberSb = new StringBuilder();
            for (int j = 0; j < settingList.size(); j++) {
                AdminNumberSetting setting = settingList.get(j);
                if (j > 0) {
                    numberSb.append("-");
                }
                if (setting.getType() == 1) {
                    numberSb.append(setting.getValue());
                } else if (setting.getType() == 2) {
                    if (ObjectUtil.isEmpty(orderDate)) {
                        numberSb.append(DateUtil.format(new Date(), setting.getValue()));
                    } else {
                        numberSb.append(DateUtil.format(orderDate, setting.getValue()));
                    }
                } else if (setting.getType() == 3) {
                    //先判断是否需要重新从起始编号生成编号
                    boolean b = setting.getLastNumber() != null && setting.getLastDate() != null;
                    if (i == 0) {
                        b = false;
                    }
                    if (setting.getLastNumber() != null && setting.getLastDate() != null) {
                        if (setting.getResetType() == 1 && !DateUtil.isSameDay(setting.getLastDate(), new Date())) {
                            b = false;
                        } else if (setting.getResetType() == 2 && !DateUtil.format(setting.getLastDate(), "yyyyMM").equals(DateUtil.format(new Date(), "yyyyMM"))) {
                            b = false;
                        } else if (setting.getResetType() == 3 && DateUtil.year(setting.getLastDate()) != DateUtil.year(new Date())) {
                            b = false;
                        }
                    }
                    if (b) {
                        Integer number = setting.getLastNumber() + setting.getIncreaseNumber();
                        //根据起始编号的位数不足补0
                        numberSb.append(String.format("%" + setting.getValue().length() + "s", number).replaceAll(" ", "0"));
                        setting.setLastNumber(number).setLastDate(new Date());
                    } else {
                        numberSb.append(setting.getValue());
                        setting.setLastNumber(Integer.parseInt(setting.getValue())).setLastDate(new Date()).update();
                    }
                }
            }
            result = numberSb.toString();
            if (i > numberList.size()) {
                Db.batchUpdate(settingList, 100);
                return result;
            }
            i++;
        } while (numberList.contains(result));
        Db.batchUpdate(settingList, 100);
        return result;
    }
}
