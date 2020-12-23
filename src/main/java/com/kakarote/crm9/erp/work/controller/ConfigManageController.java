package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.entity.WorkConfig;
import com.kakarote.crm9.erp.work.service.ConfigManageService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * @author zyy
 */
public class ConfigManageController extends Controller {

    @Inject
    ConfigManageService configManageService;

    /**
     * 分页查询配置管理
     * @param basePageRequest
     */
    @Permissions({"project:workConfig:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(configManageService.queryPageList(basePageRequest));
    }

    /**
     * 设置配置管理
     * @param workConfig
     */
    @Permissions({"project:workConfig:save","project:workConfig:update"})
    public void setConfig(@Para("") WorkConfig workConfig){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        jsonObject.fluentPut("type",2);
        renderJson(configManageService.setConfig(jsonObject));
    }

    /**
     * 根据ID查询配置管理
     */
    public void queryById(@Para("id") Integer id){
        renderJson(configManageService.queryById(id));
    }

    /**
     * 批量删除配置管理
     */
    @Permissions({"project:workConfig:delete"})
    public void delConfig(@Para("ids")String ids){
        renderJson(configManageService.delConfig(ids));
    }

    /**
     * 导出
     */
    @Permissions({"project:workConfig:excelexport"})
    public void export(BasePageRequest<Work> basePageRequest){

        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)configManageService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"associated_id","config_id","create_time","create_user_id","main_user_id","type","update_time","work_id"};
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
            response.setHeader("Content-Disposition", "attachment;filename=configManage.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }
    /**
     * 导入
     */
    @Permissions({"project:workConfig:excelImport"})
    public void excelImport(@Para("file") UploadFile file, @Para("ownerUserId") Long ownerUserId, @Para("repeatHandling") Integer repeatHandling){
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.WORKCONFIG, file.getFile().getAbsolutePath(), null, null, repeatHandling,null);
        renderJson(R.ok().put("data",messageId));
    }

    /**
     * 下载导入模版
     */
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

            String name = field.getStr("name");

            handers.add(name);
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }
        try(HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("配置管理导入模版",handers, downData, downRows,null,null);) {

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
        List<String> names = Arrays.asList("配置项(*)", "关联项目编号(*)", "关联项目名称(*)", "计划时间(*)", "负责人(*)", "版本", "版本说明", "版本时间");
        List<String> fieldName = Arrays.asList("config_items", "associated_num", "associated_name", "plan_time", "main_user_name", "version", "version_describe", "version_time");
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            record.set("type", FormTypeEnum.TEXT.getType());
            fields.add(record);
        }
    }
}
