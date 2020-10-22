package com.kakarote.crm9.erp.work.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.LoginFormCookie;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.work.entity.TaskFile;
import com.kakarote.crm9.erp.work.service.TaskFileService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author zyy
 */
public class WorkTaskFileController extends Controller {

    @Inject
    TaskFileService taskFileService;

    /**
     * 分页查询任务资料
     */
    @Permissions({"project:workFile:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(taskFileService.queryPageList(basePageRequest));
    }

    /**
     * 添加任务资料
     */
    @Permissions({"project:workFile:save","project:workFile:update"})
    public void upload(@Para("") TaskFile taskFile) {
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskFileService.upload(jsonObject));
    }

    /**
     * 下载文件
     * @param fileId
     */
    @Permissions({"project:workFile:download"})
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
     * 导出任务资料!!!
     */
    @Permissions({"project:workFile:excelexport"})
    public void excelexport(){

    }

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
    public void queryById() {
        renderJson(taskFileService.queryById(getPara("id")));
    }

    /**
     *
     * 通过ID删除
     */
    @Permissions({"project:workFile:delete"})
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



}
