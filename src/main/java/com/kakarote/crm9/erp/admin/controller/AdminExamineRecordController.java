package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminExamineRecordService;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 审核合同或回款
 *
 * @author zxy
 */
public class AdminExamineRecordController extends Controller {
    @Inject
    private AdminExamineRecordService examineRecordService;

    /**
     * 审核合同或者回款 recordId:审核记录id status:审批状态：审核状态  1 审核通过 2 审核拒绝 4 已撤回
     * remarks:审核备注 id:审核对象的id（合同或者回款的id）
     */
    public void auditExamine() {
        Integer recordId = getInt("recordId");
        Integer status = getInt("status");
        Integer id = getInt("id");
        String remarks = get("remarks");
        Long nextUserId = getLong("nextUserId");
        Long ownerUserId = getLong("ownerUserId");
        renderJson(examineRecordService.auditExamine(recordId, status, remarks, id, nextUserId, ownerUserId));
    }

    /**
     * 根据审核记录id，查询审核日志
     * recordId 审核记录id
     */
    public void queryExamineLogList() {
        Integer recordId = getInt("recordId");
        if (getExamineObjIdByRecordId(recordId)) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(examineRecordService.queryExamineLogList(recordId));
    }

    /**
     * 根据审核记录id，查询审核日志
     * recordId 审核记录id ownerUserId 负责人ID
     */
    public void queryExamineRecordList() {
        Integer recordId = getInt("recordId");
        Long ownerUserId = getLong("ownerUserId");
        if (getExamineObjIdByRecordId(recordId)) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(examineRecordService.queryExamineRecordList(recordId, ownerUserId));
    }

    /**
     * 根据recordId查询权限
     *
     * @param recordId
     * @return
     */
    private boolean getExamineObjIdByRecordId(Integer recordId) {
        boolean auth = false;
        Integer id = Db.queryInt("select contract_id from `72crm_crm_contract` where examine_record_id = ?", recordId);
        if (id != null) {
            auth = AuthUtil.isCrmAuth(CrmEnum.CRM_CONTRACT, id);
        } else {
            id = Db.queryInt("select receivables_id from `72crm_crm_receivables` where examine_record_id = ?", recordId);
            if (id != null) {
                auth = AuthUtil.isCrmAuth(CrmEnum.CRM_RECEIVABLES, id);
            } else {
                id = Db.queryInt("select invoice_id from `72crm_crm_invoice` where examine_record_id = ?", recordId);
                if (id != null) {
                    auth = AuthUtil.isCrmAuth(CrmEnum.CRM_INVOICE, id);
                } else {

                   /* id = Db.queryInt("select hours_id from `72crm_task_hours` where examine_record_id = ?", recordId);
                    if(id != null){
                        List<Long> query = Db.template("work.queryDataUserId", Kv.by("ty", WorkUserEnum.HOURS.getType()).set("numId", id)).query();
                        if(!query.contains(BaseUtil.getUserId())){
                            auth = true;
                        }
                    }else {
                        List<Long> query = Db.template("work.queryDataUserId", Kv.by("ty", WorkUserEnum.WORK.getType()).set("numId", id)).query();
                        if(!query.contains(BaseUtil.getUserId())){
                            auth = true;
                        }
                    }*/
                }

            }
        }
        return auth;
    }

    public void myInitiate(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data", examineRecordService.myInitiate(basePageRequest)));
    }

    @SysLog({LogEnum.MY_CONTRACT_EXAMINE, LogEnum.MY_RECEIVABLES_EXAMINE})
    public void myExamine(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data", examineRecordService.myExamine(basePageRequest)));
    }

    /**
     * 导出审批
     */
    public void myExamineExport(BasePageRequest basePageRequest) {
        JSONObject json = basePageRequest.getJsonObject();
        Integer categoryType = json.getInteger("categoryType");
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject) examineRecordService.myExamineExport(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> fieldName = Arrays.asList( "create_time", "create_user_name", "examine_status", "examineName", "catagory", "catagory_cite_name");
            for (Record record : records) {
                Map<String, Object> columns = record.getColumns();
                Record temp = new Record();
                for (Map.Entry<String, Object> entry : columns.entrySet()) {
                    String key = entry.getKey();
                    if (fieldName.contains(key)) {
                        if ("examineName".equals(key)) {
                            temp.set("examine_name", entry.getValue());
                        } else if ("examine_status".equals(key)) {
                            int status = (int) entry.getValue();
                            switch (status) {
                                case 0:
                                    temp.set("examine_status", "未审核");
                                    break;
                                case 1:
                                    temp.set("examine_status", "审核通过");
                                    break;
                                case 2:
                                    temp.set("examine_status", "审核拒绝");
                                    break;
                                case 3:
                                    temp.set("examine_status", "撤回审核");
                                    break;
                                default:
                                    temp.set("examine_status", "未识别");
                                    break;
                            }
                        } else {
                            temp.set(key, entry.getValue());
                        }
                    }
                }
                list.add(temp.getColumns());
            }

            List<Record> headList = new ArrayList<>();
            getFields(headList,categoryType);
            headList.forEach(head -> {
                if (!Objects.equals(head.getStr("fieldName"), head.getStr("name"))) {
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")), head.getStr("name"));
                } else {
                    writer.addHeaderAlias(head.getStr("fieldName"), head.getStr("name"));
                }
            });

            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);

            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 25);
            }

            HttpServletResponse response = getResponse();

            response.setCharacterEncoding("UTF-8");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=work.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void getFields(List<Record> fields,Integer categoryType) {
        List<String> names = new ArrayList<>();
        if(categoryType ==4){
             names = Arrays.asList("审批流程", "创建日期", "创建人", "审核状态", "审批人", "审批任务编号", "审批任务名称");
        }else {
            names = Arrays.asList("审批流程", "创建日期", "创建人", "审核状态", "审批人", "审批项目编号", "审批项目名称");
        }
        List<String> fieldName = Arrays.asList("examine_type", "create_time", "create_user_name", "examine_status", "examine_name", "catagory", "catagory_cite_name");
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName", fieldName.get(i));
            record.set("name", names.get(i));
            record.set("type", FormTypeEnum.TEXT.getType());
            if (i == 6) {
                record.set("type", FormTypeEnum.SELECT.getType());
                record.set("options", "已到位,未到位");
            }
            fields.add(record);
        }
    }
}
