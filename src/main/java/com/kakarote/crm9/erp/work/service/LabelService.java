package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.erp.work.entity.WorkTaskLabel;
import com.kakarote.crm9.erp.work.entity.WorkTaskLabelOrder;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LabelService {
    @Inject
    private WorkbenchService workbenchService;

    @Inject
    private WorkService workService;

    public R setLabel(WorkTaskLabel taskLable) {
        boolean bol;
        if (taskLable.getLabelId() == null) {
            if (StrUtil.isEmpty(taskLable.getName())){
                return R.error("标签名称不能为空");
            }
            taskLable.setCreateTime(new Date());
            taskLable.setCreateUserId(BaseUtil.getUser().getUserId());
            bol = taskLable.save();
        } else {

            bol = taskLable.update();
        }
        return bol ? R.ok() : R.error();
    }

    public R deleteLabel(String labelId){
        Integer count = Db.queryInt("select count(*) from 72crm_task where label_id like concat('%,',?,',%');", labelId);
        if(count>0){
            return R.error("使用中的标签不能删除");
        }
        Db.delete("delete from 72crm_work_task_label where label_id = ?",labelId);
        return R.ok();
    }

    public R getLabelList(){
        Long userId = BaseUtil.getUserId();
        List<Record> orderList = Db.find("select label_id,order_num from `72crm_work_task_label_order` where user_id = ?", userId);
        List<Record> recordList = Db.find("select label_id, name, color, 999 orderNum from `72crm_work_task_label` ");
        recordList.forEach(record -> {
            orderList.forEach(order->{
                if (order.getInt("label_id").equals(record.getInt("label_id"))){
                    record.set("orderNum",order.getInt("order_num"));
                }
            });
        });
        List<Record> orderNum = recordList.stream().sorted(Comparator.comparingInt(record -> record.getInt("orderNum"))).collect(Collectors.toList());
        return R.ok().put("data",orderNum);
    }

    public R queryById(Integer labelId){
        return R.ok().put("data", WorkTaskLabel.dao.findById(labelId));
    }

    /**
     * 标签任务列表
     */
    public R getTaskList(Integer labelId){
        List<Record> taskList = Db.find(Db.getSqlPara("work.label.queryTaskList", Kv.by("labelId",labelId).set("userId", BaseUtil.getUserId())));
        workbenchService.taskListTransfer(taskList);
        Map<Integer,List<Record>> map = taskList.stream().collect(Collectors.groupingBy(record -> record.getInt("work_id")));
        List<Record> workList = Db.find(Db.getSqlPara("work.label.queryWorkList",Kv.by("labelId",labelId).set("userId", BaseUtil.getUserId())));
        workList.forEach(work -> work.set("list",map.get(work.getInt("work_id"))));
        return R.ok().put("data",workList);
    }

    public R getLabelListByOwn(){
        Long userId = BaseUtil.getUserId();
        List<String> labelIdList = Db.query(Db.getSql("work.label.queryLabelIdList"), userId, userId, userId);
        List<Integer> list = workService.toList(labelIdList);
        List<String> collect = list.stream().map(Object::toString).collect(Collectors.toList());
        List<WorkTaskLabel> resultList;
        if(AuthUtil.isWorkAdmin()){
            resultList = new WorkTaskLabel().dao().findAll();
        }else {
            resultList = new WorkTaskLabel().find("select * from `72crm_work_task_label` where label_id in (?)", String.join(",", collect));
        }
        return R.ok().put("data",resultList);
    }

    @Before(Tx.class)
    public void updateOrder(List<Integer> labelIds) {
        Long userId = BaseUtil.getUserId();
        Db.delete("delete from `72crm_work_task_label_order` where user_id = ?",userId);
        for (int i = 0; i < labelIds.size(); i++) {
            WorkTaskLabelOrder workTaskLabelOrder = new WorkTaskLabelOrder();
            workTaskLabelOrder.setLabelId(labelIds.get(i));
            workTaskLabelOrder.setUserId(userId);
            workTaskLabelOrder.setOrderNum(i+1);
            workTaskLabelOrder.save();
        }
    }
}
