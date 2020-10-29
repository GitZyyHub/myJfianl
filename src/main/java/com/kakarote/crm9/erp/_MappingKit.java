package com.kakarote.crm9.erp;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.card.entity.*;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.erp.email.entity.EmailFile;
import com.kakarote.crm9.erp.email.entity.EmailLately;
import com.kakarote.crm9.erp.email.entity.EmailRecord;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.erp.test.entity.TestAdminUser;
import com.kakarote.crm9.erp.work.entity.*;


public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {


		arp.addMapping("72crm_admin_field", "field_id", AdminField.class);
		arp.addMapping("72crm_admin_file", "file_id", AdminFile.class);
		arp.addMapping("72crm_admin_user", "user_id", TestAdminUser.class);


		arp.addMapping("72crm_milestone", "milestone_id", Milestone.class);
		arp.addMapping("72crm_task", "task_id", Task.class);
		arp.addMapping("72crm_task_data", "id", TaskData.class);



		arp.addMapping("72crm_task_hours_lock", "id", TaskHoursLock.class);
		arp.addMapping("72crm_task_hours", "hours_id", TaskHours.class);
		arp.addMapping("72crm_task_hours_record", "id", TaskHoursRecord.class);
		arp.addMapping("72crm_task_file", "file_id", TaskFile.class);


		arp.addMapping("72crm_work", "work_id", Work.class);

		arp.addMapping("72crm_work_change", "change_id", WorkChange.class);
		arp.addMapping("72crm_work_change_user", "id", WorkChangeUser.class);
		arp.addMapping("72crm_work_data", "id", WorkData.class);
		arp.addMapping("72crm_work_demand", "demand_id", WorkDemand.class);
		arp.addMapping("72crm_work_order", "order_id", WorkOrder.class);
		arp.addMapping("72crm_work_resources_setting", "resources_id", WorkResourcesSetting.class);
		arp.addMapping("72crm_work_risk", "risk_id", WorkRisk.class);

		arp.addMapping("72crm_work_type", "work_type_id", WorkType.class);
		arp.addMapping("72crm_work_user", "id", WorkUser.class);

		arp.addMapping("72crm_work_demand_function", "id", WorkDemandFunction.class);
		arp.addMapping("72crm_work_demand_product", "id", WorkDemandProduct.class);
		arp.addMapping("72crm_work_product", "product_id", WorkProduct.class);
		arp.addMapping("72crm_work_product_function", "function_id", WorkProductFunction.class);
		arp.addMapping("72crm_work_product_history", "id", WorkProductHistory.class);
		arp.addMapping("72crm_work_historical", "id", WorkHistorical.class);
		arp.addMapping("72crm_work_historical_record", "id", WorkHistoricalRecord.class);
		arp.addMapping("72crm_work_user_record", "id", WorkUserRecord.class);
		arp.addMapping("72crm_work_task_historical", "id", WorkTaskHistorical.class);

		arp.addMapping("72crm_work_task_historical", "id", WorkTaskHistorical.class);

		arp.addMapping("72crm_work_config", "config_id", WorkConfig.class);
		arp.addMapping("72crm_crm_action_record", "id", CrmActionRecord.class);




		arp.addMapping("72crm_admin_dept", "dept_id", AdminDept.class);
		arp.addMapping("72crm_admin_attention", "attention_id", AdminAttention.class);
		arp.addMapping("72crm_admin_examine", "examine_id", AdminExamine.class);
		arp.addMapping("72crm_admin_config", "setting_id", AdminConfig.class);
		arp.addMapping("72crm_admin_user_config", "setting_id", AdminUserConfig.class);
		arp.addMapping("72crm_admin_examine_record", "record_id", AdminExamineRecord.class);
		arp.addMapping("72crm_admin_examine_step", "step_id", AdminExamineStep.class);
		arp.addMapping("72crm_admin_examine_log", "log_id", AdminExamineLog.class);
		arp.addMapping("72crm_admin_field", "field_id", AdminField.class);
		arp.addMapping("72crm_admin_field_sort", "field_id", AdminFieldSort.class);
		arp.addMapping("72crm_admin_field_style", "id", AdminFieldStyle.class);
		arp.addMapping("72crm_admin_file", "file_id", AdminFile.class);
		arp.addMapping("72crm_admin_login_log", "id", AdminLoginLog.class);
		arp.addMapping("72crm_admin_menu", "menu_id", AdminMenu.class);
		arp.addMapping("72crm_admin_message", "message_id", AdminMessage.class);
		arp.addMapping("72crm_admin_model_sort", "id", AdminModelSort.class);
		arp.addMapping("72crm_admin_number_setting", "setting_id", AdminNumberSetting.class);
		arp.addMapping("72crm_admin_role", "role_id", AdminRole.class);
		arp.addMapping("72crm_admin_role_menu", "id", AdminRoleMenu.class);
		arp.addMapping("72crm_admin_scene", "scene_id", AdminScene.class);
		arp.addMapping("72crm_admin_scene_default", "default_id", AdminSceneDefault.class);
		arp.addMapping("72crm_admin_system_log", "id", AdminSystemLog.class);
		arp.addMapping("72crm_admin_user", "user_id", AdminUser.class);
		arp.addMapping("72crm_admin_user_role", "id", AdminUserRole.class);
		arp.addMapping("72crm_admin_official_img", "official_img_id", AdminOfficialImg.class);
		arp.addMapping("72crm_admin_visiting_card", "card_id", AdminVisitingCard.class);
		arp.addMapping("72crm_admin_user_his_table", "his_table_id", AdminUserHisTable.class);
		arp.addMapping("72crm_crm_business", "business_id", CrmBusiness.class);
		arp.addMapping("72crm_crm_business_data", "id", CrmBusinessData.class);
		arp.addMapping("72crm_crm_achievement", "achievement_id", CrmAchievement.class);
		arp.addMapping("72crm_crm_action_record", "id", CrmActionRecord.class);
		arp.addMapping("72crm_crm_activity", "activity_id", CrmActivity.class);
		arp.addMapping("72crm_crm_activity_relation", "r_id", CrmActivityRelation.class);
		arp.addMapping("72crm_crm_business_product", "r_id", CrmBusinessProduct.class);
		arp.addMapping("72crm_crm_business_status", "status_id", CrmBusinessStatus.class);
		arp.addMapping("72crm_crm_business_user_star", "id", CrmBusinessUserStar.class);
		arp.addMapping("72crm_crm_business_type", "type_id", CrmBusinessType.class);
		arp.addMapping("72crm_crm_contacts", "contacts_id", CrmContacts.class);
		arp.addMapping("72crm_crm_contacts_data", "id", CrmContactsData.class);
		arp.addMapping("72crm_crm_contacts_business", "id", CrmContactsBusiness.class);
		arp.addMapping("72crm_crm_contacts_user_star", "id", CrmContactsUserStar.class);
		arp.addMapping("72crm_crm_contract", "contract_id", CrmContract.class);
		arp.addMapping("72crm_crm_contract_data", "id", CrmContractData.class);
		arp.addMapping("72crm_crm_contract_product", "r_id", CrmContractProduct.class);
		arp.addMapping("72crm_crm_customer", "customer_id", CrmCustomer.class);
		arp.addMapping("72crm_crm_customer_data", "id", CrmCustomerData.class);
		arp.addMapping("72crm_crm_customer_pool", "pool_id", CrmCustomerPool.class);
		arp.addMapping("72crm_crm_customer_pool_field_setting", "setting_id", CrmCustomerPoolFieldSetting.class);
		arp.addMapping("72crm_crm_customer_pool_field_sort", "id", CrmCustomerPoolFieldSort.class);
		arp.addMapping("72crm_crm_customer_pool_field_style", "id", CrmCustomerPoolFieldStyle.class);
		arp.addMapping("72crm_crm_customer_pool_rule", "rule_id", CrmCustomerPoolRule.class);
		arp.addMapping("72crm_crm_customer_pool_relation", "id", CrmCustomerPoolRelation.class);
		arp.addMapping("72crm_crm_customer_setting", "setting_id", CrmCustomerSetting.class);
		arp.addMapping("72crm_crm_customer_settinguser", "id", CrmCustomerSettinguser.class);
		arp.addMapping("72crm_crm_customer_user_star", "id", CrmCustomerUserStar.class);
		arp.addMapping("72crm_crm_instrument_sort", "id", CrmInstrumentSort.class);
		arp.addMapping("72crm_crm_invoice", "invoice_id", CrmInvoice.class);
		arp.addMapping("72crm_crm_invoice_info", "info_id", CrmInvoiceInfo.class);
		arp.addMapping("72crm_crm_leads", "leads_id", CrmLeads.class);
		arp.addMapping("72crm_crm_leads_data", "id", CrmLeadsData.class);
		arp.addMapping("72crm_crm_leads_user_star", "id", CrmLeadsUserStar.class);
		arp.addMapping("72crm_crm_owner_record", "record_id", CrmOwnerRecord.class);
		arp.addMapping("72crm_crm_product", "product_id", CrmProduct.class);
		arp.addMapping("72crm_crm_product_data", "id", CrmProductData.class);
		arp.addMapping("72crm_crm_product_category", "category_id", CrmProductCategory.class);
		arp.addMapping("72crm_crm_receivables", "receivables_id", CrmReceivables.class);
		arp.addMapping("72crm_crm_receivables_data", "id", CrmReceivablesData.class);
		arp.addMapping("72crm_crm_receivables_plan", "plan_id", CrmReceivablesPlan.class);
		arp.addMapping("72crm_crm_return_visit", "visit_id", CrmReturnVisit.class);
		arp.addMapping("72crm_crm_return_visit_data", "id", CrmReturnVisitData.class);
		arp.addMapping("72crm_crm_product_detail_img", "img_id", CrmProductDetailImg.class);
		arp.addMapping("72crm_card_weixin_leads", "weixin_leads_id", CardWeixinLeads.class);
		arp.addMapping("72crm_card_weixin_browse", "browse_id", CardWeixinBrowse.class);
		arp.addMapping("72crm_card_weixin_leads_user", "weixin_user_id", CardWeixinLeadsUser.class);
		arp.addMapping("72crm_crm_marketing", "marketing_id", CrmMarketing.class);
		arp.addMapping("72crm_crm_marketing_info", "r_id", CrmMarketingInfo.class);
		arp.addMapping("72crm_email_account", "id", EmailAccount.class);
		arp.addMapping("72crm_email_file", "id", EmailFile.class);
		arp.addMapping("72crm_email_lately", "id", EmailLately.class);
		arp.addMapping("72crm_email_record", "id", EmailRecord.class);
		arp.addMapping("72crm_oa_announcement", "announcement_id", OaAnnouncement.class);
		arp.addMapping("72crm_oa_calendar_type", "type_id", OaCalendarType.class);
		arp.addMapping("72crm_oa_calendar_type_user", "id", OaCalendarTypeUser.class);
		arp.addMapping("72crm_oa_event", "event_id", OaEvent.class);
		arp.addMapping("72crm_oa_event_notice", "id", OaEventNotice.class);
		arp.addMapping("72crm_oa_event_update_record", "id", OaEventUpdateRecord.class);
		arp.addMapping("72crm_oa_event_relation", "eventrelation_id", OaEventRelation.class);
		arp.addMapping("72crm_oa_examine_category", "category_id", OaExamineCategory.class);
		arp.addMapping("72crm_oa_examine_relation", "r_id", OaExamineRelation.class);
		arp.addMapping("72crm_oa_examine_travel", "travel_id", OaExamineTravel.class);
		arp.addMapping("72crm_oa_examine", "examine_id", OaExamine.class);
		arp.addMapping("72crm_oa_examine_data", "id", OaExamineData.class);
		arp.addMapping("72crm_oa_examine_log", "log_id", OaExamineLog.class);
		arp.addMapping("72crm_oa_examine_record", "record_id", OaExamineRecord.class);
		arp.addMapping("72crm_oa_examine_step", "step_id", OaExamineStep.class);
		arp.addMapping("72crm_oa_log", "log_id", OaLog.class);
		arp.addMapping("72crm_oa_log_relation", "r_id", OaLogRelation.class);
		arp.addMapping("72crm_oa_log_record", "id", OaLogRecord.class);
		arp.addMapping("72crm_oa_log_rule", "rule_id", OaLogRule.class);
		arp.addMapping("72crm_oa_log_bulletin", "id", OaLogBulletin.class);

		arp.addMapping("72crm_task_comment", "comment_id", TaskComment.class);
		arp.addMapping("72crm_task_relation", "r_id", TaskRelation.class);

		arp.addMapping("72crm_work_collect", "collect_id", WorkCollect.class);
		arp.addMapping("72crm_work_order", "order_id", WorkOrder.class);
		arp.addMapping("72crm_work_task_class", "class_id", WorkTaskClass.class);
		arp.addMapping("72crm_work_task_label", "label_id", WorkTaskLabel.class);
		arp.addMapping("72crm_work_task_label_order", "order_id", WorkTaskLabelOrder.class);
		arp.addMapping("72crm_work_task_log", "log_id", WorkTaskLog.class);
		arp.addMapping("72crm_work_user", "id", WorkUser.class);
		arp.addMapping("72crm_crm_business_change", "change_id", CrmBusinessChange.class);
		arp.addMapping("72crm_card_weixin_leads", "weixin_leads_id", CardWeixinLeads.class);
		arp.addMapping("72crm_card_weixin_browse", "browse_id", CardWeixinBrowse.class);
		arp.addMapping("72crm_card_weixin_leads_user", "weixin_user_id", CardWeixinLeadsUser.class);
		arp.addMapping("72crm_crm_product_detail_img", "img_id", CrmProductDetailImg.class);
		arp.addMapping("72crm_crm_product_user", "product_user_id", CrmProductUser.class);









		/*arp.addMapping("72crm_admin_official_img", "official_img_id", AdminOfficialImg.class);
		arp.addMapping("72crm_admin_visiting_card", "card_id", AdminVisitingCard.class);
		arp.addMapping("72crm_admin_dept", "dept_id", AdminDept.class);
		arp.addMapping("72crm_admin_attention", "attention_id", AdminAttention.class);
		arp.addMapping("72crm_admin_examine", "examine_id", AdminExamine.class);
		arp.addMapping("72crm_admin_config", "setting_id", AdminConfig.class);
		arp.addMapping("72crm_admin_user_config", "setting_id", AdminUserConfig.class);
		arp.addMapping("72crm_admin_examine_record", "record_id", AdminExamineRecord.class);
		arp.addMapping("72crm_admin_examine_step", "step_id", AdminExamineStep.class);
		arp.addMapping("72crm_admin_examine_log", "log_id", AdminExamineLog.class);
		arp.addMapping("72crm_admin_field", "field_id", AdminField.class);
		arp.addMapping("72crm_admin_field_sort", "field_id", AdminFieldSort.class);
		arp.addMapping("72crm_admin_field_style", "id", AdminFieldStyle.class);
		arp.addMapping("72crm_admin_file", "file_id", AdminFile.class);
		arp.addMapping("72crm_admin_login_log", "id", AdminLoginLog.class);
		arp.addMapping("72crm_admin_menu", "menu_id", AdminMenu.class);
		arp.addMapping("72crm_admin_message", "message_id", AdminMessage.class);
		arp.addMapping("72crm_admin_model_sort", "id", AdminModelSort.class);
		arp.addMapping("72crm_admin_number_setting", "setting_id", AdminNumberSetting.class);
		arp.addMapping("72crm_admin_role", "role_id", AdminRole.class);
		arp.addMapping("72crm_admin_role_menu", "id", AdminRoleMenu.class);
		arp.addMapping("72crm_admin_scene", "scene_id", AdminScene.class);
		arp.addMapping("72crm_admin_scene_default", "default_id", AdminSceneDefault.class);
		arp.addMapping("72crm_admin_system_log", "id", AdminSystemLog.class);
		arp.addMapping("72crm_admin_user", "user_id", AdminUser.class);
		arp.addMapping("72crm_admin_user_role", "id", AdminUserRole.class);

		arp.addMapping("72crm_admin_user_his_table", "his_table_id", AdminUserHisTable.class);
		arp.addMapping("72crm_crm_business", "business_id", CrmBusiness.class);
		arp.addMapping("72crm_crm_business_data", "id", CrmBusinessData.class);
		arp.addMapping("72crm_crm_achievement", "achievement_id", CrmAchievement.class);
		arp.addMapping("72crm_crm_action_record", "id", CrmActionRecord.class);
		arp.addMapping("72crm_crm_activity", "activity_id", CrmActivity.class);
		arp.addMapping("72crm_crm_activity_relation", "r_id", CrmActivityRelation.class);
		arp.addMapping("72crm_crm_business_product", "r_id", CrmBusinessProduct.class);
		arp.addMapping("72crm_crm_business_status", "status_id", CrmBusinessStatus.class);
		arp.addMapping("72crm_crm_business_user_star", "id", CrmBusinessUserStar.class);
		arp.addMapping("72crm_crm_business_type", "type_id", CrmBusinessType.class);
		arp.addMapping("72crm_crm_contacts", "contacts_id", CrmContacts.class);
		arp.addMapping("72crm_crm_contacts_data", "id", CrmContactsData.class);
		arp.addMapping("72crm_crm_contacts_business", "id", CrmContactsBusiness.class);
		arp.addMapping("72crm_crm_contacts_user_star", "id", CrmContactsUserStar.class);
		arp.addMapping("72crm_crm_contract", "contract_id", CrmContract.class);
		arp.addMapping("72crm_crm_contract_data", "id", CrmContractData.class);
		arp.addMapping("72crm_crm_contract_product", "r_id", CrmContractProduct.class);
		arp.addMapping("72crm_crm_customer", "customer_id", CrmCustomer.class);
		arp.addMapping("72crm_crm_customer_data", "id", CrmCustomerData.class);
		arp.addMapping("72crm_crm_customer_pool", "pool_id", CrmCustomerPool.class);
		arp.addMapping("72crm_crm_customer_pool_field_setting", "setting_id", CrmCustomerPoolFieldSetting.class);
		arp.addMapping("72crm_crm_customer_pool_field_sort", "id", CrmCustomerPoolFieldSort.class);
		arp.addMapping("72crm_crm_customer_pool_field_style", "id", CrmCustomerPoolFieldStyle.class);
		arp.addMapping("72crm_crm_customer_pool_rule", "rule_id", CrmCustomerPoolRule.class);
		arp.addMapping("72crm_crm_customer_pool_relation", "id", CrmCustomerPoolRelation.class);
		arp.addMapping("72crm_crm_customer_setting", "setting_id", CrmCustomerSetting.class);
		arp.addMapping("72crm_crm_customer_settinguser", "id", CrmCustomerSettinguser.class);
		arp.addMapping("72crm_crm_customer_user_star", "id", CrmCustomerUserStar.class);
		arp.addMapping("72crm_crm_instrument_sort", "id", CrmInstrumentSort.class);
		arp.addMapping("72crm_crm_invoice", "invoice_id", CrmInvoice.class);
		arp.addMapping("72crm_crm_invoice_info", "info_id", CrmInvoiceInfo.class);
		arp.addMapping("72crm_crm_leads", "leads_id", CrmLeads.class);
		arp.addMapping("72crm_crm_leads_data", "id", CrmLeadsData.class);
		arp.addMapping("72crm_crm_leads_user_star", "id", CrmLeadsUserStar.class);
		arp.addMapping("72crm_crm_owner_record", "record_id", CrmOwnerRecord.class);
		arp.addMapping("72crm_crm_product", "product_id", CrmProduct.class);
		arp.addMapping("72crm_crm_product_data", "id", CrmProductData.class);
		arp.addMapping("72crm_crm_product_category", "category_id", CrmProductCategory.class);
		arp.addMapping("72crm_crm_receivables", "receivables_id", CrmReceivables.class);
		arp.addMapping("72crm_crm_receivables_data", "id", CrmReceivablesData.class);
		arp.addMapping("72crm_crm_receivables_plan", "plan_id", CrmReceivablesPlan.class);
		arp.addMapping("72crm_crm_return_visit", "visit_id", CrmReturnVisit.class);
		arp.addMapping("72crm_crm_return_visit_data", "id", CrmReturnVisitData.class);

		arp.addMapping("72crm_crm_marketing", "marketing_id", CrmMarketing.class);
		arp.addMapping("72crm_crm_marketing_info", "r_id", CrmMarketingInfo.class);

		arp.addMapping("72crm_task", "task_id", Task.class);
		arp.addMapping("72crm_task_comment", "comment_id", TaskComment.class);
		arp.addMapping("72crm_task_relation", "r_id", TaskRelation.class);
		arp.addMapping("72crm_work", "work_id", Work.class);
		arp.addMapping("72crm_work_collect", "collect_id", WorkCollect.class);
		arp.addMapping("72crm_work_order", "order_id", WorkOrder.class);
		arp.addMapping("72crm_work_task_class", "class_id", WorkTaskClass.class);
		arp.addMapping("72crm_work_task_label", "label_id", WorkTaskLabel.class);
		arp.addMapping("72crm_work_task_label_order", "order_id", WorkTaskLabelOrder.class);
		arp.addMapping("72crm_work_task_log", "log_id", WorkTaskLog.class);
		arp.addMapping("72crm_work_user", "id", WorkUser.class);
		arp.addMapping("72crm_crm_business_change", "change_id", CrmBusinessChange.class);

		arp.addMapping("72crm_crm_product_user", "product_user_id", CrmProductUser.class);
		arp.addMapping("72crm_crm_product_detail_img", "img_id", CrmProductDetailImg.class);
		arp.addMapping("72crm_card_weixin_leads", "weixin_leads_id", CardWeixinLeads.class);
		arp.addMapping("72crm_card_weixin_browse", "browse_id", CardWeixinBrowse.class);
		arp.addMapping("72crm_card_weixin_leads_user", "weixin_user_id", CardWeixinLeadsUser.class);



		arp.addMapping("72crm_email_account", "id", EmailAccount.class);
		arp.addMapping("72crm_email_file", "id", EmailFile.class);
		arp.addMapping("72crm_email_lately", "id", EmailLately.class);
		arp.addMapping("72crm_email_record", "id", EmailRecord.class);
		arp.addMapping("72crm_oa_announcement", "announcement_id", OaAnnouncement.class);
		arp.addMapping("72crm_oa_calendar_type", "type_id", OaCalendarType.class);
		arp.addMapping("72crm_oa_calendar_type_user", "id", OaCalendarTypeUser.class);
		arp.addMapping("72crm_oa_event", "event_id", OaEvent.class);
		arp.addMapping("72crm_oa_event_notice", "id", OaEventNotice.class);
		arp.addMapping("72crm_oa_event_update_record", "id", OaEventUpdateRecord.class);
		arp.addMapping("72crm_oa_event_relation", "eventrelation_id", OaEventRelation.class);
		arp.addMapping("72crm_oa_examine_category", "category_id", OaExamineCategory.class);
		arp.addMapping("72crm_oa_examine_relation", "r_id", OaExamineRelation.class);
		arp.addMapping("72crm_oa_examine_travel", "travel_id", OaExamineTravel.class);
		arp.addMapping("72crm_oa_examine", "examine_id", OaExamine.class);
		arp.addMapping("72crm_oa_examine_data", "id", OaExamineData.class);
		arp.addMapping("72crm_oa_examine_log", "log_id", OaExamineLog.class);
		arp.addMapping("72crm_oa_examine_record", "record_id", OaExamineRecord.class);
		arp.addMapping("72crm_oa_examine_step", "step_id", OaExamineStep.class);
		arp.addMapping("72crm_oa_log", "log_id", OaLog.class);
		arp.addMapping("72crm_oa_log_relation", "r_id", OaLogRelation.class);
		arp.addMapping("72crm_oa_log_record", "id", OaLogRecord.class);
		arp.addMapping("72crm_oa_log_rule", "rule_id", OaLogRule.class);
		arp.addMapping("72crm_oa_log_bulletin", "id", OaLogBulletin.class);

		arp.addMapping("72crm_card_weixin_leads", "weixin_leads_id", CardWeixinLeads.class);
		arp.addMapping("72crm_card_weixin_browse", "browse_id", CardWeixinBrowse.class);
		arp.addMapping("72crm_card_weixin_leads_user", "weixin_user_id", CardWeixinLeadsUser.class);
		arp.addMapping("72crm_crm_product_detail_img", "img_id", CrmProductDetailImg.class);*/
	}
}

