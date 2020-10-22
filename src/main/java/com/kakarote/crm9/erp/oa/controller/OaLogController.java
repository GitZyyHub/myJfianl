package com.kakarote.crm9.erp.oa.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.oa.common.OaEnum;
import com.kakarote.crm9.erp.oa.entity.OaLog;
import com.kakarote.crm9.erp.oa.entity.OaLogBulletin;
import com.kakarote.crm9.erp.oa.entity.OaLogRelation;
import com.kakarote.crm9.erp.oa.entity.OaLogRule;
import com.kakarote.crm9.erp.oa.service.OaLogService;
import com.kakarote.crm9.erp.work.service.TaskService;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * OA日志模块
 */
public class OaLogController extends Controller {

    @Inject
    private OaLogService oaLogService;
    @Inject
    private TaskService taskService;

    /**
     * 分页条件查询日志
     * @author zhangzhiwei
     */
    @SysLog({LogEnum.ALL_LOG, LogEnum.MY_SEND_LOG, LogEnum.MY_RECEIVE_LOG})
    public void queryList(BasePageRequest basePageRequest) {
        Page<OaLog> recordList=oaLogService.queryList(basePageRequest);
        renderJson(R.ok().put("data",recordList));
    }

    /**
     * 根据日志id获取日志
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    public void queryById(@Para("logId") Integer logId){
        Integer number = Db.queryInt("select count(*) from 72crm_oa_log where log_id = ?",logId);
        if (number == 0){
            renderJson(R.error("已被删除"));
            return;
        }
        OaLog oaLog=oaLogService.queryById(logId);
        renderJson(R.ok().put("data",oaLog));
    }
    /**
     * 添加日志
     * @author zhangzhiwei
     */
    public void addOrUpdate(){
        renderJson(oaLogService.saveAndUpdate(JSON.parseObject(getRawData())));
    }

    /**
     * 根据日志id删除日志
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    public void deleteById(@Para("logId") Integer logId){
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.LOG_TYPE_KEY, logId);
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(oaLogService.deleteById(logId) ? R.ok() : R.error("删除失败"));
    }

    /**
     * 日志设为已读
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    public void readLog(@Para("logId")Integer logId){
        oaLogService.readLog(logId);
        renderJson(R.ok());
    }

    /**
     * 查询crm关联日志
     */
    public void queryLogRelation(BasePageRequest<OaLogRelation> basePageRequest){
        renderJson(oaLogService.queryLogRelation(basePageRequest));
    }

    /**
     * 查询日志统计信息
     * @author zhangzhiwei
     */
    public void queryLogBulletin(){
        renderJson(oaLogService.queryLogBulletin());
    }

    /**
     * 查询日志销售简报统计列表
     * @author hmb
     */
    public void queryLogBulletinByType(BasePageRequest<OaLogBulletin> pageRequest){
        renderJson(oaLogService.queryLogBulletinByType(pageRequest));
    }

    /**
     * 查询跟进记录统计
     * @param logId
     */
    public void queryLogRecordCount(@Para("logId") Integer logId,@Para("today") Integer today){
        renderJson(oaLogService.queryLogRecordCount(logId,today));
    }

    /**
     * 获取日志欢迎语列表
     */
    public void getLogWelcomeSpeech(){
        String value = Db.queryStr("select value from `72crm_admin_config` where name = 'logWelcomeSpeech' order by rand() limit 1");
        renderJson(R.ok().put("data",value));
    }

    /**
     * 导出
     */
    public void export(BasePageRequest basePageRequest) {
        List<Map<String, Object>> list = oaLogService.export(basePageRequest);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("category", "日志类型");
            writer.addHeaderAlias("create_time", "创建日期");
            writer.addHeaderAlias("create_user_name", "创建人");
            writer.addHeaderAlias("send_name", "发送给");
            writer.addHeaderAlias("content", "今日工作内容");
            writer.addHeaderAlias("tomorrow", "明日工作内容");
            writer.addHeaderAlias("question", "遇到问题");
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.addHeaderAlias("comment", "回复");
            writer.merge(8, "日志信息");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 9; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=log.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出日志错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 查询日志完成情况统计
     */
    public void queryCompleteStats(@Para("type") Integer type) {
        renderJson(oaLogService.queryCompleteStats(type));
    }

    /**
     * 查询日志完成情况
     */
    public void queryCompleteOaLogList(BasePageRequest<OaLogRule> basePageRequest){
        renderJson(R.ok().put("data",oaLogService.queryCompleteOaLogList(basePageRequest)));
    }

    /**
     * 查询日志未完成情况
     */
    public void queryIncompleteOaLogList(BasePageRequest<OaLogRule> basePageRequest){
        renderJson(R.ok().put("data",oaLogService.queryIncompleteOaLogList(basePageRequest)));
    }

    public void queryOaLogRuleList(){
        renderJson(R.ok().put("data",oaLogService.queryOaLogRuleList()));
    }

    /**
     * 设置日志提交设置
     */
    public void setOaLogRule() {
        JSONArray jsonArray = JSON.parseArray(getRawData());
        renderJson(oaLogService.setOaLogRule(jsonArray));
    }
}
