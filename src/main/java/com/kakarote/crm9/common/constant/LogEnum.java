package com.kakarote.crm9.common.constant;

public enum LogEnum {
    /**
     * 系统日志枚举
     */
    INSTRUMENT(1,"仪表盘"),
    BACKLOG(2,"待办事项"),
    LEADS(3,"线索"),
    CUSTOMER(4,"客户"),
    CONTACTS(5,"联系人"),
    CUSTOMER_POOL(6,"客户公海"),
    BUSINESS(7,"商机"),
    CONTRACT(8,"合同"),
    RECEIVABLES(9,"回款"),
    PRODUCT(10,"产品"),
    MY_TASK(11,"我的任务"),
    SUB_TASK(12,"下属的任务"),
    MY_SEND_EXAMINE(13,"我发出的审批"),
    MY_OA_EXAMINE(14,"待我审批（办公）"),
    MY_CONTRACT_EXAMINE(15,"待我审批（合同）"),
    MY_RECEIVABLES_EXAMINE(16,"待我审批（回款）"),
    ALL_LOG(17,"全部日志"),
    MY_SEND_LOG(18,"我发出的日志"),
    MY_RECEIVE_LOG(19,"我收到的日志"),
    FOLLOW_UP_RECORD(20,"跟进记录"),
    ALL_ADDRESS_BOOK(21,"全部通讯录"),
    MY_FOLLOW_ADDRESS_BOOK(22,"我关注的通讯录"),
    PROJECT_WORKBENCH(23,"项目管理-工作台"),
    PROJECT(24,"项目"),
    PROJECT_STATISTICS(25,"统计分析"),
    PROJECT_ARCHIVE(26,"归档任务"),
    PROJECT_LABEL(27,"标签"),
    PROJECT_RECYCLE(28,"回收站"),
    CUSTOMER_TOTAL(29,"客户总量分析"),
    CUSTOMER_RECORD(30,"客户跟进次数分析"),
    CUSTOMER_RECORD_MODE(31,"客户跟进方式分析"),
    CUSTOMER_CONVERSION(32,"客户转化率分析"),
    CUSTOMER_POOL_TOTAL(33,"公海客户分析"),
    CUSTOMER_CYCLE(34,"成交周期分析"),
    BUSINESS_FUNNEL(35,"销售漏斗"),
    BUSINESS_ADD_BUSINESS(36,"新增商机分析"),
    BUSINESS_CONVERSION(37,"商机转化率分析"),
    CONTRACT_COUNT(38,"合同数量分析"),
    CONTRACT_MONEY(39,"合同金额分析"),
    CONTRACT_BACK_MONEY(40,"回款金额分析"),
    CONTRACT_SUMMARY(41,"合同汇总表"),
    PORTRAIT_ADDRESS(42,"城市分布分析"),
    PORTRAIT_CUSTOMER_INDUSTRY(43,"客户行业分析"),
    PORTRAIT_CUSTOMER_LEVEL(44,"客户级别分析"),
    PORTRAIT_CUSTOMER_SOURCE(45,"客户来源分析"),
    PRODUCT_SALES(46,"产品销售情况统计"),
    PRODUCT_CATEGORY(47,"产品分类销量分析"),
    RANKING_CONTRACT(48,"合同金额排行"),
    RANKING_RECEIVABLES(49,"回款金额排行"),
    RANKING_SIGNING(50,"签约合同排行"),
    RANKING_PRODUCT(51,"产品销量排行"),
    RANKING_ADD_CUSTOMER(52,"新增客户数排行"),
    RANKING_ADD_CONTACTS(53,"新增联系人数排行"),
    RANKING_RECORD_NUN(54,"跟进次数排行"),
    RANKING_RECORD_CUSTOMER(55,"跟进客户数排行"),
    RANKING_EXAMINE(56,"出差次数排行"),
    LOG_ANALYSIS(57,"日志分析"),
    EXAMINE_ANALYSIS(58,"审批分析"),
    TARGET_COMPLETE(59,"业绩目标完成情况");





    LogEnum(int type,String name) {
        this.name = name;
        this.type = type;
    }

    private String name;
    private int type;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
