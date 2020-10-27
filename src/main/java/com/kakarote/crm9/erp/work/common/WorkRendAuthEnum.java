package com.kakarote.crm9.erp.work.common;

/**
 * @author zyy
 */
public enum WorkRendAuthEnum {
    WORK(431),
    DEMAND(448),
    TASK(472),
    SUBTASKS(494),
    HOURS(499),
    WORKCONFIG(564),
    ;

    private Integer menuId;

    WorkRendAuthEnum(Integer menuId) {
        this.menuId = menuId;
    }


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
