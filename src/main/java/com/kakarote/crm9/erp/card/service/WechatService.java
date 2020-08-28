package com.kakarote.crm9.erp.card.service;

import cn.binarywang.wx.miniapp.api.WxMaCodeService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.code.WxMaCategory;
import cn.binarywang.wx.miniapp.bean.code.WxMaCodeAuditStatus;
import cn.binarywang.wx.miniapp.bean.code.WxMaCodeSubmitAuditRequest;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisConfigImpl;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.JfinalConfig;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.AdminUserHisTable;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.card.common.WxMaServiceCongigImpl;
import com.kakarote.crm9.erp.card.common.WxMiNiAppOpenRedisConfigStorage;
import com.kakarote.crm9.erp.card.entity.AdminVisitingCard;
import com.kakarote.crm9.erp.card.entity.CardWeixinBrowse;
import com.kakarote.crm9.erp.card.entity.CardWeixinLeads;
import com.kakarote.crm9.erp.card.entity.CardWeixinLeadsUser;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.WxUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.WxOpenMaCodeTemplate;
import me.chanjar.weixin.open.bean.fastma.WxFastMaCategory;
import me.chanjar.weixin.open.bean.ma.WxMaOpenCommitExtInfo;
import me.chanjar.weixin.open.bean.message.WxOpenMaSubmitAuditMessage;
import me.chanjar.weixin.open.bean.result.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WechatService extends WxMaServiceCongigImpl {
    @Inject
    private AdminRoleService adminRoleService;

    /**
     * 跟进微信小程序appid和code获取微信Openid
     */
    public WxMaJscode2SessionResult queryWechat(String code) {

        WxMaJscode2SessionResult wxMaJscode2SessionResult = null;
        try {
            wxMaJscode2SessionResult = jsCode2SessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return wxMaJscode2SessionResult;
    }

    public R queryPhone(JSONObject jsonObject) {
        WxMaJscode2SessionResult result = queryWechat(jsonObject.getString("code"));
        String sessionKey = result.getSessionKey();
        JSONObject json = WxUtils.wxDecrypt(
                jsonObject.getJSONObject("detail").getString("encryptedData"), sessionKey, jsonObject.getJSONObject("detail").getString("iv"));
        CardWeixinLeads leads = CardWeixinLeads.dao.findFirst("select * from 72crm_card_weixin_leads where openid = ?", result.getOpenid());
        if (leads != null) {
            leads.setMobile(json.getString("phoneNumber"));
            leads.update();
        }
        return R.ok().put("json", json);
    }

    /**
     * 绑定员工信息
     */
    public R bindingUser(JSONObject jsonObject) {
        //先判断是否是绑定员工信息
        AdminVisitingCard card = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where user_id = ?", jsonObject.getString("userId"));
        if (card == null) {
            card = new AdminVisitingCard();
        }
        Long userId = jsonObject.getLong("userId");
        card.setOpenid(jsonObject.getString("openId"));
        card.setUserId(userId);
        card.setCreateTime(DateUtil.date());
        if (card.getCardId() == null) {
            card.save();
        } else {
            card.update();
        }
        return R.ok().put("data", card);
    }

    /**
     * 绑定线索信息
     */
    public R bindingLeads(JSONObject jsonObject) {
        CardWeixinLeads leads = CardWeixinLeads.dao.findFirst("select * from 72crm_card_weixin_leads where openid = ?", jsonObject.getString("openId"));
        if (leads == null) {
            leads = new CardWeixinLeads();
        }
        Long userId = jsonObject.getLong("userId");
        AdminUser adminUser = AdminUser.dao.findById(userId);
        leads.setOpenid(jsonObject.getString("openId"));
        leads.setWeixinImg(jsonObject.getString("img"));
        leads.setCreateUserId(userId);
        leads.setOwnerUserId(userId);
        leads.setWeixinName(jsonObject.getString("weixinName"));
        leads.setCreateTime(DateUtil.date());
        leads.setMobile(jsonObject.getString("mobile"));
        leads.setSex(jsonObject.getString("sex"));
        leads.setCity(jsonObject.getString("city"));
        if (leads.getWeixinLeadsId() == null) {
            leads.save();
            CardWeixinLeadsUser leadsUser = CardWeixinLeadsUser.dao.
                    findFirst("select * from 72crm_card_weixin_leads_user where user_id = ? and weixin_leads_id = ?",
                            userId, leads.getWeixinLeadsId());
            if (leadsUser == null) {
                leadsUser = new CardWeixinLeadsUser();
                leadsUser.setUserId(userId);
                leadsUser.setWeixinLeadsId(leads.getWeixinLeadsId());
                leadsUser.save();
            }
        } else {
            leads.update();
        }
        CardWeixinLeads leadsq = CardWeixinLeads.dao.findById(leads.getWeixinLeadsId());
        return R.ok().put("data", leadsq);
    }

    /**
     * appid获取公司id
     */
    public R queryByAppid(String appid) {
      //  AdminCompanyMiniapp miniapp = AdminCompanyMiniapp.dao.findFirst("select * from 72crm_admin_company_miniapp where appid = ?", appid);
        return R.ok().put("data", null);
    }

    /**
     * openid获取公司id
     */
    public R queryByOpenId(String openId) {
        Record record = Db.findFirst("select * from 72crm_card_weixin_leads where openid = ?", openId);
        return R.ok().put("data", record);
    }

    /**
     * openid获取公司id
     */
    public R queryByUserId(Long userId) {
        return R.ok().put("data", null);//AdminCompanyMiniapp.queryApp(userId));
    }

    /**
     * 设置业务域名
     */
    public void setwebview(String appid) {
       /* try {
            String s = getWxOpenComponentService().getWxMaServiceByAppid(appid).getWebViewDomain();
            System.out.println(s);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 设置业务域名
     */
    public R getPage(String appid) {
      /*  WxOpenMaPageListResult wxOpenMaPageListResult = null;
        try {
            wxOpenMaPageListResult = getWxOpenComponentService().getWxMaServiceByAppid(appid).getPageList();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.ok().put("data", wxOpenMaPageListResult);*/
      return R.ok();
    }

    /**
     * 设置服务器域名
     */
    public R modifyDomain(String appid) {
        List<String> list = new ArrayList<>();
        list.add("c.72crm.com");
        list.add("www.72start.com");
        list.add("www.72crm.com");
      /*  try {
            WxOpenMaDomainResult wxOpenMaDomainResult = getWxOpenComponentService().getWxMaServiceByAppid(appid)
                    .modifyDomain("set", list, list, list, list);
            return R.ok().put("data",wxOpenMaDomainResult.toString());
        } catch (WxErrorException e) {
            Log.getLog(getClass()).error("",e);
        }*/
        return R.error();
    }
    /**
     * 设置服务器域名
     */
    public String release(String appid) {
      /*  try {
            WxOpenResult wxOpenResult=getWxOpenComponentService().getWxMaServiceByAppid(appid).releaesAudited();
            return wxOpenResult.toString();
        } catch (WxErrorException e) {
            Log.getLog(getClass()).error("",e);
        }*/
        return "";
    }
    /**
     * 生成二维码
     */
    public String createErWeiMa(Integer type, Long userId) {
        String prefix = BaseUtil.getDate();
        try {
            File newFile;
          /*  WxMaCodeService wxMaCodeService = getCodeService();
            newFile=new File(BaseConstant.UPLOAD_PATH+prefix+"/"+IdUtil.simpleUUID()+".jpg");
            FileUtil.writeBytes(wxMaCodeService.getQrCode("pages/activeDetail?active=true&scane=true&userId=" +userId),newFile);*/
               WxMaQrcodeService qrcodeService=getQrcodeService();
                File file = qrcodeService.createWxaCode("pages/index?active=true&scane=true&userId=" +userId, 430, true, null, false);
                newFile=new File(BaseConstant.UPLOAD_PATH+prefix+"/"+file.getName());
                FileUtil.move(file,newFile,true);
            AdminFile adminFile = new AdminFile();
            adminFile.setCreateTime(new Date());
            adminFile.setCreateUserId(userId);
            adminFile.setPath(BaseConstant.UPLOAD_PATH+prefix+"/"+newFile.getName());
            adminFile.setFilePath("/file/downFile?fileId="+IdUtil.simpleUUID());
            adminFile.setName(newFile.getName());
            adminFile.setFileType("img");
            adminFile.setSize(newFile.length());
            adminFile.save();
            return adminFile.getFilePath();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    public R query(Integer type, Long userId) {
        AdminVisitingCard card = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where user_id = ?", userId);
        if (card == null) {
            card = new AdminVisitingCard();
            String url = createErWeiMa(type, userId);
            AdminFile adminFile = AdminFile.dao.findFirst(Db.getSqlPara("admin.file.queryByPath",
                    Kv.by("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()).set("filePath",url)));
            String img = null;
            if (adminFile != null) {
                img =  adminFile.getFilePath().replace("\\", "/");
            }
            card.setUserId(userId);
            if (type.equals(1)) {
                card.setOfficialImg(url);
                card.save();
                return R.ok().put("data", img);
            } else {
                card.setWeixinImg(url);
                card.save();
                card.setWeixinImg(img);
                return R.ok().put("data", card);
            }
        } else {
            if (type.equals(1)) {
                if (StrUtil.isEmpty(card.getOfficialImg())) {
                    String url = createErWeiMa(type, userId);
                    card.setOfficialImg(url);
                    card.update();
                }
                AdminFile adminFile = AdminFile.dao.findFirst(Db.getSqlPara("admin.file.queryByPath",
                        Kv.by("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()).set("filePath",card.getOfficialImg())));
                String img = null;
                if (adminFile != null) {
                    img =  adminFile.getFilePath().replace("\\", "/");
                }else {
                    String url = createErWeiMa(type, userId);
                    card.setOfficialImg(url);
                    card.update();
                    AdminFile first = AdminFile.dao.findFirst(Db.getSqlPara("admin.file.queryByPath",
                            Kv.by("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()).set("filePath",url)));
                    img =  first.getFilePath().replace("\\", "/");
                }
                return R.ok().put("data", img);
            } else {
                if (StrUtil.isEmpty(card.getWeixinImg())) {
                    String url = createErWeiMa(type, userId);
                    card.setWeixinImg(url);
                    card.update();
                }
                AdminFile adminFile = AdminFile.dao.findFirst(Db.getSqlPara("admin.file.queryByPath",
                        Kv.by("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()).set("filePath",card.getWeixinImg())));
                String img = null;
                if (adminFile != null) {
                    img =  adminFile.getFilePath().replace("\\", "/");
                }else {
                    String url = createErWeiMa(type, userId);
                    card.setWeixinImg(url);
                    card.update();
                    AdminFile first = AdminFile.dao.findFirst(Db.getSqlPara("admin.file.queryByPath",
                            Kv.by("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()).set("filePath",url)));
                    img =  first.getFilePath().replace("\\", "/");
                }
                card.setWeixinImg(img);
                return R.ok().put("data", card);
            }
        }
    }

    /**
     * 小程序审核撤回
     */
    public R undocodeaudit(Long userId) {
        WxOpenResult wxOpenResult = null;
        try {
            getCodeService().undoCodeAudit();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.ok().put("data", wxOpenResult);
    }

    /**
     * 查询指定发布审核单的审核状态
     */
    public R getAuditstatus(Long userId, Long auditId) {
        WxMaCodeAuditStatus wxMaCodeAuditStatus = null;
        try {
            wxMaCodeAuditStatus =getCodeService().getAuditStatus(auditId);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.ok().put("data", wxMaCodeAuditStatus);
    }

    /**
     * 查询最新一次提交的审核状态
     */
    public R getLatestAuditstatus(Long userId) {
        return getLatestAuditstatus1();
    }
    private R getLatestAuditstatus1(){
        WxMaCodeAuditStatus wxMaCodeAuditStatus = null;
        try {
            wxMaCodeAuditStatus = getCodeService().getLatestAuditStatus();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.ok().put("data", wxMaCodeAuditStatus);
    }
    /**
     * 上传小程序代码
     */
    public R uploadingErWeiMaGuiZe(Long userId, Integer type) {
        return uploadingErWeiMaGuiZe1(type);
    }

    private R uploadingErWeiMaGuiZe1( Integer type){
        WxOpenMaSubmitAuditResult wxOpenMaSubmitAuditResult = null;
        try {
            //获取小程序模板
            List<WxMaCategory> wxMaCategoryList = getCodeService().getCategory();
            //上传代码
            WxMaCodeSubmitAuditRequest
                    wxMaCodeSubmitAuditRequest= WxMaCodeSubmitAuditRequest.builder().itemList(wxMaCategoryList).build();
            //提交审核
            if (type == null) {
                Long i =  getCodeService().submitAudit(wxMaCodeSubmitAuditRequest);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.ok().put("data", wxOpenMaSubmitAuditResult);
    }

    /**
     * openid绑定员工
     */
    public R saveleadsUser(String openid, Long userId) {
        CardWeixinLeads cardWeixinLeads = CardWeixinLeads.dao.findFirst("select * from 72crm_card_weixin_leads where openid = ?", openid);
        AdminUser adminUser = AdminUser.dao.findById(userId);
        if (cardWeixinLeads != null) {
            CardWeixinLeadsUser leadsUser = CardWeixinLeadsUser.dao.
                    findFirst("select * from 72crm_card_weixin_leads_user where user_id = ?  and weixin_leads_id = ?",
                            userId, cardWeixinLeads.getWeixinLeadsId());

            if (leadsUser == null) {
                leadsUser = new CardWeixinLeadsUser();
                leadsUser.setUserId(userId);
                leadsUser.setCreateTime(DateUtil.date());
                leadsUser.setWeixinLeadsId(cardWeixinLeads.getWeixinLeadsId());
                leadsUser.save();
            }
        }else {
            AdminVisitingCard card = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where openid = ? ", openid);
           if (card != null) {
               CardWeixinLeadsUser leadsUser = CardWeixinLeadsUser.dao.
                       findFirst("select * from 72crm_card_weixin_leads_user where user_id = ?  and relevance_user_id = ?",
                               userId,  card.getUserId());
               if (leadsUser == null) {
                   leadsUser = new CardWeixinLeadsUser();
                   leadsUser.setUserId(userId);
                   leadsUser.setCreateTime(DateUtil.date());
                   leadsUser.setRelevanceUserId(card.getUserId());
                   leadsUser.save();
               }
           }
        }
        return R.ok();
    }

    /**
     * 跟进微信小程序appid和code获取微信Openid
     */
    public R queryWechat(String code, Long userId, String appid, Integer type) {
        if (userId == null) {
            AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where  type = ?  and his_table = 1", 2);
            if (userHisTable != null) {
                userId = userHisTable.getUserId();
            } else {
                AdminUser adminUser = AdminUser.dao.findFirst("select * from 72crm_admin_user ");
                userId = adminUser.getUserId();
            }
        }
        Integer status = 0;
        BaseUtil.setUser(userId);
        JSONObject jsonObject = adminRoleService.auth(userId);
        if (jsonObject.get("crm") != null &&
            jsonObject.getJSONObject("crm").get("marketing") != null) {
            status = 1;
        }
        WxMaJscode2SessionResult wxMaJscode2SessionResult = queryWechat(code);
        AdminUser user = BaseUtil.getUser();
        AdminVisitingCard card = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where openid = ? ", wxMaJscode2SessionResult.getOpenid());
        if (card != null){
            type = 1;
        }
        if (type == null && card == null){
            type = 2;
        }
        Record record = new Record();
        record.set("status",status);
        record.set("openid",wxMaJscode2SessionResult.getOpenid());
        record.set("userId",userId);
        if (type == 1){
            record.set("type",1);
            if (card != null) {
                CardWeixinLeadsUser leadsUser = CardWeixinLeadsUser.dao.
                        findFirst("select * from 72crm_card_weixin_leads_user where user_id = ?  and relevance_user_id = ?",
                                userId, card.getUserId());
                if (leadsUser == null) {
                    leadsUser = new CardWeixinLeadsUser();
                    leadsUser.setUserId(userId);
                    leadsUser.setCreateTime(DateUtil.date());
                    leadsUser.setRelevanceUserId(card.getUserId());
                    leadsUser.save();
                }
                record.set("longUserId",card.getUserId());
                return R.ok().put("data", record).put("type", 1).put("userId", userId).put("status", status).put("longUserId",card.getUserId());
            } else {

                    card = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where user_id = ?", userId);
                    if (card == null) {
                        card = new AdminVisitingCard();
                    }
                    card.setOpenid(wxMaJscode2SessionResult.getOpenid());
                    card.setUserId(userId);
                    card.setCreateTime(DateUtil.date());
                    CardWeixinLeadsUser leadsUser = CardWeixinLeadsUser.dao.
                            findFirst("select * from 72crm_card_weixin_leads_user where user_id = ? and relevance_user_id = ?",
                                    userId, userId);
                    if (leadsUser == null) {
                        leadsUser = new CardWeixinLeadsUser();
                        leadsUser.setUserId(userId);
                        leadsUser.setCreateTime(DateUtil.date());
                        leadsUser.setRelevanceUserId(userId);
                        leadsUser.save();
                    }
                    if (card.getCardId() == null) {
                        card.save();
                    } else {
                        card.update();
                    }
                record.set("longUserId",card.getUserId());
                    return R.ok().put("data", record).put("type", 1).put("userId", userId).put("status", status).put("longUserId",card.getUserId());
            }
        }else {
            record.set("type",2);
            Integer leadsInfo = 1;
            Integer leadsPhone = 1;
            CardWeixinLeads cardWeixinLeads = CardWeixinLeads.dao.findFirst("select * from 72crm_card_weixin_leads where openid = ?", wxMaJscode2SessionResult.getOpenid());
            if (cardWeixinLeads != null) {
                CardWeixinBrowse browse = CardWeixinBrowse.dao.findFirst("select * from 72crm_card_weixin_browse where user_id = ? and weixin_leads_id = ?", userId, cardWeixinLeads.getWeixinLeadsId());
                if (browse == null) {
                    browse = new CardWeixinBrowse();
                    browse.setUserId(userId);
                    browse.setWeixinLeadsId(cardWeixinLeads.getWeixinLeadsId());
                    browse.save();
                } else {
                    if (browse.getNum() == null) {
                        browse.setNum(1);
                    }
                        browse.setNum(browse.getNum() + 1);
                    browse.update();
                }

                if (StrUtil.isNotEmpty(cardWeixinLeads.getWeixinImg())) {
                    leadsInfo = 0;
                }

                if (StrUtil.isNotEmpty(cardWeixinLeads.getMobile())) {
                    leadsPhone = 0;
                }
                record.set("leadsInfo",leadsInfo);
                record.set("leadsPhone",leadsPhone);
                record.set("weixinLeadsId",cardWeixinLeads.getWeixinLeadsId());
                return R.ok().put("data", record).put("type", 2).put("userId", userId).put("status", status).put("leadsInfo", leadsInfo).put("leadsPhone", leadsPhone);
            } else {
                    cardWeixinLeads = new CardWeixinLeads();
                    cardWeixinLeads.setOwnerUserId(userId);
                    cardWeixinLeads.setOpenid(wxMaJscode2SessionResult.getOpenid());
                    cardWeixinLeads.setCreateTime(DateUtil.date());
                    cardWeixinLeads.save();
                record.set("leadsInfo",leadsInfo);
                record.set("leadsPhone",leadsPhone);
                record.set("weixinLeadsId",cardWeixinLeads.getWeixinLeadsId());
                return R.ok().put("data", record).put("userId", userId).put("type", 2).put("status", status).put("leadsInfo", 1).put("leadsPhone", 1);
            }
        }
    }

    /**
     * 微信小程序授权回调
     */

    public R apiQueryAuth(String auth_code) {
        WxOpenQueryAuthResult wxOpenQueryAuthResult = null;
      /*  try {
            wxOpenQueryAuthResult = getWxOpenComponentService().getQueryAuth(auth_code);
            AdminCompanyMiniapp miniapp = AdminCompanyMiniapp.queryAppByCompanyId(companyId);
            if (miniapp == null) {
                miniapp = new AdminCompanyMiniapp();
            }
            miniapp.setAppid(wxOpenQueryAuthResult.getAuthorizationInfo().getAuthorizerAppid());
            miniapp.setCompanyId(companyId);
            miniapp.setAuthorizerRefreshToken(wxOpenQueryAuthResult.getAuthorizationInfo().getAuthorizerRefreshToken());
            if (miniapp.getId() == null) {
                miniapp.save();
            } else {
                miniapp.update();
            }
            setwebview(miniapp.getAppid());
            modifyDomain(miniapp.getAppid());
            uploadingErWeiMaGuiZe1(miniapp.getAppid(),null);
            addcategory(miniapp.getAppid());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }*/
        return R.ok();
    }

    /**
     * 获取微信小程序授权二维码
     */
    public R preauthcode() {
        String url = null;
       /* try {
            url = getWxOpenComponentService().getPreAuthUrl("https://c.72crm.com/api/wechat/apiQueryAuth?companyId=" + BaseUtil.getUser().getCompanyId());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }*/
        return R.ok().put("data", url);
    }
    /**
     * 添加类目
     */
    private void addcategory(String appId){
        List<WxFastMaCategory> categoryList = new ArrayList<>();
       /* try {
            categoryList.add(getWxFastMaCategory(287,298));
            categoryList.add(getWxFastMaCategory(210,413));
            getWxOpenComponentService().getWxFastMaServiceByAppid(appId).addCategory(categoryList);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }*/
    }

    private Integer getCateGoryIdByName( List<Record> recordList,String name){
        for (Record record: recordList) {
            if (record.get("name") != null && record.getStr("name").equals(name)){
                    return  record.getInt("id");
            }
        }
            return null;
    }

    private WxFastMaCategory getWxFastMaCategory( List<Record> recordList,String firstName,String secondName){
        WxFastMaCategory wxFastMaCategory = new WxFastMaCategory();
        wxFastMaCategory.setFirst(getCateGoryIdByName(recordList,firstName));
        wxFastMaCategory.setSecond(getCateGoryIdByName(recordList,secondName));
        return wxFastMaCategory;
    }
    private WxFastMaCategory getWxFastMaCategory( Integer first,Integer second){
        WxFastMaCategory wxFastMaCategory = new WxFastMaCategory();
        wxFastMaCategory.setFirst(first);
        wxFastMaCategory.setSecond(second);
        return wxFastMaCategory;
    }
    /**
     * 查询所有要添加的类目
     */
    public R getAllCategories(String appId){
       /* try {
            String jsonStr= getWxOpenComponentService().getWxFastMaServiceByAppid(appId).getAllCategories();
            JSONObject json = JSONObject.parseObject(jsonStr);
            return R.ok().put("jsonStr",jsonStr).put("json",json);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }*/
        return R.ok();
    }
}
