package com.kakarote.crm9.erp.crm.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.*;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.crm.entity.CrmBusiness;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmCustomer;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerSetting;
import com.kakarote.crm9.erp.crm.service.*;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;
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

public class CrmCustomerController extends Controller {
    @Inject
    private CrmCustomerService crmCustomerService;

    @Inject
    private CrmContactsService crmContactsService;//联系人

    @Inject
    private CrmBusinessService crmBusinessService;//商机

    @Inject
    private CrmContractService crmContractService;//合同

    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private AdminSceneService adminSceneService;

    @Inject
    private CrmCustomerPoolService crmCustomerPoolService;

    /**
     * @author wyq
     * 查看列表页
     */
    @Permissions({"crm:customer:index"})
    @SysLog(LogEnum.CUSTOMER)
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(crmCustomerService.queryPageList(basePageRequest,true));
    }

    /**
     * @author wyq
     * 新增或更新客户
     */
    @Permissions({"crm:customer:save", "crm:customer:update"})
    public void addOrUpdate() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(crmCustomerService.addOrUpdate(jsonObject, "noImport"));
    }

    /**
     * @author wyq
     * 根据客户id查询
     */
    @Permissions("crm:customer:read")
    @NotNullValidate(value = "customerId", message = "客户id不能为空")
    public void queryById(@Para("customerId") Integer customerId, @Para("poolId") Integer poolId) {
        Integer number = Db.queryInt("select count(1) from `72crm_crm_customer` where customer_id = ? and status != 3",customerId);
        if (number == 0){
            renderJson(R.error("客户已删除"));
            return;
        }
        boolean auth = AuthUtil.isPoolAuth(customerId);
        if (auth) {
            renderJson(R.ok().put("data",new Record().set("dataAuth",0)));
            return;
        }
        renderJson(R.ok().put("data", crmCustomerService.queryById(customerId, poolId)));
    }

    /**
     * @author wyq
     * 根据客户id查询联系人
     */
    public void queryContacts(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryContacts(basePageRequest));
    }

    /**
     * @author wyq
     * 根据id删除客户
     */
    @Permissions("crm:customer:delete")
    @NotNullValidate(value = "customerIds", message = "客户id不能为空")
    public void deleteByIds(@Para("customerIds") String customerIds) {
        renderJson(crmCustomerService.deleteByIds(customerIds));
    }

    /**
     * @author wyq
     * 根据客户id查找商机
     */
    public void queryBusiness(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryBusiness(basePageRequest));
    }

    /**
     * @author wyq
     * 根据客户id查询合同
     */
    public void queryContract(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryContract(basePageRequest));
    }

    /**
     * @author wyq
     * 根据客户id查询回款计划
     */
    public void queryReceivablesPlan(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryReceivablesPlan(basePageRequest));
    }

    /**
     * @author zxy
     * 根据客户id查询回款
     */
    public void queryReceivables(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryReceivables(basePageRequest));
    }

    /**
     * 根据客户id查询回访
     */
    public void queryReturnVisit(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryReturnVisit(basePageRequest));
    }

    /**
     * 根据客户id查询发票
     */
    public void queryInvoice(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryInvoice(basePageRequest));
    }

    /**
     * 根据客户id查询发票抬头信息
     */
    public void queryInvoiceInfo(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryInvoiceInfo(basePageRequest));
    }

    public void queryCallRecord(BasePageRequest<CrmCustomer> basePageRequest) {
        boolean auth = AuthUtil.isPoolAuth(basePageRequest.getData().getCustomerId());
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(crmCustomerService.queryCallRecord(basePageRequest));
    }

    /**
     * @author wyq
     * 客户锁定
     */
    @Permissions("crm:customer:lock")
    @NotNullValidate(value = "ids", message = "客户id不能为空")
    @NotNullValidate(value = "status", message = "锁定状态不能为空")
    public void lock(@Para("") CrmCustomer crmCustomer) {
        renderJson(crmCustomerService.lock(crmCustomer));
    }

    /**
     * @author wyq
     * 设置成交状态
     */
    public void setDealStatus(@Para("ids")String ids,@Para("dealStatus")Integer dealStatus){
        renderJson(crmCustomerService.setDealStatus(ids,dealStatus));
    }

    /**
     * 客户转移
     *
     * @author wyq
     */
    @Permissions("crm:customer:transfer")
    @NotNullValidate(value = "customerIds", message = "客户id不能为空")
    @NotNullValidate(value = "newOwnerUserId", message = "新负责人不能为空")
    @NotNullValidate(value = "transferType", message = "移除方式不能为空")
    @Before(Tx.class)
    public void transfer(@Para("") CrmCustomer crmCustomer) {
        String[] customerIdsArr = crmCustomer.getCustomerIds().split(",");
        if (!crmCustomerService.isMaxOwner(crmCustomer.getNewOwnerUserId(), customerIdsArr)) {
            renderJson(R.error("该员工拥有客户数已达上限"));
            return;
        }
        Long newOwnerUserId = crmCustomer.getNewOwnerUserId();
        String changeType = crmCustomer.getChangeType();
        for (Integer customerId : TagUtil.toSet(crmCustomer.getCustomerIds())) {
            crmCustomer.setCustomerId(customerId);
            R result = crmCustomerService.updateOwnerUserId(crmCustomer);
            Aop.get(ActionRecordUtil.class).addConversionRecord(customerId, CrmEnum.CRM_CUSTOMER, crmCustomer.getNewOwnerUserId());
            if (result.get("code").equals(500)) {
                renderJson(result);
                return;
            }
            if (StrUtil.isNotEmpty(changeType)) {
                String[] changeTypeArr = changeType.split(",");
                for (String type : changeTypeArr) {
                    //更新联系人负责人
                    if ("1".equals(type)) {
                        renderJson(crmContactsService.updateOwnerUserId(customerId, newOwnerUserId));
                    }
                    //更新商机负责人
                    if ("2".equals(type)) {
                        renderJson(crmBusinessService.updateOwnerUserId(crmCustomer));
                    }
                    //更新合同负责人
                    if ("3".equals(type)) {
                        renderJson(crmContractService.updateOwnerUserId(crmCustomer));
                    }
                }
            }
        }
        renderJson(R.ok());
    }

    /**
     * @author wyq
     * 查询团队成员
     */
    @NotNullValidate(value = "customerId", message = "客户id不能为空")
    public void getMembers(@Para("customerId") Integer customerId) {
        boolean auth = AuthUtil.isPoolAuth(customerId);
        if (auth) {
            renderJson(R.noAuth());
            return;
        }
        renderJson(R.ok().put("data", crmCustomerService.getMembers(customerId)));
    }

    /**
     * @author wyq
     * 添加团队成员
     */
    @Permissions("crm:customer:teamsave")
    @NotNullValidate(value = "ids", message = "客户id不能为空")
    @NotNullValidate(value = "memberIds", message = "成员id不能为空")
    @NotNullValidate(value = "power", message = "读写权限不能为空")
    @Before(Tx.class)
    public void addMembers(@Para("") CrmCustomer crmCustomer) {
        String changeType = crmCustomer.getChangeType();
        if (StrUtil.isNotEmpty(changeType)) {
            String[] changeTypeArr = changeType.split(",");
            for (String type : changeTypeArr) {
                //更新商机
                if ("2".equals(type)) {
                    CrmBusiness crmBusiness = new CrmBusiness();
                    crmBusiness.setIds(crmCustomerService.getBusinessIdsByCustomerIds(crmCustomer.getIds()));
                    crmBusiness.setMemberIds(crmCustomer.getMemberIds());
                    crmBusiness.setPower(crmCustomer.getPower());
                    crmBusiness.setTransferType(crmCustomer.getTransferType());
                    crmBusinessService.addMember(crmBusiness);
                }
                //更新合同
                else if ("3".equals(type)) {
                    CrmContract crmContract = new CrmContract();
                    crmContract.setIds(crmCustomerService.getContractIdsByCustomerIds(crmCustomer.getIds()));
                    crmContract.setMemberIds(crmCustomer.getMemberIds());
                    crmContract.setPower(crmCustomer.getPower());
                    crmContract.setTransferType(crmCustomer.getTransferType());
                    crmContractService.addMember(crmContract);
                }
            }
        }
        renderJson(crmCustomerService.addMember(crmCustomer));
    }

    /**
     * @author wyq
     * 编辑团队成员
     */
    @Permissions("crm:customer:teamsave")
    @NotNullValidate(value = "ids", message = "商机id不能为空")
    @NotNullValidate(value = "memberIds", message = "成员id不能为空")
    @NotNullValidate(value = "power", message = "读写权限不能为空")
    public void updateMembers(@Para("") CrmCustomer crmCustomer) {
        renderJson(crmCustomerService.addMember(crmCustomer));
    }

    /**
     * @author wyq
     * 删除团队成员
     */
    @Permissions("crm:customer:teamsave")
    @NotNullValidate(value = "ids", message = "客户id不能为空")
    @NotNullValidate(value = "memberIds", message = "成员id不能为空")
    public void deleteMembers(@Para("") CrmCustomer crmCustomer) {
        renderJson(crmCustomerService.deleteMembers(crmCustomer));
    }

    /**
     * @author wyq
     * 自助退出团队成员
     */
    @NotNullValidate(value = "customerId", message = "客户id不能为空")
    public void exitTeam(@Para("customerId") Integer customerId) {
        renderJson(crmCustomerService.exitTeam(customerId));
    }

    /**
     * @author zxy
     * 客户保护规则设置
     */
    @Permissions("manage:crm:pool")
    @NotNullValidate(value = "followupDay", message = "跟进天数不能为空")
    @NotNullValidate(value = "dealDay", message = "成交天数不能为空")
    @NotNullValidate(value = "type", message = "启用状态不能为空")
    public void updateRulesSetting() {
        //跟进天数
        Integer followupDay = getParaToInt("followupDay");
        //成交天数
        Integer dealDay = getParaToInt("dealDay");
        //启用状态
        Integer type = getParaToInt("type");
        Integer remindConfig = getParaToInt("putInPoolRemindConfig");
        Integer remindDay = getParaToInt("putInPoolRemindDays");
        renderJson(crmCustomerService.updateRulesSetting(dealDay, followupDay, type, remindDay,remindConfig));
    }

    /**
     * @author zxy
     * 获取客户保护规则设置
     */
    public void getRulesSetting() {
        renderJson(crmCustomerService.getRulesSetting());
    }

    /**
     * @author wyq
     * 客户批量导出
     */
    @Permissions("crm:customer:excelexport")
    public void batchExportExcel(BasePageRequest basePageRequest){
        JSONObject jsonObject=basePageRequest.getJsonObject();
        String ids=jsonObject.getString("ids");
        JSONObject data =new JSONObject();
        data.fluentPut("customerExport",new JSONObject().fluentPut("name","customer_id").fluentPut("condition","in").fluentPut("value", ids));
        jsonObject.fluentPut("data",data).fluentPut("search","").fluentPut("type",2).fluentPut("excel",true);
        basePageRequest.setJsonObject(jsonObject);
        JSONObject resultData = (JSONObject)crmCustomerService.queryPageList(basePageRequest,true).get("data");
        List<Record> recordList = resultData.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_CUSTOMER, recordList.size());
        export(recordList);
        renderNull();
    }

    /**
     * @author wyq
     * 全部导出
     */
    @Permissions("crm:customer:excelexport")
    public void allExportExcel(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        jsonObject.fluentPut("excel", "yes").fluentPut("type", 2);
        JSONObject data = (JSONObject)crmCustomerService.queryPageList(basePageRequest,true).get("data");
        List<Record> recordList = data.getJSONArray("list").toJavaList(Record.class);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.CRM_CUSTOMER, recordList.size());
        export(recordList);
        renderNull();
    }

    private void export(List<Record> recordList){
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.CRM_CUSTOMER.getType()));
            headList.forEach(head -> {
                if(!Objects.equals(head.getStr("fieldName"),head.getStr("name"))){
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")),head.getStr("name"));
                }else {
                    writer.addHeaderAlias(head.getStr("fieldName"),head.getStr("name"));
                }
            });
            writer.merge(headList.size()-1, "客户信息");
            HttpServletResponse response = getResponse();
            List<Map<String, Object>> list = new ArrayList<>();
            if (recordList.size() == 0){
                Record record = new Record();
                headList.forEach(head -> record.set(StrUtil.toUnderlineCase(head.getStr("fieldName")), ""));
                list.add(record.getColumns());
            }
            recordList.forEach(record -> {
                record.set("deal_status",record.getInt("deal_status").equals(1)?"已成交":"未成交");
                record.set("status",record.getInt("status").equals(1)?"未锁定":"已锁定");
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
            Log.getLog(getClass()).error("导出客户错误：",e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
    }

    /**
     * 客户放入公海
     *
     * @author zxy
     */
    @Permissions("crm:customer:putinpool")
    public void updateCustomerByIds() {
        String ids = get("ids");
        Integer poolId = getParaToInt("poolId");
        renderJson(crmCustomerService.updateCustomerByIds(ids, poolId));
    }

    /**
     * 公海分配客户
     *
     * @author zxy
     */
    @Permissions("crm:pool:distribute")
    public void distributeByIds() {
        Integer poolId = getInt("poolId");
        String ids = get("ids");
        Long userId = getLong("userId");
        renderJson(crmCustomerService.getCustomersByIds(poolId, ids, userId, 1));
    }

    /**
     * 公海领取客户
     *
     * @author zxy
     */
    @Permissions("crm:pool:receive")
    @NotNullValidate(value = "poolId", message = "公海id不能为空")
    public void receiveByIds() {
        Integer poolId = getInt("poolId");
        String ids = get("ids");
        Long userId = getLong("userId");
        renderJson(crmCustomerService.getCustomersByIds(poolId, ids, userId, 2));
    }

    /**
     * @author wyq
     * 获取导入模板
     */
    @LoginFormCookie
    public void downloadExcel() {
        List<Record> recordList = adminFieldService.queryAddField(CrmEnum.CRM_CUSTOMER);
        recordList.removeIf(record -> "file".equals(record.getStr("formType")) || "checkbox".equals(record.getStr("formType")) || "user".equals(record.getStr("formType")) || "structure".equals(record.getStr("formType")));
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("客户导入表");
        sheet.setDefaultRowHeight((short)400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < recordList.size() + 2; i++) {
            if(recordList.size()>i&&Objects.equals(recordList.get(i).getInt("type"), FormTypeEnum.DATE.getType())){
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                sheet.setDefaultColumnStyle(i,dateStyle);
            }else if(recordList.size()>i&&Objects.equals(recordList.get(i).getInt("type"), FormTypeEnum.DATETIME.getType())){
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
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        titleRow.createCell(0).setCellValue("客户导入模板(*)为必填项");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, recordList.size() + 1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < recordList.size(); i++) {
                Record record = recordList.get(i);
                final String fieldName = "_" + record.getStr("field_name");
                //省市区需特殊处理
                if ("_map_address".equals(fieldName)) {
                    HSSFCell cell1 = row.createCell(i);
                    cell1.setCellValue("省");
                    HSSFCell cell2 = row.createCell(i + 1);
                    cell2.setCellValue("市");
                    HSSFCell cell3 = row.createCell(i + 2);
                    cell3.setCellValue("区");
                    HSSFSheet hideSheet = wb.createSheet(fieldName);
                    wb.setSheetHidden(wb.getSheetIndex(hideSheet), true);
                    int rowId = 0;
                    // 设置第一行，存省的信息
                    Row provinceRow = hideSheet.createRow(rowId++);
                    provinceRow.createCell(0).setCellValue("省列表");
                    List<String> provinceList = Db.query("select city_name from 72crm_crm_area where parent_id = ?", 100000);
                    for (int x = 0; x < provinceList.size(); x++) {
                        Cell provinceCell = provinceRow.createCell(x + 1);
                        provinceCell.setCellValue(provinceList.get(x));
                    }
                    // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
                    Map<String, List<String>> areaMap = CrmExcelUtil.getAreaMap();
                    for (String key : areaMap.keySet()) {
                        List<String> son = areaMap.get(key);
                        Row subRow = hideSheet.createRow(rowId++);
                        subRow.createCell(0).setCellValue(key);
                        for (int x = 0; x < son.size(); x++) {
                            Cell cell = subRow.createCell(x + 1);
                            cell.setCellValue(son.get(x));
                        }
                        // 添加名称管理器
                        String range = CrmExcelUtil.getRange(1, rowId, son.size());
                        Name name = wb.createName();
                        // key不可重复
                        name.setNameName(key);
                        String formula = fieldName+"!" + range;
                        name.setRefersToFormula(formula);
                    }
                    // 省级下拉框
                    CellRangeAddressList provRangeAddressList = new CellRangeAddressList(2, Integer.MAX_VALUE, i, i);
                    String[] arr = provinceList.toArray(new String[]{});
                    DVConstraint provConstraint = DVConstraint.createExplicitListConstraint(arr);
                    HSSFDataValidation provinceDataValidation = new HSSFDataValidation(provRangeAddressList, provConstraint);
                    provinceDataValidation.createErrorBox("error", "请选择正确的省份");
                    sheet.addValidationData(provinceDataValidation);
                    //市 区下拉框
                    for (int x = 2; x < 10000; x++) {
                        CrmExcelUtil.setDataValidation(CrmExcelUtil.getCorrespondingLabel(i + 1), sheet, x, i + 1);
                        CrmExcelUtil.setDataValidation(CrmExcelUtil.getCorrespondingLabel(i + 2), sheet, x, i + 2);
                    }
                } else {
                    HSSFCell cell = row.createCell(i);
                    if (record.getInt("is_null") == 1) {
                        cell.setCellValue(record.getStr("name") + "(*)");
                    } else {
                        cell.setCellValue(record.getStr("name"));
                    }
                    String[] setting = record.get("setting");
                    if (setting != null && setting.length != 0) {
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
            }
            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer_import.xls");
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
     * 导入客户
     */
    @Permissions("crm:customer:excelimport")
    @NotNullValidate(value = "ownerUserId", message = "请选择负责人")
    public void uploadExcel(@Para("file") UploadFile file, @Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.CRM_CUSTOMER, file.getFile().getAbsolutePath(), ownerUserId, null, repeatHandling,null);
        renderJson(R.ok().put("data",messageId));
    }

    @NotNullValidate(value = "customerNum", message = "请填写客户数量",type = HttpEnum.JSON)
    public void customerSetting(@RequestBody CrmCustomerSetting customerSetting){
        String ipAddress = ServletUtil.getClientIP(getRequest());
        renderJson(crmCustomerService.customerSetting(customerSetting,ipAddress));
    }
    @NotNullValidate("type")
    public void queryCustomerSetting(BasePageRequest<CrmCustomerSetting> pageRequest){
        renderJson(R.ok().put("data",crmCustomerService.queryCustomerSetting(pageRequest)));
    }

    @NotNullValidate("settingId")
    public void editCustomerSetting(@Para("settingId") Integer settingId){
        renderJson(crmCustomerService.queryEditCustomerSetting(settingId));
    }

    @NotNullValidate("settingId")
    public void deleteCustomerSetting(@Para("settingId") Integer settingId){
        renderJson(crmCustomerService.deleteCustomerSetting(settingId));
    }

    /**
     * 设置首要联系人
     * @param customerId 客户ID
     * @param contactsId 联系人ID
     */
    @NotNullValidate("customerId")
    @NotNullValidate("contactsId")
    public void setContacts(@Para("customerId") Integer customerId,@Para("contactsId") Integer contactsId){
        renderJson(crmCustomerService.setContacts(customerId,contactsId));
    }

    /**
     * 数据查重
     * @param name
     * @param phone
     */
    public void dataCheck(@Para("name")String name,@Para("phone")String phone){
        renderJson(crmCustomerService.dataCheck(name,phone));
    }

    /**
     * @author wyq
     * 附件列表
     */
    public void queryFileList(@Para("customerId") Integer customerId) {
        renderJson(crmCustomerService.queryFileList(customerId));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public void num(@Para("customerId") Integer customerId) {
        renderJson(crmCustomerService.num(customerId));
    }

    /**
     * 附近的客户
     */
    @Permissions("crm:customer:nearbyCustomer")
    public void nearbyCustomer(@Para("lng") String lng, @Para("lat") String lat, @Para("type") Integer type, @Para("radius") Integer radius, @Para("ownerUserId") Long ownerUserId) {
        renderJson(crmCustomerService.nearbyCustomer(lng, lat, type, radius, ownerUserId));
    }

    /**
     * 标星（关注）
     */
    public void star(@Para("customerId") Integer customerId) {
        renderJson(crmCustomerService.star(customerId));
    }
}
