package com.kakarote.crm9.erp.test.entity;

import com.kakarote.crm9.erp.test.entity.base.TestBaseAdminUser;

import java.util.List;

/**
 * @author zyy
 */
public class TestAdminUser extends TestBaseAdminUser<TestAdminUser> {
    public static final TestAdminUser testAdminUser = new TestAdminUser().dao();

    //查询开始时间
    private String startTime;
    //查询结束时间
    private String endTime;
    //用户角色列表
    private List<Integer> roles;

    private Integer label;

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

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public Integer getLabel() {
        return label;
    }

    public TestAdminUser setLabel(Integer label) {
        this.label = label;
        return this;
    }
}
