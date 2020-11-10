package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.service.WorkProductService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkProductController extends Controller {

    @Inject
    WorkProductService workProductService;
    @Inject
    AdminFieldService adminFieldService;


    /**
     * 分页查询产品
     */
    @Permissions({"project:product:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(workProductService.queryPageList(basePageRequest,false));
    }

    /**
     * 设置产品
     */
    @Permissions({"project:product:save","project:product:update"})
    public void setWorkProduct(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workProductService.setWorkProduct(jsonObject));
    }

    /**
     * 产品删除
     */
    public void delWorkProduct(String ids){
        renderJson(workProductService.delWorkProduct(ids));
    }

    /**
     * 修改产品状态
     */
    @Permissions({"project:product:status"})
    public void setWorkProductStatus(@Para("status")String status,@Para("ids")String ids){
        renderJson(workProductService.setWorkProductStatus(status,ids));
    }

    public void queryById(Integer id){
        renderJson(R.ok().put("data",workProductService.queryById(id)));
    }

    /**
     * 查询产品版本历史记录???
     */
    public void queryVersionHistorical(BasePageRequest basePageRequest){
        renderJson(workProductService.queryVersionHistorical(basePageRequest));
    }

    /**
     * !!!产品使用统计
     */
    public void queryStatistics(BasePageRequest basePageRequest){
        renderJson(workProductService.queryStatistics(basePageRequest));
    }

    /**
     * ???导出产品使用统计
     */
    public void statisticsExport(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true);){
            JSONObject jsonObject = (JSONObject)workProductService.queryStatistics(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            //String[] s = new String[]{"create_user_id","exploit_user_id","main_user_id","product_number"};
            for (Record record : records) {
                //record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            getFields(headList);

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
            response.setHeader("Content-Disposition", "attachment;filename=product.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * !!!导出
     */
    @Permissions({"project:product:excelexport"})
    public void productExport(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.PRODUCT.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true);){
            JSONObject jsonObject = (JSONObject)workProductService.queryPageList(basePageRequest, false).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"create_user_id","exploit_user_id","main_user_id","product_number"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.PRODUCT.getType()));


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
            response.setHeader("Content-Disposition", "attachment;filename=product.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 下载任务上传模版
     */
    public void updateTemplate(){
        List<Record> fields = adminFieldService.getFields(CrmEnum.PRODUCT.getType(), null);
        //列标题
        List<String> handers = new ArrayList<>();
        //下拉框数据(数组)
        List<String[]> downData = new ArrayList<>();
        //下拉列的序号
        List<String> downRows = new ArrayList<>();

        int temp = 0;
        for (Record field : fields) {

            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }
        try(HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("产品导入模版",handers, downData, downRows,null,null);) {

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
     * ???导入
     */
    @Permissions({"project:product:excelImport"})
    public void excelImport(@Para("file") UploadFile file, @Para("ownerUserId") Long ownerUserId, @Para("repeatHandling") Integer repeatHandling){
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.PRODUCT, file.getFile().getAbsolutePath(), null, null, repeatHandling,null);
        renderJson(R.ok().put("data",messageId));
    }

    private void getFields(List<Record> fields){
        List<String> names = Arrays.asList("产品名称","项目名称","项目类型","项目负责人","项目阶段");
        List<String> fieldName = Arrays.asList("product_name","work_name","work_type","sales_name","work_phase");
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            record.set("type",FormTypeEnum.TEXT.getType());
            if(i==6){
                record.set("type",FormTypeEnum.SELECT.getType());
                record.set("options","已到位,未到位");
            }
            fields.add(record);
        }
    }
}
