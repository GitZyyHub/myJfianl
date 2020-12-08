package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.LoginFormCookie;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.TaskFile;
import com.kakarote.crm9.erp.work.service.TaskFileService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.Control;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author zyy
 */
public class TaskFileController extends Controller {

    @Inject
    TaskFileService taskFileService;

    /**
     * 分页查询任务资料
     */
    @Permissions({"project:taskFile:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(taskFileService.queryPageList(basePageRequest));
    }

    /**
     * 添加任务资料
     */
    @Permissions({"project:taskFile:save","project:taskFile:update"})
    public void upload(@Para("") TaskFile taskFile) {
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskFileService.upload(jsonObject));
    }

    /**
     * 下载文件
     * @param fileId
     */
    @Permissions({"project:taskFile:download"})
    @LoginFormCookie
    public void downFile(@Para("fileId") String fileId){
        HttpServletResponse response = getResponse();
        response.setContentType("text/html;charset=utf-8");
        File file = taskFileService.downFile(fileId);
        if (file != null){
            renderFile(file);
            return;
        }else {
            renderJson(R.error("没有权限"));
        }
    }

    /**
     * 删除文件 根据id
     */
    /*@Permissions({"project:taskFile:delete"})
    public void deleteByIds(@Para("ids") String ids){
        List<String> strings = Arrays.asList(ids.split(","));
        renderJson(taskFileService.deleteByIds(strings));
    }*/


    /**
     * 上传附件，同时清除之前同一batchId的附件
     */
    /*public void uploadById(@Para("")TaskFile taskFile) {
        String prefix = BaseUtil.getDate();
        renderJson(taskFileService.uploadById(getFile("file", prefix),taskFile, prefix));
    }*/

    /**
     *
     * 通过ID查询
     */
    public void queryById(@Para("id")String id) {
        renderJson(taskFileService.queryById(id));
    }

    /**
     *
     * 通过ID删除
     */
    @Permissions({"project:taskFile:delete"})
    public void removeById() {
        renderJson(taskFileService.removeById(getPara("ids")));
    }

    /**
     * @author zhangzhiwei
     * 上传附件 type oss：上传到阿里云 file,img :上传到本地
     */
    public void ossUpload(){
        String prefix = BaseUtil.getDate();
        renderJson(taskFileService.ossUpload(getFile("file", prefix), getPara("batchId"), getPara("type")));
    }


    /**
     * 导出
     */
    public void export(BasePageRequest basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)taskFileService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"batch_id","file_id","file_name","file_path","file_type","size","subtasks_id","task_id"};
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
            response.setHeader("Content-Disposition", "attachment;filename=taskFile.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void getFields(List<Record> fields){
        List<String> names = Arrays.asList("文件名称", "资源属性", "任务名称", "子任务名称", "负责人", "任务状态", "创建人","上传时间");
        List<String> fieldName = Arrays.asList("name", "file_attr", "task_name", "subtasks_name", "main_user_name", "status", "create_user_name","create_time");
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
