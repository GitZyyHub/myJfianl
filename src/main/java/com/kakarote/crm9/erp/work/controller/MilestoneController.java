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
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.MilestoneService;
import com.kakarote.crm9.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class MilestoneController extends Controller {


    @Inject
    MilestoneService milestoneService;

    @Inject
    AdminFieldService adminFieldService;

    /**
     * 查询里程碑
     */
    @Permissions({"project:milestone:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(milestoneService.queryPageList(basePageRequest,false));
    }

    /**
     * 批量修改完成程度
     */
    @Permissions({"project:milestone:updateDegree"})
    public void updateDegree(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        String ids = jsonObject.getString("ids");
        String degree = jsonObject.getString("degree");
        renderJson(milestoneService.updateDegree(ids,degree));
    }

    /**
     * 里程碑设置（添加/修改）
     */
    @Permissions({"project:milestone:save","project:milestone:update"})
    public void setMilestone(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(milestoneService.setMilestone(jsonObject));
    }

    /**
     * 删除里程碑
     */
    @Permissions({"project:milestone:delete"})
    public void delMilestone(){
        String milestoneIds = getPara("milestoneIds");
        renderJson(milestoneService.delMilestone(milestoneIds));
    }

    /**
     * 根据Id查询里程碑
     */
    public void queryById(@Para("id")Integer id){
        renderJson(R.ok().put("data",milestoneService.queryById(id)));
    }

    /**
     * 查询里程碑ID和名字
     */
    public void queryMilestoneIdAndName(){
        renderJson(milestoneService.queryMilestoneIdAndName());
    }

    /**
     * 导出!!!
     */
    @Permissions({"project:milestone:excelexport"})
    public void milestoneExport(BasePageRequest<Work> basePageRequest,@Para("milestoneIds") String milestoneIds, @Para("workId")String workId){
        List<String> strings = null;
        if(!StrUtil.isEmpty(milestoneIds)){
            strings = Arrays.asList(milestoneIds.split(","));
        }
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)milestoneService.queryPageList(basePageRequest, false).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"milestone_id","main_user_id","task_id","work_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.MILESTONE.getType()));

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
            response.setHeader("Content-Disposition", "attachment;filename=milestone.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();

    }

}
