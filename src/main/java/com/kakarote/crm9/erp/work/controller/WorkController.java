package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.R;
import com.mysql.jdbc.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkController extends Controller {

    @Inject
    WorkService workService;

    @Inject
    AdminFieldService adminFieldService;

    @Inject
    AdminFileService adminFileService;

    /**
     * 分页查询项目
     */
    @Permissions({"project:projectManage:index"})
    public void queryPageList(BasePageRequest<Work> basePageRequest) {
        renderJson(workService.queryPageList(basePageRequest, false));
    }

    /**
     * 设置项目（添加/修改）
     */
    @Permissions({"project:projectManage:save","project:projectManage:update"})
    public void setWork() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(workService.setWork(jsonObject));
    }

    /**
     *查询待办项目???
     */
    public void waiting(BasePageRequest<Work> basePageRequest){
        renderJson(workService.waiting(basePageRequest, false));
    }


    /**
     * 删除项目
     */
    @Permissions({"project:projectManage:delete"})
    public void delWork() {
        String workIds = getPara("workIds");
        renderJson(workService.delWork(workIds));
    }

    /**
     * 批量修改项目阶段
     */
    @Permissions({"project:projectManage:updatePhase"})
    public void updateWorkPhase(@Para("workIds")String workIds,@Para("phase") String phase,@Para("checkUserId") Long checkUserId){
        renderJson(workService.updateWorkPhase(workIds,phase,checkUserId));
    }

    /**
     * 根据ID查询项目基本信息
     */
    public void queryById(@Para("id") Integer id) {
        renderJson(workService.queryById(id));
    }

    /**
     * 导入
     * @param file
     * @param ownerUserId
     * @param repeatHandling
     */
    public void excelImport(@Para("file") UploadFile file,@Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.WORK, file.getFile().getAbsolutePath(), null, null, repeatHandling,null);
        renderJson(R.ok().put("data",messageId));
    }
    /**
     * 查询项目详情/项目扩展详情/项目历史记录/需求详情
     */
    @Permissions({"project:projectManage:readDetails"})
    public void findFieldDetails(@Para("id")Integer id){
        renderJson(R.ok().put("data",workService.findFieldDetails(id)));
    }

    /**
     * 导出项目
     */
    @Permissions({"project:projectManage:excelexport"})
    public void workExport(BasePageRequest<Work> basePageRequest) {
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject ;
            if(json.get("isToDo")!=null){ //是否是查询带我审核的
                jsonObject = (JSONObject)workService.waiting(basePageRequest, false).get("data");
            }else {
                jsonObject = (JSONObject)workService.queryPageList(basePageRequest, false).get("data");
            }
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"batch_id","work_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.WORK.getType()));

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
            response.setHeader("Content-Disposition", "attachment;filename=work.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 任务导入
     */
    /*public void excelImport(){
        UploadFile file = getFile("file", "/excel/");
        renderJson(workService.excelImport(file));
    }*/

    /**
     * 下载任务上传模版
     */
    public void updateWorkTemplate(){
        List<Record> fields = adminFieldService.getFields(CrmEnum.WORK.getType(), null);
        //列标题
        List<String> handers = new ArrayList<>();
        //下拉框数据(数组)
        List<String[]> downData = new ArrayList<>();
        //下拉列的序号
        List<String> downRows = new ArrayList<>();

        int temp = 0;
        for (Record field : fields) {

            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }
        try(HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("项目导入模版",handers, downData, downRows,null,null);) {

            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer_import.xls");

            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }


    /**
     * 导出待办项目
     */
    public void waitingExport(BasePageRequest<Work> basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)workService.waiting(basePageRequest, false).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"batch_id","work_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.WORK.getType()));

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
            response.setHeader("Content-Disposition", "attachment;filename=work.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

}
