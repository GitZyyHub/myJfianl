package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.WorkDemandService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkDemandController extends Controller {

    @Inject
    WorkDemandService workDemandService;
    @Inject
    AdminFieldService adminFieldService;


    /**
     * 分页查询需求
     */
    @Permissions({"project:demand:index"})
    public void queryPageList(BasePageRequest basePageRequest){
       renderJson(workDemandService.queryPageList(basePageRequest,false));
    }

    public void queryById(@Para("id")Integer id){
        renderJson(workDemandService.queryById(id));
    }

    /**
     * 设置需求表
     */
    @Permissions({"project:demand:save","project:demand:update"})
    public void setDemand(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workDemandService.setDemand(jsonObject));
    }

    /**
     * 设置需求完成进度
     */
    public void setDemandStatus(@Para("demandId")String demand,@Para("status")String status){
        renderJson(workDemandService.setDemandStatus(demand,status));
    }

    /**
     * 设置需求-产品-产品功能关联
     */
    @Permissions({"project:demandList:tag"})
    public void setDemandProduct(@Para("demandId") Integer demandId,@Para("productIds") String productIds,@Para("functionIds") String functionIds){
        renderJson(workDemandService.setDemandProduct(demandId,productIds,functionIds));
    }

    /**
     * 批量删除
     */
    @Permissions({"project:demand:delete"})
    public void delDemand(@Para("ids") String ids){
        renderJson(workDemandService.delDemand(ids));
    }

    /**
     * 下载导入模版
     */
    @Permissions({"project:demand:excelImport"})
    public void updateDemandTemplate(){
        List<Record> fields = adminFieldService.getFields(CrmEnum.DEMAND.getType(), null);
        //列标题
        List<String> handers = new ArrayList<>();
        //下拉框数据(数组)
        List<String[]> downData = new ArrayList<>();
        //下拉列的序号
        List<String> downRows = new ArrayList<>();

        int temp = 0;
        for (int i = 0; i < fields.size(); i++) {
            Record field = fields.get(i);
            String fieldName = field.getStr("field_name");
            if("productName".equals(field.getStr("field_name"))||"productVersion".equals(field.getStr("field_name"))||
                    "productNumber".equals(field.getStr("field_name"))||"functionName".equals(field.getStr("field_name"))||
                    "functionNumber".equals(field.getStr("field_name"))){
                continue;
            }
            Arrays.asList("");
            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }

        try (HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("需求导入模版",handers, downData, downRows,null,null);) {

            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer_import.xls");

            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 导入
     */
    /*public void excelImport(@Para("file") UploadFile file,@Para("workId")String workId){
        //UploadFile file = getFile("file", "/excel/");
        renderJson(workDemandService.excelImport(file,workId));
    }*/
    @Permissions({"project:demand:excelImport"})
    public void excelImport(@Para("file") UploadFile file,@Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling,@Para("workId")String workId) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.DEMAND, file.getFile().getAbsolutePath(), null, null, repeatHandling,workId);
        renderJson(R.ok().put("data",messageId));
    }
    /**
     * 导出
     */
    @Permissions({"project:demand:excelexport","project:demandList:excelexport"})
    public void demandExport(BasePageRequest basePageRequest,@Para("workIds") String demandIds){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        List<String> strings = null;
        if(!StrUtil.isEmpty(demandIds)){
            strings = Arrays.asList(demandIds.split(","));
        }
        try (ExcelWriter writer = ExcelUtil.getWriter(true);){
            JSONObject jsonObject = (JSONObject)workDemandService.queryPageList(basePageRequest, false).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"work_id","is_associated","demand_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.DEMAND.getType()));


            headList.forEach(head -> {
                if (!Objects.equals(head.getStr("fieldName"), head.getStr("name"))) {
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")), head.getStr("name"));
                } else {
                    writer.addHeaderAlias(head.getStr("fieldName"), head.getStr("name"));
                }
            });
            writer.write(list,true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);

            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 25);
            }

            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=demand.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /*------------重复接口--------------*/
    /**
     * 分页查询需求
     */
    @Permissions({"project:demandList:index"})
    public void queryPageDemandList(BasePageRequest basePageRequest){
        renderJson(workDemandService.queryPageList(basePageRequest,false));
    }

    /**
     * 设置需求表
     */
    @Permissions({"project:demandList:save","project:demandList:update"})
    public void setDemandList(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workDemandService.setDemand(jsonObject));
    }

    /**
     * 批量删除
     */
    @Permissions({"project:demandList:delete"})
    public void delDemandList(@Para("ids") String ids){
        renderJson(workDemandService.delDemand(ids));
    }
}
