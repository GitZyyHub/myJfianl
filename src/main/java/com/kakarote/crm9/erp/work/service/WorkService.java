package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;

import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.common.enums.FieldTypeEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminExamineRecordService;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerData;
import com.kakarote.crm9.erp.work.common.WorkAuthVerify;
import com.kakarote.crm9.erp.work.common.WorkPermissions;
import com.kakarote.crm9.erp.work.common.WorkRendAuthEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.utils.*;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author zyy
 */
public class WorkService extends AbstractBasePageList {

    @Inject
    AdminFieldService adminFieldService;

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    @Inject
    private AdminExamineRecordService examineRecordService;

    @Inject
    WorkAuthVerify workAuthVerify;


    /**
     * 查询项目
     *
     * @return
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        Long userId = BaseUtil.getUserId();
        CrmEnum work = CrmEnum.WORK;
        Kv kv = getKv(basePageRequest, work, isDataAuth);
        JSONObject jsonObject = basePageRequest.getJsonObject();
        kv.set("workPhase", jsonObject.get("workPhase"));
        kv.set("userId", BaseUtil.getUserId());
        queryDataAuth(WorkUserEnum.WORK, kv, CrmEnum.WORK, null, WorkRendAuthEnum.WORK, null);
        if (kv == null) {
            return R.error("无权限");
        }
        SqlPara sqlPara = Db.getSqlPara("work.advancedSearch", kv);
        List<Record> records = Db.find(sqlPara);
        JSONObject resultJsonObject = new JSONObject();
        resultJsonObject.put("list", records);
        Integer integer = Db.template("work.queryWoekPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", integer);
        return R.ok().put("data", resultJsonObject);
    }

    /**
     * 查询我的待办项目
     */
    public R waiting(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum work = CrmEnum.WORK;
        Kv kv = getKv(basePageRequest, work, isDataAuth);
        JSONObject jsonObject = basePageRequest.getJsonObject();
        kv.set("userId", BaseUtil.getUserId());
        kv.set("isWaiting", jsonObject.get("isWaiting"));
        queryDataAuth(WorkUserEnum.WORK, kv, CrmEnum.WORK, null, WorkRendAuthEnum.WORK, 1);
        if (kv == null) {
            return R.error("无权限");
        }
        SqlPara sqlPara = Db.getSqlPara("work.waiting", kv);
        List<Record> records = Db.find(sqlPara);
        JSONObject resultJsonObject = new JSONObject();
        resultJsonObject.put("list", records);
        Integer integer = Db.template("work.waitingCount", kv).queryInt();
        resultJsonObject.put("totalRow", integer);
        return R.ok().put("data", resultJsonObject);
    }

    /**
     * 根据id查询详细信息
     *
     * @param id
     * @return
     */
    public R queryById(Integer id) {
        Record work = Db.findFirst(Db.getSql("work.findWorkById"), id);
        work.set("user_id",BaseUtil.getUserId());
        return R.ok().put("data", work);
    }


    public Record queryField(CrmEnum crmEnum, Integer id) {
        Record work = Db.findFirst(Db.getSql("work.findWorkById"), id);
        return work;
    }

    /**
     * 设置项目
     *
     * @param
     * @return
     */
    @Before(Tx.class)
    public R setWork(JSONObject jsonObject) {
        Work work = jsonObject.getObject("entity", Work.class);
        if (Arrays.asList(work._getAttrNames()).contains("name") && StrUtil.isEmpty(work.getWorkName())) {
            return R.error("项目名称不能为空！");
        }
        Date startTime = work.getStartTime();
        Date endTime = work.getEndTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if (startTime != null && endTime != null) {
            if(!startTime.before(endTime)){
                return R.error("结束时间应大于开始时间");
            }else if((startTime.getTime() - endTime.getTime()) / (24 * 60 * 60 * 1000) > 30){
                return R.error("开始时间到结束时间不能超过30天");
            }
        }
        Long userId = BaseUtil.getUserId();
        boolean temp;
        if (work.getWorkId() == null) {
            //添加
            if (StringUtils.isNullOrEmpty(work.getWorkNum())) {

                AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 19");
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                work.setWorkNum(result);
            }
            work.setBatchId(IdUtil.simpleUUID());
            work.setCreateUserId(userId);
            work.setCreateTime(new Date());
            work.setUpdateTime(new Date());
            temp = work.save();
            workHistoricalRecordService.add(work.getWorkId(), WorkRecordEnum.WORK_DETAILS, BehaviorEnum.SAVE, work.getWorkNum(), null, null, work.getWorkId());
        } else {
            //修改
            Record first = Db.findFirst("SELECT work_num,STATUS,work_name,description,work_phase,work_type,(" +
                    "SELECT NAME FROM 72crm_admin_dept WHERE dept_id= w.dept_id) dept_id,(" +
                    "SELECT NAME FROM 72crm_admin_dept WHERE dept_id= w.area) area,(" +
                    "SELECT username FROM 72crm_admin_user WHERE user_id =w.sales_id) sales_id,(" +
                    "SELECT username FROM 72crm_admin_user WHERE user_id= w.manager_id) manager_id,DATE_FORMAT( start_time, '%Y-%m-%d %H:%i:%S' ) start_time, DATE_FORMAT( end_time, '%Y-%m-%d %H:%i:%S' ) end_time FROM `72crm_work` w WHERE work_id=?", work.getWorkId());
            if ("结项".equals(first.getStr("work_phase")) && "结项".equals(work.getWorkPhase())) {
                return R.error("当前项目阶段为结项，如果需要修改信息请先修改项目阶段！");
            }
            temp = true;
            work.update();
            Record after = Db.findFirst("SELECT work_num,STATUS,work_name,description,work_phase,work_type,(" +
                    "SELECT NAME FROM 72crm_admin_dept WHERE dept_id =w.dept_id) dept_id,(" +
                    "SELECT NAME FROM 72crm_admin_dept WHERE dept_id =w.area) area,(" +
                    "SELECT username FROM 72crm_admin_user WHERE user_id =w.sales_id) sales_id,(" +
                    "SELECT username FROM 72crm_admin_user WHERE user_id= w.manager_id) manager_id, DATE_FORMAT( start_time, '%Y-%m-%d %H:%i:%S' ) start_time, DATE_FORMAT( end_time, '%Y-%m-%d %H:%i:%S' ) end_time FROM `72crm_work` w WHERE work_id=?", work.getWorkId());
            workHistoricalRecordService.add(work.getWorkId(), WorkRecordEnum.WORK_DETAILS, BehaviorEnum.UPDATE, work.getWorkNum(), after, first, work.getWorkId());

            if ("结项".equals(first.getStr("work_phase"))) {
                R checkUserId = addExamine(work.getWorkId(), work.getWorkPhase(), jsonObject.getLong("checkUserId"));
                if ((int) (checkUserId.get("code")) != 0) {
                    return R.error("项目设置错误,添加审核失败");
                }
                return checkUserId;
            }
        }
        Work byId = Aop.get(Work.class).dao().findById(work.getWorkId());
        HashSet<Long> set = new HashSet<>();
        set.add(byId.getSalesId());
        set.add(byId.getManagerId());
        set.add(byId.getCreateUserId());
        setWorkRelation(set, work.getWorkId(), WorkUserEnum.WORK.getType(), work.getWorkId());
        return temp ? R.ok() : R.error("项目设置错误");
    }

    /**
     * 批量修改项目阶段
     */
    @Before(Tx.class)
    public R updateWorkPhase(Long string, String phase, Long checkUserId) {
        /*List<String> strings = Arrays.asList(ids.split(","));
        for (String string : strings) {*/
        Record first = Db.findFirst(Db.getSql("work.findWorkById"), string);
        if ("结项".equals(first.getStr("work_phase"))) {
            R r = addExamine(string, phase, checkUserId);
            return r;
        }
        Db.update(Db.getSqlPara("work.updateWorkPhase", Kv.by("id", string).set("phase", phase)));
        Record after = Db.findFirst(Db.getSql("work.findWorkById"), string);
        Work work = Aop.get(Work.class).findById(string);
        workHistoricalRecordService.add(work.getWorkId(), WorkRecordEnum.WORK_DETAILS, BehaviorEnum.UPDATE, work.getWorkNum(), after, first, work.getWorkId());
        //}
        return R.ok();
    }

    /**
     * 添加审核
     */
    public R addExamine(Long string, String phase, Long checkUserId) {
        Record examineData = new Record();

        Work work = Work.dao.findById(string);
        //work.setWorkPhase(phase)
        examineData = examineRecordService.saveExamineRecord(5, checkUserId, work.getSalesId(), null, null);
        if (examineData.getInt("status") == 0) {
            return R.error("没有启动的审核步骤，不能添加！");
        } else if (examineData.getInt("status") == 2) {
            return R.error("没有审批人，不能添加！");
        } else {
            work.setExamineRecordId(examineData.getInt("recordId"));
            Db.update("update 72crm_admin_examine_record set remarks=? where record_id =?", phase, examineData.getInt("recordId"));
            if (work.getCheckStatus() != null && work.getCheckStatus() == 5)
                work.setCheckStatus(5);
            else
                work.setCheckStatus(0);
            if (!work.update()) {
                return R.error("修改状态失败");
            }
        }

        return R.ok();
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @Before(Tx.class)
    public R delWork(String workIds) {
        String[] split = workIds.split(",");
        boolean temp = false;
        for (String workId : split) {
            temp = false;
            Work byId = Aop.get(Work.class).dao().findById(workId);
            if(byId == null){
                continue;
            }
            Integer examineRecordId = byId.getExamineRecordId();
            if(examineRecordId!=null){
                Db.delete("DELETE FROM 72crm_admin_examine_record WHERE record_id =?", examineRecordId);
                Db.delete("DELETE FROM 72crm_admin_examine_log WHERE record_id =?", examineRecordId);
            }
            /*Db.delete("DELETE FROM 72crm_task_hours WHERE task_id in (SELECT task_id from 72crm_task WHERE work_id = ?)", workId);
            Db.delete("DELETE FROM 72crm_task WHERE work_id =?", workId);
            Db.delete("DELETE FROM 72crm_milestone WHERE work_id =?", workId);*/
            temp = Aop.get(Work.class).dao().deleteById(workId);
            if (!temp) {
                return R.error("服务器异常删除失败");
            }
        }
        return R.ok();
    }

    /**
     * 操作关联人员表
     *
     * @param longs  人员列表
     * @param id     对应修改数据的ID
     * @param type   模块类型
     * @param workId 项目ID
     */
    public static void setWorkRelation(Collection<Long> longs, Object id, Integer type, Long workId) {
        longs.removeAll(Collections.singleton(""));
        longs.removeAll(Collections.singleton(null));
        Db.delete("delete from 72crm_work_user_record where num_id=? and type=?", id, type);
        Date date = new Date();
        longs.forEach(user -> {
            WorkUserRecord workUser = new WorkUserRecord();
            workUser.setWorkId(workId);
            workUser.setNumId(id).setUserId(user).setType(type);
            workUser.save();
        });
    }

    public JSONObject auth(Long userId, Integer workId) {
        Work work = Work.dao.findById(workId);
        JSONObject jsonObject = new JSONObject();
        List<Integer> roleIds = Aop.get(AdminRoleService.class).queryRoleIdsByUserId(userId);
        Record workMenu = Db.findFirst("select * from 72crm_admin_menu where parent_id = 0 and realm = 'work'");
        Record project = Db.findFirst("select * from 72crm_admin_menu where parent_id = ? and realm = 'project'", workMenu.getInt("menu_id"));
        JSONObject object = new JSONObject();
        List<String> realmList = new ArrayList<>();
        if (roleIds.contains(BaseUtil.getSuperRole()) || BaseUtil.getSuperUser().equals(userId) || roleIds.contains(BaseUtil.getWorkAdminRole())) {
            realmList = Db.query("select realm from 72crm_admin_menu where parent_id = ?", project.getInt("menu_id"));
        } else if (work.getIsOpen() == 0) {
            Integer roleId = Db.queryInt("select role_id from 72crm_work_user where work_id = ? and user_id = ?", workId, userId);
            realmList = Db.query(Db.getSql("work.queryWorkAuthByUserId"), roleId, project.getInt("menu_id"));
        } else if (work.getIsOpen() == 1) {
            if (work.getOwnerRole().equals(BaseUtil.getSmallAdminRole())) {
                realmList = Db.query("select realm from 72crm_admin_menu where parent_id = ?", project.getInt("menu_id"));
            } else {
                realmList = Db.query(Db.getSql("work.queryWorkAuthByUserId"), work.getOwnerRole(), project.getInt("menu_id"));
            }
        }
        realmList.forEach(realm -> {
            object.fluentPut(realm, true);
        });
        jsonObject.fluentPut(workMenu.getStr("realm"), new JSONObject().fluentPut(project.getStr("realm"), object));
        return jsonObject;
    }


    /**
     * 普通搜索
     *
     * @param search
     * @return
     */
    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (work_name like '%{search}%' or description like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    /**
     * 导入
     *
     * @param file
     * @return
     */
    public R excelImport(UploadFile file) {
        if (file.getFile() == null || !file.getFile().exists()) return R.ok();

        //错误集合
        ArrayList<Object> errList = new ArrayList<>();

        AtomicReference<Integer> num = new AtomicReference<>(0);
        //遍历文件
        try {
            ExcelUtil.readBySax(file.getFile().getAbsolutePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                System.out.println(rowIndex);
                if (rowIndex > 1) {

                    num.getAndSet(num.get() + 1);
                    if (StrUtil.isEmptyIfStr(rowList.get(1))) {
                        rowList.add(0, "项目名称不能为空");
                        errList.add(rowList);
                        return;
                    }
                    String workName = rowList.get(1).toString().trim();

                    Work work = new Work();
                    work.setCreateTime(new Date());
                    work.setWorkName(workName);

                    if (StringUtils.isNullOrEmpty(rowList.get(0).toString().trim())) {
                        AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 19");
                        String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                        work.setWorkNum(result);
                    } else {
                        work.setWorkNum(rowList.get(0).toString().trim());
                    }

                    Record first;

                    first = Db.findFirst(Db.getSql("admin.dept.queryDeptIdByName"), rowList.get(2).toString().trim());
                    if (first == null) {
                        rowList.add(3, "该部门不存在");
                        errList.add(rowList);
                        return;
                    }
                    work.setDeptId(first.getInt("dept_id"));


                    first = Db.findFirst(Db.getSql("admin.dept.queryDeptIdByName"), rowList.get(3).toString().trim());
                    if (first == null) {
                        rowList.add(4, "该部门不存在");
                        errList.add(rowList);
                        return;
                    }
                    work.setArea(first.getInt("dept_id"));


                    first = Db.findFirst(Db.getSql("admin.user.queryIdByName"), rowList.get(4).toString().trim());
                    if (first == null) {
                        rowList.add(5, "该员工不存在");
                        errList.add(rowList);
                        return;
                    }
                    work.setManagerId(first.getInt("user_id"));

                    first = Db.findFirst(Db.getSql("admin.user.queryIdByName"), rowList.get(5).toString().trim());
                    if (first == null) {
                        rowList.add(6, "该员工不存在");
                        errList.add(rowList);
                        return;
                    }
                    work.setSalesId(Long.parseLong(first.getStr("user_id")));

                    first = Db.findFirst(Db.getSql("admin.user.queryIdByName"), rowList.get(5).toString().trim());
                    if (first == null) {
                        rowList.add(6, "该员工不存在");
                        errList.add(rowList);
                        return;
                    }
                    work.setWorkPhase(rowList.get(6).toString().trim());


                    try {
                        Object date = rowList.get(8);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date startTime = sdf.parse((String) date);
                        date = rowList.get(9).toString();
                        Date end = sdf.parse((String) date);
                        if (startTime.compareTo(end) > 0) {
                            rowList.add(8, "时间跨界");
                            errList.add(rowList);
                            return;
                        }
                        work.setStartTime(startTime).setEndTime(end);

                    } catch (Exception e) {
                    }


                    work.setCreateUserId(BaseUtil.getUserId());
                    try {
                        work.setWorkType(rowList.get(7).toString().trim());
                    } catch (Exception e) {
                    }

                    work.setCreateTime(new Date());

                    work.save();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传出错");
        }
        R result = R.ok().put("totalSize", num.get()).put("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(file.getUploadPath() + "/" + token);

                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);

                writer.writeHeadRow(Arrays.asList("错误信息", "手机号(*)", "登录密码(*)", "姓名(*)", "性别", "邮箱", "岗位"));
                writer.write(errList);
                result.put("errSize", errList.size()).put("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }


    /**
     * 查询项目详情/项目扩展详情/项目历史记录/需求详情
     */
    public List<Record> findFieldDetails(Integer id) {
        List<String> name = Arrays.asList("项目名称", "立项评审", "项目类型", "方案对标", "风险", "合同解读", "目标", "合同金额", "有效金额", "实施成本", "运维成本", "预算", "实际成本", "客户名称", "客户联系人", "客户联系电话", "预估工作量（人/时）");
        List<String> fieldName = Arrays.asList("work_name", "examine", "work_type", "solution", "risk", "unscramble", "target", "contract_amount", "effective_amount", "implement_cost", "maintenance_cost", "budget", "practical_cost", "customer_name", "customer_contact", "customer_calls", "forecast_work");
        List<Record> fields = adminFieldService.getFields(CrmEnum.WORK.getType(), id);
        for (Record field : fields) {
            String field_name = field.getStr("field_name");
            if("area".equals(field_name) || "deptId".equals(field_name)){
                field.set("authLevel", 2);
            }
        }
        Record first = Db.findFirst(Db.getSql("work.queryWorkExtensionById"), id);
        for (Map.Entry<String, Object> entry : first.getColumns().entrySet()) {
            Record record = new Record();
            int temp;
            if ((temp = fieldName.indexOf(entry.getKey())) >= 0) {
                record.set("authLevel", 3).set("sysInformation", 2).set("fieldName", entry.getKey()).set("name", name.get(temp)).set("value", entry.getValue())
                        .set("formType", "text").set("isNull", 0).set("isUnique", 0).set("remark", null).set("inputTips", null).set("defaultValue", null)
                        .set("label", CrmEnum.WORK.getType()).set("type", 1).set("sorting", temp).set("options", null).set("fieldType", 1);
            }
            fields.add(record);
        }
        /*List<String> progressName = Arrays.asList("需求总数", "需求完成数", "需求完成率（%）", "任务总数", "任务完成数",
                "任务完成度（%）", "总工作量（人/时）", "已消耗工作量（人/时）", "消耗工作量比例（%）");
        List<String> progressFieldName = Arrays.asList("demand_total", "demand_completion", "demand_rate", "task_total", "task_completion",
                "task_rate", "total_work", "consumption_work", "rate_work");

        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        //numberFormat.setMaximumFractionDigits(1);


        Record demand = Db.findFirst("select ifnull(count(1),0) demand_completion,(select ifnull(count(1),0) demand_total from 72crm_work_demand where work_id=?) demand_total " +
                "from 72crm_work_demand where work_id=? and demand_status='100%'", id, id);
        String demandTotal = demand.getStr("demand_total");
        String demandCompletion = demand.getStr("demand_completion");
        double v1 = Double.parseDouble(demandCompletion) / Double.parseDouble(demandTotal) * 100;
        String demandRate = Double.isNaN(v1) ? "0" : numberFormat.format(v1);

        String forecastWork = first.getStr("forecast_work");
        String consumptionWork = Db.queryStr("SELECT ifnull(sum(working_hours),0) from 72crm_task_hours WHERE subtasks_id in (SELECT task_id from 72crm_task WHERE work_id=?)", id);
        double v2 = Double.parseDouble(forecastWork) / Double.parseDouble(consumptionWork) * 100;
        String rateWork = Double.isNaN(v2) ? "0" : numberFormat.format(v2);

        Record task = Db.findFirst("SELECT ifnull(count(1),0) task_completion,(SELECT ifnull(COUNT(1),0) FROM 72crm_task WHERE work_id=?) " +
                " task_total FROM 72crm_task WHERE work_id=? AND completeness='100%'", id, id);
        String taskCompletion = task.getStr("task_completion");
        String taskTotal = task.getStr("task_total");
        double v3 = Double.parseDouble(taskCompletion) / Double.parseDouble(taskTotal) * 100;
        String taskRate = Double.isNaN(v3) ? "0" : numberFormat.format(v3);


        List<String> progressData = Arrays.asList(demandTotal, demandCompletion, demandRate, taskTotal, taskCompletion, taskRate, forecastWork, consumptionWork, rateWork);

        for (int i = 0; i < progressName.size(); i++) {
            Record record = new Record();
            record.set("authLevel", 3).set("sysInformation", 3).set("fieldName", progressFieldName.get(i)).set("name", progressName.get(i)).set("value", progressData.get(i))
                    .set("formType", "text").set("isNull", 0).set("isUnique", 0).set("remark", null).set("inputTips", null).set("defaultValue", null)
                    .set("label", CrmEnum.WORK.getType()).set("type", 1).set("sorting", i).set("options", null).set("fieldType", 1);
            fields.add(record);
        }*/

        return adminFieldService.findFieldDetails(CrmEnum.WORK, fields, id);
    }

    public R progress(Integer id) {
        List<String> progressName = Arrays.asList("需求总数", "需求完成数", "需求完成率（%）", "任务总数", "任务完成数",
                "任务完成度（%）", "总工作量（人/时）", "已消耗工作量（人/时）", "消耗工作量比例（%）");
        List<String> progressFieldName = Arrays.asList("demand_total", "demand_completion", "demand_rate", "task_total", "task_completion",
                "task_rate", "total_work", "consumption_work", "rate_work");
        Record record = new Record();
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        //numberFormat.setMaximumFractionDigits(1);

        Record first = Db.findFirst(Db.getSql("work.queryWorkExtensionById"), id);
        Record demand = Db.findFirst("select ifnull(count(1),0) demand_completion,(select ifnull(count(1),0) demand_total from 72crm_work_demand where work_id=?) demand_total " +
                "from 72crm_work_demand where work_id=? and demand_status='100'", id, id);
        String demandTotal = demand.getStr("demand_total");
        String demandCompletion = demand.getStr("demand_completion");
        double v1 = Double.parseDouble(demandCompletion) / Double.parseDouble(demandTotal) * 100;
        String demandRate = Double.isNaN(v1) ? "0" : numberFormat.format(v1);
        record.set("demandTotal", demandTotal).set("demandCompletion", demandCompletion).set("demandRate", demandRate);


        String forecastWork = first.getStr("forecast_work");
        String consumptionWork = Db.queryStr("SELECT ifnull(sum(working_hours),0) from 72crm_task_hours WHERE subtasks_id in (SELECT task_id from 72crm_task WHERE work_id=?)", id);
        double v2 = Double.parseDouble(forecastWork) / Double.parseDouble(consumptionWork) * 100;
        String rateWork = Double.isNaN(v2) ? "0" : numberFormat.format(v2);
        record.set("forecastWork", forecastWork).set("consumptionWork", consumptionWork).set("rateWork", rateWork);

        Record task = Db.findFirst("SELECT ifnull(count(1),0) task_completion,(SELECT ifnull(COUNT(1),0) FROM 72crm_task WHERE work_id=?) " +
                " task_total FROM 72crm_task WHERE work_id=? AND completeness='100'", id, id);
        String taskCompletion = task.getStr("task_completion");
        String taskTotal = task.getStr("task_total");
        double v3 = Double.parseDouble(taskCompletion) / Double.parseDouble(taskTotal) * 100;
        String taskRate = Double.isNaN(v3) ? "0" : numberFormat.format(v3);
        record.set("taskTotal", taskTotal).set("taskCompletion", taskCompletion).set("taskRate", taskRate);


       /* List<String> progressData = Arrays.asList(demandTotal, demandCompletion, demandRate, taskTotal, taskCompletion, taskRate, forecastWork, consumptionWork, rateWork);

        for (int i = 0; i < progressName.size(); i++) {
            Record record = new Record();
            record.set("authLevel", 3).set("sysInformation", 3).set("fieldName", progressFieldName.get(i)).set("name", progressName.get(i)).set("value", progressData.get(i))
                    .set("formType", "text").set("isNull", 0).set("isUnique", 0).set("remark", null).set("inputTips", null).set("defaultValue", null)
                    .set("label", CrmEnum.WORK.getType()).set("type", 1).set("sorting", i).set("options", null).set("fieldType", 1);
            fields.add(record);
        }*/
        return R.ok().put("data", record);
    }


    /**
     * 拼接
     *
     * @param
     * @param
     * @param
     * @return
     */
    /*@Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject jsonObject = null;
        *//*        super.appendOtherSql(isDataAuth, kv, crmEnum);
        String conditions = " and status != 3 ";
        jsonObject.put("sql", jsonObject.getString("sql") + conditions);*//*
        return jsonObject;
    }*/
    public List<Integer> toList(List<String> labelList) {
        List<Integer> list = new ArrayList<>();
        if (labelList == null || labelList.size() == 0) {
            return list;
        }
        labelList.forEach(ids -> {
            if (StrUtil.isNotEmpty(ids)) {
                for (String id : ids.split(",")) {
                    if (StrUtil.isNotEmpty(id)) {
                        list.add(Integer.valueOf(id));
                    }
                }
            }
        });
        return list;
    }


    public R setLabel(String workId, String labelId) {
        Db.update("update 72crm_work set label_id=? where work_id=?", labelId, workId);
        return R.ok();
    }

    public R queryLabel(String workId) {
        String labelId = Aop.get(Work.class).findById(workId).getLabelId();
        if (labelId != null) {
            List<String> strings = Arrays.asList(labelId.split(","));
            List<Record> records = Db.find(Db.getSqlPara("work.queryLabel", Kv.by("ids", strings)));
            return R.ok().put("data", records);
        } else {
            return R.ok();
        }
    }

    public R delLabel(String workId, String labelId) {
        String label = Aop.get(Work.class).findById(workId).getLabelId();
        String[] list = label.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.length; i++) {
            String item = list[i];
            if (!labelId.equals(item)) {
                stringBuffer.append(list[i] + ",");
            }
        }
        return setLabel(workId, stringBuffer.toString());
    }
}
