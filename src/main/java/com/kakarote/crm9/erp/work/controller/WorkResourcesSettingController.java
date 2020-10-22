package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
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
import com.kakarote.crm9.erp.work.service.WorkResourcesSettingService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkResourcesSettingController extends Controller {


    @Inject
    WorkResourcesSettingService workResourcesSettingService;

    /**
     * 分页查询资源配置
     */
    @Permissions({"project:resources:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(workResourcesSettingService.queryPageList(basePageRequest));
    }

    /**
     * 设置项目资源配置
     */
    @Permissions({"project:resources:save","project:resources:update"})
    public void setResources(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workResourcesSettingService.setResources(jsonObject));
    }

    /**
     * 删除项目资源配置
     */
    @Permissions({"project:resources:delete"})
    public void delResources(@Para("ids") String ids){
        renderJson(workResourcesSettingService.delResources(ids));
    }

    /**
     * 根据ID查询资源配置
     */
    public void queryById(@Para("resourcesId") Integer resourcesId){
        renderJson(R.ok().put("data",Db.findFirst(Db.getSql("work.queryResourceById"),resourcesId)));
    }

    /**
     * 导出!!!
     */
    @Permissions({"project:resources:excelexport"})
    public void resourcesExport(BasePageRequest<Work> basePageRequest){

        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)workResourcesSettingService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"resources_id","create_time","create_user_id","main_user_id","update_time","work_id"};
            for (Record record : records) {
                record.remove(s);
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
            response.setHeader("Content-Disposition", "attachment;filename=resource.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 导入!!!
     */
    @Permissions({"project:resources:excelImport"})
    public void excelImport(@Para("file") UploadFile file,@Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling,@Para("workId")String workId){
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.RESOURCES, file.getFile().getAbsolutePath(), null, null, repeatHandling,workId);
        renderJson(R.ok().put("data",messageId));
    }

    /**
     * 下载导入模版!!!
     */
    @Permissions({"project:resources:excelImport"})
    public void updateResourcesTemplate(){
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

            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }
        try(HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("资源配置导入模版",handers, downData, downRows,null,null);) {

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
        List<String> names = Arrays.asList("资源名称", "资源描述", "资源提供方", "负责人", "计划时间", "实际时间", "资源状态");
        List<String> fieldName = Arrays.asList("resources_name", "resources_describe", "resources_provide", "main_user_name", "plan_time", "actual_time", "resources_status");
        for (int i = 0; i < 7; i++) {
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
