package com.kakarote.crm9.erp.crm.controller;

import cn.hutool.core.util.StrUtil;
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
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmReceivables;
import com.kakarote.crm9.erp.crm.service.CrmReceivablesService;
import com.kakarote.crm9.erp.crm.service.CrmRecordService;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrmReceivablesController extends Controller {

    @Inject
    private CrmReceivablesService crmReceivablesService;

    @Inject
    private AdminSceneService adminSceneService;

    /**
     * @author wyq
     * 查看列表页
     */
    @Permissions({"crm:receivables:index"})
    @SysLog(LogEnum.RECEIVABLES)
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(crmReceivablesService.queryPageList(basePageRequest,true));
    }

    /**@author zxy
     * 分页查询回款
     */
    public void  queryPage(BasePageRequest<CrmReceivables> basePageRequest){
        renderJson(R.ok().put("data",crmReceivablesService.queryPage(basePageRequest)));
    }
    /**
     * @author zxy
     * 添加或者修改
     */
    @Permissions({"crm:receivables:save","crm:receivables:update"})
    public void  saveOrUpdate(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(crmReceivablesService.saveOrUpdate(jsonObject));
    }
    /**
     * @author zxy
     * 根据回款id查询
     */
    @Permissions("crm:receivables:read")
    @NotNullValidate(value = "receivablesId",message = "回款id不能为空")
    public void  queryById(@Para("receivablesId") Integer receivablesId){
        Integer number = Db.queryInt("select count(1) from 72crm_crm_receivables where receivables_id = ? and check_status != 7",receivablesId);
        if (number == 0){
            renderJson(R.error("回款已删除"));
            return;
        }
        renderJson(R.ok().put("data",crmReceivablesService.queryById(receivablesId)));
    }
    /**
     * @author zxy
     * 根据回款id删除
     */
    @Permissions("crm:receivables:delete")
    @NotNullValidate(value = "receivablesIds",message = "回款id不能为空")
    public void  deleteByIds(@Para("receivablesIds") String receivablesIds){
        renderJson(crmReceivablesService.deleteByIds(receivablesIds));
    }
    /**
     * 根据条件查询回款
     * @author zxy
     */
    public void queryList(@Para("") CrmReceivables receivables){
        renderJson(R.ok().put("data",crmReceivablesService.queryList(receivables)));
    }

    /**
     * 回款转移
     */
    public void transfer(@Para("receivablesIds")String receivablesIds, @Para("newOwnerUserId") Long newOwnerUserId){
        renderJson(crmReceivablesService.transfer(receivablesIds,newOwnerUserId));
    }

    /**
     * @author wyq
     * 附件列表
     */
    public void queryFileList(@Para("receivablesId") Integer receivablesId) {
        renderJson(crmReceivablesService.queryFileList(receivablesId));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public void num(@Para("receivablesId") Integer receivablesId) {
        renderJson(crmReceivablesService.num(receivablesId));
    }

    /**
     * @author wyq
     * 批量导出合同
     */
    @Permissions("crm:receivables:excelexport")
    public void batchExportExcel(BasePageRequest basePageRequest) {
        JSONObject jsonObject=basePageRequest.getJsonObject();
        String ids=jsonObject.getString("ids");
        JSONObject data =new JSONObject();
        data.fluentPut("receivablesExport",new JSONObject().fluentPut("name","receivables_id").fluentPut("condition","in").fluentPut("value", ids));
        jsonObject.fluentPut("data",data).fluentPut("search","").fluentPut("type",7).fluentPut("excel",true);
        basePageRequest.setJsonObject(jsonObject);
        JSONObject resultData = (JSONObject)crmReceivablesService.queryPageList(basePageRequest,true).get("data");
        List<Record> recordList = resultData.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_RECEIVABLES, recordList.size());
        export(recordList);
        renderNull();
    }

    /**
     * @author wyq
     * 导出全部回款
     */
    @Permissions("crm:receivables:excelexport")
    public void allExportExcel(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        jsonObject.fluentPut("excel", "yes").fluentPut("type", "7");
        JSONObject data = (JSONObject) crmReceivablesService.queryPageList(basePageRequest, true).get("data");
        List<Record> recordList = data.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_RECEIVABLES, recordList.size());
        export(recordList);
        renderNull();
    }

    private void export(List<Record> recordList) {
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.CRM_RECEIVABLES.getType()));
            headList.forEach(head -> writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")), head.getStr("name")));
            writer.merge(headList.size() - 1, "回款信息");
            HttpServletResponse response = getResponse();
            List<Map<String, Object>> list = new ArrayList<>();
            if (recordList.size() == 0) {
                Record record = new Record();
                headList.forEach(head -> record.set(StrUtil.toUnderlineCase(head.getStr("fieldName")), ""));
                list.add(record.getColumns());
            }
            recordList.forEach(record -> {
                String checkStatus;
                //0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交
                switch (record.getInt("check_status")) {
                    case 1:
                        checkStatus = "通过";
                        break;
                    case 2:
                        checkStatus = "拒绝";
                        break;
                    case 3:
                        checkStatus = "审核中";
                        break;
                    case 4:
                        checkStatus = "撤回";
                        break;
                    case 5:
                        checkStatus = "未提交";
                        break;
                    case 7:
                        checkStatus = "已删除";
                        break;
                    default:
                        checkStatus = "待审核";
                }
                record.set("check_status", checkStatus);
                list.add(record.getColumns());
            });
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < headList.size(); i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=receivables.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出回款错误：",e);
        }
    }
}
