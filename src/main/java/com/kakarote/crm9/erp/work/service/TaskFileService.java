package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.TaskFile;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyy
 */
public class TaskFileService {

    public final static Prop prop = PropKit.use("config/crm9-config.txt");

    /**
     * 分页查询
     */
    public R queryPageList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv by = Kv.by("taskId", jsonObject.get("taskId")).set("workId",jsonObject.get("workId")).set("type",jsonObject.get("type")).set("search",jsonObject.get("search"));
        by.set("fileAttr",jsonObject.get("fileAttr"));
        SqlPara sqlPara = Db.getSqlPara("task.file.queryPageList", by);
        if (jsonObject.get("excel") != null) {
            List<Record> records = Db.find(sqlPara);
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", records);
            return R.ok().put("data", resultJsonObject);
        } else {
            Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), sqlPara);
            List<Record> list = paginate.getList();
            for (Record record : list) {
                try {
                    if (record.getStr("subtasks_id").equals(record.getStr("task_id"))) {
                        record.set("subtasks_name", null);
                        record.set("subtasks_id", null);
                    }
                } catch (NullPointerException e) {
                    continue;
                }
            }
            return R.ok().put("data", paginate);
        }
    }

    /**
     * 添加修改
     *
     * @param
     * @param
     */
    public R upload(JSONObject jsonObject) {
        TaskFile taskFile = jsonObject.getObject("entity", TaskFile.class);
        String name = taskFile.getName();
        taskFile.setFileName(name);
        int i = name.lastIndexOf(".");
        name = name.substring(0, i);
        taskFile.setName(name);
        if(taskFile.getType()==2){
            if("资料成果".equals(taskFile.getFileAttr())){
                int temp ;
                if(taskFile.getSubtasksId()==null){
                    temp = Db.queryInt("select count(1) from 72crm_task_hours where subtasks_id =?",taskFile.getTaskId());
                }else {
                    temp = Db.queryInt("select count(1) from 72crm_task_hours where subtasks_id =?",taskFile.getSubtasksId());
                }
                if(temp == 0){
                    return R.error("该任务没有存在的工时");
                }
            }
        }

        if (taskFile.getSubtasksId() == null) {
            taskFile.setSubtasksId(taskFile.getTaskId());
        }
        Long workId = Aop.get(Task.class).findById(taskFile.getSubtasksId()).getWorkId();
        taskFile.setWorkId(workId);
        if (taskFile.getFileId() == null) {
            taskFile.setCreateUserId(BaseUtil.getUserId());
            taskFile.setCreateTime(new Date());
            taskFile.save();
        } else {
            taskFile.update();
        }
        HashSet<Long> set = new HashSet<>();
        set.add(taskFile.getCreateUserId());
        WorkService.setWorkRelation(set,taskFile.getFileId(), WorkUserEnum.HOURS.getType(),taskFile.getWorkId());
        return R.ok();
    }

    /**
     * 下载任务文件
     *
     * @param
     */
    public File downFile(String fileId) {
        fileId = "/file/downFile?fileId=" + fileId;
        //查询是否是自己所属的任务
        /*Long userId = BaseUtil.getUserId();
        TaskFile taskFile = TaskFile.dao.findFirst("SELECT * FROM `72crm_task_file` where file_path=? LIMIT 0,1", fileId);
        Integer taskId = taskFile.getTaskId();
        Integer subtasksId = taskFile.getSubtasksId();
        Integer integer = Db.queryInt("select count(1) from 72crm_work_user_record where user_id =? and (num_id=? || num_id=?) and type=1", userId, taskId, subtasksId);
        if(integer == null){
            return null;
        }*/
        AdminFile adminFile = AdminFile.dao.findFirst("SELECT * FROM `72crm_admin_file` where file_path=? LIMIT 0,1", fileId);
        if (adminFile != null) {
            File file = new File(adminFile.getPath());
            return new File(adminFile.getPath());
        } else {
            return null;
        }
        /*if (taskFile != null) {
            File file = new File(taskFile.getPath());
            return new File(taskFile.getPath());
        } else {
            return null;
        }*/
    }


    /**
     * 通过ID查询
     *
     * @param id 文件ID
     */
    public R queryById(String id) {
        if (id == null) {
            return R.error("id参数为空");
        }
        Record first = Db.findFirst(Db.getSql("task.file.queryById"), id);
        if(first.getInt("task_id").equals(first.getInt("subtasks_id"))){
            first.set("subtasks_id",null);
            first.set("subtasks_name",null);
        }
        return R.ok().put("data", first);
    }

    /**
     * 通过ID删除,包括文件
     *
     * @param id 文件ID
     */
    @SuppressWarnings("all")
    @Before(Tx.class)
    public R removeById(String ids) {
        if (ids == null) {
            return R.error("id参数为空");
        }
        List<String> strings = Arrays.asList(ids.split(","));
        for (String id : strings) {
            TaskFile taskFile = TaskFile.dao.findById(id);
            if (taskFile != null) {
                try {
                    File file = new File(taskFile.getPath());
                    if (file.exists() && !file.isDirectory()) {
                        file.delete();
                        taskFile.delete();
                    }
                } catch (NullPointerException e) {
                    taskFile.delete();
                }
            }
        }
        return R.ok();
    }

    /**
     * 根据id删除
     *
     * @param ids
     */
   /* public R deleteByIds( List<String> ids) {
        Db.template("task.file.deleteFileByIds", Kv.by("ids", ids)).delete();
        return R.ok();
    }*/


    /**
     * 上传文件
     *
     * @param fileType oss,file,img
     */
    public R ossUpload(UploadFile file, String batchId, String fileType) {
        if (StrUtil.isBlank(fileType)) {
            String fileValue = "oss:" + fileType;
            fileType = fileValue;
        }
        if (batchId == null || "".equals(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        AdminFile adminFile = new AdminFile();
        adminFile.setBatchId(batchId);
        adminFile.setCreateTime(new Date());
        adminFile.setCreateUserId(3L);
        adminFile.setPath(file.getFile().getAbsolutePath());
        adminFile.setFilePath("/file/downFile?fileId=" + IdUtil.simpleUUID());
        adminFile.setName(file.getFileName());
        adminFile.setFileType(fileType);
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length() / 1000 + "KB").put("file_id", adminFile.getFileId()) : R.error();
    }


    /**
     * @param file    文件
     * @param batchId 批次ID
     */
    public R upload(UploadFile file, String batchId, String fileType, String prefix) {
        if (batchId == null || "".equals(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        AdminFile adminFile = new AdminFile();
        adminFile.setBatchId(batchId);
        adminFile.setCreateTime(new Date());
        adminFile.setCreateUserId(BaseUtil.getUser().getUserId());
        adminFile.setPath(file.getFile().getAbsolutePath());
        adminFile.setFilePath("/file/downFile?fileId=" + IdUtil.simpleUUID());
        adminFile.setName(file.getFileName());
        if (StrUtil.isNotBlank(fileType)) {
            adminFile.setFileType(fileType);
        }
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length()).put("file_id", adminFile.getFileId()) : R.error();
    }
}





/* public final static Prop prop = PropKit.use("config/crm9-config.txt");

 *//**
 * 分页查询
 *
 * @param file 文件
 * @param 下载任务文件
 * @param fileId
 * <p>
 * 通过ID查询
 * @param id 文件ID
 * <p>
 * 通过ID删除,包括文件
 * @param id 文件ID
 * <p>
 * 根据id删除
 * @param ids
 * <p>
 * 上传文件
 * @param fileType oss,file,img
 * @param file    文件
 * @param batchId 批次ID
 * @param file 文件
 * @param 下载任务文件
 * @param fileId
 * <p>
 * 通过ID查询
 * @param id 文件ID
 * <p>
 * 通过ID删除,包括文件
 * @param id 文件ID
 * <p>
 * 根据id删除
 * @param ids
 * <p>
 * 上传文件
 * @param fileType oss,file,img
 * @param file    文件
 * @param batchId 批次ID
 *//*
    public R queryPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv by = Kv.by("taskId", jsonObject.get("taskId"));
        SqlPara sqlPara = Db.getSqlPara("task.file.queryPageList", by);
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(),sqlPara );
        return R.ok().put("data",paginate);
    }

    *//**
 * @param file 文件
 * @param
 *//*
    public R upload(UploadFile file, TaskFile taskFile, String prefix) {
        String batchId = taskFile.getBatchId();
        if (batchId == null || "".equals(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        if(taskFile.getSubtasksId() == null){
            taskFile.setSubtasksId(taskFile.getTaskId());
        }
        //taskFile.setName(file.getFileName());
        taskFile.setPath(file.getFile().getAbsolutePath());
        taskFile.setFilePath("/file/downFile?fileId=" + IdUtil.simpleUUID());
        taskFile.setSize(file.getFile().length());
        taskFile.setCreateUserId(BaseUtil.getUserId());
        taskFile.setCreateTime(new Date());
        taskFile.setUpdateTime(new Date());
        return taskFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", taskFile.getFilePath()).put("size", file.getFile().length()).put("file_id", taskFile.getFileId()) : R.error();
    }

    public R uploadByBatchId(UploadFile file, TaskFile taskFile, String prefix) {
        Db.delete("delete from 72crm_task_file where batch_id = ?", taskFile.getBatchId());
        return upload(file, taskFile, prefix);
    }

    public R uploadById(UploadFile file, TaskFile taskFile, String prefix) {
        Db.delete("delete from 72crm_task_file where file_id = ?", taskFile.getFileId());
        return upload(file, taskFile, prefix);
    }

    *//**
 * 下载任务文件
 *
 * @param fileId
 *//*
    public File downFile(Integer fileId) {

        //查询是否是自己所属的任务
        Long userId = BaseUtil.getUserId();
        TaskFile taskFile = TaskFile.dao.findFirst("SELECT * FROM `72crm_task_file` where file_id=? LIMIT 0,1", fileId);
        Integer taskId = taskFile.getTaskId();
        Integer subtasksId = taskFile.getSubtasksId();
        Integer integer = Db.queryInt("select count(1) from 72crm_work_user where user_id =? and (num_id=? || num_id=?) and type=1", userId, taskId, subtasksId);
        if(integer == null){
            return null;
        }
        *//*List<Object> query = Db.query(Db.getSql("work.task.findTaskByFileId"), taskFile.getBatchId(), userId);
        if (CollectionUtils.isEmpty(query)) {
            return null;
        }*//*
        if (taskFile != null) {
            File file = new File(taskFile.getPath());
            return new File(taskFile.getPath());
        } else {
            return null;
        }
    }

    *//**
 * 通过ID查询
 * @param id 文件ID
 *//*
    public R queryById(String id) {
        if (id == null) {
            return R.error("id参数为空");
        }
        return R.ok().put("data", Db.find(Db.getSql("task.file.queryById"), id));
    }

    *//**
 * 通过ID删除,包括文件
 *
 * @param id 文件ID
 *//*
    @SuppressWarnings("all")
    @Before(Tx.class)
    public R removeById(String ids) {
        if (ids == null) {
            return R.error("id参数为空");
        }
        List<String> strings = Arrays.asList(ids.split(","));
        for (String id : strings) {
            TaskFile taskFile = TaskFile.dao.findById(id);
            if (taskFile != null) {
                File file = new File(taskFile.getPath());
                if (file.exists() && !file.isDirectory()) {
                    file.delete();
                    taskFile.delete();
                }
            }
        }
        return R.ok();
    }

    *//**
 * 根据id删除
 *
 * @param ids
 *//*
    public R deleteByIds(List<Long> ids) {
        Db.template("task.file.deleteFileByIds", Kv.by("ids", ids)).delete();
        return R.ok();
    }


    *//**
 * 上传文件
 *
 * @param fileType oss,file,img
 *//*
    public R ossUpload(UploadFile file, String batchId, String fileType) {
        if (StrUtil.isBlank(fileType)) {
            String fileValue = "oss:" + fileType;
            fileType = fileValue;
        }
        if (batchId == null || "".equals(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        AdminFile adminFile = new AdminFile();
        adminFile.setBatchId(batchId);
        adminFile.setCreateTime(new Date());
        adminFile.setCreateUserId(3L);
        adminFile.setPath(file.getFile().getAbsolutePath());
        adminFile.setFilePath("/file/downFile?fileId=" + IdUtil.simpleUUID());
        adminFile.setName(file.getFileName());
        adminFile.setFileType(fileType);
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length() / 1000 + "KB").put("file_id", adminFile.getFileId()) : R.error();
    }






    *//**
 * @param file    文件
 * @param batchId 批次ID
 *//*
    public R upload(UploadFile file, String batchId, String fileType, String prefix) {
        if (batchId == null || "".equals(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        AdminFile adminFile = new AdminFile();
        adminFile.setBatchId(batchId);
        adminFile.setCreateTime(new Date());
        adminFile.setCreateUserId(BaseUtil.getUser().getUserId());
        adminFile.setPath(file.getFile().getAbsolutePath());
        adminFile.setFilePath("/file/downFile?fileId=" + IdUtil.simpleUUID());
        adminFile.setName(file.getFileName());
        if (StrUtil.isNotBlank(fileType)) {
            adminFile.setFileType(fileType);
        }
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length()).put("file_id", adminFile.getFileId()) : R.error();
    }*/
