package com.kakarote.crm9.erp.crm.common;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

/**
 * 保存CRM数据时验证数据格式
 */
public class CrmVerify {

    private CrmEnum crmEnum;

    protected CrmVerify(CrmEnum crmEnum) {
        this.crmEnum = crmEnum;
    }

    public R verify(JSONObject jsonObject) {
        switch (crmEnum) {
            case CRM_LEADS:
                return verifyLeads(jsonObject);
            case CRM_CUSTOMER:
                return verifyLeads(jsonObject);
            case CRM_CONTACTS:
                return verifyLeads(jsonObject);
            case CRM_PRODUCT:
                return verifyProduct(jsonObject);
            default:
                return R.error("参数错误");
        }
    }

    /**
     * 验证字段的正确性
     * 目前线索，客户，联系人需要验证的逻辑是相同的
     *
     * @param jsonObject json数据
     * @return 验证结果
     */
    private R verifyLeads(JSONObject jsonObject) {
        JSONObject entity = jsonObject.getJSONObject("entity");
        if (StrUtil.isNotEmpty(entity.getString("mobile"))) {
            if (!ReUtil.isMatch("^(\\+?0?\\d{2,4}\\-?)?\\d{6,11}$", entity.getString("mobile").trim())) {
                return R.error("手机号格式错误");
            }
        }
        if (StrUtil.isNotEmpty(entity.getString("next_time"))) {
            R result = verifyDateTime(entity.getString("next_time"));
            if (!result.isSuccess()) {
                return result;
            }
        }
        R result = verifyField(jsonObject);
        if (!result.isSuccess()) {
            return result;
        }
        return R.ok();
    }

    /**
     * 验证字段的正确性
     *
     * @param jsonObject json数据
     * @return 验证结果
     */
    private R verifyProduct(JSONObject jsonObject) {
        JSONObject entity = jsonObject.getJSONObject("entity");
        if (StrUtil.isNotEmpty(entity.getString("price"))) {
            if (!ReUtil.isMatch(PatternPool.MONEY, entity.getString("price"))) {
                return R.error("价格应为数字格式");
            }
        }
        R result = verifyField(jsonObject);
        if (!result.isSuccess()) {
            return result;
        }
        return R.ok();
    }

    /**
     * 验证自定义字段的正确性
     *
     * @param jsonObject json数据
     * @return 验证结果
     */
    private R verifyField(JSONObject jsonObject) {
        JSONArray array = jsonObject.getJSONArray("field");
        for (Object obj : array) {
            JSONObject object = (JSONObject) obj;
            int type = object.getInteger("type");
            String value = object.getString("value");
            if(StrUtil.isEmpty(value)){
                continue;
            }
            if (type == 4) {
                R result = verifyDate(value);
                if (!result.isSuccess()) {
                    return result;
                }
            } else if (type == 5) {
                if (!ReUtil.isMatch(PatternPool.NUMBERS, value)) {
                    return R.error("价格应为数字格式");
                }
            } else if (type == 7) {
                if (!ReUtil.isMatch("^(\\+?0?\\d{2,4}\\-?)?\\d{6,11}$", value)) {
                    return R.error("手机号格式错误");
                }
            } else if (type == 13) {
                R result = verifyDateTime(value);
                if (!result.isSuccess()) {
                    return result;
                }
            }
        }
        return R.ok();
    }

    /**
     * 验证日期时间的正确性
     *
     * @param str 日期时间字符串
     * @return 验证结果
     */
    private R verifyDateTime(String str) {
        if (DatePattern.NORM_DATETIME_PATTERN.length() != str.length()) {
            return R.error("日期格式错误，例:2020-01-01 00:00:00");
        }
        //将日期切割成 yyyy-MM-dd 和 HH:mm:ss 两部分
        String[] dates = str.split(" ");
        if (dates.length != 2) {
            return R.error("日期格式错误，例:2020-01-01 00:00:00");
        }
        if (!BaseUtil.isTime(dates[0]) || !ReUtil.isMatch(PatternPool.TIME, dates[1])) {
            return R.error("日期格式错误，例:2020-01-01 00:00:00");
        }
        return R.ok();
    }

    /**
     * 验证日期的正确性
     *
     * @param str 日期字符串
     * @return 验证结果
     */
    private R verifyDate(String str) {
        if (!Validator.isBirthday(str)) {
            return R.error("日期格式错误，例:2020-01-01");
        }
        return R.ok();
    }
}
