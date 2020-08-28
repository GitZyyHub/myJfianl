package com.kakarote.crm9.erp.oa.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.oa.common.OaEnum;
import com.kakarote.crm9.erp.oa.entity.OaExamineLog;
import com.kakarote.crm9.erp.oa.entity.OaExamineRelation;
import com.kakarote.crm9.erp.oa.service.OaExamineService;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OaExamineController extends Controller {

    @Inject
    private OaExamineService oaExamineService;


    /**
     * @author hmb
     * 我发起的审批
     * @param basePageRequest 分页对象
     */
    @SysLog(LogEnum.MY_SEND_EXAMINE)
    public void myInitiate(BasePageRequest basePageRequest) {
        renderJson(oaExamineService.myInitiate(basePageRequest));
    }

    /**
     * @author hmb
     * 我审批的
     * @param basePageRequest 分页对象
     */
    @SysLog(LogEnum.MY_OA_EXAMINE)
    public void myOaExamine(BasePageRequest basePageRequest) {
        renderJson(oaExamineService.myOaExamine(basePageRequest));
    }

    public void getField(){
        String id = getPara("examineId");
        Integer isDetail = getInt("isDetail");//1详情 2 编辑
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.NULL_TYPE_KEY, Integer.valueOf(id));
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(oaExamineService.getField(id,isDetail));
    }

    /**
     * @author hmb
     * 创建审批
     */
    public void setOaExamine(){
        String data = getRawData();
        JSONObject jsonObject = JSON.parseObject(data);
        renderJson(oaExamineService.setOaExamine(jsonObject));
    }

    /**
     * @author hmb
     * 审批
     */
    public void auditExamine(){
        Integer recordId = getInt("recordId");
        Integer status = getInt("status");
        String remarks = get("remarks");
        Long nextUserId = getLong("nextUserId");
        OaExamineLog oaExamineLog = new OaExamineLog();
        oaExamineLog.setRecordId(recordId);
        oaExamineLog.setExamineStatus(status);
        oaExamineLog.setRemarks(remarks);
        renderJson(oaExamineService.oaExamine(oaExamineLog,nextUserId));
    }

    /**
     * @author hmb
     * 查询审批详情
     */
    @NotNullValidate(value = "examineId",message = "审批id不能为空")
    public void queryOaExamineInfo(){
        String id = getPara("examineId");
        Integer number = Db.queryInt("select count(1) from `72crm_oa_examine` where examine_id = ?",id);
        if (number == 0){
            renderJson(R.error("审批已删除"));
            return;
        }
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.NULL_TYPE_KEY, Integer.valueOf(id));
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(oaExamineService.queryOaExamineInfo(id));
    }

    /**
     * @author hmb
     * 查询审批步骤
     */
    public void queryExamineRecordList(){
        Integer recordId = getInt("recordId");
        Integer examineId = Db.queryInt("select examine_id from `72crm_oa_examine_record` where record_id = ?",recordId);
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.NULL_TYPE_KEY, examineId);
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(oaExamineService.queryExamineRecordList(recordId));
    }

    /**
     * @author hmb
     * 查询审批历史
     */
    @NotNullValidate(value = "recordId",message = "记录id不能为空")
    public void queryExamineLogList(){
        Integer recordId = getInt("recordId");
        Integer examineId = Db.queryInt("select examine_id from `72crm_oa_examine_record` where record_id = ?",recordId);
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.NULL_TYPE_KEY, examineId);
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(oaExamineService.queryExamineLogList(recordId));
    }

    /**
     * @author hmb
     * 删除审批
     */
    @NotNullValidate(value = "examineId",message = "审批id不能为空")
    public void deleteOaExamine(){
        Integer oaExamineId = getParaToInt("examineId");
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.EXAMINE_TYPE_KEY, oaExamineId);
        if(oaAuth){renderJson(R.noAuth()); return;}
        renderJson(oaExamineService.deleteOaExamine(oaExamineId));
    }

    /**
     * @author hmb
     * 查询审批步骤
     */
    public void queryExaminStep(){
        String categoryId = getPara("categoryId");
        renderJson(oaExamineService.queryExaminStep(categoryId));
    }

    /**
     * @author hmb
     * 查询审批关联业务
     */
    public void queryExamineRelation(@Para("")BasePageRequest<OaExamineRelation> pageRequest){
        renderJson(oaExamineService.queryExamineRelation(pageRequest));
    }

    /**
     * 导出
     */
    public void export(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer categoryId = jsonObject.getInteger("categoryId");
        Record category = Db.findFirst("select type,title from 72crm_oa_examine_category where category_id = ?", categoryId);
        Integer type = category.getInt("type");
        List<Record> fieldList = new ArrayList<>();
        if (type == 0) {
            fieldList = Db.find("select field_name,name,type,field_type from 72crm_admin_field where examine_category_id = ? and label = 10", categoryId);
        }
        List<Map<String, Object>> list = oaExamineService.export(basePageRequest, type, category.getStr("title"), fieldList);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.OA_EXAMINE, list.size());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            Integer columnNum = 1;
            writer.addHeaderAlias("category", "审批类型");
            writer.addHeaderAlias("create_time", "创建日期");
            writer.addHeaderAlias("create_user_name", "创建人");
            writer.addHeaderAlias("examine_status", "状态");
            writer.addHeaderAlias("examine_user_name", "当前审批人");
            writer.addHeaderAlias("previous_examine_user_name", "上一审批人");
            switch (type) {
                case 1:
                    writer.addHeaderAlias("content", "审批内容");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 9;
                    break;
                case 2:
                    writer.addHeaderAlias("content", "审批内容");
                    writer.addHeaderAlias("start_time", "开始时间");
                    writer.addHeaderAlias("end_time", "结束时间");
                    writer.addHeaderAlias("duration", "时长");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 12;
                    break;
                case 3:
                    writer.addHeaderAlias("content", "出差事由");
                    writer.addHeaderAlias("remark", "备注");
                    writer.addHeaderAlias("duration", "出差总天数");
                    writer.addHeaderAlias("vehicle", "交通工具");
                    writer.addHeaderAlias("trip", "单程往返");
                    writer.addHeaderAlias("start_address", "出发城市");
                    writer.addHeaderAlias("end_address", "目的城市");
                    writer.addHeaderAlias("start_time", "开始时间");
                    writer.addHeaderAlias("end_time", "结束时间");
                    writer.addHeaderAlias("travel_duration", "时长");
                    writer.addHeaderAlias("description", "出差备注");
                    columnNum = 18;
                    break;
                case 4:
                    writer.addHeaderAlias("content", "加班原因");
                    writer.addHeaderAlias("start_time", "开始时间");
                    writer.addHeaderAlias("end_time", "结束时间");
                    writer.addHeaderAlias("duration", "加班总天数");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 12;
                    break;
                case 5:
                    writer.addHeaderAlias("content", "差旅内容");
                    writer.addHeaderAlias("money", "报销总金额");
                    writer.addHeaderAlias("remark", "备注");
                    writer.addHeaderAlias("start_address", "出发城市");
                    writer.addHeaderAlias("end_address", "目的城市");
                    writer.addHeaderAlias("start_time", "开始时间");
                    writer.addHeaderAlias("end_time", "结束时间");
                    writer.addHeaderAlias("traffic", "交通费");
                    writer.addHeaderAlias("stay", "住宿费");
                    writer.addHeaderAlias("diet", "餐饮费");
                    writer.addHeaderAlias("other", "其他费用");
                    writer.addHeaderAlias("travel_money", "合计");
                    writer.addHeaderAlias("description", "费用明细描述");
                    columnNum = 20;
                    break;
                case 6:
                    writer.addHeaderAlias("content", "审批内容");
                    writer.addHeaderAlias("money", "借款金额");
                    writer.addHeaderAlias("remark", "备注");
                    columnNum = 10;
                    break;
                case 0:
                    fieldList.forEach(field -> writer.addHeaderAlias(field.getStr("field_name"), field.getStr("name")));
                    columnNum = fieldList.size() + 7;
                    break;
                default:
                    break;
            }
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(columnNum - 1, "审批信息");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < columnNum; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=oa_examine.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出日志错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }
}
