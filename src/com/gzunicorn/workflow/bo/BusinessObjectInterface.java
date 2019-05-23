package com.gzunicorn.workflow.bo;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.gzunicorn.common.util.Msg;

public interface BusinessObjectInterface {
	/****基础申请信息操作********************************************************************************************/
	/**
	 * 新建审批申请
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toPrepareApply(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 保存及提交审批申请
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toSaveAndSubmitApply(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 保存审批申请
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toSaveApply(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 准备更新审批申请
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toPrepareUpdateApply(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 更新审批申请
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toUpdateApply(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 更新及提交审批
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toUpdateAndSubmitApply(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 查看流程信息
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toViewProcess(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 删除流程信息
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toDelProcess(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 查询流程信息
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toSearchProcess(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 导出Excel
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toExportExcel(ActionForm form,HttpServletRequest request,HashMap arg);
	
	
	/****审批信息操作********************************************************************************************/
	/**
	 * 准备审批
	 */
	public Msg toPrepareApprove(ActionForm form,HttpServletRequest request,HashMap arg);
	/**
	 * 保存审批结果
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toSaveApprove(ActionForm form,HttpServletRequest request,HashMap arg);
	/**
	 * 保存审批结果及提交
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toSaveAndSubmitApprove(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 保存或提交数据检查
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg toApplyCheck(ActionForm form,HttpServletRequest request,HashMap arg);
	
	/**
	 * 取业务相关信息
	 * @param flag
	 * @param jnlno
	 * @param tokenid
	 * @return
	 */
	public Msg getBO(int flag,String jnlno,String tokenid);
	
	/**
	 * 取业务相关信息
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 */
	public Msg getBO(ActionForm form,HttpServletRequest request,HashMap arg);
	
	
}
