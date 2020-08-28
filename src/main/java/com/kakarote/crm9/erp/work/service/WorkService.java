package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.entity.AdminMenu;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMenuService;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.utils.*;
import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class WorkService {

    @Inject
    private AdminFileService adminFileService;

    @Inject
    private WorkbenchService workbenchService;

    @Inject
    private AdminMenuService adminMenuService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    @Before(Tx.class)
    public R setWork(Work work) {
        if (Arrays.asList(work._getAttrNames()).contains("name") && StrUtil.isEmpty(work.getName())) {
            return R.error("项目名称不能为空！");
        }
        Long userId = BaseUtil.getUser().getUserId();
        boolean bol;
        if (work.getWorkId() == null) {
            if (StrUtil.isEmpty(work.getBatchId())) {
                work.setBatchId(IdUtil.simpleUUID());
            }
            Set<Long> ownerUserIds = new HashSet<>();
            ownerUserIds.add(userId);
            if (work.getOwnerUserId() != null) {
                ownerUserIds.addAll(TagUtil.toLongSet(work.getOwnerUserId()));
            }
            if (work.getIsOpen() == 1) {
                //公开项目删除负责人
                ownerUserIds.clear();
            }
            work.setOwnerUserId(TagUtil.fromLongSet(ownerUserIds));
            work.setCreateUserId(userId);
            work.setCreateTime(new Date());
            bol = work.save();
            WorkTaskClass workTaskClass = new WorkTaskClass();
            workTaskClass.setClassId(0);
            workTaskClass.setName("要做");
            workTaskClass.setCreateTime(new Date());
            workTaskClass.setCreateUserId(userId);
            workTaskClass.setWorkId(work.getWorkId());
            workTaskClass.setOrderNum(1);
            workTaskClass.save();
            workTaskClass.setName("在做");
            workTaskClass.setOrderNum(2);
            workTaskClass.save();
            workTaskClass.setName("待定");
            workTaskClass.setOrderNum(3);
            workTaskClass.save();
            ownerUserIds.forEach(ownerUserId -> {
                WorkUser workUser = new WorkUser();
                workUser.setWorkId(work.getWorkId());
                workUser.setUserId(ownerUserId);
                if (ownerUserId.equals(userId)) {
                    workUser.setRoleId(BaseUtil.getSmallAdminRole());
                    workUser.save();
                } else {
                    workUser.setRoleId(BaseUtil.getSmallEditRole());

                    workUser.save();
                }
            });
            actionRecordUtil.addObjectActionRecord(CrmEnum.WORK, work.getWorkId(), BehaviorEnum.SAVE);
        } else {
            Integer workId = work.getWorkId();
            Map<String, Object> columns = work.toRecord().getColumns();
            Work oldWork = new Work().findById(workId);
            if (columns.containsKey("owner_user_id")) {
                if (!ObjectUtil.isNull(columns.get("owner_user_id"))) {
                    work.setOwnerUserId(TagUtil.fromString(work.getOwnerUserId()));
                    Set<Long> oldOwnerUserIds = TagUtil.toLongSet(oldWork.getOwnerUserId());
                    Set<Long> ownerUserIds = TagUtil.toLongSet(work.getOwnerUserId());
                    Collection<Long> intersection = CollectionUtil.intersection(oldOwnerUserIds, ownerUserIds);
                    oldOwnerUserIds.removeAll(intersection);
                    ownerUserIds.removeAll(intersection);
                    for (Long next : oldOwnerUserIds) {
                        leave(work.getWorkId(), next);
                    }
                    for (Long ownerUserId : ownerUserIds) {
                        WorkUser workUser = new WorkUser();
                        workUser.setWorkId(work.getWorkId());
                        workUser.setUserId(ownerUserId);
                        workUser.setRoleId(BaseUtil.getSmallEditRole());
                        workUser.save();
                    }
                } else {
                    Db.delete("delete from `72crm_work_user` where work_id = ?", workId);
                    work.setOwnerUserId("," + userId + ",");
                    WorkUser workUser = new WorkUser();
                    workUser.setWorkId(work.getWorkId());
                    workUser.setUserId(userId);
                    workUser.setRoleId(BaseUtil.getSmallAdminRole());
                    workUser.save();
                }
            }
            //判断私有转公开还是公开转私有
            if (work.getIsOpen() != null && !work.getIsOpen().equals(oldWork.getIsOpen())) {
                if (work.getIsOpen() == 1) {
                    //公开项目删除负责人
                    work.setOwnerUserId(null);
                    Db.delete("delete from `72crm_work_user` where work_id = ?", workId);
                } else if (work.getIsOpen() == 0) {
                    List<Long> userList = Db.query("select user_id from `72crm_admin_user` where status != 0");
                    userList.remove(userId);
                    List<WorkUser> workUserList = new ArrayList<>();
                    WorkUser nowWorkUser = new WorkUser();
                    nowWorkUser.setWorkId(work.getWorkId());
                    nowWorkUser.setUserId(userId);
                    nowWorkUser.setRoleId(BaseUtil.getSmallAdminRole());
                    workUserList.add(nowWorkUser);
                    userList.forEach(id -> {
                        WorkUser workUser = new WorkUser();
                        workUser.setWorkId(work.getWorkId());
                        workUser.setUserId(id);
                        workUser.setRoleId(BaseUtil.getSmallEditRole());
                    });
                    Db.batchSave(workUserList, 1000);
                    userList.add(userId);
                    work.setOwnerUserId(TagUtil.fromLongSet(userList));
                }
            }
            if (work.getStatus() != null && work.getStatus() == 3) {
                work.setArchiveTime(new Date());
                actionRecordUtil.addObjectActionRecord(CrmEnum.WORK, workId, BehaviorEnum.ARCHIVE);
            }
            if (work.getStatus() != null && oldWork.getStatus() == 3 && work.getStatus() == 1) {
                actionRecordUtil.addObjectActionRecord(CrmEnum.WORK, workId, BehaviorEnum.RESTORE);
            }
            actionRecordUtil.updateRecord(new Work().findById(workId), work, CrmEnum.WORK);
            bol = work.update();
        }
        return bol ? queryOwnerRoleList(work.getWorkId()).put("work", work) : R.error();
    }

    public R deleteWork(Integer workId) {
        Db.delete("delete from `72crm_task_relation` where task_id in (select task_id from `72crm_task` where work_id = ?)", workId);
        Db.delete("delete from `72crm_task` where work_id = ?", workId);
        Db.delete("delete from `72crm_work_task_class` where work_id = ?", workId);
        Db.delete("delete from `72crm_work_user` where work_id = ?", workId);
        actionRecordUtil.addObjectActionRecord(CrmEnum.WORK, workId, BehaviorEnum.DELETE);
        int update = Db.delete("delete from `72crm_work` where work_id = ?", workId);
        return update > 0 ? R.ok() : R.error();
    }

    public R queryWorkNameList() {
        List<Record> recordList;
        Long userId = BaseUtil.getUserId();
        List<Integer> collectWorkIdList = Db.template("work.queryWorkCollectWorkId", Kv.by("userId", userId)).query();
        List<Record> workOrder;
        if (AuthUtil.isWorkAdmin()) {
            workOrder = Db.template("work.queryWorkOrderList", Kv.by("userId", userId)).find();
            recordList = Db.find(Db.getSqlPara("work.queryWorkNameList"));
        } else {
            workOrder = Db.template("work.queryWorkOrderList", Kv.by("userId", userId)).find();
            recordList = Db.find(Db.getSqlPara("work.queryWorkNameList", Kv.by("userId", userId)));
        }
        Integer workMenuId = Db.queryInt("select menu_id from `72crm_admin_menu` where parent_id = 0 and realm = 'work'");
        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(workMenuId);
        recordList.forEach(record -> {
            workOrder.forEach(order -> {
                if (order.getInt("work_id").equals(record.getInt("work_id"))) {
                    record.set("orderNum", order.getInt("order_num"));
                }
            });
            if (collectWorkIdList.contains(record.getInt("work_id"))) {
                record.set("collect", 1);
            }
            //拼接当前用户在每个项目的权限
            Integer workId = record.getInt("work_id");
            AdminUser user = BaseUtil.getUser();
            Integer roleId = Db.queryInt("select role_id from `72crm_work_user` where work_id = ? and user_id = ?", workId, userId);
            List<AdminMenu> menuRecords = AdminMenu.dao.find(Db.getSql("work.queryMenuByRoleId"), roleId);
            JSONObject object = new JSONObject();
            adminMenus.forEach(menu -> {
                JSONObject authObject = new JSONObject();
                List<AdminMenu> childMenus = adminMenuService.queryMenuList(menu.getMenuId());
                if ((roleId != null && roleId.equals(BaseUtil.getSmallAdminRole())) || userId.equals(BaseUtil.getSuperUser()) || user.getRoles().contains(BaseUtil.getWorkAdminRole()) || user.getRoles().contains(BaseUtil.getSuperRole())) {
                    childMenus.forEach(child -> {
                        authObject.put(child.getRealm(), true);
                    });
                } else {
                    if (record.getInt("is_open") == 1) {
                        if (record.getInt("owner_role").equals(BaseUtil.getSmallAdminRole())) {
                            childMenus.forEach(child -> {
                                authObject.put(child.getRealm(), true);
                            });
                        } else {
                            List<String> realmList = Db.query(Db.getSql("work.queryWorkAuthByUserId"), record.getInt("owner_role"), menu.getMenuId());
                            realmList.forEach(realm -> {
                                authObject.put(realm, true);
                            });
                        }
                    } else {
                        menuRecords.forEach(r -> {
                            if (menu.getMenuId().equals(r.getParentId())) {
                                authObject.put(r.getRealm(), true);
                            }
                        });
                    }
                }
                if (!authObject.isEmpty()) {
                    object.put(menu.getRealm(), authObject);
                }
            });
            record.set("authList", object);
        });
        List<Record> orderNum = recordList.stream().sorted(Comparator.comparingInt(record -> record.getInt("orderNum"))).collect(Collectors.toList());
        return R.ok().put("data", orderNum);
    }

    public R queryTaskByWorkId(JSONObject jsonObject) {
        Integer workId = jsonObject.getInteger("workId");
        List<Record> classList = Db.find("select class_id as classId, name as className from `72crm_work_task_class` where work_id = ? order by order_num", workId);
        LinkedList<Record> linkedList = new LinkedList<>(classList);
        Record item = new Record();
        item.set("className", "(未分组)");
        item.set("classId", -1);
        linkedList.addFirst(item);
        List<Record> finalClassList = new CopyOnWriteArrayList<>(linkedList);
        finalClassList.forEach(workClass -> {
            List<Record> recordList = Db.find(Db.getSqlPara("work.queryTaskByWorkId", Kv.by("workId", workId).set("stopTimeType", jsonObject.getInteger("stopTimeType")).set("userIds", jsonObject.getJSONArray("mainUserId")).set("labelIds", jsonObject.getJSONArray("labelId")).set("classId", workClass.getInt("classId")).set("taskName", jsonObject.getString("taskName"))));
            workClass.set("count", recordList.size());
            if (recordList.size() == 0) {
                if (workClass.getInt("classId") != -1) {
                    workClass.set("list", new ArrayList<>());
                } else {
                    finalClassList.remove(workClass);
                }
            } else {
                workbenchService.taskListTransfer(recordList);
                //先保证已完成的在最后，再按照order_num排序
                recordList.sort((r1, r2) -> {
                    if (r1.getInt("status") == 5) {
                        return 1;
                    } else if (r2.getInt("status") == 5) {
                        return -1;
                    } else {
                        return r1.getInt("order_num").compareTo(r2.getInt("order_num"));
                    }
                });
                workClass.set("list", recordList);
            }
        });
        return R.ok().put("data", finalClassList);
    }

    public R queryOwnerTaskListByWorkId(JSONObject jsonObject) {
        Integer workId = jsonObject.getInteger("workId");
        List<Record> taskList = Db.find(Db.getSqlPara("work.queryOwnerTaskListByWorkId", Kv.by("workId", workId).set("stopTimeType", jsonObject.getInteger("stopTimeType")).set("userIds", jsonObject.getJSONArray("mainUserId")).set("labelIds", jsonObject.getJSONArray("labelId")).set("taskName", jsonObject.getString("taskName"))));
        if (taskList.size() == 0) {
            return R.ok().put("data", new ArrayList<>());
        } else {
            workbenchService.taskListTransfer(taskList);
            Map<Long, List<Record>> map = taskList.stream().collect(Collectors.groupingBy(record -> record.getLong("main_user_id")));
            List<Long> userIdList = new ArrayList<>(map.keySet());
            List<Record> userList = Db.find(Db.getSqlPara("work.queryOwnerUser", Kv.by("userIds", userIdList)));
            userList.forEach(user -> {
                List<Record> recordList = map.get(user.getLong("user_id"));
                recordList.sort((r1, r2) -> {
                    if (r1.getInt("status") == 5) {
                        return 1;
                    } else if (r2.getInt("status") == 5) {
                        return -1;
                    } else {
                        return -r1.getDate("update_time").compareTo(r2.getDate("update_time"));
                    }
                });
                user.set("list", recordList).set("count", recordList.size());
            });
            return R.ok().put("data", userList);
        }
    }

    public R queryTaskFileByWorkId(BasePageRequest<JSONObject> data) {
        Page<Record> workFile = Db.paginate(data.getPage(), data.getLimit(), Db.getSqlPara("work.queryTaskFileByWorkId", Kv.by("workId", data.getData().getInteger("workId"))));
        return R.ok().put("data", workFile);
    }

    public R queryArchiveWorkList(BasePageRequest request) {
        Page<Record> recordPage = Db.paginate(request.getPage(), request.getLimit(), "select work_id,archive_time,name,color ", "from 72crm_work where status = 3");
        return R.ok().put("data", recordPage);
    }

    public R workStatistics(String workId) {
        Long userId1 = BaseUtil.getUserId();
        if ("all".equals(workId)) {
            Record taskStatistics = new Record();
            List<Record> memberTaskStatistics = new ArrayList<>();
            if (AuthUtil.isWorkAdmin()) {
                taskStatistics = Db.findFirst(Db.getSqlPara("work.workStatistics"));
                memberTaskStatistics = Db.find("select user_id,img,realname from `72crm_admin_user` where user_id in (select main_user_id from `72crm_task` where ishidden = 0 and work_id > 0)");
                memberTaskStatistics.forEach(record -> {
                    Record first = Db.findFirst(Db.getSqlPara("work.workStatistics", Kv.by("mainUserId", record.getLong("user_id"))));
                    record.setColumns(first);
                });
            } else {
                List<Record> recordList = Db.find(Db.getSqlPara("work.queryOwnerWorkIdList", Kv.by("userId", userId1)));
                List<Integer> workIds = recordList.stream().map(record -> record.getInt("work_id")).collect(Collectors.toList());
                if (workIds.size() == 0) {
                    taskStatistics.set("unfinished", 0).set("overdue", 0).set("complete", 0).set("archive", 0).set("completionRate", 0).set("overdueRate", 0);
                } else {
                    taskStatistics = Db.findFirst(Db.getSqlPara("work.workStatistics", Kv.by("workIds", CollectionUtil.join(workIds, ","))));
                    memberTaskStatistics = Db.find(Db.getSql("work.getTaskOwnerOnWork"), CollectionUtil.join(workIds, ","));
                    memberTaskStatistics.forEach(record -> {
                        Record first = Db.findFirst(Db.getSqlPara("work.workStatistics", Kv.by("mainUserId", record.getLong("user_id")).set("workIds", CollectionUtil.join(workIds, ","))));
                        record.setColumns(first);
                    });
                }
            }

            return R.ok().put("data", Kv.by("taskStatistics", taskStatistics).set("memberTaskStatistics", memberTaskStatistics));
        }
        Record taskStatistics = Db.findFirst(Db.getSqlPara("work.workStatistics", Kv.by("workId", workId)));
        String ownerUserId = Db.queryStr("select owner_user_id from `72crm_work` where work_id = ?", workId);
        List<Record> ownerList = new ArrayList<>();
        for (Long userId : TagUtil.toLongSet(ownerUserId)) {
            Record ownerUser = Db.findFirst("select b.user_id,realname,img from `72crm_work_user` a left join `72crm_admin_user` b on a.user_id = b.user_id where a.work_id = ? and a.user_id = ? and a.role_id = ?", workId, userId, BaseUtil.getSmallAdminRole());
            if (ownerUser != null) {
                ownerList.add(ownerUser);
            }
        }
        List<Record> userList = new ArrayList<>();
        for (Long userId : TagUtil.toLongSet(ownerUserId)) {
            userList.add(Db.findFirst("select user_id,realname,username,img from `72crm_admin_user` where user_id = ?", userId));
        }
        List<Record> classStatistics = new ArrayList<>();
        List<Record> labelStatistics = new ArrayList<>();
        List<Record> recordList = Db.find("select class_id classId,name className from 72crm_work_task_class a  where a.work_id = ?", workId);
        Map<String, Object> classMap = new HashMap<>();
        recordList.forEach(record -> classMap.put(record.getStr("classId"), record.getStr("className")));
        classMap.forEach((classId, name) -> {
            Record first = Db.findFirst("select count(status = 5 or null) as complete,count(status != 5 or null) as undone from 72crm_task where class_id = ? and work_id = ? and ishidden = 0 and (is_archive = 0 or (is_archive = 1 and status = 5))", classId, workId);
            first.set("className", classMap.get(classId));
            classStatistics.add(first);
        });
        List<Record> labelList = Db.find("select label_id,status from `72crm_task` where work_id  = ? and label_id is not null and ishidden = 0 and (is_archive = 0 or (is_archive = 1 and status = 5))", workId);
        List<String> labelIdList = labelList.stream().map(record -> record.getStr("label_id")).collect(Collectors.toList());
        Set<Integer> labelIdSet = new HashSet<>(toList(labelIdList));
        Map<Integer, Record> labelMap = new HashMap<>();
        labelIdSet.forEach(id -> {
            Record record = Db.findFirst("select label_id,name,color from 72crm_work_task_label where label_id = ?", id);
            labelMap.put(record.getInt("label_id"), record);
        });
        labelMap.forEach((id, record) -> {
            AtomicReference<Integer> complete = new AtomicReference<>(0);
            AtomicReference<Integer> undone = new AtomicReference<>(0);
            labelList.forEach(label -> {
                if (label.getStr("label_id").contains(id.toString())) {
                    if (label.getInt("status") == 1) {
                        undone.getAndSet(undone.get() + 1);
                    } else if (label.getInt("status") == 5) {
                        complete.getAndSet(complete.get() + 1);
                    }
                }
            });
            record.set("complete", complete.get());
            record.set("undone", undone.get());
            labelStatistics.add(record);
        });
        List<Record> memberTaskStatistics = memberTaskStatistics(workId);
        Kv result = Kv.by("taskStatistics", taskStatistics).set("classStatistics", classStatistics).set("labelStatistics", labelStatistics).set("memberTaskStatistics", memberTaskStatistics).set("userList", userList).set("ownerList", ownerList);
        return R.ok().put("data", result);
    }

    public List<Integer> toList(List<String> labelList) {
        List<Integer> list = new ArrayList<>();
        if (labelList == null || labelList.size() == 0) {
            return list;
        }
        labelList.forEach(ids -> {
            if (StrUtil.isNotEmpty(ids)) {
                for (String id : ids.split(",")) {
                    if (StrUtil.isNotEmpty(id)) {
                        list.add(Integer.valueOf(id));
                    }
                }
            }
        });
        return list;
    }


    /**
     * 项目成员任务统计
     */
    public List<Record> memberTaskStatistics(String workId) {
        List<Record> list = new ArrayList<>();
        Work work = new Work().findById(workId);
        if (work.getIsOpen() == 1) {
            list = Db.find(Db.getSql("work.getTaskOwnerOnWork"), workId);
            list.forEach(record -> {
                Record first = Db.findFirst(Db.getSqlPara("work.workStatistics", Kv.by("mainUserId", record.getLong("user_id")).set("workIds", workId)));
                record.setColumns(first);
            });
        } else {
            String ownerUserIds = Db.queryStr("select owner_user_id from 72crm_work where work_id = ?", workId);
            if (StrUtil.isEmpty(ownerUserIds)) {
                return list;
            }
            for (String userId : ownerUserIds.split(",")) {
                if (StrUtil.isEmpty(userId)) {
                    continue;
                }
                Record user = Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", userId);
                Record first = Db.findFirst(Db.getSqlPara("work.workStatistics", Kv.by("workId", workId).set("userId", userId)));
                user.setColumns(first);
                list.add(user);
            }
        }
        return list;
    }

    public R queryWorkOwnerList(String workId) {
        String ownerUserId = Db.queryStr("select owner_user_id from `72crm_work` where work_id = ?", workId);
        List<Record> userList = new ArrayList<>();
        for (Long userId : TagUtil.toLongSet(ownerUserId)) {
            userList.add(Db.findFirst("select user_id,realname,username,img from `72crm_admin_user` where user_id = ?", userId));
        }
        return R.ok().put("data", userList);
    }

    public R updateOrder(JSONObject jsonObject) {
        String updateSql = "update `72crm_task` set class_id = ?,order_num = ? where task_id = ?";
        if (jsonObject.containsKey("toList")) {
            JSONArray tolist = jsonObject.getJSONArray("toList");
            Integer toId = jsonObject.getInteger("toId");
            if (toId == null) {
                toId = -1;
            }
            for (int i = 1; i <= tolist.size(); i++) {
                Db.update(updateSql, toId, i, tolist.get(i - 1));
            }
        }
        if (jsonObject.containsKey("fromList")) {
            JSONArray fromList = jsonObject.getJSONArray("fromList");
            Integer fromId = jsonObject.getInteger("fromId");
            for (int i = 1; i <= fromList.size(); i++) {
                Db.update(updateSql, fromId, i, fromList.get(i - 1));
            }
        }
        return R.ok();

    }

    public R leave(Integer workId, Long userId) {
        Work work = new Work().findById(workId);
        if (work.getCreateUserId().equals(userId)) {
            return R.error("项目创建人不可以退出");
        }
        Db.update(Db.getSqlPara("work.leave", Kv.by("workId", workId).set("userId", userId)));
        Db.update(Db.getSqlPara("work.leave1", Kv.by("workId", workId).set("userId", userId)));
        Set<Long> ownerUserIds = TagUtil.toLongSet(work.getOwnerUserId());
        ownerUserIds.remove(userId);
        work.setOwnerUserId(TagUtil.fromLongSet(ownerUserIds));
        boolean update = work.update();
        Db.update("delete from `72crm_work_user` where work_id = ? and user_id = ?", workId, userId);
        actionRecordUtil.addObjectActionRecord(CrmEnum.WORK, workId, BehaviorEnum.EXIT);
        return update ? R.ok() : R.error();
    }

    public R getWorkById(String workId) {
        Work work = new Work().findById(workId);
        if (work == null) {
            return R.error("项目不存在！");
        }
        if (work.getIsSystemCover() != null && work.getIsSystemCover() == 0) {
            Record file = Db.findFirst("select file_path,file_id from 72crm_admin_file where batch_id = ?", work.getBatchId());
            work.put("file_id", file.getInt("file_id"));
            work.setCoverUrl(StrUtil.isNotEmpty(file.getStr("file_path")) ? file.getStr("file_path") : "");
        }
//        if (work.getIsSystemCover() != null && work.getIsSystemCover() == 0 && StrUtil.isNotEmpty(work.getBatchId())) {
//            work.put("coverUrl", Db.queryStr("select file_path from 72crm_admin_file where batch_id = ?", work.getBatchId()));
//        }
        work.put("ownerUser", Db.find("select user_id,realname,img from 72crm_admin_user where find_in_set(user_id,?)", work.getOwnerUserId()));
        int isUpdate = 0;
        if (AuthUtil.isWorkAdmin() || BaseUtil.getUser().getRoles().contains(BaseUtil.getSmallAdminRole())) {
            isUpdate = 1;
        }
        AdminUser user = BaseUtil.getUser();
        Long userId = BaseUtil.getUserId();
        Integer roleId = Db.queryInt("select role_id from `72crm_work_user` where work_id = ? and user_id = ?", workId, userId);

        JSONObject root = new JSONObject();

        List<AdminMenu> menuRecords = AdminMenu.dao.find(Db.getSql("work.queryMenuByRoleId"), roleId);
        Integer workMenuId = Db.queryInt("select menu_id from `72crm_admin_menu` where parent_id = 0 and realm = 'work'");

        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(workMenuId);
        JSONObject object = new JSONObject();
        adminMenus.forEach(menu -> {
            JSONObject authObject = new JSONObject();
            List<AdminMenu> childMenus = adminMenuService.queryMenuList(menu.getMenuId());
            if ((roleId != null && roleId.equals(BaseUtil.getSmallAdminRole())) || userId.equals(BaseUtil.getSuperUser()) || user.getRoles().contains(BaseUtil.getWorkAdminRole()) || user.getRoles().contains(BaseUtil.getSuperRole())) {
                childMenus.forEach(child -> {
                    authObject.put(child.getRealm(), true);
                });
            } else {
                if (work.getIsOpen() == 1) {
                    if (work.getOwnerRole().equals(BaseUtil.getSmallAdminRole())) {
                        childMenus.forEach(child -> {
                            authObject.put(child.getRealm(), true);
                        });
                    } else {
                        List<String> realmList = Db.query(Db.getSql("work.queryWorkAuthByUserId"), work.getOwnerRole(), menu.getMenuId());
                        realmList.forEach(realm -> {
                            authObject.put(realm, true);
                        });
                    }
                } else {
                    menuRecords.forEach(record -> {
                        if (menu.getMenuId().equals(record.getParentId())) {
                            authObject.put(record.getRealm(), true);
                        }
                    });
                }
            }
            if (!authObject.isEmpty()) {
                object.put(menu.getRealm(), authObject);
            }
        });
        work.put("permission", Kv.by("isUpdate", isUpdate)).put("authList", object);
        return R.ok().put("data", work);
    }

    /**
     * 查询项目角色类型列表
     *
     * @author wyq
     */
    public R queryRoleList() {
        return R.ok().put("data", Db.find(Db.getSql("work.queryRoleList")));
    }

    /**
     * @author wyq
     * 查询项目成员所属角色列表
     */
    public R queryOwnerRoleList(Integer workId) {
        Integer isOpen = Db.queryInt("select is_open from 72crm_work where work_id = ?", workId);
        if (workId == 0 || isOpen == 1) {
            return R.ok().put("data", Db.find("select user_id,realname from 72crm_admin_user"));
        }
        return R.ok().put("data", Db.find(Db.getSql("work.queryOwnerRoleList"), workId));
    }

    /**
     * @author wyq
     * 保存项目成员角色设置
     */
    @Before(Tx.class)
    public R setOwnerRole(JSONObject jsonObject) {
        List<WorkUser> workUserList = jsonObject.getJSONArray("list").toJavaList(WorkUser.class);
        Integer workId = jsonObject.getInteger("workId");
        workUserList.forEach(workUser -> {
            workUser.setWorkId(workId);
        });
        Db.delete("delete from 72crm_work_user where work_id = ?", workId);
        Db.batchSave(workUserList, 100);
        List<Record> recordList = Db.find(Db.getSql("work.queryOwnerRoleList"), workId);
        return R.ok().put("data", recordList);
    }

    @Before(Tx.class)
    public R deleteTaskList(Integer workId, Integer classId) {
        Db.update("update  `72crm_task` set ishidden = 1,class_id = null,hidden_time = now() where class_id = ? and work_id = ? and is_archive != 1", classId, workId);
        WorkTaskClass taskClass = new WorkTaskClass();
        taskClass.setClassId(classId);
        taskClass.setWorkId(workId);
        actionRecordUtil.addTaskClassRecord(taskClass, BehaviorEnum.DELETE);
        boolean delete = taskClass.deleteById(classId);
        return delete ? R.ok() : R.error();
    }

    public R archiveTask(String classId) {
        Integer count = Db.queryInt("select count(*) from `72crm_task` where class_id = ? and status = 5 and ishidden = 0", classId);
        if (count == 0) {
            return R.error("暂无已完成任务，归档失败!");
        }
        int update = Db.update("update  `72crm_task` set is_archive = 1,archive_time = now() where class_id = ? and status = 5 and ishidden = 0", classId);
        return update > 0 ? R.ok() : R.error();
    }

    public R archiveTask(Long userId, Integer workId) {
        Integer count = Db.queryInt("select count(*) from `72crm_task` where main_user_id = ? and work_id = ? and status = 5 and ishidden = 0", userId, workId);
        if (count == 0) {
            return R.error("暂无已完成任务，归档失败!");
        }
        int update = Db.update("update  `72crm_task` set is_archive = 1,archive_time = now() where main_user_id = ? and work_id = ? and status = 5 and ishidden = 0", userId, workId);
        return update > 0 ? R.ok() : R.error();
    }

    public R archList(String workId) {
        List<Record> recordList = Db.find(Db.getSql("work.archList"), workId);
        workbenchService.taskListTransfer(recordList);
        return R.ok().put("data", recordList);
    }

    public R remove(Long userId, Integer workId) {
        Integer roleId = Db.queryInt("select role_id from `72crm_work_user` where work_id = ? and user_id = ?", workId, userId);
        if (roleId.equals(BaseUtil.getSmallAdminRole())) {
            return R.error("管理员不能被删除！");
        }
        Work work = new Work().findById(workId);
        Set<Long> userIds = TagUtil.toLongSet(work.getOwnerUserId());
        userIds.remove(userId);
        work.setOwnerUserId(TagUtil.fromLongSet(userIds));
        boolean update = work.update();
        Db.delete("delete from `72crm_work_user` where work_id = ? and user_id = ?", workId, userId);
        return update ? R.ok() : R.error();
    }

    public R updateClassOrder(JSONObject jsonObject) {
        String sql = "update `72crm_work_task_class` set order_num = ? where work_id = ? and class_id = ?";
        Integer workId = jsonObject.getInteger("workId");
        JSONArray classIds = jsonObject.getJSONArray("classIds");
        for (int i = 0; i < classIds.size(); i++) {
            Db.update(sql, i, workId, classIds.get(i));
        }
        return R.ok();
    }

    public R activation(Integer taskId) {
        Task task = new Task().findById(taskId);
        Integer count = Db.queryInt("select count(*) from `72crm_work_task_class` where class_id = ?", task.getClassId());
        int update;
        if (count > 0) {
            update = Db.update("update  `72crm_task` set is_archive = 0,archive_time = null where task_id = ?", taskId);
        } else {
            update = Db.update("update  `72crm_task` set is_archive = 0,archive_time = null,class_id = null where task_id = ?", taskId);
        }
        actionRecordUtil.addObjectActionRecord(CrmEnum.TASK, taskId, BehaviorEnum.ACTIVE);
        return update > 0 ? R.ok() : R.error();
    }


    /**
     * @author wyq
     * 查询有项目模块查看权限的员工
     */
    public R queryProjectUser() {
        List<Record> recordList = Db.find(Db.getSql("work.queryProjectUser"), 152);
        return R.ok().put("data", recordList);
    }

    public R collect(Integer workId) {
        boolean bol;
        Long userId = BaseUtil.getUserId();
        Integer count = Db.queryInt("select count(1) from `72crm_work_collect` where user_id = ? and work_id = ?", userId, workId);
        if (count == 0) {
            WorkCollect workCollect = new WorkCollect();
            workCollect.setWorkId(workId);
            workCollect.setUserId(userId);
            bol = workCollect.save();
        } else {
            bol = Db.delete("delete from `72crm_work_collect` where user_id = ? and work_id = ?", userId, workId) > 0;
        }
        return bol ? R.ok() : R.error();
    }

    @Before(Tx.class)
    public void updateWorkOrder(List<Integer> workIds) {
        Long userId = BaseUtil.getUserId();
        Db.delete("delete from `72crm_work_order` where user_id = ?", userId);
        for (int i = 0; i < workIds.size(); i++) {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setWorkId(workIds.get(i));
            workOrder.setUserId(userId);
            workOrder.setOrderNum(i + 1);
            workOrder.save();
        }
    }

    public List<Map<String, Object>> workTaskExport(Integer workId) {
        Kv kv=Kv.by("workId", workId);
        List<Record> recordList = Db.find(Db.getSqlPara("work.workTaskExport", kv));
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

    @Before(Tx.class)
    public R excelImport(UploadFile file, Integer workId) {
        List<String> sqlList = new ArrayList<>();
        List<List<Object>> errList = new ArrayList<>();
        Map<String, Integer> classMap = new HashMap<>();
        List<Record> workUserList = Db.find(Db.getSql("work.queryWorkOwnerUser"), workId);
        Map<String, Long> workUserMap = workUserList.stream().collect(Collectors.toMap(record -> record.getStr("realname"), record -> record.getLong("user_id")));
        if (file.getFile() == null || !file.getFile().exists()) {
            return R.ok();
        }
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(file.getFile().getAbsolutePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            if (rowIndex > 1001) {
                rowList.add(0, "最多同时导入1000条数据");
                errList.add(rowList);
                return;
            }
            if (rowIndex > 1) {
                if (rowList.size() < 7) {
                    for (int i = rowList.size() - 1; i < 7; i++) {
                        rowList.add(null);
                    }
                }
                num.getAndSet(num.get() + 1);
                if (StrUtil.isEmptyIfStr(rowList.get(0))) {
                    rowList.add(0, "任务名称不能为空");
                    errList.add(rowList);
                    return;
                }
                if (!StrUtil.isEmptyIfStr(rowList.get(2))) {
                    Object object = rowList.get(2);
                    String time;
                    if(object instanceof Long){
                        time = DateUtil.formatDate(org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) object));
                    }else {
                        time = object.toString();
                    }
                    if (!BaseUtil.isTime(time)) {
                        rowList.add(0, "开始时间日期格式错误，例:2000-01-01");
                        errList.add(rowList);
                        return;
                    }
                }
                if (!StrUtil.isEmptyIfStr(rowList.get(3))) {
                    Object object = rowList.get(3);
                    String time;
                    if(object instanceof Long){
                        time = DateUtil.formatDate(org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) object));
                    }else {
                        time = object.toString();
                    }
                    if (!BaseUtil.isTime(time)) {
                        rowList.add(0, "结束时间日期格式错误，例:2000-01-01");
                        errList.add(rowList);
                        return;
                    }
                }
                if (StrUtil.isEmptyIfStr(rowList.get(6))) {
                    rowList.add(0, "所属任务列表不能为空");
                    errList.add(rowList);
                    return;
                }
                String taskName = rowList.get(0).toString().trim();
                String description = rowList.get(1).toString().trim();
                Date startTime = null;
                if (ObjectUtil.isNotEmpty(rowList.get(2))) {
                    if (rowList.get(2) instanceof Long) {
                        startTime = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) rowList.get(2));
                    } else {
                        startTime = DateUtil.parse(rowList.get(2).toString().trim());
                    }
                }
                Date stopTime = null;
                if (ObjectUtil.isNotEmpty(rowList.get(3))) {
                    if (rowList.get(3) instanceof Long) {
                        stopTime = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) rowList.get(3));
                    } else {
                        stopTime = DateUtil.parse(rowList.get(3).toString().trim());
                    }
                }
                String mainUserName = rowList.get(4).toString().trim();
                Long mainUserId = BaseUtil.getUserId();
                if (StrUtil.isNotEmpty(mainUserName)) {
                    if (ObjectUtil.isEmpty(workUserMap.get(mainUserName))) {
                        rowList.add(0, "负责人与当前项目成员不匹配");
                        errList.add(rowList);
                        return;
                    } else {
                        mainUserId = workUserMap.get(mainUserName);
                    }
                }
                String ownerUserName = rowList.get(5).toString().trim();
                if (StrUtil.isNotEmpty(ownerUserName)) {
                    List<String> ownerUserNameList = new ArrayList<>(Arrays.asList(ownerUserName.split(",")));
                    for (String userName : ownerUserNameList) {
                        if (ObjectUtil.isEmpty(workUserMap.get(userName))) {
                            rowList.add(0, "参与人与当前项目成员不匹配");
                            errList.add(rowList);
                            return;
                        }
                    }
                }
                String className = rowList.get(6).toString().trim();
                //任务列表 填写的列表名不存在则新增列表
                Integer classId = Db.queryInt("select class_id from 72crm_work_task_class where name = ? and work_id = ? limit 1", className, workId);
                List<String> classNameList = new ArrayList<>(classMap.keySet());
                if (!classNameList.contains(className) && classId == null) {
                    WorkTaskClass taskClass = new WorkTaskClass();
                    taskClass.setWorkId(workId);
                    taskClass.setName(className);
                    Integer orderNum = Db.queryInt("select max(order_num) from `72crm_work_task_class` where work_id = ?", workId);
                    taskClass.setOrderNum(orderNum + classNameList.size() + 1);
                    taskClass.setCreateUserId(BaseUtil.getUserId());
                    taskClass.setCreateTime(new Date());
                    taskClass.save();
                    classId = taskClass.getClassId();
                    classMap.put(className, classId);
                } else if (classNameList.contains(className)) {
                    classId = classMap.get(className);
                }
                Task task = new Task();
                task.setWorkId(workId);
                task.setName(taskName);
                task.setDescription(description);
                task.setStartTime(startTime);
                task.setStopTime(stopTime);
                task.setMainUserId(mainUserId);
                String ownerUserIds = Db.queryStr("select GROUP_CONCAT(distinct user_id) from 72crm_admin_user where find_in_set(realname,?)", ownerUserName);
                if (StrUtil.isNotEmpty(ownerUserIds)) {
                    ownerUserIds = "," + ownerUserIds + ",";
                } else {
                    ownerUserIds = ",";
                }
                task.setOwnerUserId(ownerUserIds);
                task.setClassId(classId);
                Aop.get(TaskService.class).setTask(task, new TaskRelation());
            }
        });
        Db.batch(sqlList, 1000);
        R result = R.ok().put("totalSize", num.get()).put("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(file.getUploadPath() + "/excel/" + token);

                writer.merge(7, "任务导入模板(*)为必填项");
                for (int i = 0; i < 8; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "任务名称(*)", "任务描述", "开始时间", "结束时间", "负责人", "参与人", "所属任务列表(*)"));
                writer.write(errList);
                result.put("errSize", errList.size()).put("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    public JSONObject auth(Long userId, Integer workId) {
        Work work = Work.dao.findById(workId);
        JSONObject jsonObject = new JSONObject();
        List<Integer> roleIds = Aop.get(AdminRoleService.class).queryRoleIdsByUserId(userId);
        Record workMenu = Db.findFirst("select * from 72crm_admin_menu where parent_id = 0 and realm = 'work'");
        Record project = Db.findFirst("select * from 72crm_admin_menu where parent_id = ? and realm = 'project'", workMenu.getInt("menu_id"));
        JSONObject object = new JSONObject();
        List<String> realmList = new ArrayList<>();
        if (roleIds.contains(BaseUtil.getSuperRole()) || BaseUtil.getSuperUser().equals(userId) || roleIds.contains(BaseUtil.getWorkAdminRole())) {
            realmList = Db.query("select realm from 72crm_admin_menu where parent_id = ?", project.getInt("menu_id"));
        } else if (work.getIsOpen() == 0) {
            Integer roleId = Db.queryInt("select role_id from 72crm_work_user where work_id = ? and user_id = ?", workId, userId);
            realmList = Db.query(Db.getSql("work.queryWorkAuthByUserId"), roleId, project.getInt("menu_id"));
        } else if (work.getIsOpen() == 1) {
            if (work.getOwnerRole().equals(BaseUtil.getSmallAdminRole())) {
                realmList = Db.query("select realm from 72crm_admin_menu where parent_id = ?", project.getInt("menu_id"));
            } else {
                realmList = Db.query(Db.getSql("work.queryWorkAuthByUserId"), work.getOwnerRole(), project.getInt("menu_id"));
            }
        }
        realmList.forEach(realm -> {
            object.fluentPut(realm, true);
        });
        jsonObject.fluentPut(workMenu.getStr("realm"), new JSONObject().fluentPut(project.getStr("realm"), object));
        return jsonObject;
    }
}
