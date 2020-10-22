package com.kakarote.crm9.common.enums;

/**
 * @author zyy
 */
public enum  FieldTypeEnum {

    WORK(1,"项目类型")
    ;

    private Integer typeId;
    private String typeName;

    FieldTypeEnum(Integer typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public static FieldTypeEnum parse(int type) {
        for (FieldTypeEnum Enum : FieldTypeEnum.values()) {
            if (Enum.getTypeId() == type) {
                return Enum;
            }
        }
        return null;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
