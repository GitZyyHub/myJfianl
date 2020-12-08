package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmInvoice;
import com.kakarote.crm9.erp.crm.entity.CrmReceivables;
import com.kakarote.crm9.erp.work.entity.TaskHours;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;

public class AdminExamineRecordService {
    /**
     * 第一次添加审核记录和审核日志 type 1 合同 2 回款 userId:授权审批人
     */
    @Before(Tx.class)
    public Record saveExamineRecord(Integer type, Long userId, Long ownerUserId, Integer recordId,Integer status) {
        Record examineData = new Record();
        List<Long> examineLogIdList = new ArrayList<>();
        //创建审核记录
        AdminExamineRecord examineRecord = new AdminExamineRecord();
        if (recordId != null) {
            examineRecord = AdminExamineRecord.dao.findById(recordId);
            Db.update(Db.getSql("admin.examineLog.updateExamineLogIsRecheckByRecordId"), recordId);

            //如果是重新提交，将之前的审核日志全部变为撤回状态
            Db.update("update 72crm_admin_examine_log set is_recheck = 1 where record_id = ?", recordId);
        } else {
            examineRecord.setCreateTime(DateUtil.date());
            examineRecord.setCreateUser(BaseUtil.getUser().getUserId());
        }
        //创建审核日志
        AdminExamineLog examineLog = new AdminExamineLog();
        examineLog.setCreateTime(DateUtil.date());
        examineLog.setCreateUser(BaseUtil.getUser().getUserId());
        if (status != null) {
            examineLog.setExamineStatus(5);
            examineRecord.setExamineStatus(5);
        }else {
            examineLog.setExamineStatus(0);
            examineRecord.setExamineStatus(0);
        }
        examineLog.setOrderId(1);
        //根据type查询当前启用审批流程
        AdminExamine examine = AdminExamine.dao.findFirst(Db.getSql("admin.examine.getExamineByCategoryType"), type);
        if (examine == null) {
            examineData.set("status", 0);
        } else {
            examineRecord.setExamineId(examine.getExamineId());

            //先判断该审批流程是否为固定审批
            if (examine.getExamineType() == 1 ) {
                //固定审批
                //先查询该审批流程的审批步骤的第一步
                AdminExamineStep examineStep = AdminExamineStep.dao.findFirst(Db.getSql("admin.examineStep.queryExamineStepByExamineIdOrderByStepNum"), examine.getExamineId());
                examineRecord.setExamineStepId(examineStep.getStepId());
                examineLog.setExamineStepId(examineStep.getStepId());
                if (recordId == null) {
                    examineRecord.save();
                } else {
                    examineRecord.update();
                }
                if (status != null){
                    examineData.set("status", 1);
                    examineData.set("id", examineRecord.getRecordId());
                    return examineData;
                }
                if (examineStep.getStepType() == 2 || examineStep.getStepType() == 3) {
                    String[] userIds = examineStep.getCheckUserId().split(",");
                    for (String id : userIds) {
                        if (StrUtil.isNotEmpty(id)) {
                            examineLog.setLogId(null);
                            examineLog.setExamineUser(Long.valueOf(id));
                            examineLog.setRecordId(examineRecord.getRecordId());
                            examineLog.setIsRecheck(0);
                            examineLog.save();
                            examineLogIdList.add(examineLog.getLogId());
                        }
                    }
                } else if (examineStep.getStepType() == 1) {
                    //如果是负责人主管审批 获取主管ID
                    Record r = Db.findFirst(Db.getSql("admin.examineLog.queryUserByUserId"), ownerUserId);
                    if (r == null || r.getLong("user_id") == null){
                        examineLog.setExamineUser(BaseUtil.getSuperUser());
                    }else {
                    examineLog.setExamineUser(r.getLong("user_id"));}
                    examineLog.setRecordId(examineRecord.getRecordId());
                    examineLog.setIsRecheck(0);
                    examineLog.save();
                    examineLogIdList.add(examineLog.getLogId());
                } else {
                    //如果是负责人主管审批 获取主管的主管ID
                    Record r = Db.findFirst(Db.getSql("admin.examineLog.queryUserByUserId"), Db.findFirst(Db.getSql("admin.examineLog.queryUserByUserId"), ownerUserId).getLong("user_id"));
                    if (r == null || r.getLong("user_id") == null){
                        examineLog.setExamineUser(BaseUtil.getSuperUser());
                    }else {
                        examineLog.setExamineUser(r.getLong("user_id"));
                    }
                    examineLog.setRecordId(examineRecord.getRecordId());
                    examineLog.setIsRecheck(0);
                    examineLog.save();
                    examineLogIdList.add(examineLog.getLogId());
                }

            } else {
                if (userId == null) {
                    return examineData.set("status", 2);
                }
                //授权审批
                examineLog.setExamineUser(userId);
                if (recordId == null) {
                    examineRecord.save();
                } else {
                    examineRecord.update();
                }
                examineLog.setRecordId(examineRecord.getRecordId());
                if (userId != null) {
                    examineLog.save();
                    examineLogIdList.add(examineLog.getLogId());
                }
            }
            examineData.set("examineLogIds",examineLogIdList);
            examineData.set("status", 1);
            examineData.set("recordId", examineRecord.getRecordId());
        }
        return examineData;
    }

    /**
     * 审核合同或者回款 recordId:审核记录id status:审批状态：审核状态  1 审核通过 2 审核拒绝 4 已撤回
     * remarks:审核备注 id:审核对象的id（合同或者回款的id）nextUserId:下一个审批人 ownerUserId:负责人
     */
    @Before(Tx.class)
    public R auditExamine(Integer recordId, Integer status, String remarks, Integer id, Long nextUserId, Long ownerUserId) {

        //当前审批人
        Long auditUserId = BaseUtil.getUser().getUserId();

        //根据审核记录id查询审核记录
        AdminExamineRecord examineRecord = AdminExamineRecord.dao.findById(recordId);
        if (examineRecord.getExamineStatus() == 4){
            return R.error("该审批已撤回");
        }
        if (status == 4) {
            if (!examineRecord.getCreateUser().equals(auditUserId) && !auditUserId.equals(BaseUtil.getSuperUser())) {
                return R.error("当前用户没有审批权限！");
            }
        } else {
            //【判断当前审批人是否有审批权限
            Record reco = Db.findFirst(Db.getSqlPara("admin.examineLog.queryExamineLog",
                    Kv.by("recordId", recordId).set("auditUserId", auditUserId).set("stepId", examineRecord.getExamineStepId())));
            if (reco == null) {
                return R.error("当前用户没有审批权限！");
            }
        }
        examineRecord.setExamineStatus(status);
        //查询审批流程
        AdminExamine examine = AdminExamine.dao.findById(examineRecord.getExamineId());
        if (examine.getCategoryType() == 1) {
            ownerUserId = CrmContract.dao.findById(id).getOwnerUserId();
        } else if (examine.getCategoryType() == 2) {
            ownerUserId = CrmReceivables.dao.findById(id).getOwnerUserId();
        } else if(examine.getCategoryType() == 4){
            ownerUserId = TaskHours.dao.findById(id).getMainUserId();
        }else if(examine.getCategoryType() == 5){
            ownerUserId = Work.dao.findById(id).getSalesId();
        } else {
            ownerUserId = CrmInvoice.dao.findById(id).getOwnerUserId();
        }
        //查询当前审批步骤
        AdminExamineStep examineStep = AdminExamineStep.dao.findById(examineRecord.getExamineStepId());
        //查询当前审核日志
        AdminExamineLog nowadayExamineLog;
        if (examine.getExamineType() == 1) {
            nowadayExamineLog = AdminExamineLog.dao.findFirst(Db.getSql("admin.examineLog.queryNowadayExamineLogByRecordIdAndStepId"), examineRecord.getRecordId(), examineRecord.getExamineStepId(), auditUserId);
        } else {
            nowadayExamineLog = AdminExamineLog.dao.findFirst(Db.getSql("admin.examineLog.queryNowadayExamineLogByRecordIdAndStatus"), examineRecord.getRecordId(), auditUserId);
        }

        //审核日志 添加审核人
        if (nowadayExamineLog != null) {
            nowadayExamineLog.setExamineTime(DateUtil.date());
            nowadayExamineLog.setRemarks(remarks);
        }

        if (status == 2) {
            //判断审核拒绝
            nowadayExamineLog.setExamineStatus(status);
            if (examineStep != null && examineStep.getStepType() == 2) {
//                examineRecord.setExamineStatus(2);
                Record record = Db.findFirst(Db.getSqlPara("admin.examineLog.queryCountByStepId", Kv.by("recordId", recordId).set("stepId", examineStep.getStepId())));
                if (record.getInt("toCount") == 0) {
                    examineRecord.setExamineStatus(status);
                }
            }

            if (examine.getCategoryType() == 1) {
                //合同
                Db.update(Db.getSql("crm.contract.updateCheckStatusById"),status,id);
            } else if (examine.getCategoryType() == 2) {
                //回款
                Db.update(Db.getSql("crm.receivables.updateCheckStatusById"),status,id);
            } else if (examine.getCategoryType() == 3) {
                //回款
                Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), status, id);
            }else if(examine.getCategoryType() == 4){
                //工时
                Db.update(Db.getSql("task.hours.updateCheckStatusById"), BaseUtil.getUserId(),status, id);
            }else if(examine.getCategoryType() == 5){
                //结项
                Db.update(Db.getSql("work.updateCheckStatusById"), status, id);
            }
        } else if (status == 4) {
            //先查询该审批流程的审批步骤的第一步
            AdminExamineStep oneExamineStep = AdminExamineStep.dao.findFirst(Db.getSql("admin.examineStep.queryExamineStepByExamineIdOrderByStepNum"), examine.getExamineId());
            //判断审核撤回
            AdminExamineLog examineLog = new AdminExamineLog();
            examineLog.setLogId(null);
            examineLog.setExamineUser(auditUserId);
            examineLog.setCreateTime(DateUtil.date());
            examineLog.setCreateUser(auditUserId);
            examineLog.setExamineStatus(status);
            examineLog.setExamineTime(new Date());
            if (examine.getExamineType() == 1) {
                examineRecord.setExamineStepId(oneExamineStep.getStepId());
                examineLog.setExamineStepId(examineStep.getStepId());
                examineLog.setOrderId(examineStep.getStepNum());
            } else {
                Integer orderId = Db.queryInt(Db.getSql("admin.examineStep.queryExamineStepOrderId"), recordId);
                if (orderId == null) {
                    orderId = 1;
                }
                examineLog.setOrderId(orderId);
            }
            examineLog.setRecordId(examineRecord.getRecordId());
            examineLog.setRemarks(remarks);
            examineLog.save();
            Db.update(Db.getSql("admin.examineLog.updateExamineLogIsRecheckByRecordId"), recordId);
            if (examine.getCategoryType() == 1) {
                //合同
                CrmContract contract = CrmContract.dao.findById(id);
                if (contract.getCheckStatus() == 1) {
                    return R.error("该合同已审核通过，不能撤回！");
                }
                Db.update(Db.getSql("crm.contract.updateCheckStatusById"),4,id);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_CONTRACT, recordId, BehaviorEnum.RECHECK_EXAMINE);
            } else if (examine.getCategoryType() == 2) {
                //回款
                CrmReceivables receivables = CrmReceivables.dao.findById(id);
                if (receivables.getCheckStatus() == 1) {
                    return R.error("该回款已审核通过，不能撤回！");
                }
                Db.update(Db.getSql("crm.receivables.updateCheckStatusById"),4,id);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_RECEIVABLES, recordId, BehaviorEnum.RECHECK_EXAMINE);
            }else if(examine.getCategoryType() == 4){
                TaskHours taskHours = TaskHours.dao.findById(id);
                if(taskHours.getCheckStatus() ==1){
                    return R.error("该工时已审核通过，不能撤回！");
                }
                //工时
                Db.update(Db.getSql("task.hours.updateCheckStatusById"), BaseUtil.getUserId(),4, id);
            }else if(examine.getCategoryType() == 5){
                Work work = Work.dao.findById(id);
                if(work.getCheckStatus() == 1){
                    return R.error("该工时已审核通过，不能撤回！");
                }
                //结项
                Db.update(Db.getSql("work.updateCheckStatusById"), 4, id);
            } else {
                CrmInvoice crmInvoice = CrmInvoice.dao.findById(id);
                if (crmInvoice.getCheckStatus() == 1) {
                    return R.error("该发票已审核通过，不能撤回！");
                }
                Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), 4, id);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_INVOICE, recordId, BehaviorEnum.RECHECK_EXAMINE);
            }
        } else {
            //审核通过
            nowadayExamineLog.setExamineStatus(status);
            //判断该审批流程类型
            if (examine.getExamineType() == 1) {
                //固定审批

                //查询下一个审批步骤
                AdminExamineStep nextExamineStep =
                        AdminExamineStep.dao.findFirst(Db.getSql("admin.examineStep.queryExamineStepByNextExamineIdOrderByStepId"), examine.getExamineId(), examineRecord.getExamineStepId());

                boolean flag = true;
                //判断是否是并签
                if (examineStep.getStepType() == 3) {
                    //查询当前并签是否都完成
                    //根据审核记录ID，审核步骤ID，查询审核日志
                    //当前并签人员
                    nowadayExamineLog.update();
                    String[] userIds = examineStep.getCheckUserId().split(",");
                    for (String userId : userIds) {
                        if (StrUtil.isNotEmpty(userId)) {
                            AdminExamineLog examineLog = AdminExamineLog.dao.findFirst(Db.getSql("admin.examineLog.queryNowadayExamineLogByRecordIdAndStepId"), examineRecord.getRecordId(), examineRecord.getExamineStepId(), userId);
                            if (examineLog.getExamineStatus() == 0) {
                                //并签未走完
                                flag = false;
                                break;
                            }

                        }
                    }
                    //并签未完成
                    if (!flag) {
                        examineRecord.setExamineStatus(3);
                        if (examine.getCategoryType() == 1) {
                            //合同
                            Db.update(Db.getSql("crm.contract.updateCheckStatusById"),3,id);
                        } else if (examine.getCategoryType() == 2) {
                            //回款
                            Db.update(Db.getSql("crm.receivables.updateCheckStatusById"),3,id);
                        }else if(examine.getCategoryType() == 4){
                            //工时
                            Db.update(Db.getSql("task.hours.updateCheckStatusById"), BaseUtil.getUserId(),3, id);
                        }else if(examine.getCategoryType() == 5){
                            //结项
                            Db.update(Db.getSql("work.updateCheckStatusById"), 3, id);
                        } else {
                            Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), 3, id);
                        }
                    }
                }
                if (flag) {
                    //判断是否有下一步流程三大队
                    if (nextExamineStep != null) {
                        //有下一步流程
                        examineRecord.setExamineStatus(3);
                        examineRecord.setExamineStepId(nextExamineStep.getStepId());

                        AdminExamineLog examineLog = new AdminExamineLog();
                        examineLog.setOrderId(nextExamineStep.getStepNum());
                        if (nextExamineStep.getStepType() == 2 || nextExamineStep.getStepType() == 3) {
                            //并签或者或签
                            String[] userIds = nextExamineStep.getCheckUserId().split(",");
                            for (String uid : userIds) {
                                if (StrUtil.isNotEmpty(uid)) {
                                    examineLog.setLogId(null);
                                    examineLog.setExamineUser(Long.valueOf(uid));
                                    examineLog.setCreateTime(DateUtil.date());
                                    examineLog.setCreateUser(BaseUtil.getUser().getUserId());
                                    examineLog.setExamineStatus(0);
                                    examineLog.setIsRecheck(0);
                                    examineLog.setExamineStepId(nextExamineStep.getStepId());
                                    examineLog.setRecordId(examineRecord.getRecordId());

                                    examineLog.save();
                                    if (examine.getCategoryType() == 1){
                                        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_EXAMINE,examineLog,ownerUserId);
                                    }else if (examine.getCategoryType() == 2){
                                        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE,examineLog,ownerUserId);
                                    } else if (examine.getCategoryType() == 3) {
                                        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_EXAMINE, examineLog, ownerUserId);
                                    }
                                }
                            }
                        } else if (nextExamineStep.getStepType() == 1) {
                            Record r = Db.findFirst(Db.getSql("admin.examineLog.queryUserByUserId"), ownerUserId);
                            examineLog.setLogId(null);
                            if (r == null|| r.getLong("user_id") == null) {
                                examineLog.setExamineUser(BaseUtil.getSuperUser());
                            } else {
                                examineLog.setExamineUser(r.getLong("user_id"));

                            }
                            examineLog.setExamineStatus(0);
                            examineLog.setCreateTime(DateUtil.date());
                            examineLog.setCreateUser(BaseUtil.getUser().getUserId());
                            examineLog.setIsRecheck(0);
                            examineLog.setExamineStepId(nextExamineStep.getStepId());
                            examineLog.setRecordId(examineRecord.getRecordId());
                            examineLog.save();
                            if (examine.getCategoryType() == 1){
                                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_EXAMINE,examineLog,ownerUserId);
                            }else if (examine.getCategoryType() == 2){
                                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE,examineLog,ownerUserId);
                            } else if (examine.getCategoryType() == 3) {
                                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_EXAMINE, examineLog, ownerUserId);
                            }
                        } else {

                            examineLog.setLogId(null);
                            AdminUser adminUser = AdminUser.dao.findById(nowadayExamineLog.getExamineUser());
                            if (adminUser != null && adminUser.getParentId() != null) {
                                examineLog.setExamineUser(adminUser.getParentId());
                            }else {
                                examineLog.setExamineUser(nowadayExamineLog.getExamineUser());
                            }

                            examineLog.setExamineStatus(0);
                            examineLog.setCreateTime(DateUtil.date());
                            examineLog.setCreateUser(BaseUtil.getUser().getUserId());
                            examineLog.setExamineStepId(nextExamineStep.getStepId());
                            examineLog.setRecordId(examineRecord.getRecordId());
                            examineLog.setIsRecheck(0);
                            examineLog.save();
                            if (examine.getCategoryType() == 1){
                                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_EXAMINE,examineLog,ownerUserId);
                            }else if (examine.getCategoryType() == 2){
                                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE,examineLog,ownerUserId);
                            } else if (examine.getCategoryType() == 3) {
                                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_EXAMINE, examineLog, ownerUserId);
                            }
                        }

                        // AdminExamineLog examineLog = new AdminExamineLog();
                        if (examine.getCategoryType() == 1) {
                            //合同
                            Db.update(Db.getSql("crm.contract.updateCheckStatusById"),3,id);
                        } else if (examine.getCategoryType() == 2) {
                            //回款
                            Db.update(Db.getSql("crm.receivables.updateCheckStatusById"),3,id);
                        }else if(examine.getCategoryType() == 4){
                            //工时
                            Db.update(Db.getSql("task.hours.updateCheckStatusById"),BaseUtil.getUserId(), 3, id);
                        }else if(examine.getCategoryType() == 5){
                            //结项
                            Db.update(Db.getSql("work.updateCheckStatusById"), 3, id);
                        } else {
                            Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), 3, id);
                        }
                    } else {
                        //没有下一审批流程步骤
                        if (examine.getCategoryType() == 1) {
                            //合同
                            Db.update(Db.getSql("crm.contract.updateCheckStatusById"),1,id);
                            CrmContract contract = CrmContract.dao.findById(id);
                            Db.update(Db.getSql("crm.customer.updateDealStatusById"),1,contract.getOrderDate() != null ? contract.getOrderDate() : new Date(),contract.getCustomerId());
                        } else if (examine.getCategoryType() == 2) {
                            Db.update(Db.getSql("crm.receivables.updateCheckStatusById"), 1, id);
                            updateContractMoney(id);
                        }else if(examine.getCategoryType() == 4){
                            //工时
                            Db.update("update 72crm_task_hours set check_status = ?,examine_time=?,check_user_id=? where hours_id = ?", 1,new Date(),BaseUtil.getUserId(),id);
                        }else if(examine.getCategoryType() == 5){
                            //结项
                            Db.update(Db.getSql("work.updateCheckStatusByIdThrough"), 1,new Date(), id);
                            Work byId = Aop.get(Work.class).findById(id);
                            String s = Db.queryStr("select remarks from 72crm_admin_examine_record where record_id=? ", byId.getExamineRecordId());
                            Db.update("update 72crm_work set work_phase=? where work_id=?",s,id);
                        } else {
                            Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), 1, id);
                        }

                    }
                }
            } else {
                //授权审批
                if (nextUserId != null) {
                    //有下一审批人
                    examineRecord.setExamineStatus(3);
                    AdminExamineLog examineLog = new AdminExamineLog();
                    examineLog.setCreateTime(DateUtil.date());
                    examineLog.setCreateUser(BaseUtil.getUser().getUserId());
                    examineLog.setExamineUser(nextUserId);
                    examineLog.setExamineStatus(0);
                    examineLog.setIsRecheck(0);
                    examineLog.setRecordId(examineRecord.getRecordId());
                    examineLog.setOrderId(nowadayExamineLog.getOrderId() + 1);
                    examineLog.save();
                    if (examine.getCategoryType() == 1) {
                        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_EXAMINE, examineLog, ownerUserId);
                    } else if (examine.getCategoryType() == 2) {
                        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE, examineLog, ownerUserId);
                    } else if (examine.getCategoryType() == 3) {
                        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_EXAMINE, examineLog, ownerUserId);
                    }
                    if (examine.getCategoryType() == 1) {
                        //合同
                        Db.update(Db.getSql("crm.contract.updateCheckStatusById"),3,id);
                    } else if (examine.getCategoryType() == 2) {
                        //回款
                        Db.update(Db.getSql("crm.receivables.updateCheckStatusById"),3,id);
                    }else if(examine.getCategoryType() == 4){
                        //工时
                        Db.update(Db.getSql("task.hours.updateCheckStatusById"),BaseUtil.getUserId(), 3, id);
                    }else if(examine.getCategoryType() == 5){
                        //结项
                        Db.update(Db.getSql("work.updateCheckStatusById"), 3, id);
                    } else {
                        Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), 3, id);
                    }
                } else {
                    //没有下一审批人
                    if (examine.getCategoryType() == 1) {
                        //合同
                        Db.update(Db.getSql("crm.contract.updateCheckStatusById"),1,id);
                        CrmContract contract = CrmContract.dao.findById(id);
                        Db.update(Db.getSql("crm.customer.updateDealStatusById"),1,contract.getOrderDate() != null ? contract.getOrderDate() : new Date(),contract.getCustomerId());
                    } else if (examine.getCategoryType() == 2) {
                        Db.update(Db.getSql("crm.receivables.updateCheckStatusById"), 1, id);
                        updateContractMoney(id);
                    }else if(examine.getCategoryType() == 4){
                        //工时
                        Db.update("update 72crm_task_hours set check_status = ?,examine_time=?,check_user_id=? where hours_id = ?", 1,new Date(),BaseUtil.getUserId(),id);
                    }else if(examine.getCategoryType() == 5){
                        //结项
                        Db.update(Db.getSql("work.updateCheckStatusByIdThrough"), 1,new Date(), id);
                        Work byId = Aop.get(Work.class).findById(id);
                        String s = Db.queryStr("select remarks from 72crm_admin_examine_record where record_id=? ", byId.getExamineRecordId());
                        Db.update("update 72crm_work set work_phase=? where work_id=?",s,id);
                    }else {
                        Db.update(Db.getSql("crm.invoice.updateCheckStatusById"), 1, id);
                    }
                }
            }
        }
        if (status != 4) {
            nowadayExamineLog.update();
        }
        examineRecord.setRemarks(remarks);
        if(examineRecord.getExamineStatus().equals(1)){
            if(examine.getCategoryType().equals(1)){
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_PASS,examineRecord,auditUserId);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_CONTRACT, recordId, BehaviorEnum.PASS_EXAMINE);
            } else if (examine.getCategoryType().equals(2)) {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_PASS,examineRecord,auditUserId);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_RECEIVABLES, recordId, BehaviorEnum.PASS_EXAMINE);
            } else if (examine.getCategoryType().equals(3)) {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_PASS, examineRecord, auditUserId);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_INVOICE, recordId, BehaviorEnum.PASS_EXAMINE);
            }
        }else if(examineRecord.getExamineStatus().equals(2)){
            if(examine.getCategoryType().equals(1)){
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_REJECT,examineRecord,auditUserId);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_CONTRACT, recordId, BehaviorEnum.REJECT_EXAMINE);
            } else if (examine.getCategoryType().equals(2)) {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_REJECT,examineRecord,auditUserId);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_RECEIVABLES, recordId, BehaviorEnum.REJECT_EXAMINE);
            } else if (examine.getCategoryType().equals(3)) {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_REJECT, examineRecord, auditUserId);
                Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_INVOICE, recordId, BehaviorEnum.REJECT_EXAMINE);
            }
        }
        examineRecord.setRemarks(null);
        return examineRecord.update() ? R.ok() : R.error();
    }

    private boolean updateContractMoney(Integer id){
        CrmReceivables receivables = CrmReceivables.dao.findById(id);
        CrmContract contract = CrmContract.dao.findById(receivables.getContractId());
        int update = Db.template("crm.contract.updateReceivedMoney", Kv.by("money", receivables.getMoney()).set("contractId", contract.getContractId()).set("unreceivedMoney", contract.getUnreceivedMoney())).update();
        return update>0;
    }

    public R queryExamineLogList(Integer recordId) {
        //根据审核记录id查询审核记录
        Integer type = Db.queryInt(Db.getSql("admin.examineLog.queryExamineByRecordId"),recordId);
        Record record;
        if (type == 1) {
            record = Db.findFirst(Db.getSql("admin.examineLog.queryContractByRecordId"), recordId);
        } else if (type == 2) {
            record = Db.findFirst(Db.getSql("admin.examineLog.queryReceivablesByRecordId"), recordId);
        } else if (type == 4) {
            record = Db.findFirst(Db.getSql("admin.examineLog.queryHoursByRecordId"), recordId);
        } else if (type == 5) {
            record = Db.findFirst(Db.getSql("admin.examineLog.queryWorkByRecordId"), recordId);
        } else {
            record = Db.findFirst(Db.getSql("admin.examineLog.queryInvoiceByRecordId"), recordId);
        }
        List<Record> logs = Db.find(Db.getSql("admin.examineLog.queryExamineLogByRecordIdByStep"), recordId);
        List<Record> recordList = new ArrayList<>();
        if (record.getInt("check_status") != 5){
            recordList.add(new Record().set("examine_status",6).set("examine_time",record.getDate("create_time")).set("orderId",0).set("realname",record.getStr("realname")).set("remarks","").set("user_id",record.getInt("owner_user_id")));
        }else {
            recordList.add(new Record().set("examine_status",5).set("examine_time",record.getDate("create_time")).set("orderId",0).set("realname",record.getStr("realname")).set("remarks","").set("user_id",record.getInt("owner_user_id")));
        }
        recordList.addAll(logs);
        return R.ok().put("data", recordList);
    }

    /**
     * 根据审核记录id，查询审核日志
     * ownerUserId 负责人ID
     */
    public R queryExamineRecordList(Integer recordId, Long ownerUserId) {
        JSONObject jsonObject = new JSONObject();
        Record examineRecord = Db.findFirst(Db.getSql("admin.examineStep.queryExamineRecordById"), recordId);
        //如果当前审批已撤回
        if (examineRecord.getInt("examine_status") == 4 ||  examineRecord.getInt("examine_status") == 5) {
            jsonObject.put("examineType", 1);
            List<Record> user = Db.find(Db.getSql("admin.examineLog.queryUserByRecordId"), recordId,examineRecord.getInt("examine_status"));

            if (examineRecord.getInt("examine_status") == 5) {
                user = Db.find(Db.getSqlPara("admin.examineLog.queryRecordByUserIdAndStatus", Kv.by("create_user", examineRecord.getInt("create_user")).set("examineTime", examineRecord.getDate("examineTime"))));
                examineRecord.set("examine_status", 6);
            }
            examineRecord.set("userList", user);
            List<Record> records = new ArrayList<>();
            records.add(examineRecord);
            jsonObject.put("steps", records);
            return R.ok().put("data", jsonObject);
        }
        AdminExamine adminExamine = AdminExamine.dao.findById(examineRecord.getInt("examine_id"));
        List<Record> list = new ArrayList<>();
        Record rec = Db.findFirst(Db.getSql("admin.examineLog.queryRecordAndId"), recordId);


        //当前审批人
        Long auditUserId = BaseUtil.getUser().getUserId();
        //jsonObject.put("isRecheck",0);
        //判断是否有撤回权限
        if ((auditUserId.equals(examineRecord.getLong("create_user")) || auditUserId.equals(BaseUtil.getSuperUser()))
                && (examineRecord.getInt("examine_status") == 0 ||examineRecord.getInt("examine_status") == 3)) {
            jsonObject.put("isRecheck", 1);
        } else {
            jsonObject.put("isRecheck", 0);
        }
        if (adminExamine.getExamineType() == 2) {
            Record log = Db.findFirst(Db.getSqlPara("admin.examineLog.queryRecordByUserIdAndStatus", Kv.by("create_user", rec.getInt("create_user")).set("examineTime", rec.getDate("examineTime"))));
            rec.set("examinUser", log);
            list.add(rec);
            //授权审批
            List<Record> logs = Db.find(Db.getSql("admin.examineLog.queryExamineLogAndUserByRecordId"), recordId);
            logs.forEach(r -> {
                Record l = Db.findFirst(Db.getSql("admin.examineLog.queryExamineLogAndUserByLogId"), r.getInt("log_id"));
                r.set("examinUser", l);
            });
            list.addAll(logs);
            //判断当前用户有没有权限审核
            Record reco = Db.findFirst(Db.getSqlPara("admin.examineLog.queryExamineLog",
                    Kv.by("recordId", recordId).set("auditUserId", auditUserId)));
            if (reco != null) {
                jsonObject.put("isCheck", 1);
            } else {
                jsonObject.put("isCheck", 0);
            }
            jsonObject.put("examineType", 2);
            jsonObject.put("steps", list);

        } else {
            jsonObject.put("examineType", 1);
            //固定审批
            List<Record> steps = Db.find("select * from 72crm_admin_examine_step where  examine_id = ? ORDER BY step_num", adminExamine.getExamineId());
               //上一审核步骤
                Long lsatuUserId = null;
            for (Record step: steps) {
                List<Record> logs ;
                if (step.getInt("step_type") == 1) {
                    //负责人主管
                     logs = Db.find(Db.getSql("admin.examineLog.queryUserByRecordIdAndStepIdAndStatus"), recordId, step.getInt("step_id"));
                    if (logs.size() == 1){
                        if (logs.get(0).getInt("user_id")==null){
                            logs = null;
                        }

                    }
                    //已经创建审核日志
                    if (logs != null && logs.size() != 0) {
                        for (Record record : logs) {
                            step.set("examine_status", record.getInt("examine_status"));
                        }
                        step.set("userList", logs);
                    } else {
                        step.set("examine_status", 0);
                        //还未创建审核日志
                        //查询负责人主管
                        List<Record> r = Db.find(Db.getSql("admin.examineLog.queryUserByUserId"), ownerUserId);
                        if (r.size() == 1){
                            if (r.get(0).getInt("user_id")==null){
                                r = null;
                            }

                        }
                        if (r == null || r.size() == 0) {
                            r = Db.find(Db.getSql("admin.examineLog.queryUserByUserIdAnd"), BaseUtil.getSuperUser());
                        }
                        step.set("userList", r);
                    }
                } else if (step.getInt("step_type") == 2 || step.getInt("step_type") == 3) {
                    //先判断是否已经审核过
                   logs = Db.find(Db.getSql("admin.examineLog.queryUserByRecordIdAndStepIdAndStatus"), recordId, step.getInt("step_id"));
                    if (logs != null && logs.size() != 0) {
                        //已经创建审核日志
                        int status = 0;
                        if (step.getInt("step_type") == 2) {
                            Optional<Record> optional = logs.stream().filter(log -> log.getDate("examineTime") != null).sorted(Comparator.comparingLong(log -> log.getDate("examineTime").getTime())).findFirst();
                            if (optional.isPresent()){
                                status = optional.get().getInt("examine_status");
                            }
                        }
                        if (step.getInt("step_type") == 3) {
                            int i = 0;
                            for (Record record : logs) {
                                if (record.getInt("examine_status") == 2) {
                                    status = 2;
                                }
                                if (record.getInt("examine_status") == 1) {
                                    i++;
                                }
                            }
                            if (i == logs.size()) {
                                status = 1;
                            }
                        }
                        step.set("examine_status", status);
                        step.set("userList", logs);
                    } else {
                        //该步骤还未审核
                        logs = new ArrayList<>();
                        String[] userIds = step.getStr("check_user_id").split(",");
                        for (String userId : userIds) {
                            if (StrUtil.isNotEmpty(userId)) {
                                Record user = Db.findFirst(Db.getSql("admin.examineLog.queryUserByUserIdAndStatus"), userId);
                                if (user!=null){
                                    logs.add(user);
                                }
                            }
                        }
                        step.set("examine_status", 0);
                        step.set("userList", logs);
                    }
                } else {
                    //主管的主管
                    logs = Db.find(Db.getSql("admin.examineLog.queryUserByRecordIdAndStepIdAndStatus"), recordId, step.getInt("step_id"));
                    if (logs.size() == 1){
                        if (logs.get(0).getInt("user_id")==null){
                            logs = null;
                        }

                    }
                    //已经创建审核日志
                    if (logs != null && logs.size() != 0) {
                        for (Record record : logs) {
                            step.set("examine_status", record.getInt("examine_status"));
                        }
                        step.set("userList", logs);
                    } else {
                        step.set("examine_status", 0);
                        AdminUser adminUser = AdminUser.dao.findById(lsatuUserId);
                        if (adminUser != null && adminUser.getParentId() != null) {
                            logs = Db.find(Db.getSql("admin.examineLog.queryUserByUserIdAnd"),adminUser.getParentId());
                        }else {
                            logs = Db.find(Db.getSql("admin.examineLog.queryUserByUserIdAnd"),lsatuUserId);
                        }
                        //查询负责人主管的主管
                        step.set("userList", logs );
                    }
                }
                if (logs.size()>0){
                    lsatuUserId = logs.get(0).getLong("user_id");
                }
            }
            //判断当前用户有没有权限审核
            Record reco = Db.findFirst(Db.getSqlPara("admin.examineLog.queryExamineLog",
                    Kv.by("recordId", recordId).set("auditUserId", auditUserId).set("stepId", examineRecord.getInt("examine_step_id"))));
            if (reco != null  && (examineRecord.getInt("examine_status") == 3 || examineRecord.getInt("examine_status") == 0)) {
                jsonObject.put("isCheck", 1);
            } else {
                jsonObject.put("isCheck", 0);
            }
            List<Record> logs = Db.find(Db.getSqlPara("admin.examineLog.queryRecordByUserIdAndStatus", Kv.by("create_user", rec.getInt("create_user")).set("examineTime", rec.getDate("examineTime"))));
            rec.set("userList", logs).set("stepType", 5);
            list.add(rec);
            list.addAll(steps);
            jsonObject.put("steps", list);
        }
        return R.ok().put("data", jsonObject);
    }
    public Page<Record> myInitiate(BasePageRequest basePageRequest){
        basePageRequest.getJsonObject().put("userId", BaseUtil.getUserId());
        SqlPara sqlPara=Db.getSqlPara("admin.examineLog.myInitiate",basePageRequest.getJsonObject());
        Page<Record> recordPage=Db.paginate(basePageRequest.getPage(),basePageRequest.getLimit(),sqlPara);
        transfer(recordPage.getList());
        return recordPage;
    }

    public Page<Record> myExamine(BasePageRequest basePageRequest){
        basePageRequest.getJsonObject().put("userId", BaseUtil.getUserId());
        basePageRequest.getJsonObject().put("isAdmin", BaseUtil.isAdmin());
        basePageRequest.getJsonObject().put("status",basePageRequest.getJsonObject().getInteger("status"));
        basePageRequest.getJsonObject().put("categoryType",basePageRequest.getJsonObject().getInteger("categoryType"));
        SqlPara sqlPara=Db.getSqlPara("admin.examineLog.myExamine",basePageRequest.getJsonObject());
        Page<Record> recordPage=Db.paginate(basePageRequest.getPage(),basePageRequest.getLimit(),sqlPara);
        transfer(recordPage.getList());
        return recordPage;
    }
    public R myExamineExport(BasePageRequest basePageRequest){
        basePageRequest.getJsonObject().put("userId", BaseUtil.getUserId());
        basePageRequest.getJsonObject().put("isAdmin", BaseUtil.isAdmin());
        basePageRequest.getJsonObject().put("status",basePageRequest.getJsonObject().getInteger("status"));
        basePageRequest.getJsonObject().put("categoryType",basePageRequest.getJsonObject().getInteger("categoryType"));
        SqlPara sqlPara=Db.getSqlPara("admin.examineLog.myExamine",basePageRequest.getJsonObject());
        List<Record> records = Db.find(sqlPara);
        transfer(records);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }
    public void transfer(List<Record> recordList){
        Long userId= BaseUtil.getUserId();
        recordList.forEach(record -> {
            SqlPara examineSqlPara=Db.getSqlPara("admin.examineLog.queryExamineUserByExamineLog",record);
            record.set("examineName",Db.queryStr(examineSqlPara.getSql(),examineSqlPara.getPara()));
            AdminUser user= AdminUser.dao.findByIdLoadColumns(record.getInt("create_user_id"),"user_id,realname,img,parent_id");
            record.set("create_user", user);
            Integer examineStatus = record.getInt("examine_status");
            Map<String,Integer> permission = new HashMap<>();
            Long createUserId = record.getLong("create_user_id");

            if((createUserId.equals(userId) || userId.equals(BaseUtil.getSuperUser())) && (examineStatus == 0 ||examineStatus == 3)){
                permission.put("isRecheck", 1);
            }else{
                permission.put("isRecheck", 0);
            }
            SqlPara sqlPara = Db.getSqlPara("admin.examineLog.queryExamineLog",Kv.by("recordId", record.get("record_id")).set("auditUserId", userId).set("stepId", record.get("examine_step_id")));
            Record ExamineLog = Db.findFirst(sqlPara);;
            if(ExamineLog != null){
                permission.put("isCheck", 1);
            }else{
                permission.put("isCheck", 0);
            }
            record.set("permission", permission);
        });
    }
    public void deleteExamine(Integer recordId){
        AdminExamineRecord.dao.deleteById(recordId);
        Db.delete("delete from `72crm_admin_examine_log` where record_id = ?",recordId);
    }
}
