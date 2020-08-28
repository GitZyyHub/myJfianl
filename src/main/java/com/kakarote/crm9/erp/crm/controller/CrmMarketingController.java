package com.kakarote.crm9.erp.crm.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.entity.CrmMarketing;
import com.kakarote.crm9.erp.crm.service.CrmMarketingService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * crm推广模块
 *
 * @author hmb
 */
public class CrmMarketingController extends Controller {

    @Inject
    private CrmMarketingService crmMarketingService;

    /**
     * 添加推广
     *
     * @param crmMarketing
     */
    @Permissions("crm:marketing:save")
    public void add(@Para("") CrmMarketing crmMarketing) {
        renderJson(crmMarketingService.addOrUpdate(crmMarketing));
    }

    /**
     * 修改推广
     * @param crmMarketing
     */
    @Permissions("crm:marketing:update")
    public void update(@Para("") CrmMarketing crmMarketing) {
        renderJson(crmMarketingService.addOrUpdate(crmMarketing));
    }

    /**
     * 查询推广列表
     * @param basePageRequest
     */
    @Permissions("crm:marketing:index")
    public void queryPageList(BasePageRequest<CrmMarketing> basePageRequest) {
        renderJson(crmMarketingService.queryPageList(basePageRequest,null));
    }
    /**
     * 小程序查询推广列表
     * @param basePageRequest
     */
   @Clear
    public void queryMiNiPageList(BasePageRequest<CrmMarketing> basePageRequest) {
        BaseUtil.setUser(basePageRequest.getJsonObject().getLong("userId"));
        renderJson(crmMarketingService.queryPageList(basePageRequest,1));
    }

    /**
     * 推广详情
     * @param marketingId
     */
    @Permissions("crm:marketing:read")
    public void queryById(@Para("marketingId") Integer marketingId) {
        renderJson(crmMarketingService.queryById(marketingId));
    }
    /**
     * 小程序推广详情
     * @param marketingId
     */
    @Clear
    public void queryMiNiById(@Para("marketingId") Integer marketingId,@Para("userId") Long userId) {
        BaseUtil.setUser(userId);
        renderJson(crmMarketingService.queryById(marketingId));
    }


    /**
     * 删除推广
     * @param marketingIds
     */
    @Permissions("crm:marketing:delete")
    public void deleteByIds(@Para("marketingIds") String marketingIds) {
        renderJson(crmMarketingService.deleteByIds(marketingIds));
    }

    /**
     * 查询自定义字段
     * @param marketingId
     */
    public void queryField(@Para("marketingId") Integer marketingId) {
        renderJson(R.ok().put("data", crmMarketingService.queryField(marketingId)));
    }


    /**
     * 修改状态
     */
    @Permissions("crm:marketing:updateStatus")
    public void updateStatus(@Para("marketingIds") String marketingIds, @Para("status") Integer status) {
        renderJson(crmMarketingService.updateStatus(marketingIds, status));
    }
    /**
     * 修改分享次数
     */
    @Clear
    public void updateShareNum(@Para("marketingId") Long marketingId,@Para("num") Integer num){
        renderJson(crmMarketingService.updateShareNum(marketingId, num));
    }

    /**
     * 查询推广客户信息
     */
    public void census(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data", crmMarketingService.census(basePageRequest)));
    }

    /**
     * 查询自定义字段
     *
     */
    @Clear
    public void queryAddField() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        String marketingId = jsonObject.getString("marketingId");
        renderJson(crmMarketingService.queryAddField(marketingId));
    }

    /**
     * 保存推广客户信息
     */
    @Clear
    public void saveMarketingInfo() {
        JSONObject data = JSON.parseObject(getRawData());
        renderJson(crmMarketingService.saveMarketingInfo(data));
    }

    /**
     * 手机端推广详情
     */
    @Clear
    public void queryMarketingId() {
        JSONObject data = JSON.parseObject(getRawData());
        AES aes = SecureUtil.aes(CrmMarketingService.bytes);
        Integer marketingId = Integer.valueOf(aes.decryptStr(data.getString("marketingId")));
        Long currentUserId = Long.valueOf(aes.decryptStr(data.getString("currentUserId")));
        BaseUtil.setUser(currentUserId);
        String device = data.getString("device");
        renderJson(crmMarketingService.queryById(marketingId,device));
    }


    /**
     * 同步数据
     */
    public void syncData(@Para("marketingId")Integer marketingId,@Para("rIds") String rIds) {
        renderJson(crmMarketingService.syncData(marketingId,rIds));
    }

    public void customerExportExcel(@Para("marketingId") Integer marketingId,@Para("status") Integer status) throws IOException {
        Long userId = BaseUtil.getUserId();
        List<Long> userIds = Aop.get(AdminUserService.class).queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        userIds.add(BaseUtil.getUserId());
        List<Record> fieldList = Db.template("crm.marketing.census", Kv.by("marketingId", marketingId).set("userIds", userIds).set("status",status)).find();
        List<Record> nameList = crmMarketingService.queryField(marketingId);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            nameList.forEach(record -> writer.addHeaderAlias(record.getStr("field_name"), record.getStr("name")));
            writer.addHeaderAlias("ownerUserName","负责人");
            writer.merge(nameList.size()-1, "客户信息");
            List<Map<String, Object>> list = fieldList.stream().map(record->{
                JSONObject jsonObject = JSON.parseObject(record.getStr("field_info"));
                JSONObject entity = jsonObject.getJSONObject("entity");
                jsonObject.getJSONArray("field").forEach(field->{
                    JSONObject adminFieldv = (JSONObject)field;
                    entity.put(adminFieldv.getString("name"),adminFieldv.getString("value"));
                });
                entity.put("ownerUserName",record.getStr("owner_user_name"));
                return entity.getInnerMap();
            }).collect(Collectors.toList());
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            Cell cell = writer.getCell(0, 0);
            for (int i = 0; i < nameList.size(); i++) {
                writer.setColumnWidth(i, 20);
            }
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=customer.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }
}
