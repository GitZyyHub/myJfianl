package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminFileService {
    public final static Prop prop = PropKit.use("config/crm9-config.txt");

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
        adminFile.setFilePath("/file/downFile?fileId="+IdUtil.simpleUUID());
        adminFile.setName(file.getFileName());
        if (StrUtil.isNotBlank(fileType)) {
            adminFile.setFileType(fileType);
        }
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length()).put("file_id", adminFile.getFileId()) : R.error();
    }

    public R uploadByBatchId(UploadFile file, String batchId, String fileType, String prefix) {
        Db.delete("delete from 72crm_admin_file where batch_id = ?", batchId);
        return upload(file, batchId, fileType, prefix);
    }

    /**
     * 通过批次ID查询
     *
     * @param batchId 批次ID
     */
    public void queryByBatchId(String batchId, Record record) {
        if (batchId == null || "".equals(batchId)) {
            record.set("img", new ArrayList<>()).set("file", new ArrayList<>());
            return;
        }
        List<AdminFile> adminFiles = AdminFile.dao.find(Db.getSql("admin.file.queryByBatchId"), batchId);

        Map<String, List<AdminFile>> collect = adminFiles.stream().collect(Collectors.groupingBy(AdminFile::getFileType));
        collect.forEach(record::set);
        if (!record.getColumns().containsKey("img") || record.get("img") == null) {
            record.set("img", new ArrayList<>());
        }
        if (!record.getColumns().containsKey("file") || record.get("file") == null) {
            record.set("file", new ArrayList<>());
        }

    }
    public Record queryByBatchId(String batchId) {
        Record record=new Record();
        if (batchId == null || "".equals(batchId)) {
            record.set("img", new ArrayList<>()).set("file", new ArrayList<>());
            return record;
        }
        List<AdminFile> adminFiles = AdminFile.dao.find(Db.getSql("admin.file.queryByBatchId"), batchId);

        Map<String, List<AdminFile>> collect = adminFiles.stream().collect(Collectors.groupingBy(AdminFile::getFileType));
        collect.forEach(record::set);
        if (!record.getColumns().containsKey("img") || record.get("img") == null) {
            record.set("img", new ArrayList<>());
        }
        if (!record.getColumns().containsKey("file") || record.get("file") == null) {
            record.set("file", new ArrayList<>());
        }
        return record;
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
        return R.ok().put("data", AdminFile.dao.findById(id));
    }

    /**
     * 通过ID删除
     *
     * @param id 文件ID
     */
    @SuppressWarnings("all")
    @Before(Tx.class)
    public R removeById(String id) {
        if (id == null) {
            return R.error("id参数为空");
        }
        AdminFile adminFile = AdminFile.dao.findById(id);
        if (adminFile != null) {
            File file = new File(adminFile.getPath());
            if (file.exists() && !file.isDirectory()) {
                file.delete();
                adminFile.delete();
            }
        }
        return R.ok();
    }
    /**
     * 通过批次ID删除
     *
     * @param batchId 批次ID
     */
    public void removeByBatchId(String... batchId) {
        removeByBatchId(0,batchId);
    }
    /**
     * 通过批次ID删除
     *
     * @param batchId 批次ID
     */
    public void removeByBatchId(Integer type,String... batchId) {
        if (batchId.length==0) {
            return;
        }
        String fileType="";
        if(type.equals(1)){
            fileType="file";
        }else if(type.equals(2)){
            fileType="img";
        }
        Kv kv=Kv.by("batchId",batchId).set("type",fileType);
        List<String> paths = Db.template("admin.file.queryPathByBatchId",kv).query();
        //删除文件
        paths.stream().map(File::new).filter(file -> file.exists() && !file.isDirectory()).forEach(File::delete);
        //删除数据库数据
        Db.template("admin.file.deleteFileByBatchId",kv).delete();
    }

    public boolean renameFileById(AdminFile file) {
        return Db.update("72crm_admin_file", "file_id", file.toRecord());
    }
    /**
     * 上传文件
     * @param fileType   oss,file,img
     */
    public R ossUpload(UploadFile file,String batchId, String fileType){
        if (StrUtil.isBlank(fileType)) {
            String fileValue = "oss:"+fileType;
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
        adminFile.setFilePath("/file/downFile?fileId="+IdUtil.simpleUUID());
        adminFile.setName(file.getFileName());
        adminFile.setFileType(fileType);
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length() / 1000 + "KB").put("file_id", adminFile.getFileId()) : R.error();
    }
    /**
     * 下载文件
     * @param fileId
     */
    public File downFile(String fileId){
        fileId="/file/downFile?fileId="+fileId;
        AdminFile adminFile = AdminFile.dao.findFirst("SELECT * FROM `72crm_admin_file` where file_path=? LIMIT 0,1",fileId);
        if (adminFile != null) {
            File file = new File(adminFile.getPath());
            return new File(adminFile.getPath());
        }else {
            return null;
        }
    }
    /**
     * 删除文件 根据id删除
     * @param ids
     */
    public R deleteByIds(List<Long> ids){
        Db.template("admin.file.deleteFileByIds",Kv.by("ids",ids)).delete();
        return R.ok();
    }
    /**
     * 删除文件 根据批次id删除
     * @param batchIds
     */
    public R deleteByBatchIds(List<String> batchIds){
        Db.template("admin.file.deleteFileByBatchId",Kv.by("batchId",batchIds)).delete();
        return R.ok();
    }
}
