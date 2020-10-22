package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.TaskHoursLockService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class TaskHoursLockController extends Controller {

    @Inject
    TaskHoursLockService taskHoursLockService;

    /**
     * 分页查询工时锁定
     */
    @Permissions({"project:hoursLock:index"})
    public void pageList(BasePageRequest basePageRequest){
        renderJson(taskHoursLockService.queryPageList(basePageRequest));
    }

    /**
     * 添加工时锁定
     */
    @Permissions({"project:hoursLock:save"})
    public void setHoursLock(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskHoursLockService.setHoursLock(jsonObject));
    }

    /**
     * !!!导出
     */
    @Permissions({"project:hoursLock:excelexport"})
    public void export(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)taskHoursLockService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Record record : records) {
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
            response.setHeader("Content-Disposition", "attachment;filename=hoursLock.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void getFields(List<Record> fields){
        List<String> names = Arrays.asList("序号", "锁定时间", "操作时间");
        List<String> fieldName = Arrays.asList("id", "lock_time", "create_time");
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            record.set("type", FormTypeEnum.TEXT.getType());
            fields.add(record);
        }
    }
}
