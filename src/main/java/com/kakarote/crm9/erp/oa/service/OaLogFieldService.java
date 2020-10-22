package com.kakarote.crm9.erp.oa.service;

import cn.hutool.core.collection.CollectionUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.oa.entity.OaLogField;
import com.kakarote.crm9.erp.oa.entity.OaLogFieldType;
import com.kakarote.crm9.utils.R;

import java.util.*;

import static com.jfinal.plugin.activerecord.Db.delete;

/**
 * @author zyy
 */
public class OaLogFieldService {


    /**
     * 更新日志字段
     *
     * @param
     */
    public R updateLogField(List<OaLogFieldType> oaLogFieldTypes) {

        List<String> errorList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        Db.tx(() -> {

            oaLogFieldTypes.forEach(oaLogFieldType -> objects.add(oaLogFieldType.getFieldTypeName()));
            HashSet<Object> set = new HashSet<>(objects);
            if (set.size() != objects.size()) {
                errorList.add("日志类型重复，请重新编写");
                return false;
            }

            HashSet<Object> setType = new HashSet<>();
            oaLogFieldTypes.forEach(oaLogFieldType -> setType.add(oaLogFieldType.getFieldTypeId()));
            List<Record> records = Db.find(Db.getSql("oa.logFieldType.findType"));
            records.forEach(record -> {
                if (!setType.contains(Integer.parseInt(record.getStr("field_type_id")))) {
                    delete("72crm_oa_log_field_type", "field_type_id", record);
                    delete("delete from 72crm_oa_log_field where log_field_type_id =?", record.getStr("field_type_id"));
                }
            });
            oaLogFieldTypes.forEach(oaLogFieldType -> {
                List<OaLogField> oaLogFields = oaLogFieldType.getOaLogFields();
                List<Record> findFieldId = Db.find(Db.getSql("oa.logField.findIdByTypeId"), oaLogFieldType.getFieldTypeId());

                List<Integer> ids = new ArrayList<>();
                for (Record record : findFieldId) {
                    String log_field_id = record.getStr("log_field_id");
                    try {
                        ids.add(Integer.parseInt(log_field_id));
                    } catch (NumberFormatException e) {
                    }
                }

                if (!CollectionUtil.isEmpty(ids)) {
                    StringBuffer stringBuffer = new StringBuffer("DELETE FROM 72crm_oa_log_field WHERE log_field_id IN (");
                    for (int i = 0; i < oaLogFields.size(); i++) {
                        for (int y = 0; y < ids.size(); y++) {
                            try {
                                if (oaLogFields.get(i).getLogFieldId().equals(ids.get(y)))
                                    ids.remove(y);
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                    for (int i = 0; i < ids.size(); i++) {
                        stringBuffer.append(ids.get(i)+",");
                    }
                    try {
                        String s = stringBuffer.toString().substring(0,stringBuffer.length()-1)+")";
                        System.out.println(s);
                        Db.delete(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });

            for (OaLogFieldType oaLogFieldType : oaLogFieldTypes) {
                if (oaLogFieldType.getFieldTypeId() == null) {
                    OaLogFieldType saveOaLogFieldType = new OaLogFieldType();

                    List<Record> sorts = Db.find(Db.getSql("oa.logFieldType.findSort"));
                    List<Integer> sortList = new ArrayList<>();
                    sorts.forEach(sort -> {
                        sortList.add(Integer.parseInt(sort.getStr("sort")));
                    });

                    boolean contains = sortList.contains(oaLogFieldType.getSort());
                    if (contains) {
                        Integer max = Collections.max(sortList);
                        saveOaLogFieldType.setSort(max + 1);
                    } else {
                        saveOaLogFieldType.setSort(oaLogFieldType.getSort());
                    }

                    saveOaLogFieldType.setFieldTypeName(oaLogFieldType.getFieldTypeName()).setUpdateTime(new Date());
                    if (!saveOaLogFieldType.save()) {
                        errorList.add("类型添加失败");
                        return false;
                    }
                    oaLogFieldType.setFieldTypeId(saveOaLogFieldType.getFieldTypeId());
                }
                List<OaLogField> oaLogFields = oaLogFieldType.getOaLogFields();
                for (OaLogField oaLogField : oaLogFields) {
                    if (oaLogField.getLogFieldId() != null) {
                        oaLogField.update();
                    } else {
                        Integer fieldtypeId = oaLogFieldType.getFieldTypeId();
                        List<Record> sorts = Db.find("select sort from 72crm_oa_log_field where log_field_type_id=?", fieldtypeId/*Db.getSql("oa.logField.findSort"),fieldtypeId*/);
                        List<Integer> sortList = new ArrayList<>();
                        sorts.forEach(sort -> {
                            sortList.add(Integer.parseInt(sort.getStr("sort")));
                        });

                        boolean contains = sortList.contains(oaLogField.getSort());
                        if (contains) {
                            Integer max = Collections.max(sortList);
                            oaLogField.setSort(max + 1);
                        }

                        if (!oaLogField.setLogFieldTypeId(oaLogFieldType.getFieldTypeId()).save()) {
                            errorList.add("添加失败");
                            return false;
                        }

                    }

                }

            }


            return true;
        });


        /*Db.tx(() -> {
            //查看是否有重复的日志类型
            oaLogFields.forEach(oaLogField -> objects.add(oaLogField.getFieldtypeName()));
            HashSet<Object> set = new HashSet<>(objects);
            if (set.size() != objects.size()) {
                errorList.add("日志类型重复，请重新编写");
                return false;
            }

            //删除数据库中不存在的
            List<Record> records = Db.find(Db.getSql("oa.logFieldType.findType"));
            records.forEach(record -> {
                if (!set.contains(record.getStr("field_type_name"))) {
                    Db.delete("72crm_oa_log_field_type", "field_type_id", record);
                    Db.delete(Db.getSql("oa.logField.delByTypeId"), record.getStr("field_type_id"));
                }
            });
            Db.template("oa.logField.delById", oaLogFields);

            //更新
            for (OaLogField oaLogField : oaLogFields) {

                if (oaLogField.getFieldtypeId() == null) {

                    OaLogFieldType oaLogFieldType = new OaLogFieldType();
                    oaLogFieldType.setFieldtypeName(oaLogField.getFieldtypeName());
                    oaLogFieldType.setUpdateTime(new Date());

                    if (!oaLogFieldType.save() || !oaLogField.setLogfieldTypeId(oaLogFieldType.getFieldtypeId()).save()) {
                        errorList.add("添加失败");
                        return false;
                    }

                } else {

                    if (oaLogField.getLogFieldId() != null) {
                        if (!oaLogField.remove("fieldtypeName").update()) {
                            errorList.add("添加失败");
                            return false;
                        }
                    } else {

                        List<Record> sorts = Db.find(Db.getSql("oa.logField.findSort"));
                        List<Integer> sortList = new ArrayList<>();
                        sorts.forEach(sort -> {
                            sortList.add(Integer.parseInt(sort.getStr("sort")));
                        });

                        boolean contains = sortList.contains(oaLogField.getSort());
                        if (!contains) {
                            Integer max = Collections.max(sortList);
                            oaLogField.setSort(max + 1);
                        }

                        if (!oaLogField.remove("fieldtypeName").setLogfieldTypeId(oaLogField.getFieldtypeId()).save()) {
                            errorList.add("添加失败");
                            return false;
                        }
                    }

                }
            }
            return true;
        });*/

        if (CollectionUtil.isNotEmpty(errorList)) {
            return R.error(errorList.get(0));
        }

        return R.ok();
    }


}
