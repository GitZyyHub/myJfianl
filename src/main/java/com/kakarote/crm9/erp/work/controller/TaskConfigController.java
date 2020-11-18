package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmMarketing;

import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.TaskConfigService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zyy
 */
public class TaskConfigController extends Controller {

    @Inject
    TaskConfigService taskConfigService;

    /**
     * 分页查询任务配置计划
     * @param basePageRequest
     */
    @Permissions({"project:configManage:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(taskConfigService.queryPageList(basePageRequest));
    }

    /**
     * 设置任务配置计划
     * @param
     */
    @Permissions({"project:configManage:save","project:configManage:update"})
    public void setConfig(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskConfigService.setConfig(jsonObject));
    }

    /**
     * 根据ID查询任务配置计划
     */
    public void queryById(@Para("id") Integer id){
        renderJson(taskConfigService.queryById(id));
    }

    /**
     * 批量删除任务配置计划
     */
    @Permissions({"project:configManage:delete"})
    public void delConfig(@Para("ids")String ids){
        renderJson(taskConfigService.delConfig(ids));
    }

    /**
     * 导出!!!
     */
    @Permissions({"project:configManage:excelexport"})
    public void configManageExport(BasePageRequest<Work> basePageRequest){

        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)taskConfigService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"config_id","create_time","create_user_id","main_user_id","type","update_time","associated_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            //List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.WORK.getType()));
            List<Record> headList = new ArrayList<>();
            addField(headList,true);
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
            response.setHeader("Content-Disposition", "attachment;filename=taskConfig.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void addField(List<Record> headList,boolean tag){

            headList.add(new Record().set("fieldName", "config_items").set("name", "配置项"));
            headList.add(new Record().set("fieldName", "plan_time").set("name", "计划时间"));
            if(tag){
                headList.add(new Record().set("fieldName", "associated_num").set("name", "任务编号"));
                headList.add(new Record().set("fieldName", "associated_name").set("name", "任务名称"));
            }else {
                headList.add(new Record().set("fieldName", "associated_num").set("name", "项目编号"));
                headList.add(new Record().set("fieldName", "associated_name").set("name", "项目名称"));
            }
            headList.add(new Record().set("fieldName", "main_user_name").set("name", "负责人"));

            headList.add(new Record().set("fieldName", "create_user_name").set("name", "创建人"));
            headList.add(new Record().set("fieldName", "main_user_dept").set("name", "负责人部门"));
            headList.add(new Record().set("fieldName", "version").set("name", "版本"));
            headList.add(new Record().set("fieldName", "version_time").set("name", "版本时间"));
            headList.add(new Record().set("fieldName", "version_describe").set("name", "版本说明"));

    }


}
