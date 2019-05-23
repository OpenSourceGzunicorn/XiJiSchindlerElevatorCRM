
package com.gzunicorn.common.util;

/**
 * Created on 2005-7-12
 * <p>Title:	</p>
 * <p>Description:	系统常量配置</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */

public class SysConfig {

    public final static String WEB_APPNAME = "XJSCRM";//Web应用程序名称
   
    public final static String LOGIN_ACTION_NAME = "Logon.do";//登录ACTION名称
    
    public final static String LOGIN_OUT_PAGE = "logout/logout.jsp";//登录ACTION名称
    
    public final static String CONFIG_FILENAME = "com/gzunicorn/common/resources/Config.properties";//配置文件名定义
    public final static String HIBERNATE_CFG_FILENAME= "/com/gzunicorn/hibernate/hibernate.cfg.xml";//Hibernate配置文件名定义

    public final static String TOOLBAR_RIGHT = "TOOLBAR_RIGHT";//登录用户Toolbar权限信息
    
    public final static String LOGIN_USER_INFO = "USER_INFO";//登录用户信息

    public final static String BACKGROUD_USER_ID = "Background";//系统后台用户ID
    
    public final static String ALL_LOGIN_USER_INFO = "ALL_USER_INFO";//所有登录用户信息LIST
    
    public final static int TREE_NODE_LEVEL = 2;//树节点层与层的编码长度
    
    public final static String TREE_ROOT_NODE = "d.add(0,-1,'XJSCRM',null,'XJSCRM','main');\n";//树根节点代码;
    
    public final static int HTML_TABLE_LENGTH = 12;//HTML表格List记录条数;
    
    public final static int HTML_TABLE_LENGTH50 = 50;//HTML表格List记录条数;
    
    public final static String NULL_URL = "#";//没有链接节点的URL，即目录节点;
    
    /****商务分析状态*****/
    public final static String COMMERCEASSAYSTATE_A = "A";//编制
    public final static String COMMERCEASSAYSTATE_B = "B";//内部审批中
    public final static String COMMERCEASSAYSTATE_C = "C";//审批通过
    public final static String COMMERCEASSAYSTATE_D = "D";//审批不通过
    public final static String COMMERCEASSAYSTATE_E = "E";//市场驳回

	 public final static String switch_flag ="N";//开关标志:Y :显示 N:不显示

	 public final static String INFOISSUE_READER_TYPE_DEPT = "1";// 部门
	 public final static String DOC_PURVIEW_TYPE_USER = "0";// 个人
	 public final static String DOC_PURVIEW_TYPE_DEPT = "1";// 部门
	 public final static String DOC_PURVIEW_TYPE_CLASS = "3";// 职位群组
	 
	 public final static int QUOTECONNECT_VERSION_JNLNO_LEN = 6;//报价联络书版本号长度
	 public final static String WORKSPACEBASEFIT_JNLNO_FLAG = "WO";//销售电梯合同流水号标志
	 
	 /************CRM后台自动计算特殊技术要求参数  end ************/
	 
	 /*获取工作平台 相关列表 标识值,与WorkspaceBaseProperty表WSKey 值对应*/
	 public final static String TOGET_DUTYS = "MyDutys";//获取 菜单任务列表
	 public final static String TOGET_Fault = "FaultRepairEntry";//获取 故障报修列表
	 public final static String TOGET_Contract = "ContractMaster";//获取 维保合同到期
	 public final static String TOGET_Complaints = "AdvisoryComplaintsManage";//获取 全质办咨询投诉管理
	 public final static String TOGET_QualityCheck = "QualityCheckManagement";//获取 维保质量检查 
	 public final static String TOGET_ElevatorTrans = "ElevatorTransferCaseRegister";//获取 安装维保交接电梯情况
	 public final static String TOGET_CustomerVisit = "CustomerVisitPlanDetail";//获取 客户拜访反馈
	 public final static String TOGET_ContractDelay = "MaintContractDelayMaster";//维保合同延保申请提醒 
	 public final static String TOGET_ContractMaster = "MaintContractMaster";//维保合同/委托合同提醒 
	 public final static String TOGET_LostElevator = "LostElevatorReport";//维保合同退保提醒 
	 public final static String TOGET_MaintContractQuoteMaster = "MaintContractQuoteMaster";//维保报价审核通过提醒 
	 public final static String TOGET_ContractInvoice = "ContractInvoiceManage";//合同开票审核提醒 
	 public final static String TOGET_CustReturnRegister = "CustReturnRegisterMaster";//客户回访登记
	 public final static String TOGET_ContractAssigned = "ContractAssigned";//维保合同下达签收
	 public final static String TOGET_LoginUserAudit = "LoginUserAudit";//用户信息审核
	 public final static String TOGET_HotCalloutAudit  = "HotCalloutAudit";//急修安全经理审核
	 public final static String TOGET_AccessoriesRequisition  = "AccessoriesRequisition";//配件申请单(仓库管理员)处理
	 public final static String TOGET_AccessoriesRequisitionCkbl  = "AccessoriesRequisitionCkbl";//配件申请单出库办理 
	 public final static String TOGET_TransferAssign  = "ContractTransferAssign";//合同交接资料派工 
	 public final static String TOGET_TransferUpload  = "ContractTransferUpload";//合同交接资料上传
	 public final static String TOGET_TransferAudit  = "ContractTransferAudit";//合同交接资料审核
	 public final static String TOGET_TransferFeedBack  = "ContractTransferFeedBack";//合同交接资料反馈查看
	 public final static String TOGET_TransferManagerAudit  = "ContractTransferManagerAudit";//合同交接资料经理审核
	 
     private SysConfig() {
     
    }
    
}
