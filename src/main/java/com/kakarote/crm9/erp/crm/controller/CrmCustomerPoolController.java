package com.kakarote.crm9.erp.crm.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.common.AdminModelEnum;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.service.CrmCustomerPoolService;
import com.kakarote.crm9.erp.crm.service.CrmCustomerService;
import com.kakarote.crm9.erp.crm.service.CrmRecordService;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.AdminLogUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrmCustomerPoolController extends Controller {

    @Inject
    private CrmCustomerPoolService customerPoolService;

    @Inject
    private CrmCustomerService crmCustomerService;


    /**
     * @author wyq
     * 查看公海列表页
     */
    @Permissions({"crm:pool:index"})
    @SysLog(LogEnum.CUSTOMER_POOL)
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(customerPoolService.queryPoolPageList(basePageRequest));
    }

    /**
     * @author wyq
     * 公海全部导出
     */
    @Permissions("crm:pool:excelexport")
    public void allExportExcel(BasePageRequest basePageRequest) throws IOException {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer poolId = jsonObject.getInteger("poolId");
        jsonObject.fluentPut("excel", "yes").fluentPut("type", 9);
        if (AuthUtil.isPoolAdmin(poolId)) {
            renderJson(R.error("无权导出"));
            return;
        }
        JSONObject data = (JSONObject)customerPoolService.queryPoolPageList(basePageRequest).get("data");
        List<Record> recordList = data.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_CUSTOMER, recordList.size());
        export(recordList, poolId);
        renderNull();
    }

    /**
     * @author wyq
     * 公海批量导出
     */
    @Permissions("crm:pool:excelexport")
    public void batchExportExcel(BasePageRequest basePageRequest) throws IOException {
        JSONObject jsonObject=basePageRequest.getJsonObject();
        Integer poolId = jsonObject.getInteger("poolId");
        if (AuthUtil.isPoolAdmin(poolId)) {
            renderJson(R.error("无权导出"));
            return;
        }
        String ids=jsonObject.getString("ids");
        JSONObject data =new JSONObject();
        data.fluentPut("poolExport",new JSONObject().fluentPut("name","customer_id").fluentPut("condition","in").fluentPut("value", ids));
        jsonObject.fluentPut("data",data).fluentPut("search","").fluentPut("type",9).fluentPut("excel",true);
        basePageRequest.setJsonObject(jsonObject);
        JSONObject resultData = (JSONObject) customerPoolService.queryPoolPageList(basePageRequest).get("data");
        List<Record> recordList = resultData.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_CUSTOMER, recordList.size());
        export(recordList, poolId);
        renderNull();
    }


    private void export(List<Record> recordList, Integer poolId) throws IOException {
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<Record> headList = Aop.get(AdminFieldService.class).queryPoolListHead(poolId);
            headList.forEach(head -> writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("field_name")), head.getStr("name")));
            writer.merge(headList.size() - 1, "客户信息");
            HttpServletResponse response = getResponse();
            List<Map<String, Object>> list = new ArrayList<>();
            if (recordList.size() == 0) {
                Record record = new Record();
                headList.forEach(head -> record.set(StrUtil.toUnderlineCase(head.getStr("fieldName")), ""));
                list.add(record.getColumns());
            }
            recordList.forEach(record -> {
                record.set("deal_status", record.getInt("deal_status").equals(1) ? "已成交" : "未成交");
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
            response.setHeader("Content-Disposition", "attachment;filename=customer.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出公海错误：",e);
        }
    }

    /**
     * @author wyq
     * 公海导入客户
     */
    @Permissions("crm:customer:excelimport")
    @NotNullValidate(value = "poolId", message = "请选择公海")
    public void uploadExcel(@Para("file") UploadFile file, @Para("poolId") Integer poolId, @Para("repeatHandling") Integer repeatHandling) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.CRM_CUSTOMER, file.getFile().getAbsolutePath(), null, poolId, repeatHandling);
        renderJson(R.ok().put("data", messageId));
    }

    /**
     * 根据id删除公海数据
     */
    @Permissions("crm:pool:delete")
    @NotNullValidate(value = "customerIds", message = "客户id不能为空")
    public void deleteByIds(@Para("poolId") Integer poolId, @Para("customerIds") String customerIds) {
        renderJson(customerPoolService.deleteByIds(poolId, customerIds));
    }

    /**
     * 后台公海设置列表页
     */
    public void queryPoolSettingList(BasePageRequest basePageRequest) {
        renderJson(customerPoolService.queryPoolSettingList(basePageRequest));
    }

    /**
     * 公海客户转移
     */
    public void transfer(@Para("prePoolId") Integer prePoolId, @Para("postPoolId") Integer postPoolId) {
        renderJson(customerPoolService.transfer(prePoolId, postPoolId));
    }

    /**
     * 后台公海选择列表
     */
    public void queryPoolNameList() {
        renderJson(customerPoolService.queryPoolNameList());
    }

    /**
     * 前台公海选择列表
     */
    public void queryPoolNameListByAuth() {
        renderJson(customerPoolService.queryPoolNameListByAuth());
    }

    /**
     * 公海启停
     */
    public void changeStatus(@Para("poolId") Integer poolId, @Para("status") Integer status) {
        renderJson(customerPoolService.changeStatus(poolId, status));
    }

    /**
     * 后台查询公海设置详情
     */
    public void queryPoolById(@Para("poolId") Integer poolId) {
        renderJson(customerPoolService.queryPoolById(poolId));
    }

    /**
     * 获取公海默认字段
     */
    public void queryPoolField() {
        renderJson(customerPoolService.queryPoolField());
    }

    /**
     * 获取客户级别选项
     */
    public void queryCustomerLevel() {
        renderJson(customerPoolService.queryCustomerLevel());
    }

    /**
     * 设置公海
     */
    public void setCustomerPool(@RequestBody JSONObject jsonObject) {
        String ipAddress = ServletUtil.getClientIP(getRequest());
        renderJson(customerPoolService.setCustomerPool(jsonObject,ipAddress));
    }

    /**
     * 删除公海
     */
    public void deleteCustomerPool(@Para("poolId") Integer poolId) {
        renderJson(customerPoolService.deleteCustomerPool(poolId));
    }
}
