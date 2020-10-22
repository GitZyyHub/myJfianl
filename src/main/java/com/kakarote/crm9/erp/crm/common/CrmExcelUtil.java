package com.kakarote.crm9.erp.crm.common;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbTemplate;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.admin.entity.AdminMessage;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.erp.crm.service.CrmContactsService;
import com.kakarote.crm9.erp.crm.service.CrmCustomerService;
import com.kakarote.crm9.erp.crm.service.CrmLeadsService;
import com.kakarote.crm9.erp.crm.service.CrmProductService;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.erp.work.service.*;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.ToolUtils;
import com.mysql.jdbc.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class CrmExcelUtil {
    //超出的线程直接抛出异常
    private static final ExecutorService threadPool = new ThreadPoolExecutor(1, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2048), new ThreadPoolExecutor.AbortPolicy());
    //暂存导入线程
    private static final ConcurrentHashMap<Long, ExcelImport> threadMap = new ConcurrentHashMap<>(32, 0.5f);

    public CrmExcelUtil() {
    }

    public final Long addWork(CrmEnum crmEnum, String filePath, Long ownerUserId, Integer poolId, Integer repeatHandling, String workId) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateUser(BaseUtil.getUserId());
        adminMessage.setCreateTime(DateUtil.date());
        adminMessage.setRecipientUser(BaseUtil.getUserId());
        adminMessage.setLabel(6);
        switch (crmEnum) {
            case CRM_CUSTOMER:
                adminMessage.setType(AdminMessageEnum.CRM_CUSTOMER_IMPORT.getType());
                break;
            case CRM_LEADS:
                adminMessage.setType(AdminMessageEnum.CRM_LEADS_IMPORT.getType());
                break;
            case CRM_CONTACTS:
                adminMessage.setType(AdminMessageEnum.CRM_CONTACTS_IMPORT.getType());
                break;
            case CRM_PRODUCT:
                adminMessage.setType(AdminMessageEnum.CRM_PRODUCT_IMPORT.getType());
                break;
            case WORK:
                adminMessage.setType(AdminMessageEnum.CRM_PRODUCT_IMPORT.getType());
                break;
            case DEMAND:
                adminMessage.setType(AdminMessageEnum.CRM_DEMAND_IMPORT.getType());
                break;
            case RISK:
                adminMessage.setType(AdminMessageEnum.WORK_RISK_IMPORT.getType());
                break;
            case TASK:
                adminMessage.setType(AdminMessageEnum.WORK_TASK_IMPORT.getType());
                break;
            case RESOURCES:
                adminMessage.setType(AdminMessageEnum.WORK_RESOURCES.getType());
                break;
            case PRODUCT:
                adminMessage.setType(AdminMessageEnum.WORK_RESOURCES.getType());
                break;
            case PRODUCT_FUNCTIONE:
                adminMessage.setType(AdminMessageEnum.WORK_RESOURCES.getType());
                break;
            case WORKCONFIG:
                adminMessage.setType(AdminMessageEnum.WORK_RESOURCES.getType());
                break;
            default:
        }
        if (!adminMessage.save()) {
            return null;
        }
        ExcelImport excelImport = new ExcelImport(crmEnum, filePath, ownerUserId, poolId, adminMessage.getMessageId(), BaseUtil.getUser(), repeatHandling, workId);
        //注意执行顺序
        threadPool.execute(excelImport);
        threadMap.put(adminMessage.getMessageId(), excelImport);
        return adminMessage.getMessageId();
    }

    /**
     * 根据消息ID获取当前导入数量
     *
     * @param messageId 消息ID
     * @return 导入数量
     */
    public static Integer getThreadMapNum(Long messageId) {
        if (messageId == null) {
            return null;
        }
        if (threadMap.containsKey(messageId)) {
            Integer number = threadMap.get(messageId).num;
            if (number > 2) {
                number = number - 2;
            } else {
                number = 0;
            }
            return number;
        }
        return null;
    }

    public static void interrupt(Long messageId) {
        ExcelImport excelImport = threadMap.get(messageId);
        if (excelImport != null) {
            excelImport.interrupted = true;
            AdminMessage adminMessage = AdminMessage.dao.findById(messageId);
            adminMessage.setTitle(excelImport.num - 2 + "");
            adminMessage.setContent(excelImport.errorList.size() - 2 + "");
            adminMessage.update();
        }
    }

    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

    /**
     * offset为主列, 市引用省一列的值，区引用市一列的值
     */
    public static void setDataValidation(String offset, HSSFSheet sheet, int rowNum, int colNum) {
        Integer i = rowNum + 1;
        DVConstraint formula = DVConstraint.createFormulaListConstraint("INDIRECT($" + offset + "$" + i + ")");
        CellRangeAddressList rangeAddressList = new CellRangeAddressList(rowNum, rowNum, colNum, colNum);
        HSSFDataValidation cacse = new HSSFDataValidation(rangeAddressList, formula);
        cacse.createErrorBox("error", "请选择正确的地区");
        sheet.addValidationData(cacse);
    }

    /**
     * 判断数字和excel字符列对应关系
     *
     * @param columnNo 列
     * @return 对应列字符
     */
    public static String getCorrespondingLabel(int columnNo) {
        if (columnNo < 1 || columnNo > 16384) {
            throw new IllegalArgumentException();
        }
        String[] sources = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"
                , "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder sb = new StringBuilder(5);
        //求最右边的字母
        int remainder = columnNo % 26;
        //说明(num_n-1)=26，第26个字母是Z
        if (remainder == 0) {
            sb.append("Z");
            //因为接下来W-(num_n-1)也就是columnNo-remainder,所以需要把remainder赋值回26
            remainder = 26;
        } else {  //如果最右边字母不是Z的话，就去sources数组相应的位置取字母，remainder不用变
            sb.append(sources[remainder - 1]);
        }
        //用来判断接下来是否还有其他字母
        columnNo = (columnNo - remainder) / 26 - 1;

        //当前循环是求最后一个字母时（从右往左），(columnNo-remainder)/26就会是0，再减1也就是-1。
        //因此通过判断(columnNo-remainder)/26-1是否大于-1来判断结束
        while (columnNo > -1) {
            remainder = columnNo % 26;
            sb.append(sources[remainder]);
            columnNo = (columnNo - remainder) / 26 - 1;
        }
        //因为是从右往左解析的 所以需要反转
        return sb.reverse().toString();
    }

    /**
     * 获取市区对应关系
     *
     * @return 对应关系
     */
    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> getAreaMap() {
        cn.hutool.json.JSONObject readJSONObject = JSONUtil.readJSONObject(new File(PathKit.getRootClassPath() + "/config/area.json"), Charset.defaultCharset());
        return readJSONObject.toBean(Map.class);
    }

    private class ExcelImport implements Runnable {

        private String filePath;
        private CrmEnum crmEnum;
        private Long ownerUserId;
        private Integer poolId;
        private Integer num = 0;
        private Integer updateNum = 0;
        private Long messageId;
        private List<List<Object>> errorList = new ArrayList<>();
        private List<Record> fieldList;
        private List<Record> fixedFieldList;
        private List<Record> uniqueList = new ArrayList<>();
        private List<Integer> isNullList = new ArrayList<>();
        private boolean templateErr = false;
        private boolean interrupted = false;
        private Integer repeatHandling;
        private Kv kv = new Kv();
        private AdminUser adminUser;
        private String workId;

        ExcelImport(CrmEnum crmEnum, String filePath, Long ownerUserId, Integer poolId, Long messageId, AdminUser adminUser, Integer repeatHandling, String workId) {
            this.crmEnum = crmEnum;
            this.filePath = filePath;
            this.poolId = poolId;
            this.ownerUserId = ownerUserId;
            this.adminUser = adminUser;
            this.messageId = messageId;
            this.repeatHandling = repeatHandling;
            this.workId = workId;
        }

        @Override
        public void run() {
            if (interrupted) {
                return;
            }
            BaseUtil.setUser(adminUser);
            try {
                switch (crmEnum) {
                    case CRM_CUSTOMER:
                    case CRM_CUSTOMER_POOL:
                        customerImport();
                        break;
                    case CRM_LEADS:
                        leadsImport();
                        break;
                    case CRM_CONTACTS:
                        contactsImport();
                        break;
                    case CRM_PRODUCT:
                        productImport();
                        break;
                    case WORK:
                        workImport();
                        break;
                    case DEMAND:
                        demandImport();
                        break;
                    case RISK:
                        riskImport();
                        break;
                    case TASK:
                        taskImport();
                        break;
                    case RESOURCES:
                        resourcesImport();
                        break;
                    case PRODUCT:
                        projectProductImport();
                        break;
                    case PRODUCT_FUNCTIONE:
                        productFunctioneImport();
                        break;
                    case WORKCONFIG:
                        workconfigimport();
                        break;
                    default:
                }
            } catch (Exception e) {
                Log.getLog(getClass()).error("导入数据错误：", e);
            } finally {
                BaseUtil.removeThreadLocal();
                CrmExcelUtil.threadMap.remove(messageId);
            }
            AdminMessage adminMessage = AdminMessage.dao.findById(messageId);
            adminMessage.setTitle(String.valueOf(num - 2));
            adminMessage.setContent((errorList.size() - 2) + "," + updateNum);
            if (errorList.size() > 2) {
                File file = new File(BaseConstant.UPLOAD_PATH + "excel/" + BaseUtil.getDate() + "/" + IdUtil.simpleUUID() + ".xlsx");
                BigExcelWriter writer = ExcelUtil.getBigWriter(file);
                CellStyle textStyle = writer.getWorkbook().createCellStyle();
                DataFormat format = writer.getWorkbook().createDataFormat();
                textStyle.setDataFormat(format.getFormat("@"));
                for (int i = 0; i < errorList.get(1).size(); i++) {
                    writer.setColumnWidth(i, 20);
                    writer.getSheet().setDefaultColumnStyle(i, textStyle);
                }
                writer.merge(errorList.get(1).size() - 1, errorList.remove(0).get(0));
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                // 一次性写出内容，使用默认样式
                writer.write(errorList);
                // 关闭writer，释放内存
                writer.close();
                AdminFile adminFile = new AdminFile();
                adminFile.setName("EXCEL导入");
                adminFile.setCreateUserId(adminUser.getUserId());
                adminFile.setPath(file.getAbsolutePath());
                adminFile.setFilePath("/file/downFile?fileId=" + IdUtil.simpleUUID());
                adminFile.setCreateTime(new Date());
                adminFile.setSize(file.length());
                adminFile.save();
                adminMessage.setTypeId(adminFile.getFileId());
            }
            adminMessage.update();
            FileUtil.del(filePath);
        }


        /**
         * ???
         */
        private void workconfigimport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (rowIndex > 1) {
                    try {
                        WorkConfig workConfig = new WorkConfig();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
                        for (int i = 0; i < rowList.size(); i++) {
                            List<Integer> integers = Arrays.asList(0,1,3);
                            if (integers.contains(i)) {
                                if (StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    rowList.add(0, "必填选项，请添加");
                                    errorList.add(rowList);
                                    return;
                                }
                            } else if (i == 4) {
                                List<String> users = null;
                                try {
                                    users = Arrays.asList(rowList.get(i).toString().trim().split(","));
                                } catch (NullPointerException e) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                List<Integer> userIds = Db.template("admin.user.queryCountByNames", Kv.by("names", users)).query();
                                if (userIds.size() != users.size()) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                String s = userIds.toString();
                                rowList.set(i, s.substring(1, s.length() - 1));
                            }else if(i==1){
                                Record first = Db.findFirst("select work_id,work_num,work_name from 72crm_work where work_num =?", rowList.get(i).toString().trim());
                                workConfig.setAssociatedId(first.getLong("work_id"));
                                workConfig.setAssociatedNum(first.getStr("work_num"));
                                workConfig.setAssociatedName(first.getStr("work_name"));
                            }
                        }
                        workConfig.setConfigItems(rowList.get(0).toString().trim());
                        workConfig.setPlanTime(sdf.parse(rowList.get(3).toString().trim()));
                        workConfig.setMainUserId(Integer.parseInt(rowList.get(4).toString().trim()));
                        workConfig.setVersion(rowList.get(6).toString().trim());
                        workConfig.setVersionDescribe(rowList.get(7).toString().trim());
                        workConfig.setVersionTime(sdf.parse(rowList.get(8).toString().trim()));
                        workConfig.setCreateTime(new Date());
                        workConfig.setCreateUserId(BaseUtil.getUserId());
                        if (workConfig.save()) {
                            updateNum++;
                        }
                    }catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        /**
         * ???
         */
        private void productFunctioneImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (rowIndex > 1) {
                    try {
                        WorkProductFunction workProductFunction = new WorkProductFunction();

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
                        for (int i = 0; i < rowList.size(); i++) {
                            List<Integer> integers = Arrays.asList(0, 1);
                            if (integers.contains(i)) {
                                if (StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    rowList.add(0, "必填选项，请添加");
                                    errorList.add(rowList);
                                    return;
                                }
                            }
                        }
                        //查询对应的产品（work这里代表产品ID）
                        WorkProduct byId = Aop.get(WorkProduct.class).dao().findById(workId);
                        workProductFunction.setProductName(byId.getProductName());
                        workProductFunction.setProductId(byId.getProductId());
                        workProductFunction.setProductVersion(byId.getProductVersion());
                        workProductFunction.setMainUserId(byId.getMainUserId());
                        workProductFunction.setProductNumber(byId.getProductNumber());
                        workProductFunction.setExploitUserId(byId.getExploitUserId());
                        workProductFunction.setProductStatus(byId.getProductStatus());

                        workProductFunction.setFunctionName(rowList.get(0).toString().trim());
                        workProductFunction.setFunctionNumber(rowList.get(1).toString().trim());
                        workProductFunction.setFunctionDescribe(rowList.get(2).toString().trim());
                        if (workProductFunction.save()) {
                            updateNum++;
                        }
                    } catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void projectProductImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (rowIndex > 1) {
                    try {
                        WorkProduct workProduct = new WorkProduct();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
                        for (int i = 0; i < rowList.size(); i++) {
                            List<Integer> integers = Arrays.asList(1, 3, 4, 6);
                            if (integers.contains(i)) {
                                if (StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    rowList.add(0, "必填选项，请添加");
                                    errorList.add(rowList);
                                    return;
                                }
                            } else if (i == 4 || i == 5) {
                                List<String> users = null;
                                try {
                                    users = Arrays.asList(rowList.get(i).toString().trim().split(","));
                                } catch (NullPointerException e) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                List<Integer> userIds = Db.template("admin.user.queryCountByNames", Kv.by("names", users)).query();
                                if (userIds.size() != users.size()) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                String s = userIds.toString();
                                rowList.set(i, s.substring(1, s.length() - 1));
                            }
                        }

                        workProduct.setProductName(rowList.get(1).toString().trim());
                        workProduct.setProductVersion(rowList.get(2).toString().trim());
                        workProduct.setProductType(rowList.get(3).toString().trim());
                        workProduct.setMainUserId(Integer.parseInt(rowList.get(4).toString().trim()));
                        workProduct.setExploitUserId(Integer.parseInt(rowList.get(5).toString().trim()));
                        workProduct.setProductStatus(rowList.get(6).toString().trim());
                        workProduct.setReleaseTime(sdf.parse(rowList.get(7).toString().trim()));
                        workProduct.setVersionDescribe(rowList.get(8).toString().trim());
                        WorkProductService workProductService = new WorkProductService();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("entity", workProduct);
                        R r = workProductService.setWorkProduct(jsonObject);
                        if ((int) r.get("code") == 0) {
                            updateNum++;
                        }
                    } catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        public void resourcesImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (rowIndex > 1) {
                    try {
                        WorkResourcesSetting workResourcesSetting = new WorkResourcesSetting();
                        workResourcesSetting.setWorkId(Long.parseLong(workId));
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");

                        for (int i = 0; i < rowList.size(); i++) {
                            List<Integer> integers = Arrays.asList(0, 1, 2);
                            if (integers.contains(i)) {
                                if (StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    rowList.add(0, "必填选项，请添加");
                                    errorList.add(rowList);
                                    return;
                                }
                            } else if (i == 3) {
                                List<String> users = null;
                                try {
                                    users = Arrays.asList(rowList.get(i).toString().trim().split(","));
                                } catch (NullPointerException e) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                List<Integer> userIds = Db.template("admin.user.queryCountByNames", Kv.by("names", users)).query();
                                if (userIds.size() != users.size()) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                String s = userIds.toString();
                                rowList.set(i, s.substring(1, s.length() - 1));
                            }
                        }

                        workResourcesSetting.setResourcesName(rowList.get(0).toString().trim());
                        workResourcesSetting.setResourcesDescribe(rowList.get(1).toString().trim());
                        workResourcesSetting.setResourcesProvide(rowList.get(2).toString().trim());
                        workResourcesSetting.setMainUserId(Integer.parseInt(rowList.get(3).toString().trim()));
                        workResourcesSetting.setPlanTime(sdf.parse(rowList.get(4).toString().trim()));
                        workResourcesSetting.setActualTime(sdf.parse(rowList.get(5).toString().trim()));
                        workResourcesSetting.setResourcesStatus(rowList.get(6).toString().trim());
                        WorkResourcesSettingService workResourcesSettingService = new WorkResourcesSettingService();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("entity",workResourcesSetting);
                        R r = workResourcesSettingService.setResources(jsonObject);
                        if ((int)r.get("code") == 0) {
                            updateNum++;
                        }
                    } catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void taskImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (rowIndex > 1) {
                    try {

                        Task task = new Task();
                        task.setWorkId(Integer.parseInt(workId));
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");

                        for (int i = 0; i < rowList.size(); i++) {
                            List<Integer> integers = Arrays.asList(0, 3, 4, 6, 7, 10, 11, 12);
                            if (integers.contains(i)) {
                                if (StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    rowList.add(0, "必填选项，请添加");
                                    errorList.add(rowList);
                                    return;
                                }
                            } else if (i == 8 || i == 9) {
                                List<String> users = null;
                                try {
                                    users = Arrays.asList(rowList.get(i).toString().trim().split(","));
                                } catch (NullPointerException e) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                List<Integer> userIds = Db.template("admin.user.queryCountByNames", Kv.by("names", users)).query();
                                if (userIds.size() != users.size()) {
                                    rowList.add(0, "该人员不存在");
                                    errorList.add(rowList);
                                    return;
                                }
                                String s = userIds.toString();
                                rowList.set(i, s.substring(1, s.length() - 1));
                            }
                        }

                        Integer workId = Db.queryInt("select work_id from 72crm_work where work_num=?", rowList.get(0).toString().trim());
                        if (workId == null) {
                            rowList.add(0, "该项目不存在");
                            errorList.add(rowList);
                            return;
                        }
                        task.setWorkId(workId);

                        Integer taskId = Db.queryInt("select task_id from 72crm_task where task_num=?", rowList.get(1).toString().trim());
                        if (taskId == null) {
                            rowList.add(0, "该任务不存在");
                            errorList.add(rowList);
                            return;
                        }
                        task.setBeforeId(taskId);

                        if (StrUtil.isEmptyIfStr(rowList.get(2))) {
                            AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 21");
                            String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                            task.setTaskNum(result);
                        }

                        Long milestoneId = Db.queryLong("select milestone_id from 72crm_milestone where milestone_num=?", rowList.get(3).toString().trim());
                        if (milestoneId == null) {
                            rowList.add(0, "该里程碑不存在");
                            errorList.add(rowList);
                            return;
                        }

                        task.setMilestoneId(milestoneId);
                        task.setTaskName(rowList.get(4).toString().trim());
                        task.setClassId(rowList.get(5).toString().trim());
                        task.setCompleteness(rowList.get(6).toString().trim());
                        task.setStatus(rowList.get(7).toString().trim());
                        task.setMainUserId(Long.parseLong(rowList.get(8).toString().trim()));
                        task.setOwnerUserId(rowList.get(9).toString().trim());
                        task.setPriority(rowList.get(10).toString().trim());
                        task.setDifficulty(rowList.get(11).toString().trim());
                        task.setDegree(rowList.get(12).toString().trim());

                        task.setStartTime(sdf.parse(rowList.get(13).toString().trim()));
                        task.setEndTime(sdf.parse(rowList.get(14).toString().trim()));
                        task.setDescription(rowList.get(15).toString().trim());
                        task.setCreateTime(new Date());
                        task.setCreateUserId(BaseUtil.getUserId());
                        TaskService taskService = new TaskService();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("entity",task);
                        R r = taskService.setTask(jsonObject);
                        if ((int)r.get("code") == 0) {
                            updateNum++;
                        }

                    } catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void riskImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                System.out.println(rowIndex);
                if (rowIndex > 1) {

                    try {
                        WorkRisk workRisk = new WorkRisk();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
                        String found = null;
                        String tracking = null;
                        String solve = null;
                        for (int i = 0; i < rowList.size(); i++) {
                            if (i != 11 || i != 12 || i != 13) {
                                if (StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    rowList.add(0, "必填选项，请添加");
                                    errorList.add(rowList);
                                    return;
                                }
                            }

                            if (i == 5 || i == 7 || i == 11) {
                                if (!StrUtil.isEmptyIfStr(rowList.get(i))) {
                                    List<String> users = null;
                                    try {
                                        users = Arrays.asList(rowList.get(i).toString().trim().split(","));
                                    } catch (NullPointerException e) {
                                        rowList.add(0, "该人员不存在");
                                        errorList.add(rowList);
                                        return;
                                    }
                                    List<Integer> userIds = Db.template("admin.user.queryCountByNames", Kv.by("names", users)).query();
                                    if (userIds.size() != users.size()) {
                                        rowList.add(0, "该人员不存在");
                                        errorList.add(rowList);
                                        return;
                                    }
                                    StringBuilder stringBuilder = new StringBuilder();
                                    workRisk.setFoundUserId(userIds.stream().map((user) -> {
                                        return stringBuilder.append(userIds + ",");
                                    }).toString());
                                    switch (i) {
                                        case 5:
                                            found = stringBuilder.toString();
                                            break;
                                        case 7:
                                            tracking = stringBuilder.toString();
                                            break;
                                        case 11:
                                            solve = stringBuilder.toString();
                                            break;
                                    }
                                }
                            }
                        }

                        workRisk.setFoundPhase(rowList.get(0).toString().trim());
                        workRisk.setFoundWay(rowList.get(1).toString().trim());
                        workRisk.setDirection(rowList.get(2).toString().trim());
                        workRisk.setRiskType(rowList.get(3).toString().trim());
                        workRisk.setRiskLevel(rowList.get(4).toString().trim());

                        workRisk.setFoundUserId(found);

                        try {
                            Object date = rowList.get(6);
                            Date startTime = sdf.parse((String) date);
                            workRisk.setSolveTime(startTime);
                        } catch (Exception e) {
                            rowList.add(0, "日期格式错误");
                            errorList.add(rowList);
                            return;
                        }
                        workRisk.setTrackingUserId(tracking);

                        workRisk.setDetailed(rowList.get(8).toString().trim());
                        workRisk.setScope(rowList.get(9).toString().trim());
                        workRisk.setSolveAdvice(rowList.get(10).toString().trim());

                        workRisk.setSolveUserId(solve);
                        try {
                            Object time = rowList.get(12);
                            if (time != null) {
                                Object date = rowList.get(12);
                                Date startTime = sdf.parse((String) date);
                                workRisk.setFoundTime(startTime);
                            }
                        } catch (Exception e) {
                            rowList.add(0, "日期格式错误");
                            errorList.add(rowList);
                            return;
                        }
                        workRisk.setRiskStatus(rowList.get(13).toString().trim());
                        workRisk.setCreateTime(new Date());
                        WorkRiskService workRiskService = new WorkRiskService();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("entity",workRisk);
                        R r = workRiskService.setRisk(jsonObject);
                        if ((int)r.get("code") == 0) {
                            updateNum++;
                        }
                    } catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void workImport() {

            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                System.out.println(rowIndex);
                if (rowIndex > 1) {

                    try {
                        if (StrUtil.isEmptyIfStr(rowList.get(1))) {
                            rowList.add(0, "项目名称不能为空");
                            errorList.add(rowList);
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
                        String trim = rowList.get(2).toString().trim();
                        first = Db.findFirst(Db.getSql("admin.dept.queryDeptIdByName"), trim);
                        if (first == null) {
                            rowList.add(3, "该部门不存在");
                            errorList.add(rowList);
                            return;
                        }
                        work.setDeptId(first.getInt("dept_id"));


                        first = Db.findFirst(Db.getSql("admin.dept.queryDeptIdByName"), rowList.get(3).toString().trim());
                        if (first == null) {
                            rowList.add(4, "该部门不存在");
                            errorList.add(rowList);
                            return;
                        }
                        work.setArea(first.getInt("dept_id"));


                        first = Db.findFirst(Db.getSql("admin.user.queryIdByName"), rowList.get(4).toString().trim());
                        if (first == null) {
                            rowList.add(5, "该员工不存在");
                            errorList.add(rowList);
                            return;
                        }
                        work.setManagerId(first.getInt("user_id"));

                        first = Db.findFirst(Db.getSql("admin.user.queryIdByName"), rowList.get(5).toString().trim());
                        if (first == null) {
                            rowList.add(6, "该员工不存在");
                            errorList.add(rowList);
                            return;
                        }
                        work.setSalesId(Long.parseLong(first.getStr("user_id")));

                        first = Db.findFirst(Db.getSql("admin.user.queryIdByName"), rowList.get(5).toString().trim());
                        if (first == null) {
                            rowList.add(6, "该员工不存在");
                            errorList.add(rowList);
                            return;
                        }
                        work.setWorkPhase(rowList.get(6).toString().trim());


                        try {
                            Object date = rowList.get(8).toString().trim();
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
                            Date startTime = sdf.parse((String) date);
                            date = rowList.get(9).toString();
                            Date end = sdf.parse((String) date);
                            if (startTime.compareTo(end) > 0) {
                                rowList.add(8, "时间跨界");
                                errorList.add(rowList);
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

                        //boolean save = work.save();
                        WorkService workService = new WorkService();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("entity",work);
                        R r = workService.setWork(jsonObject);
                        if ((int)r.get("code") == 0) {
                            updateNum++;
                        }

                    } catch (Exception e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void demandImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (rowIndex > 1) {
                    try {
                        WorkDemand workDemand = new WorkDemand();
                        workDemand.setWorkId(Integer.parseInt(workId));
                        if (StrUtil.isEmptyIfStr(rowList.get(0))) {
                            rowList.add(0, "子系统不能为空");
                            errorList.add(rowList);
                            return;
                        }
                        workDemand.setSubsystem(rowList.get(0).toString().trim());
                        if (StrUtil.isEmptyIfStr(rowList.get(1))) {
                            rowList.add(0, "子模块不能为空");
                            errorList.add(rowList);
                            return;
                        }
                        workDemand.setChildModule(rowList.get(1).toString().trim());
                        if (StrUtil.isEmptyIfStr(rowList.get(2))) {
                            rowList.add(0, "一级功能不能为空");
                            errorList.add(rowList);
                            return;
                        }
                        workDemand.setFunction1(rowList.get(2).toString().trim());
                        workDemand.setFunction2(rowList.get(3).toString().trim());
                        if (StrUtil.isEmptyIfStr(rowList.get(4))) {
                            rowList.add(0, "需求描述不能为空");
                            errorList.add(rowList);
                            return;
                        }
                        workDemand.setDescribe(rowList.get(4).toString().trim());
                        workDemand.setDemandStatus(rowList.get(5).toString().trim());
                        workDemand.setDemandVersion(rowList.get(6).toString().trim());
                        workDemand.setPlan(rowList.get(7).toString().trim());
                        workDemand.setDemandNumber(rowList.get(8).toString().trim());
                        workDemand.setProductPercentage(rowList.get(9).toString().trim());
                        workDemand.setProductDescribe(rowList.get(10).toString().trim());
                        if (StrUtil.isEmptyIfStr(rowList.get(11))) {
                            rowList.add(0, "需求描述不能为空");
                            errorList.add(rowList);
                            return;
                        }
                        workDemand.setDemandType(rowList.get(11).toString().trim());
                        workDemand.save();

                        WorkDemandService workDemandService = new WorkDemandService();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("entity",workDemand);
                        R r = workDemandService.setDemand(jsonObject);
                        if ((int)r.get("code") == 0) {
                            updateNum++;
                        }

                    } catch (NumberFormatException e) {
                        Log.getLog(getClass()).error("导入数据异常:", e);
                        rowList.add(0, "导入异常");
                        errorList.add(rowList);
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void customerImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (interrupted) {
                    return;
                }
                if (rowList.size() < kv.entrySet().size()) {
                    for (int i = rowList.size() - 1; i < kv.entrySet().size(); i++) {
                        rowList.add(null);
                    }
                }
                if (num > 10001) {
                    rowList.add(0, "最多同时导入10000条数据");
                    errorList.add(rowList);
                    return;
                }
                if (rowIndex > 1) {
                    if (templateErr) {
                        rowList.add(0, "请使用最新的模板");
                        errorList.add(rowList);
                    } else {
                        Map<String, List<String>> areaMap = getAreaMap();
                        try {

                            for (Integer integer : isNullList) {
                                if (ObjectUtil.isEmpty(rowList.get(integer))) {
                                    rowList.add(0, "必填字段未填写");
                                    errorList.add(rowList);
                                    return;
                                }
                            }
                            List<Record> recordList = new ArrayList<>();
                            for (Record record : uniqueList) {
                                Object value = rowList.get(kv.getInt(record.getStr("field_name")));
                                if (ObjectUtil.isNotEmpty(value)) {
                                    Record newRecord = new Record();
                                    newRecord.setColumns(record);
                                    newRecord.set("value", value);
                                    recordList.add(newRecord);
                                }
                            }
                            Kv unique = Kv.by("primaryKey", crmEnum.getTableId() + ",owner_user_id").set("label", crmEnum.getType());
                            unique.set("tableName", crmEnum.getTableName()).set("dataTableName", crmEnum.getDataTableName());
                            unique.set("uniqueList", recordList);
                            List<CrmCustomer> customerList = recordList.size() > 0 ? CrmCustomer.dao.template("admin.field.queryFieldValueNoDelete", unique).find() : new ArrayList<>();
                            boolean isUpdate = false;
                            if (customerList.size() > 0) {
                                if (Objects.equals(2, repeatHandling)) {
                                    rowList.add(0, "根据查重规则，该条数据重复");
                                    errorList.add(rowList);
                                    return;
                                } else if (customerList.size() == 1 && ObjectUtil.isEmpty(customerList.get(0).getOwnerUserId())) {
                                    rowList.add(0, "公海数据，无覆盖权限");
                                    errorList.add(rowList);
                                    return;
                                } else if (customerList.size() > 1) {
                                    rowList.add(0, "数据与多条唯一性字段重复");
                                    errorList.add(rowList);
                                    return;
                                }
                                isUpdate = true;
                            }
                            String province = ObjectUtil.isEmpty(rowList.get(kv.getInt("province"))) ? "" : rowList.get(kv.getInt("province")).toString();
                            String city = ObjectUtil.isEmpty(rowList.get(kv.getInt("city"))) ? "" : rowList.get(kv.getInt("city")).toString();
                            String site = ObjectUtil.isEmpty(rowList.get(kv.getInt("site"))) ? "" : rowList.get(kv.getInt("site")).toString();
                            if (StrUtil.isNotEmpty(city) && (!areaMap.containsKey(province) || !areaMap.get(province).contains(city))) {
                                rowList.add(0, "省市区不合要求，请核对");
                                errorList.add(rowList);
                                return;
                            }
                            if (StrUtil.isNotEmpty(site) && (!areaMap.containsKey(city) || !areaMap.get(city).contains(site))) {
                                rowList.add(0, "省市区不合要求，请核对");
                                errorList.add(rowList);
                                return;
                            }
                            JSONObject object = new JSONObject();
                            JSONObject entityObject = new JSONObject();
                            fixedFieldList.forEach(field -> {
                                String fieldName = field.getStr("field_name");
                                entityObject.fluentPut(fieldName, rowList.get(kv.getInt(fieldName)));
                            });
                            entityObject.fluentPut("address", StrUtil.isNotEmpty(province) ? province + "," + city + "," + site : "")
                                    .fluentPut("owner_user_id", ownerUserId);
                            if (customerList.size() == 1) {
                                CrmCustomer customer = customerList.get(0);
                                boolean auth = AuthUtil.isCrmAuth(CrmEnum.CRM_CUSTOMER, customer.getCustomerId());
                                if (auth) {
                                    rowList.add(0, "数据无覆盖权限");
                                    errorList.add(rowList);
                                    return;
                                }
                                if (ObjectUtil.isNotEmpty(poolId)) {
                                    Integer isPool = Db.queryInt("select count(1) from 72crm_crm_customer_pool_relation where pool_id = ? and customer_id = ?", poolId, customer.getCustomerId());
                                    if (isPool.equals(0)) {
                                        rowList.add(0, "重复数据不在当前公海，无权覆盖");
                                        errorList.add(rowList);
                                        return;
                                    }
                                }
                                entityObject.put("customer_id", customer.getCustomerId());
                                entityObject.put("batch_id", customer.getBatchId());
                            }
                            object.put("field", addFieldArray(rowList));
                            object.put("entity", entityObject);
                            CrmVerify verify = new CrmVerify(crmEnum);
                            R result = verify.verify(object);
                            if (!result.isSuccess()) {
                                rowList.add(0, result.get("msg"));
                                errorList.add(rowList);
                                return;
                            }
                            boolean isSuccess = Db.tx(() -> {
                                R r = Aop.get(CrmCustomerService.class).addOrUpdate(object, null);
                                if (!r.isSuccess()) {
                                    rowList.add(0, r.get("msg"));
                                    errorList.add(rowList);
                                    return false;
                                }
                                if (ObjectUtil.isNotEmpty(poolId) && ObjectUtil.isNotEmpty(r.get("data"))) {
                                    CrmCustomerPoolRelation relation = new CrmCustomerPoolRelation();
                                    Integer customerId = JSONObject.parseObject(JSONObject.toJSONString(r.get("data"))).getInteger("customer_id");
                                    relation.setCustomerId(customerId).setPoolId(poolId);
                                    relation.save();
                                }
                                return true;
                            });
                            if (isSuccess && isUpdate) {
                                updateNum++;
                            }
                        } catch (Exception ex) {
                            Log.getLog(getClass()).error("导入数据异常:", ex);
                            rowList.add(0, "导入异常");
                            errorList.add(rowList);
                        }
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }


        private void leadsImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (interrupted) {
                    return;
                }
                if (rowList.size() < kv.entrySet().size()) {
                    for (int i = rowList.size() - 1; i < kv.entrySet().size(); i++) {
                        rowList.add(null);
                    }
                }
                if (num > 10001) {
                    rowList.add(0, "最多同时导入10000条数据");
                    errorList.add(rowList);
                    return;
                }
                if (rowIndex > 1) {
                    if (templateErr) {
                        rowList.add(0, "请使用最新的模板");
                        errorList.add(rowList);
                    } else {
                        try {
                            for (Integer integer : isNullList) {
                                if (ObjectUtil.isEmpty(rowList.get(integer))) {
                                    rowList.add(0, "必填字段未填写");
                                    errorList.add(rowList);
                                    return;
                                }
                            }
                            List<Record> recordList = new ArrayList<>();
                            for (Record record : uniqueList) {
                                Object value = rowList.get(kv.getInt(record.getStr("field_name")));
                                if (ObjectUtil.isNotEmpty(value)) {
                                    Record newRecord = new Record();
                                    newRecord.setColumns(record);
                                    newRecord.set("value", value);
                                    recordList.add(newRecord);
                                }
                            }
                            Kv unique = Kv.by("primaryKey", crmEnum.getTableId()).set("label", crmEnum.getType());
                            unique.set("tableName", crmEnum.getTableName()).set("dataTableName", crmEnum.getDataTableName());
                            unique.set("uniqueList", recordList);
                            List<CrmLeads> leadsList = recordList.size() > 0 ? CrmLeads.dao.template("admin.field.queryFieldValueNoDelete", unique).find() : new ArrayList<>();
                            boolean isUpdate = false;
                            if (leadsList.size() > 0) {
                                if (Objects.equals(2, repeatHandling)) {
                                    return;
                                }
                                if (leadsList.size() > 1) {
                                    rowList.add(0, "数据与多条唯一性字段重复");
                                    errorList.add(rowList);
                                    return;
                                }
                                isUpdate = true;
                            }
                            JSONObject object = new JSONObject();
                            JSONObject entityObject = new JSONObject();
                            fixedFieldList.forEach(field -> {
                                String fieldName = field.getStr("field_name");
                                entityObject.fluentPut(fieldName, rowList.get(kv.getInt(fieldName)));
                            });
                            entityObject.fluentPut("owner_user_id", ownerUserId);
                            if (leadsList.size() == 1) {
                                CrmLeads leads = leadsList.get(0);
                                boolean auth = AuthUtil.isCrmAuth(CrmEnum.CRM_LEADS, leads.getLeadsId());
                                if (auth) {
                                    rowList.add(0, "数据无覆盖权限");
                                    errorList.add(rowList);
                                    return;
                                }
                                entityObject.put("leads_id", leads.getLeadsId());
                                entityObject.put("batch_id", leads.getBatchId());
                            }
                            object.put("field", addFieldArray(rowList));
                            object.put("entity", entityObject);
                            CrmVerify verify = new CrmVerify(crmEnum);
                            R result = verify.verify(object);
                            if (!result.isSuccess()) {
                                rowList.add(0, result.get("msg"));
                                errorList.add(rowList);
                                return;
                            }
                            boolean isSuccess = Db.tx(() -> {
                                R r = Aop.get(CrmLeadsService.class).addOrUpdate(object);
                                if (!r.isSuccess()) {
                                    rowList.add(0, r.get("msg"));
                                    errorList.add(rowList);
                                    return false;
                                }
                                return true;
                            });
                            if (isSuccess && isUpdate) {
                                updateNum++;
                            }
                        } catch (Exception ex) {
                            Log.getLog(getClass()).error("导入数据异常:", ex);
                            rowList.add(0, "导入异常");
                            errorList.add(rowList);
                        }
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void contactsImport() {
            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (interrupted) {
                    return;
                }
                if (rowList.size() < kv.entrySet().size()) {
                    for (int i = rowList.size() - 1; i < kv.entrySet().size(); i++) {
                        rowList.add(null);
                    }
                }
                if (num > 10001) {
                    rowList.add(0, "最多同时导入10000条数据");
                    errorList.add(rowList);
                    return;
                }
                if (rowIndex > 1) {
                    if (templateErr) {
                        rowList.add(0, "请使用最新的模板");
                        errorList.add(rowList);
                    } else {
                        try {
                            String customeName = rowList.get(kv.getInt("customer_id")).toString();
                            CrmCustomer customer = CrmCustomer.dao.findFirstByCache("CUSTOME_NAME", customeName, "select customer_id from 72crm_crm_customer where customer_name = ? and status = 1 limit 1", customeName);
                            if (customer == null) {
                                rowList.add(0, "填写的客户不存在");
                                errorList.add(rowList);
                                return;
                            }
                            for (Integer integer : isNullList) {
                                if (ObjectUtil.isEmpty(rowList.get(integer))) {
                                    rowList.add(0, "必填字段未填写");
                                    errorList.add(rowList);
                                    return;
                                }
                            }
                            List<Record> recordList = new ArrayList<>();
                            for (Record record : uniqueList) {
                                Object value = rowList.get(kv.getInt(record.getStr("field_name")));
                                if (ObjectUtil.isNotEmpty(value)) {
                                    Record newRecord = new Record();
                                    newRecord.setColumns(record);
                                    newRecord.set("value", value);
                                    recordList.add(newRecord);
                                }
                            }
                            Kv unique = Kv.by("primaryKey", crmEnum.getTableId()).set("label", crmEnum.getType());
                            unique.set("tableName", crmEnum.getTableName()).set("dataTableName", crmEnum.getDataTableName());
                            unique.set("uniqueList", recordList);
                            List<CrmContacts> contactsList = recordList.size() > 0 ? CrmContacts.dao.template("admin.field.queryFieldValueNoDelete", unique).find() : new ArrayList<>();
                            boolean isUpdate = false;
                            if (contactsList.size() > 0) {
                                if (Objects.equals(2, repeatHandling)) {
                                    return;
                                }
                                if (contactsList.size() > 1) {
                                    rowList.add(0, "数据与多条唯一性字段重复");
                                    errorList.add(rowList);
                                    return;
                                }
                                isUpdate = true;
                            }
                            JSONObject object = new JSONObject();
                            JSONObject entityObject = new JSONObject();
                            fixedFieldList.forEach(field -> {
                                if (!field.getStr("field_name").equals("customer_id")) {
                                    String fieldName = field.getStr("field_name");
                                    entityObject.fluentPut(fieldName, rowList.get(kv.getInt(fieldName)));
                                }
                            });
                            entityObject
                                    .fluentPut("customer_id", customer.getCustomerId())
                                    .fluentPut("owner_user_id", ownerUserId);
                            if (contactsList.size() == 1) {
                                CrmContacts contacts = contactsList.get(0);
                                boolean auth = AuthUtil.isCrmAuth(CrmEnum.CRM_CONTACTS, contacts.getContactsId());
                                if (auth) {
                                    rowList.add(0, "数据无覆盖权限");
                                    errorList.add(rowList);
                                    return;
                                }
                                entityObject.put("contacts_id", contacts.getContactsId());
                                entityObject.put("batch_id", contacts.getBatchId());
                            }
                            object.put("field", addFieldArray(rowList));
                            object.put("entity", entityObject);
                            CrmVerify verify = new CrmVerify(crmEnum);
                            R result = verify.verify(object);
                            if (!result.isSuccess()) {
                                rowList.add(0, result.get("msg"));
                                errorList.add(rowList);
                                return;
                            }
                            boolean isSuccess = Db.tx(() -> {
                                R r = Aop.get(CrmContactsService.class).addOrUpdate(object);
                                if (!r.isSuccess()) {
                                    rowList.add(0, r.get("msg"));
                                    errorList.add(rowList);
                                    return false;
                                }
                                return true;
                            });
                            if (isSuccess && isUpdate) {
                                updateNum++;
                            }
                        } catch (Exception ex) {
                            Log.getLog(getClass()).error("导入数据异常:", ex);
                            rowList.add(0, "导入异常");
                            errorList.add(rowList);
                        }
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        private void productImport() {

            ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                num++;
                if (interrupted) {
                    return;
                }
                if (rowList.size() < kv.entrySet().size()) {
                    for (int i = rowList.size() - 1; i < kv.entrySet().size(); i++) {
                        rowList.add("");
                    }
                }
                if (num > 10001) {
                    rowList.add(0, "最多同时导入10000条数据");
                    errorList.add(rowList);
                    return;
                }
                if (rowIndex > 1) {
                    if (templateErr) {
                        rowList.add(0, "请使用最新的模板");
                        errorList.add(rowList);
                    } else {
                        try {
                            String categoryName = rowList.get(kv.getInt("category_id")).toString();
                            CrmProductCategory category = CrmProductCategory.dao.findFirstByCache("CATEGORY_NAME", categoryName, "select category_id from 72crm_crm_product_category where name = ? limit 1", categoryName);
                            if (category == null) {
                                rowList.add(0, "填写的产品类型不存在");
                                errorList.add(rowList);
                                return;
                            }
                            for (Integer integer : isNullList) {
                                if (ObjectUtil.isEmpty(rowList.get(integer))) {
                                    rowList.add(0, "必填字段未填写");
                                    errorList.add(rowList);
                                    return;
                                }
                            }
                            List<Record> recordList = new ArrayList<>();
                            for (Record record : uniqueList) {
                                Object value = rowList.get(kv.getInt(record.getStr("field_name")));
                                if (ObjectUtil.isNotEmpty(value)) {
                                    Record newRecord = new Record();
                                    newRecord.setColumns(record);
                                    newRecord.set("value", value);
                                    recordList.add(newRecord);
                                }
                            }
                            Kv unique = Kv.by("primaryKey", crmEnum.getTableId()).set("label", crmEnum.getType());
                            unique.set("tableName", crmEnum.getTableName()).set("dataTableName", crmEnum.getDataTableName());
                            unique.set("uniqueList", recordList);
                            List<CrmProduct> productList = recordList.size() > 0 ? CrmProduct.dao.template("admin.field.queryFieldValueNoDelete", unique).find() : new ArrayList<>();
                            boolean isUpdate = false;
                            if (productList.size() > 0) {
                                if (Objects.equals(2, repeatHandling)) {
                                    return;
                                }
                                if (productList.size() > 1) {
                                    rowList.add(0, "数据与多条唯一性字段重复");
                                    errorList.add(rowList);
                                    return;
                                }
                                isUpdate = true;
                            }
                            JSONObject object = new JSONObject();
                            JSONObject entityObject = new JSONObject();
                            if (productList.size() == 1) {
                                CrmProduct product = productList.get(0);
                                boolean auth = AuthUtil.isCrmAuth(CrmEnum.CRM_PRODUCT, product.getProductId());
                                if (auth) {
                                    rowList.add(0, "数据无覆盖权限");
                                    errorList.add(rowList);
                                    return;
                                }
                                entityObject.put("product_id", product.getProductId());
                                entityObject.put("batch_id", product.getBatchId());
                            }
                            fixedFieldList.forEach(field -> {
                                if (!field.getStr("field_name").equals("category_id")) {
                                    String fieldName = field.getStr("field_name");
                                    entityObject.fluentPut(fieldName, rowList.get(kv.getInt(fieldName)));
                                }
                            });
                            entityObject
                                    .fluentPut("category_id", category.getCategoryId())
                                    .fluentPut("owner_user_id", ownerUserId);
                            object.put("field", addFieldArray(rowList));
                            object.put("entity", entityObject);
                            CrmVerify verify = new CrmVerify(crmEnum);
                            R result = verify.verify(object);
                            if (!result.isSuccess()) {
                                rowList.add(0, result.get("msg"));
                                errorList.add(rowList);
                                return;
                            }
                            boolean isSuccess = Db.tx(() -> {
                                R r = Aop.get(CrmProductService.class).saveAndUpdate(object);
                                if (!r.isSuccess()) {
                                    rowList.add(0, r.get("msg"));
                                    errorList.add(rowList);
                                    return false;
                                }
                                return true;
                            });
                            if (isSuccess && isUpdate) {
                                updateNum++;
                            }
                        } catch (Exception ex) {
                            Log.getLog(getClass()).error("导入数据异常:", ex);
                            rowList.add(0, "导入异常");
                            errorList.add(rowList);
                        }
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        /**
         * 查询导入顺序
         *
         * @param rowList 行数据
         * @author zhangzhiwei
         */
        private void queryExcelHead(List<Object> rowList) {
            fieldList = Aop.get(AdminFieldService.class).queryAddField(crmEnum);
            fixedFieldList = Db.find("select * from 72crm_admin_field where label = ? and field_type = 1 and is_hidden = 0", crmEnum.getType());
            fieldList.removeIf(record -> Arrays.asList("file", "checkbox", "user", "structure").contains(record.getStr("formType")));
            HashMap<String, String> nameMap = new HashMap<>();
            HashMap<String, Integer> isNullMap = new HashMap<>();
            fieldList.forEach(record -> {
                if (crmEnum.getType() == CrmEnum.CRM_CUSTOMER.getType() && "map_address".equals(record.getStr("field_name"))) {
                    nameMap.put("省", "province");
                    nameMap.put("市", "city");
                    nameMap.put("区", "site");
                    isNullMap.put("省", 0);
                    isNullMap.put("市", 0);
                    isNullMap.put("区", 0);
                } else {
                    if (record.get("is_unique", 0).equals(1)) {
                        uniqueList.add(record);
                    }
                    if (record.getInt("is_null").equals(1)) {
                        nameMap.put(record.getStr("name") + "(*)", record.getStr("field_name"));
                        isNullMap.put(record.getStr("name") + "(*)", record.getInt("is_null"));
                    } else {
                        nameMap.put(record.getStr("name"), record.getStr("field_name"));
                        isNullMap.put(record.getStr("name"), record.getInt("is_null"));
                    }
                }

            });
            List<String> nameList = new ArrayList<>(nameMap.keySet());
            if (nameList.size() != rowList.size() || !nameList.containsAll(rowList)) {
                templateErr = true;
            } else {
                for (int i = 0; i < rowList.size(); i++) {
                    kv.set(nameMap.get(rowList.get(i).toString()), i);
                    if (1 == isNullMap.get(rowList.get(i).toString())) {
                        isNullList.add(i);
                    }
                }
            }
            rowList.add(0, "错误原因");
            errorList.add(rowList);
        }

        /**
         * 将自定义字段组装成想要的格式
         *
         * @param rowList 行数据
         * @return 自定义字段数组
         */
        private JSONArray addFieldArray(List<Object> rowList) {
            JSONArray jsonArray = new JSONArray();
            fieldList.forEach(record -> {
                if (record.getInt("field_type").equals(0) || record.getInt("field_type").equals(2)) {
                    Integer columnsNum = kv.getInt(record.getStr("field_name"));
                    Object value = rowList.get(columnsNum);
                    if (Objects.equals(record.getInt("type"), 4) && value instanceof Long) {
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) value);
                        record.set("value", DateUtil.formatDate(date));
                    } else {
                        record.set("value", value);
                    }

                    jsonArray.add(JSONObject.parseObject(record.toJson()));
                }
            });
            return jsonArray;
        }
    }
}
