package com.kakarote.crm9.erp.crm.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.service.InstrumentService;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.util.ArrayList;
import java.util.List;

public class InstrumentController extends Controller {

    @Inject
    private InstrumentService instrumentService;
    @Inject
    private AdminUserService adminUserService;

    /**
     * 销售简报
     *
     * @author zxy
     * type 1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年11.自定义
     * deptIds 部门id
     * userIds 员工或者部门id拼写id之间用‘,’隔开
     */
//    @SysLog(LogEnum.INSTRUMENT)
    public void queryBulletin() {
        String type = getPara("type");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
        }
        renderJson(instrumentService.queryBulletin(type, userIds,startTime,endTime));
    }

    public void queryDataInfo(){
        String type = getPara("type");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
        }
        renderJson(instrumentService.queryDataInfo(type, userIds,startTime,endTime));
    }
    /**
     * @author zhangzhiwei
     * 销售简报的数据查看详情
     */
    public void queryBulletinInfo(BasePageRequest basePageRequest, @Para("isUser") Integer isUser, @Para("deptId") Integer deptId, @Para("userId") Integer userId, @Para("type") String type, @Para("label") Integer label, @Para("checkStatus") Integer checkStatus, @Para("moneyType") Integer moneyType, @Para("startTime") String startTime, @Para("endTime") String endTime) {
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
            return;
        }
        renderJson(instrumentService.queryBulletinInfo(basePageRequest, userIds, type, label, checkStatus, moneyType, startTime, endTime));
    }
    /**
     * 销售趋势
     * type 1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年11.自定义
     * userIds 员工id拼写id之间用‘,’隔开
     * deptIds 部门id
     */
    public void sellMonth() {
        String type = getPara("type");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        Integer label = getParaToInt("label");
        String deptIds = null;
        if(label==null){
            label=1;
        }
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
            return;
        }
        renderJson(instrumentService.salesTrend(type, userIds, deptIds, startTime, endTime, label));
    }

    /**
     * 业绩指标
     * status 1 合同 2.回款
     * deptIds 门id拼写id之间用‘,’隔开
     * userIds 员工id拼写id之间用‘,’隔开
     */
    public void queryPerformance() {
        Integer status = getInt("status");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        String type = getPara("type");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
            return;
        }
        renderJson(instrumentService.queryPerformance(startTime, endTime, userIds, deptId, status, type));
    }
    /**
     * 销售漏斗
     */
    public void queryBusiness() {
        String type = getPara("type");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        Integer typeId = getInt("typeId");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
            return;
        }
        renderJson(instrumentService.sellFunnel(type,userIds,startTime, endTime,typeId));
    }

    /**
     * 查询销售简报的跟进记录统计
     */
    public void queryRecordCount() {
        String type = getPara("type");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
            return;
        }
        renderJson(instrumentService.queryRecordCount(type, userIds,startTime,endTime));
    }

    /**
     * 查询跟进记录统计列表
     * @param basePageRequest
     */
    @SysLog(LogEnum.FOLLOW_UP_RECORD)
    public void queryRecordList(BasePageRequest basePageRequest){
        String type = getPara("type");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer isUser = getParaToInt("isUser");
        String crmType = getPara("crmType");
        Integer queryType = getParaToInt("queryType");
        Integer subUser = getInt("subUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
            return;
        }
        if (subUser != null){
            if (subUser == 1 && ObjectUtil.isAllEmpty(userId,deptId)){
                userIds = TagUtil.fromLongSet(Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
            }else if (subUser == 0){
                userIds =  BaseUtil.getUserId()+"";
            }
        }
        if (BaseUtil.isAdmin() && ObjectUtil.isAllEmpty(userId,deptId) && subUser == null){
            userIds  = TagUtil.fromLongSet(Db.query("select user_id from `72crm_admin_user`"));
        }
        renderJson(instrumentService.queryRecordList(basePageRequest, type, userIds, startTime, endTime, crmType, queryType));
    }

    /**
     * 遗忘客户统计
     */
    public void forgottenCustomerCount(){
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        Integer isUser = getParaToInt("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
        }
        renderJson(instrumentService.forgottenCustomerCount(userIds));
    }

    /**
     * 遗忘客户列表
     */
    public void forgottenCustomerPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer userId = jsonObject.getInteger("userId");
        Integer deptId = jsonObject.getInteger("deptId");
        Integer isUser = jsonObject.getInteger("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
        }
        renderJson(instrumentService.forgottenCustomerPageList(basePageRequest,userIds));
    }

    /**
     * 未联系客户列表
     */
    public void unContactCustomerPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer userId = jsonObject.getInteger("userId");
        Integer deptId = jsonObject.getInteger("deptId");
        Integer isUser = jsonObject.getInteger("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
        }
        renderJson(instrumentService.unContactCustomerPageList(basePageRequest,userIds));
    }

    /**
     * 查询仪表盘排序
     */
    public void queryModelSort() {
        renderJson(instrumentService.queryModelSort());
    }

    /**
     * 设置仪表盘排序
     */
    public void setModelSort(@RequestBody JSONObject jsonObject) {
        renderJson(instrumentService.setModelSort(jsonObject));
    }

    public void ranking() {
        String type = getPara("type");
        Integer label = getParaToInt("label");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Long userId = getLong("userId");
        Integer deptId = getParaToInt("deptId");
        Integer isUser = getParaToInt("isUser");
        String userIds = "";
        if (1 == isUser) {
            if (userId == null) {
                if (BaseUtil.isAdmin()) {
                    userIds = Db.queryStr("select GROUP_CONCAT(user_id) from 72crm_admin_user");
                } else {
                    userIds = Aop.get(AdminUserService.class).querySubUserIds(BaseUtil.getUserId());
                }
            } else {
                userIds = userId.toString();
            }
            userIds = AuthUtil.filterUserId(userIds);
        } else if (0 == isUser) {
            if (deptId == null) {
                renderJson(R.ok().put("data", new ArrayList<>()));
                return;
            } else {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
                String deptIds = AuthUtil.filterDeptId(CollectionUtil.join(deptIdList, ","));
                userIds = adminUserService.queryUserIdsByDept(deptIds);
            }
        } else {
            renderJson(R.error("isUser参数不符合要求"));
        }
        renderJson(instrumentService.ranking(type, label, startTime, endTime, userIds));
    }
}
