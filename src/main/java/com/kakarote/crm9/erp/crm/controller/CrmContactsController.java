package com.kakarote.crm9.erp.crm.controller;

import cn.hutool.core.date.DatePattern;
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
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.LoginFormCookie;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.crm.entity.CrmContacts;
import com.kakarote.crm9.erp.crm.service.CrmContactsService;
import com.kakarote.crm9.erp.crm.service.CrmRecordService;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CrmContactsController extends Controller {

    @Inject
    private CrmContactsService crmContactsService;

    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private AdminSceneService adminSceneService;

    /**
     * @author wyq
     * 查看列表页
     */
    @Permissions({"crm:contacts:index"})
    @SysLog(LogEnum.CONTACTS)
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(crmContactsService.queryPageList(basePageRequest,true));
    }



    /**
     * @author wyq
     * 根据id查询联系人
     */
    @Permissions("crm:contacts:read")
    public void queryById(@Para("contactsId") Integer contactsId) {
        Integer number = Db.queryInt("select count(1) from `72crm_crm_contacts` where contacts_id = ?",contactsId);
        if (number == 0){
            renderJson(R.error("联系人已删除"));
            return;
        }
        renderJson(R.ok().put("data", crmContactsService.queryById(contactsId)));
    }

    /**
     * @author wyq
     * 根据联系人id查询商机
     */
    public void queryBusiness(BasePageRequest<CrmContacts> basePageRequest) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.CRM_CONTACTS, basePageRequest.getData().getContactsId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmContactsService.queryBusiness(basePageRequest));
    }

    /**
     * @author wyq
     * 联系人关联商机
     */
    public void relateBusiness(@Para("contactsId") Integer contactsId, @Para("businessIds") String businessIds) {
        renderJson(crmContactsService.relateBusiness(contactsId, businessIds));
    }

    /**
     * @author wyq
     * 联系人解除关联商机
     */
    public void unrelateBusiness(@Para("contactsId") Integer contactsId, @Para("businessIds") String businessIds) {
        renderJson(crmContactsService.unrelateBusiness(contactsId, businessIds));
    }

    /**
     * @author wyq
     * 新建或更新联系人
     */
    @Permissions({"crm:contacts:save", "crm:contacts:update"})
    public void addOrUpdate() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(crmContactsService.addOrUpdate(jsonObject));
    }

    /**
     * @author wyq
     * 根据id删除联系人
     */
    @Permissions("crm:contacts:delete")
    public void deleteByIds(@Para("contactsIds") String contactsIds) {
        renderJson(crmContactsService.deleteByIds(contactsIds));
    }

    /**
     * @author wyq
     * 联系人转移
     */
    @Permissions("crm:contacts:transfer")
    @NotNullValidate(value = "contactsIds", message = "联系人id不能为空")
    @NotNullValidate(value = "newOwnerUserId", message = "新负责人不能为空")
    public void transfer(@Para("") CrmContacts crmContacts) {
        renderJson(crmContactsService.transfer(crmContacts));
    }



    /**
     * @author wyq
     * 批量导出联系人
     */
    @Permissions("crm:contacts:excelexport")
    public void batchExportExcel(BasePageRequest basePageRequest){
        JSONObject jsonObject=basePageRequest.getJsonObject();
        String ids=jsonObject.getString("ids");
        JSONObject data =new JSONObject();
        data.fluentPut("contactsExport",new JSONObject().fluentPut("name","contacts_id").fluentPut("condition","in").fluentPut("value", ids));
        jsonObject.fluentPut("data",data).fluentPut("search","").fluentPut("type",3).fluentPut("excel",true);
        basePageRequest.setJsonObject(jsonObject);
        JSONObject resultData = (JSONObject)crmContactsService.queryPageList(basePageRequest,true).get("data");
        List<Record> recordList = resultData.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_CONTACTS, recordList.size());
        export(recordList);
        renderNull();
    }

    /**
     * @author wyq
     * 导出全部联系人
     */
    @Permissions("crm:contacts:excelexport")
    public void allExportExcel(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        jsonObject.fluentPut("excel", "yes").fluentPut("type", "3");
        JSONObject data = (JSONObject)crmContactsService.queryPageList(basePageRequest,true).get("data");
        List<Record> recordList = data.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_CONTACTS, recordList.size());
        export(recordList);
        renderNull();
    }

    private void export(List<Record> recordList){
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.CRM_CONTACTS.getType()));
            headList.forEach(head -> writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")), head.getStr("name")));
            writer.merge(headList.size() - 1, "联系人信息");
            HttpServletResponse response = getResponse();
            List<Map<String, Object>> list = new ArrayList<>();
            if (recordList.size() == 0) {
                Record record = new Record();
                headList.forEach(head -> record.set(StrUtil.toUnderlineCase(head.getStr("fieldName")), ""));
                list.add(record.getColumns());
            }
            recordList.forEach(record -> list.add(record.getColumns()));
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
            response.setHeader("Content-Disposition", "attachment;filename=contacts.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出联系人错误：",e);
        }
    }

    /**
     * @author wyq
     * 获取联系人导入模板
     */
    @LoginFormCookie
    public void downloadExcel() {
        List<Record> recordList = adminFieldService.queryAddField(CrmEnum.CRM_CONTACTS);
        recordList.removeIf(record -> "file".equals(record.getStr("formType")) || "checkbox".equals(record.getStr("formType")) || "user".equals(record.getStr("formType")) || "structure".equals(record.getStr("formType")));
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("联系人导入表");
        sheet.setDefaultRowHeight((short)400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < recordList.size(); i++) {
            if(Objects.equals(recordList.get(i).getInt("type"), FormTypeEnum.DATE.getType())){
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                sheet.setDefaultColumnStyle(i,dateStyle);
            }else if(Objects.equals(recordList.get(i).getInt("type"), FormTypeEnum.DATETIME.getType())){
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATETIME_PATTERN));
                sheet.setDefaultColumnStyle(i,dateStyle);
            }else {
                sheet.setDefaultColumnStyle(i,textStyle);
            }
            sheet.setColumnWidth(i,20*256);
        }
        HSSFRow titleRow = sheet.createRow(0);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)16);
        cellStyle.setFont(font);
        titleRow.createCell(0).setCellValue("联系人导入模板(*)为必填项");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0,0 , 0, recordList.size()-1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < recordList.size(); i++) {
                Record record = recordList.get(i);
                String[] setting = record.get("setting");
                // 在第一行第一个单元格，插入选项
                HSSFCell cell = row.createCell(i);
                // 普通写入操作
                if (record.getInt("is_null") == 1) {
                    cell.setCellValue(record.getStr("name") + "(*)");
                } else {
                    cell.setCellValue(record.getStr("name"));
                }
                if (setting.length != 0) {
                    String fieldName = "_" + record.getStr("field_name");
                    HSSFSheet hidden = wb.createSheet(fieldName);
                    HSSFCell sheetCell = null;
                    for (int j = 0, length = setting.length; j < length; j++)
                    {
                        String name = setting[j];
                        HSSFRow sheetRow = hidden.createRow(j);
                        sheetCell = sheetRow.createCell(0);
                        sheetCell.setCellValue(name);
                    }
                    Name namedCell = wb.createName();
                    namedCell.setNameName(fieldName);
                    namedCell.setRefersToFormula(fieldName+"!$A$1:$A$"+setting.length);
                    CellRangeAddressList regions = new CellRangeAddressList(2, Integer.MAX_VALUE, i, i);
                    DVConstraint constraint = DVConstraint.createFormulaListConstraint(fieldName);
                    HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                    wb.setSheetHidden(wb.getSheetIndex(hidden),true);
                    sheet.addValidationData(dataValidation);
                }
            }
            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=contacts_import.xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            Log.getLog(getClass()).error("error", e);
        } finally {
            try {
                wb.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        renderNull();
    }

    /**
     * @author wyq
     * 联系人导入
     */
    @Permissions("crm:contacts:excelimport")
    @NotNullValidate(value = "ownerUserId", message = "请选择负责人")
    public void uploadExcel(@Para("file") UploadFile file,@Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.CRM_CONTACTS, file.getFile().getAbsolutePath(), ownerUserId, null, repeatHandling);
        renderJson(R.ok().put("data",messageId));
    }

    /**
     * @author wyq
     * 附件列表
     */
    public void queryFileList(@Para("contactsId") Integer contactsId) {
        renderJson(crmContactsService.queryFileList(contactsId));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public void num(@Para("contactsId") Integer contactsId) {
        renderJson(crmContactsService.num(contactsId));
    }

    /**
     * 标星（关注）
     */
    public void star(@Para("contactsId") Integer contactsId) {
        renderJson(crmContactsService.star(contactsId));
    }
}
