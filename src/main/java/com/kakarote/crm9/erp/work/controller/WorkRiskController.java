package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.WorkRisk;
import com.kakarote.crm9.erp.work.service.WorkRiskService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkRiskController extends Controller {

    @Inject
    WorkRiskService workRiskService;

    /**
     * 按照批次分页查询风险管理
     */
    @Permissions({"project:risk:index"})
    public void queryRiskByWorkId(BasePageRequest<WorkRisk> basePageRequest){
        renderJson(workRiskService.queryPageList(basePageRequest));
    }

    /**
     * 设置风险管理
     */
    @Permissions({"project:risk:save","project:risk:update"})
    public void setRisk(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workRiskService.setRisk(jsonObject));
    }

    /**
     * 批量删除风险管理
     */
    @Permissions({"project:risk:delete"})
    public void delRisk(@Para("ids") String ids){
        renderJson(workRiskService.delRisk(ids));
    }

    /**
     * 根据Id查询风险管理
     */
    public void queryById(@Para("id")Integer id){
        renderJson(workRiskService.queryById(id));
    }

    /**
     * 导入
     */
    @Permissions({"project:risk:excelImport"})
    public void excelImport(@Para("file") UploadFile file,@Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling,@Para("workId")String workId) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.RISK, file.getFile().getAbsolutePath(), null, null, repeatHandling,workId);
        renderJson(R.ok().put("data",messageId));
    }

    /**
     * 下载导出模版
     */
    @Permissions({"project:risk:excelImport"})
    public void updateRiskTemplate(){
        List<String> handers = Arrays.asList("发生阶段","发现方式","方向","风险类型","风险等级","发现人员","发现日期","跟踪人员","详细说明","影响范围","解决建议","解决人员","解决日期","风险状态");
        //列标题

        //下拉框数据(数组)
        List<String[]> downData = Arrays.asList(
                new String[]{"售前阶段","立项阶段","计划阶段","需求阶段","设计阶段","开发阶段","测试阶段","实施阶段","运维阶段"},
                new String[]{"风险","机会"},
                new String[]{"技术","管理","商业","社会"},
                new String[]{"低","中","高"},
                new String[]{"无","关闭"});
        //下拉列的序号
        List<String> downRows =  Arrays.asList("0","2","3","4","12");

        int temp = 0;
        /*for (Record field : fields) {

            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }*/
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
     * 导出
     */
    @Permissions({"project:risk:excelexport"})
    public void riskExport(BasePageRequest basePageRequest,@Para("riskIds") String riskIds){

        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes");
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)workRiskService.queryPageList(basePageRequest).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"foundUser","found_user_id","risk_id","solveUser","solve_user_id","trackingUser","tracking_user_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            addField(headList);
            headList.forEach(head -> {
                if (!Objects.equals(head.getStr("field_name"), head.getStr("name"))) {
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("field_name")), head.getStr("name"));
                } else {
                    writer.addHeaderAlias(head.getStr("field_name"), head.getStr("name"));
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
            response.setHeader("Content-Disposition", "attachment;filename=risk.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void addField(List<Record> headList){
        headList.add(new Record().set("field_name", "found_phase").set("name", "发生阶段"));
        headList.add(new Record().set("field_name", "found_way").set("name", "发现方式"));
        headList.add(new Record().set("field_name", "direction").set("name", "方向"));
        headList.add(new Record().set("field_name", "risk_type").set("name", "风险类型"));
        headList.add(new Record().set("field_name", "risk_level").set("name", "风险等级"));
        headList.add(new Record().set("field_name", "foundUser").set("name", "发现人员"));
        headList.add(new Record().set("field_name", "found_time").set("name", "发现日期"));
        headList.add(new Record().set("field_name", "trackingUser").set("name", "跟踪人员"));
        headList.add(new Record().set("field_name", "detailed").set("name", "详细说明"));
        headList.add(new Record().set("field_name", "scope").set("name", "影响范围"));
        headList.add(new Record().set("field_name", "solve_advice").set("name", "解决建议"));
        headList.add(new Record().set("field_name", "solveUser").set("name", "解决人员"));
        headList.add(new Record().set("field_name", "solve_time").set("name", "解决日期"));
        headList.add(new Record().set("field_name", "risk_status").set("name", "风险状态"));

    }
}
