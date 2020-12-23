package com.kakarote.crm9.erp.admin.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.PathKit;
import com.kakarote.crm9.common.annotation.LoginFormCookie;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class AdminFileController extends Controller {
    @Inject
    private AdminFileService adminFileService;

    public void index() {
        renderJson(R.ok());
    }

    /**
     * @author zhangzhiwei
     * 上传附件
     */
    public void upload() {
        String prefix = BaseUtil.getDate();
        renderJson(adminFileService.upload(getFile("file", prefix), getPara("batchId"), getPara("type"), prefix));
    }

    /**
     * 上传附件，同时清除之前同一batchId的附件
     */
    public void uploadByBatchId() {
        String prefix = BaseUtil.getDate();
        renderJson(adminFileService.uploadByBatchId(getFile("file", prefix), getPara("batchId"), getPara("type"), prefix));
    }

    /**
     * @author zhangzhiwei
     * 通过批次ID查询
     */
    public void queryByBatchId() {
        renderJson(R.ok().put("data", adminFileService.queryByBatchId(getPara("batchId"))));
    }

    /**
     * @author zhangzhiwei
     * 通过ID查询
     */
    public void queryById() {
        renderJson(adminFileService.queryById(getPara("id")));
    }

    /**
     * @author zhangzhiwei
     * 通过ID删除
     */
    public void removeById() {
        renderJson(adminFileService.removeById(getPara("id")));
    }

    /**
     * @author zhangzhiwei
     * 通过批次ID删除
     */
    public void removeByBatchId() {
        adminFileService.removeByBatchId(getInt("type", 0), getPara("batchId"));
        renderJson(R.ok());
    }

    /**
     * @author zhangzhiwei
     * 重命名文件
     */
    public void renameFileById() {
        AdminFile file = new AdminFile();
        file.setFileId(getInt("fileId"));
        file.setName(getPara("name"));
        renderJson(adminFileService.renameFileById(file) ? R.ok() : R.error());
    }

    /**
     * 下载文件
     * @param fileId
     */

   /* public void downFile(@Para("fileId") String fileId) {
        fileId="/file/downFile?fileId="+fileId;
        AdminFile adminFile = AdminFile.dao.findFirst("SELECT * FROM `72crm_admin_file` where file_path=? LIMIT 0,1",fileId);
        if (adminFile != null) {
            renderFile(new File(adminFile.getPath()));
            return;
        }
        renderNull();
    }*/

    /**
     * @author zhangzhiwei
     * 上传附件 type oss：上传到阿里云 file,img :上传到本地
     */
    public void ossUpload() {
        String prefix = BaseUtil.getDate();
        renderJson(adminFileService.ossUpload(getFile("file", prefix), getPara("batchId"), getPara("type")));
    }

    /**
     * 下载文件
     *
     * @param fileId
     */
    @LoginFormCookie
    public void downFile(@Para("fileId") String fileId) {
        // File file = adminFileService.downFile(fileId);
        fileId = "/file/downFile?fileId=" + fileId;
        AdminFile adminFile = AdminFile.dao.findFirst("SELECT * FROM `72crm_admin_file` where file_path=? LIMIT 0,1", fileId);
        File file = null;
        if (adminFile != null) {
            file = new File(adminFile.getPath());
        }
        String name = file.getName();
        name =  name.substring(name.lastIndexOf("."));

        if (file != null) {
            renderFile(file,"file"+name);
            return;
        }
        renderNull();
    }

    /**
     * 删除文件 根据id
     */
    public void deleteByIds() {
        List<Long> ids = JSON.parseArray(getRawData(), Long.class);
        renderJson(adminFileService.deleteByIds(ids));
    }

    /**
     * 删除文件 根据批次
     */
    public void deleteByBatchIds() {
        List<String> batchIds = JSON.parseArray(getRawData(), String.class);
        renderJson(adminFileService.deleteByBatchIds(batchIds));
    }

    /**
     * app软件下载
     */
    public void  softwareDownload (){
        String path = PathKit.getWebRootPath() + File.separator + "WEB-INF/classes/app-release.apk";
        System.out.println(path);
        File file = new File(path);
        System.out.println(file.exists());
        if (file.exists()) {
            renderFile(file);
        } else {
            renderJson("文件不存在");
        }

    }
}

/*HttpServletResponse response = getResponse();
        try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));ServletOutputStream outputStream = response.getOutputStream();){
            byte[] buf = new byte[1024];
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            String name = file.getName();
            name =  name.substring(name.lastIndexOf("."));
            response.setHeader("Content-Disposition", "attachment; filename=file." + name);
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码

            int len = 0;
            while ((len = br.read(buf)) > 0)
                outputStream.write(buf, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
