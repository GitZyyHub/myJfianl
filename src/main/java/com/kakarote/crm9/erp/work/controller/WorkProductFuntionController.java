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
import com.kakarote.crm9.erp.work.service.WorkProductFuntionService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkProductFuntionController extends Controller {

    @Inject
    WorkProductFuntionService workProductFuntionService;

    @Inject
    AdminFieldService adminFieldService;

    /**
     * 添加产品功能
     */
    @Permissions({"project:productFunction:save","project:productFunction:update"})
    public void setProductFunction(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workProductFuntionService.setProductFunction(jsonObject));
    }

    /**
     * 分页查询产品功能
     */
    @Permissions({"project:productFunction:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(workProductFuntionService.queryPageList(basePageRequest,false));
    }

    /**
     * 根据多个产品ID查询产品功能
     */
    public void queryFuntionByProductId(@Para("productIds")String productIds){
        renderJson(workProductFuntionService.queryFuntionByProductId(productIds));
    }

    /**
     * 批量删除
     */
    @Permissions({"project:productFunction:delete"})
    public void delProductFuntion(@Para("ids")String ids){
        renderJson(workProductFuntionService.delProductFuntion(ids));
    }

    /**
     * !!!导出
     */
    @Permissions({"project:productFunction:excelexport"})
    public void productFuntionExport(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.PRODUCT_FUNCTIONE.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true);){
            JSONObject jsonObject = (JSONObject)workProductFuntionService.queryPageList(basePageRequest, false).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"product_id","function_id","product_number"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.PRODUCT_FUNCTIONE.getType()));


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
            response.setHeader("Content-Disposition", "attachment;filename=productFunction.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * !!!导入
     */
    @Permissions({"project:productFunction:excelImport"})
    public void excelImport(@Para("file") UploadFile file, @Para("ownerUserId") Long ownerUserId, @Para("repeatHandling") Integer repeatHandling,@Para("productId")String productId){
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.PRODUCT_FUNCTIONE, file.getFile().getAbsolutePath(), null, null, repeatHandling,productId);
        renderJson(R.ok().put("data",messageId));
    }

    /**
     *下载上传模版
     */
    public void updateTemplate(){
        List<Record> fields = new ArrayList<>();
        getFields(fields);
        //列标题
        List<String> handers = new ArrayList<>();
        //下拉框数据(数组)
        List<String[]> downData = new ArrayList<>();
        //下拉列的序号
        List<String> downRows = new ArrayList<>();

        int temp = 0;
        for (Record field : fields) {
            String name = field.getStr("name");
            if("创建人".equals(name)){
                continue;
            }
            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }
        try(HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("产品功能导入模版",handers, downData, downRows,null,null);) {

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

    private void getFields(List<Record> fields){
        List<String> names = Arrays.asList("产品功能编号(*)", "产品功能名称(*)", "版本描述");
        List<String> fieldName = Arrays.asList("function_number", "function_name", "function_describe");
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


    /**
     * !!!查看产品版本记录
     */
}
