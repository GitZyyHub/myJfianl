package com.kakarote.crm9.erp.email.common;

public enum EmailEnum {
    /**
     * CallEnum
     */
    SMTP_SSL_NOT_ENABLED("未启用", 0),
    SMTP_SSL_ENABLED("已启用", 1),

    //是否读取
    HAVE_READ("已读取", 1),
    DID_NOT_READ("未读取", 0),

    RECEIPT_EMAIL("收件", 1, "INBOX"),
    RECORD_SEND("发送", 2, "Sent Messages"),
    RECORD_DRAFT("草稿", 3, "Drafts"),
    RECORD_DELETE("删除", 4, "Deleted Messages"),
    RECORD_GARBAGE("垃圾", 5, "垃圾邮件"),
    ;

    private String name;
    private Integer types;
    private String key;

    EmailEnum(String name, Integer types, String key) {
        this.name = name;
        this.types = types;
        this.key = key;
    }

    EmailEnum(String name, Integer types) {
        this(name, types, (String) null);
    }

    public static EmailEnum valueOf(Integer types) {
        for (EmailEnum anEnum : EmailEnum.values()) {
            if (anEnum.getKey() != null && anEnum.getTypes().equals(types)) {
                return anEnum;
            }
        }
        return RECEIPT_EMAIL;
    }

    public static EmailEnum valueOf(String name, boolean isKey) {
        if (isKey) {
            for (EmailEnum anEnum : EmailEnum.values()) {
                if (anEnum.getKey() != null && anEnum.getKey().equals(name)) {
                    return anEnum;
                }
            }
            return RECEIPT_EMAIL;
        } else {
            return valueOf(name);
        }
    }

    public static String getName(Integer types) {
        for (EmailEnum c : EmailEnum.values()) {
            if (c.getTypes().equals(types)) {
                return c.name;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
