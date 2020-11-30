package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.service.WorkHistoricalRecordService;
import com.kakarote.crm9.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkHistoricalRecordController extends Controller {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 查询历史记录
     * @param basePageRequest
     */
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(workHistoricalRecordService.queryRecordList(basePageRequest,true));
    }

    /**
     * 导出!!!
     */
    //@Permissions({"project:milestone:excelexportHistorical"})
    public void historicalRecordExport(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            R r = workHistoricalRecordService.queryRecordList(basePageRequest, true);
            if((int)r.get("code") != 0){
                renderJson(r);
            }
            JSONObject jsonObject = (JSONObject)r.get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"create_time","id","operation_id","operation_user_id","work_id","task_id","module_two"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            addField(headList,json.getInteger("type"));
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
            response.setHeader("Content-Disposition", "attachment;filename=historical.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void addField(List<Record> fields,Integer type){
        List<String> names =new ArrayList<>();
        List<String> fieldName = new ArrayList<>();
        WorkRecordEnum parse = WorkRecordEnum.parse(type);
        List<Integer> integers = Arrays.asList(2, 3, 4, 5, 6,13);
        List<Integer> task = Arrays.asList(7, 12, 11);
        if (integers.contains(parse.getType())) {
            names.add("模块");
            fieldName.add("module");
        } else if (task.contains(parse.getType())) {
            names.add("模块1");
            fieldName.add("module_one");
        }else {
            names.add("项目名称");
            names.add("模块1");
            names.add("模块2");
            names.add("模块3");
            fieldName.add("work_name");
            fieldName.add("module_one");
            fieldName.add("module_two");
            fieldName.add("module_three");
        }
        names.addAll(Arrays.asList("操作人", "历史变更时间", "模块", "行为", "字段名称", "内容变更前", "内容变更后", "编号"));
        fieldName.addAll(Arrays.asList("operation_user_name", "change_time", "module", "behavior", "field", "content_before", "content_after", "number"));

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
