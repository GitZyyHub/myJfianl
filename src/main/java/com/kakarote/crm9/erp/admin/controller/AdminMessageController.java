package com.kakarote.crm9.erp.admin.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Page;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.admin.entity.AdminMessage;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.io.File;

/**
 * 消息提醒
 * @author zhangzhiwei
 */
public class AdminMessageController extends Controller {

    @Inject
    private AdminMessageService adminMessageService;

    /**
     * 查询消息列表
     * @author zhangzhiwei
     */
    public void queryList(BasePageRequest<AdminMessage> basePageRequest){
       Page<AdminMessage> page = adminMessageService.queryList(basePageRequest);
       renderJson(R.ok().put("data",page));
    }

    /**
     * 查询未读消息数量
     */
    public void queryUnreadCount(){
        renderJson(adminMessageService.queryUnreadCount());
    }

    /**
     * @author wyq
     * 单个标记为已读
     */
    public void readMessage(@Para("messageId") Long messageId){
        renderJson(adminMessageService.readMessage(messageId));
    }

    /**
     * @author wyq
     * 全部标记为已读
     */
    public void readAllMessage(@Para("label") Integer label){
        renderJson(adminMessageService.readAllMessage(label));
    }

    /**
     * 查询当前导入数量
     * @param messageId 消息ID
     * @author zhangzhiwei
     */
    public void queryImportNum(@Para("messageId") Long messageId){
        Integer num = CrmExcelUtil.getThreadMapNum(messageId);
        renderJson(R.ok().put("data", num));
    }

    /**
     * 终止当前导入
     * @param messageId 消息ID
     * @author zhangzhiwei
     */
    public void stopImport(@Para("messageId") Long messageId){
        AdminMessage adminMessage= AdminMessage.dao.findById(messageId);
        if(adminMessage==null||!adminMessage.getCreateUser().equals(BaseUtil.getUserId())){
            renderJson(R.noAuth());
            return;
        }
        CrmExcelUtil.interrupt(messageId);
        renderJson(R.ok());
    }

    /**
     * 下载错误数据
     * @param messageId 消息ID
     * @author zhangzhiwei
     */
    public void downImportError(@Para("messageId") Long messageId){
        AdminMessage adminMessage= AdminMessage.dao.findById(messageId);
        if(adminMessage==null){
            renderNull();
            return;
        }
        AdminFile adminFile= AdminFile.dao.findById(adminMessage.getTypeId());
        if(adminFile!=null&&adminFile.getCreateUserId().equals(BaseUtil.getUserId())){
            File file=new File(adminFile.getPath());
            if(file.exists()){
                renderFile(file,"import_error.xlsx");
                return;
            }
        }
        renderNull();
    }

    /**
     * 查询当前导入详情
     * @param messageId 消息ID
     * @author zhangzhiwei
     */
    public void queryImportInfo(@Para("messageId") Long messageId){
        AdminMessage adminMessage= AdminMessage.dao.findById(messageId);
        if(adminMessage!=null){
            String[] content = adminMessage.getContent().split(",");
            R r = R.ok().put("totalSize", adminMessage.getTitle()).put("errSize", content[0]);
            r.put("updateSize", content.length > 1 ? content[1] : 0);
            renderJson(r);
        }else {
            renderJson(R.ok().put("totalSize", 0).put("errSize", 0).put("updateSize", 0));
        }
    }

    /**
     * 清空通知
     *
     * @author wyq
     */
    public void clear(@Para("label") Integer label) {
        renderJson(adminMessageService.clear(label));
    }

    /**
     * 删除通知
     *
     * @author wyq
     */
    public void deleteById(@Para("messageId") Integer messageId) {
        renderJson(adminMessageService.deleteById(messageId));
    }

}
