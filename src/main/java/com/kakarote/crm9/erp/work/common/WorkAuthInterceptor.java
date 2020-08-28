package com.kakarote.crm9.erp.work.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.WorkPermissions;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class WorkAuthInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        //TODO 权限功能后台拦截
        WorkPermissions permissions=invocation.getMethod().getAnnotation(WorkPermissions.class);
        if (permissions != null && permissions.value().length > 0) {
            Integer workId;
            HttpServletRequest request = invocation.getController().getRequest();
            String contentType = request.getHeader("Content-Type");
            if ("/work/updateOrder".equals(request.getRequestURI())){
                JSONObject jsonObject = JSON.parseObject(invocation.getController().getRawData());
                if (jsonObject.getJSONArray("toList").size()>0){
                    Integer taskId = (Integer) jsonObject.getJSONArray("toList").get(0);
                    workId = Db.queryInt("select work_id from 72crm_task where task_id = ?", taskId);
                }else {
                    Integer taskId = (Integer) jsonObject.getJSONArray("fromList").get(0);
                    workId = Db.queryInt("select work_id from 72crm_task where task_id = ?", taskId);
                }
            } else if ("/task/setWorkChildTaskStatus".equals(request.getRequestURI()) || "/task/updateWorkChildTask".equals(request.getRequestURI()) || "/task/deleteWorkChildTask".equals(request.getRequestURI())) {
                Integer taskId = invocation.getController().getInt("taskId");
                workId = Db.queryInt("select work_id from 72crm_task where task_id = (select pid from 72crm_task where task_id = ?)", taskId);
            } else if ("/task/addWorkChildTask".equals(request.getRequestURI())) {
                Integer pid = invocation.getController().getInt("pid");
                workId = Db.queryInt("select work_id from 72crm_task where task_id = ?", pid);
            } else if ("/work/excelImport".equals(request.getRequestURI())) {
                UploadFile file = invocation.getController().getFile();
                workId = invocation.getController().getInt("workId");
            }else {
                if ("application/json;charset=UTF-8".equals(contentType)){
                    JSONObject jsonObject = JSON.parseObject(invocation.getController().getRawData());
                    workId = jsonObject.getInteger("workId");
                    if (workId == null) {
                        Integer taskId = jsonObject.getInteger("taskId");
                        workId = Db.queryInt("select work_id from 72crm_task where task_id = ?", taskId);
                    }
                }else {
                    workId = invocation.getController().getInt("workId");
                    if (workId == null) {
                        Integer taskId = invocation.getController().getInt("taskId");
                        workId = Db.queryInt("select work_id from 72crm_task where task_id = ?", taskId);
                    }
                }
            }

            JSONObject jsonObject= Aop.get(WorkService.class).auth(BaseUtil.getUserId(),workId);
            //组装应有权限列表
            List<String> arr= AuthUtil.queryAuth(jsonObject, "");
            boolean isRelease=false;
            for (String key : permissions.value()) {
                if(!isRelease){
                    if(arr.contains(key)){
                        isRelease=true;
                    }
                }
            }
            if(!isRelease){
                invocation.getController().renderJson(R.error("无权访问"));
                return;
            }
        }
        invocation.invoke();
    }
}
