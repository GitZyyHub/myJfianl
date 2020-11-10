package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.util.*;
import java.util.stream.Collectors;

public class WorkbenchService {

    @Inject
    private TaskService taskService;

    public R myTask(Long userId){
        List<Record> result = new ArrayList<>();
        result.add(new Record().set("title", "收件箱").set("is_top", 0).set("count", 0).set("list", new ArrayList<>()));
        result.add(new Record().set("title", "今天要做").set("is_top", 1).set("count", 0).set("list", new ArrayList<>()));
        result.add(new Record().set("title", "下一步要做").set("is_top", 2).set("count", 0).set("list", new ArrayList<>()));
        result.add(new Record().set("title", "以后要做").set("is_top", 3).set("count", 0).set("list", new ArrayList<>()));
        result.forEach(record -> {
            Integer isTop = record.getInt("is_top");
            List<Record> resultList = Db.find(Db.getSqlPara("work.workbench.myTask", Kv.by("userId", userId).set("isTop", isTop)));
            record.set("count", resultList.size());
            if (resultList.size() != 0) {
                resultList.sort(Comparator.comparingInt(o -> o.getInt("top_order_num")));
                taskListTransfer(resultList);
                record.set("list", resultList);
            }

        });
        return R.ok().put("data", result);
    }

    public List<Map<String, Object>> workBenchTaskExport() {
        Kv kv=Kv.by("userId", BaseUtil.getUserId());
        List<Record> recordList = Db.find(Db.getSqlPara("work.workbench.myTaskExport", kv));
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> {
            //拼接关联业务内容
            String relateCrmWork = "";
            if (StrUtil.isNotEmpty(record.getStr("customer_ids"))) {
                relateCrmWork = relateCrmWork + "客户 【" + record.getStr("customer_name") + "】\n";
            }
            if (StrUtil.isNotEmpty(record.getStr("contacts_ids"))) {
                relateCrmWork = relateCrmWork + "联系人 【" + record.getStr("contacts_name") + "】\n";
            }
            if (StrUtil.isNotEmpty(record.getStr("business_ids"))) {
                relateCrmWork = relateCrmWork + "商机 【" + record.getStr("business_name") + "】\n";
            }
            if (StrUtil.isNotEmpty(record.getStr("contract_ids"))) {
                relateCrmWork = relateCrmWork + "合同 【" + record.getStr("contract_name") + "】\n";
            }
            record.set("relateCrmWork", relateCrmWork);
            list.add(record.getColumns());
        });
        return list;
    }

    public void taskListTransfer(List<Record> taskList){
        List<Integer> taskIdList = taskList.stream().map(record -> record.getInt("task_id")).collect(Collectors.toList());
        List<Record> allLabelList = Db.find(Db.getSqlPara("work.workbench.queryTaskLabel", Kv.by("taskIds", taskIdList)));
        Map<Integer, List<Record>> labelMap = allLabelList.stream().collect(Collectors.groupingBy(record -> record.getInt("task_id")));
        taskList.forEach(task -> {
            Date stopTime = task.getDate("stop_time");
            if(stopTime != null){
                if(stopTime.getTime() < System.currentTimeMillis()){
                    task.set("isEnd", 1);
                }else{
                    task.set("isEnd", 0);
                }
            }else{
                task.set("isEnd", 0);
            }
            Integer taskId = task.getInt("task_id");
            if (ObjectUtil.isNotEmpty(task.getLong("main_user_id"))) {
                task.set("mainUser", new Record().set("img", task.getStr("main_user_img")).set("realname", task.getStr("main_user_name")).set("userId", task.getLong("main_user_id")));
            } else {
                task.set("mainUser", null);
            }
            ArrayList<Record> labelList = new ArrayList<>();
            if (labelMap.get(taskId) != null) {
                labelList.addAll(labelMap.get(taskId));
            }
            task.set("labelList", labelList);
            int relationCount = 0;
            relationCount += TagUtil.toSet(task.getStr("customer_ids")).size();
            relationCount += TagUtil.toSet(task.getStr("contacts_ids")).size();
            relationCount += TagUtil.toSet(task.getStr("business_ids")).size();
            relationCount += TagUtil.toSet(task.getStr("contract_ids")).size();
            task.set("relationCount", relationCount);
        });
    }

    public R dateList(String startTime, String endTime){
        List<Task> taskList = Task.dao.find(Db.getSqlPara("work.workbench.dateList", Kv.by("userId", BaseUtil.getUser().getUserId()).set("startTime", startTime).set("endTime", endTime)));
        return R.ok().put("data", taskList);
    }

    @Before(Tx.class)
    public R updateTop(JSONObject jsonObject){
        String updateSql = "update `72crm_task` set is_top = ?,top_order_num = ? where task_id = ?";
        if(jsonObject.containsKey("fromList")){
            JSONArray fromlist = jsonObject.getJSONArray("fromList");
            Integer fromTopId = jsonObject.getInteger("fromTopId");
            for(int i = 1; i <= fromlist.size(); i++){
                Db.update(updateSql, fromTopId, i, fromlist.get(i - 1));
            }
        }
        if(jsonObject.containsKey("toList")){
            JSONArray tolist = jsonObject.getJSONArray("toList");
            Integer toTopId = jsonObject.getInteger("toTopId");
            for(int i = 1; i <= tolist.size(); i++){
                Db.update(updateSql, toTopId, i, tolist.get(i - 1));
            }
        }
        return R.ok();
    }
}
