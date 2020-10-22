package com.kakarote.crm9.erp.oa.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.service.CrmActivityService;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;
import com.mysql.jdbc.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zyy
 */
public class OaUserLogService {

    @Inject
    private CrmActivityService crmActivityService;
    @Inject
    private AdminFileService adminFileService;
    @Inject
    private OaCommentService commentService;


    public void addUserLog(JSONObject object, Integer logId, int temp) {
        Long userId = new Long(777777L);
        Date date = new Date();
        Integer categoryId = object.getInteger("categoryId");

        List<Record> fields = Db.find(Db.getSqlPara("oa.logField.findByTypeId", Kv.by("typeId", categoryId)));

        fields.forEach(field -> {
            //获得文本
            Integer log_field_id = field.getInt("log_field_id");
            String fieldText = object.getString(log_field_id + "");

            switch (temp) {
                case 1:
                    //添加
                    if (!StringUtils.isNullOrEmpty(fieldText))
                        saveUserLog(userId, log_field_id, field.getStr("log_field_name"), fieldText, logId, date);
                    break;
                case 2:
                    //更新
                    List<Record> records = Db.find(Db.getSql("oa.userLog.findFieldByid"), logId);
                    AtomicBoolean flag = new AtomicBoolean(false);
                    //判断是否存在
                    records.forEach(record -> {
                        if (record.getInt("field_id").equals(log_field_id))
                            flag.set(true);
                    });
                    if (!StringUtils.isNullOrEmpty(fieldText)) {
                        if (flag.get())
                            Db.update("UPDATE 72crm_oa_user_log SET field_text = ?,update_time = now() WHERE field_id = ? and log_id = ?", fieldText, field.getStr("log_field_id"), logId);
                        else
                            saveUserLog(userId, log_field_id, field.getStr("log_field_name"), fieldText, logId, date);
                    } else {
                        if (flag.get()) {
                            Db.delete(Db.getSql("oa.userLog.delLogFieldById"), logId, log_field_id);
                        }
                    }
                    break;
            }
        });
    }


    /**
     * @param object object
     * @return 响应结果
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public R saveAndUpdate(JSONObject object) {
        int temp;
        //AdminUser user = BaseUtil.getUser();
        Long userId = new Long(777777L);
        Integer type = object.getInteger("getBulletin");
        OaLog oaLog = object.toJavaObject(OaLog.class);
        OaLogRelation oaLogRelation = object.toJavaObject(OaLogRelation.class);
        oaLog.setCreateUserId(userId);
        oaLog.setCreateTime(new Date());
        oaLog.setReadUserIds(",,");
        oaLog.setSendUserIds(TagUtil.fromString(oaLog.getSendUserIds()));
        oaLog.setSendDeptIds(TagUtil.fromString(oaLog.getSendDeptIds()));
        if (oaLog.getLogId() != null) {
            temp = 2;
            /*boolean oaAuth = AuthUtil.isOaAuth(OaEnum.LOG_TYPE_KEY, oaLog.getLogId());
            if (oaAuth) {
                return R.noAuth();
            }*/
            oaLog.update();
            //Aop.get(ActionRecordUtil.class).addOaLogUpdateRecord(CrmEnum.OA_LOG, oaLog.getLogId());
            //Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_LOG_SEND, oaLog, userId);
        } else {
            temp = 1;
            oaLog.save();
            //Aop.get(ActionRecordUtil.class).addOaLogSaveRecord(CrmEnum.OA_LOG, oaLog.getLogId());
            if (Objects.equals(type, 1)) {
                saveLogRecord(oaLog.getLogId());
            }
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_LOG_SEND, oaLog,userId);
        }
        if (oaLogRelation != null) {
            Db.deleteById("72crm_oa_log_relation", "log_id", oaLog.getLogId());
            oaLogRelation.setLogId(oaLog.getLogId());
            oaLogRelation.setBusinessIds(TagUtil.fromString(oaLogRelation.getBusinessIds()));
            oaLogRelation.setContactsIds(TagUtil.fromString(oaLogRelation.getContactsIds()));
            oaLogRelation.setContractIds(TagUtil.fromString(oaLogRelation.getContractIds()));
            oaLogRelation.setCustomerIds(TagUtil.fromString(oaLogRelation.getCustomerIds()));
            oaLogRelation.setCreateTime(DateUtil.date());
            oaLogRelation.save();
            //crmActivityService.addActivity(2, CrmActivityEnum.LOG, oaLog.getLogId(), "");
        }
        addUserLog(object, oaLog.getLogId(), temp);
        return R.ok();
    }


    /**
     * 保存日志和销售简报关联信息
     *
     * @param logId 日志ID
     */
    private void saveLogRecord(Integer logId) {
        OaLogRecord logRecord = new OaLogRecord();
        logRecord.setLogId(logId);
        Record record = Db.template("crm.Instrument.queryBulletinByLog", Kv.by("userIds", new Long(777777L))).findFirst();
        logRecord.put(record);
        logRecord.setCreateTime(new Date());
        logRecord.save();
        List<OaLogBulletin> bulletinList = OaLogBulletin.dao.template("crm.Instrument.queryBulletinByLogInfo", Kv.by("userIds", new Long(777777L))).find();
        Date date = new Date();
        bulletinList.forEach(bulletin -> {
            bulletin.setLogId(logId);
            bulletin.setCreateTime(date);
        });
        Db.batchSave(bulletinList, 200);
    }

    private boolean saveUserLog(Long userId, Integer logFieldId, String logFieldName, String text, Integer logId, Date date) {
        //Record first = Db.findFirst(Db.getSql("oa.userLog.findField"), logFieldId);
        OaUserLog oaUserLog = new OaUserLog();
        oaUserLog.setUserId(userId);
        oaUserLog.setFieldId(logFieldId);
        oaUserLog.setFieldName(logFieldName);
        oaUserLog.setFieldText(text);
        oaUserLog.setLogId(logId);
        oaUserLog.setCreateTime(date);
        return oaUserLog.save();
    }


    /**
     * 根据id删除日志
     *
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public boolean deleteById(Integer logId) {
        OaLog oaLog = OaLog.dao.findById(logId);
        if (oaLog != null) {
            Db.deleteById("72crm_oa_log_relation", "log_id", logId);
            adminFileService.removeByBatchId(oaLog.getBatchId());
            Db.deleteById("72crm_oa_log", "log_id", logId);
            commentService.deleteComment(2, logId);
            Db.delete(Db.getSql("oa.userLog.delUserLog"), logId);
            return true;
        }
        return false;
    }

    /**
     * 分页查询用户日志
     *
     * @return
     */
    public R findUserLog(BasePageRequest<OaUserLog> basePageRequest) {
        Long userId = new Long(777777L);
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(),
                new SqlPara().setSql(Db.getSql("oa.userLog.findLogByUserId")).addPara(userId));
        List<Record> list = paginate.getList();
        list.forEach(record -> {
            List<Record> text = Db.find(Db.getSql("oa.userLog.findLogText"), record.getStr("log_id"));
            text.forEach(userLog -> {
                record.set(userLog.getStr("field_id"), userLog.getStr("field_text"));
            });
        });
        paginate.setList(list);
        return R.ok().put("data", paginate);
    }


    /*public R findUserLog(BasePageRequest<OaUserLog> basePageRequest) {
        //Long userId = BaseUtil.getUserId();
        List<Record> records = Db.find(Db.getSql("oa.userLog.findLogIdSort"));
        int totalPage = (int) Math.ceil(Double.valueOf(records.size()) / Double.valueOf(basePageRequest.getLimit()));//总页数
        int totalRow = records.size();                                 //总行数
        int pageSize = basePageRequest.getLimit();                      //每页多少数据
        int pageNumber = basePageRequest.getPage();                     //当前页

        int size = pageSize * (pageNumber - 1);

        List<Object> objects = new ArrayList<>();

        try {
            for (int i = size; i < size + pageSize; i++) {
                List<Record> log_id = Db.find(Db.getSql("oa.userLog.findLogText"), records.get(i).getStr("log_id"));

                objects.add(log_id);
            }
        } catch (Exception e) {
        }

        Page<Record> page = new Page(objects, pageNumber, pageSize, totalPage, totalRow);
        *//*for (Record record : page.getList()) {
            List<Record> log_id = Db.find(Db.getSql("oa.userLog.findLogText"), records.get(i).getStr("log_id"));
            log_id.forEach(s->{
                record.set(s.getStr("field_name"),s.getStr("value"));
            });

        }*//*
        return R.ok().put("data", page);
    }*/


    /**
     * 添加和修改用户日志
     *
     * @param
     * @return
     */
    /* public R addUserLog(List<Map<String, Object>> oaUserLogs) {

       //方案一：根据类型获得所有字段

        //方案二：获得所有的类型
        boolean save = false;
        String logId = UUID.randomUUID().toString();
        Long userId = new Long(777777L) BaseUtil.getUserId();

        Date date = new Date();

        boolean temp = false;
        String str = null;
        for (Map<String, Object> map : oaUserLogs) {
            if (!StringUtils.isEmpty((String) map.get("logTextId"))) {
                str = (String) map.get("logTextId");
                temp = true;
            }
        }

        HashMap<String, String> hashMap = new HashMap<>();
        if(temp){
            List<Record> records = Db.find(Db.getSql("oa.userLog.findFieldByid"), str);
            for (Record record : records)
                hashMap.put(record.getStr("field_id"),record.getStr("field_id"));
        }

        for (Map<String, Object> map : oaUserLogs) {
            Integer logFieldId = (Integer) map.get("logFieldId");
            String fieldText = (String) map.get("fieldText");
            if (!temp) {
                //添加逻辑
                if (!StringUtils.isEmpty(fieldText)) {
                    save = saveUserLog(userId, logFieldId, fieldText, logId, date);
                }
            } else {
                hashMap.remove(map.get("logFieldId").toString());
                save = true;
                if (StringUtils.isEmpty((String) map.get("logTextId"))) {
                    save = saveUserLog(userId, logFieldId, fieldText, (String) map.get("logTextId"), date);
                } else {
                    //修改逻辑
                    if (!StringUtils.isEmpty(fieldText)) {
                        //修改
                        int update = Db.update("UPDATE 72crm_oa_user_log SET field_text = ?,update_time = now() WHERE field_id = ? and log_id = ?", fieldText, logFieldId, map.get("logTextId"));
                    }
                }
            }
        }
        if(temp){
            StringBuffer stringBuffer = new StringBuffer("DELETE FROM 72crm_oa_user_log WHERE log_id='"+ str +"' and field_id IN (");

            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                stringBuffer.append(stringStringEntry.getValue()+",");
            }
            Db.delete(stringBuffer.toString().substring(0, stringBuffer.length() - 1) + ")");
        }
        return R.isSuccess(save, "日志操作失败");
        return null;
    }*/
}
