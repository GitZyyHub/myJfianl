package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.service.WorkChangeService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkChangeController extends Controller {

    @Inject
    WorkChangeService workChangeService;

    /**
     * 分页查询变更表
     */
    @Permissions({"project:change:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(workChangeService.queryPageList(basePageRequest));
    }

    /**
     * 设置变更表
     */
    @Permissions({"project:change:save","project:change:update"})
    public void setWorkChange(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workChangeService.setWorkChange(jsonObject));
    }

    /**
     * 批量删除
     */
    @Permissions({"project:change:delete"})
    public void delWorkChange(@Para("ids") String ids){
        renderJson(workChangeService.delWorkChange(ids));
    }

    /**
     * 根据Id查询变更表
     */
    public void queryById(@Para("id") Integer id){
        renderJson(workChangeService.queryById(id));
    }

    /**
     * 导出!!!
     */
    @Permissions({"project:change:excelexport"})
    public void changeExport(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)workChangeService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"batch_id","forward_user","change_id","change_num","create_time","forward_main_user_id","owner_user","owner_user","owner_user_id","update_time","work_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            addField(headList);
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
            response.setHeader("Content-Disposition", "attachment;filename=change.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void addField(List<Record> fields){
        List<String> names = Arrays.asList("变更项", "变更类型", "提出时间", "提出人员", "变更原因", "变更方案", "变更影响", "变更负责人", "参与人", "开始时间", "结束时间", "说明", "是否通过");
        List<String> fieldName = Arrays.asList("change_project", "change_type", "forward_time", "forward_user_name", "forward_why", "forward_plan", "forward_impact", "forward_main_user_name", "owner_user_name", "start_time", "end_time", "instructions", "is_through");
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            record.set("type", FormTypeEnum.TEXT.getType());
            if(i==6){
                record.set("type",FormTypeEnum.SELECT.getType());
                record.set("options","已到位,未到位");
            }
            fields.add(record);
        }
    }
}
