package com.kakarote.crm9.erp.statistics.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wyq
 */
public class WorkParams {
    private String deptId;
    private String area;
    private String type;
    private String search;
    private String startTime;
    private String endTime;
    private Integer isDept;//0 区域 1 部门
    private Integer typeId;
    private Integer rate;
    private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getIsDept() {
        return isDept;
    }

    public void setIsDept(Integer isDept) {
        this.isDept = isDept;
    }

    public String getDeptId() {
        return deptId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
