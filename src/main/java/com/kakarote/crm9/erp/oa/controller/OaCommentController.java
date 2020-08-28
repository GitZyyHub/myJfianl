package com.kakarote.crm9.erp.oa.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.oa.common.OaEnum;
import com.kakarote.crm9.erp.oa.service.OaCommentService;
import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.TaskComment;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.util.Objects;

public class OaCommentController extends Controller{

    @Inject
    private OaCommentService commentService;
    /**
     * @author hmb
     * 添加评论或者修改
     * @param comment 评论对象
     */
    public void setComment(@Para("") TaskComment comment){
        if(comment.getType() == 1){
            boolean oaAuth = AuthUtil.isOaAuth(OaEnum.TASK_TYPE_KEY, comment.getTypeId());
            if(oaAuth){renderJson(R.noAuth());return;}
        }
        renderJson(commentService.setComment(comment));
    }

    /**
     * 项目任务评论，无需权限限制
     *
     * @param comment
     */
    public void setWorkTaskComment(@Para("") TaskComment comment) {
        renderJson(commentService.setComment(comment));
    }


    public void deleteWorkTaskComment() {
        Integer commentId = getParaToInt("commentId");
        renderJson(commentService.deleteComment(commentId));
    }

    /**
     * 删除评论
     */
    public void deleteComment(){
        Integer commentId = getParaToInt ( "commentId" );
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.COMMENT_TYPE_KEY, commentId);
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(commentService.deleteComment (commentId));
    }

    /**
     * @author hmb
     * 查询评论列表
     */
    public void queryCommentList(){
        Integer typeId = getParaToInt("typeId");
        String type = getPara("type");
        if (!BaseUtil.isAdmin() && !BaseUtil.getUser().getRoles().contains(BaseUtil.getWorkAdminRole())) {
            if ("1".equals(type)) {
                Task task = Task.dao.findById(typeId);
                boolean auth = true;
                if (Objects.equals(0, task.getWorkId())) {
                    auth = AuthUtil.isOaAuth(OaEnum.TASK_TYPE_KEY, typeId);
                } else {
                    Work work = Work.dao.findById(task.getWorkId());
                    if (TagUtil.toLongSet(work.getOwnerUserId()).contains(BaseUtil.getUserId()) || work.getIsOpen() == 1) {
                        auth = false;
                    }
                }
                if (auth) {
                    renderJson(R.noAuth());
                    return;
                }
            }
        }
        renderJson(R.ok().put("data",commentService.queryCommentList(typeId,type)));
    }
}
