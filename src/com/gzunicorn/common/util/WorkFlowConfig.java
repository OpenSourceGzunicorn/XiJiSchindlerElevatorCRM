package com.gzunicorn.common.util;

public class WorkFlowConfig {
	
	/**
	 * 流程定义
	 */
	//public static String Process_AgentGoodsPlan="AgentGoodsPlan";
	
	//public static String Process_FeeApply="feeapply";
	
	//public static String Process_CarExtSale="CarExtSale";
	
	public static String Process_InstanceId="InstanceId";
	
	public static String[][] Process_Define={
												{"enginequotemaster","enginequotemaster"},//配置流程状态
												{"enginequotemasterProcessName","维保报价审批流程"},
												{"maintcontractdelaymaster","maintcontractdelaymaster"},
												{"maintcontractdelaymasterProcessName","维保合同延保审批流程"},
												{"elevatortransfercaseregister","elevatortransfercaseregister"},
												{"elevatortransfercaseregisterProcessName","安装维保交接电梯审批流程"},
												{"servicingcontractquotemaster","servicingcontractquotemaster"},
												{"servicingcontractquotemasterProcessName","维改报价审批流程"},	
												{"qualitycheckmanagement","qualitycheckmanagement"},
												{"qualitycheckmanagementProcessName","维保质量检查审批流程"},	
												{"contractpaymentmanage","contractpaymentmanage"},
												{"contractpaymentmanageProcessName","合同付款审批流程"},
												{"outsourcecontractquotemaster","outsourcecontractquotemaster"},
												{"outsourcecontractquotemasterProcessName","维改委托报价审批流程"},	
												{"entrustcontractquotemaster","entrustcontractquotemaster"},
												{"entrustcontractquotemasterProcessName","维保委托报价审批流程"}										
												
											};
	
	
	public static int State_NoStart=-1;		//未启动流程
	public static int State_ApplyMod=0;		//申请人修改
	public static int State_Approve=100;	//审批中
	public static int State_Pass=1;			//审批通过
	public static int State_NoPass=2;		//审批不通过
	public static int State_CutOff=3;		//中止
	
	/**
	 * [][1] 0:同意的流向，1:不同意的流向，2:结束结点，[][2]=0 nopass,[][1] pass
	 */
	public static String[][] TranKeyWord={{"同意","0",""},
										{"提交","0",""},
										{"退回","1",""},
										{"不同意","1",""},
										{"中止","2","0"},
										{"终止","2","0"},
										{"取消","2","0"},
										{"通过","2","1"}};
	/***********************************************************************************************************/
	/**
	 * 流程角色定义
	 */
	/**代理商*/
	public static String Role_AGP_Agent="Role_AGP_Agent";
	/**区域经理*/
	public static String Role_AGP_ZonalManager="Role_AGP_ZonalManager";
	/**订单管理员*/
	public static String Role_AGP_OrderAdmin="Role_AGP_OrderAdmin";
	/**内销经理*/
	public static String Role_AGP_SaleManager="Role_AGP_SaleManager";
	
	
//	public static String RoleAgent="agent";
//	public static String RoleAgent="agent";
//	public static String RoleAgent="agent";
//	public static String RoleAgent="agent";
//	public static String RoleAgent="agent";
	
	/***********************************************************************************************************/
	public static String Flow_IssueMan="Flow_IssueMan";
	
	public static String Flow_Client="Flow_Client";
	
	public static String Flow_RefUserId="Flow_RefUserId";
	
	public static String Approve_Level="Approve_Level";
	
	public static String Approve_Result="Approve_Result";
	
	public static String Approve_Result_MsgInfo="Approve_Result_MsgInfo";
	
	public static boolean Flow_IsAutoActor=true;
	
	public static String Flow_NodeId="Flow_NodeId";
	
	public static String Decision_Amount="Decision_Amount";
	
	/**流程信息传递*/
	public static String Flow_Bean="Flow_Bean";
	
	/**流程信息传递*/
	public static String Appoint_Actors="Appoint_Actors";
	
	public static String Flow_Pass_Prefix="同意";
	public static String Flow_NoPass_Prefix="不同意";
	/***********************************************************************************************************/

	/**
	 * 公用的方法,由键值获取到对应的属性值
	 * @param str
	 * @param key
	 * @return
	 */
	private static synchronized String getNameByKey(String[][] str,String key ){
		String name = "";
		if(str!=null){
			int len=str.length;
			for(int i=0;i<len;i++){
				if(str[i][0].equalsIgnoreCase(key)){
					name = str[i][1];
					break;
				}
			}
		}
		return name;
	}

	/**
	 * 各模块的上传附件功能上传文件的相对路径
	 * @param key
	 * @return
	 */
	public static String getProcessDefine(String key ){
		return getNameByKey(Process_Define,key);
	}
	
	/**
	 * 判断是否是通过关键字
	 * @param selpath
	 * @return
	 */
	public static synchronized boolean isPassTranKeyWord(String selpath){
		if(selpath!=null && selpath.length()>0){
			for(int i=0;i<TranKeyWord.length;i++){
//				if(TranKeyWord[i][1].equalsIgnoreCase("0") && selpath.indexOf(TranKeyWord[i][0])!=-1){
//					return true;
//				}
				if(TranKeyWord[i][1].equalsIgnoreCase("0") && selpath.startsWith(TranKeyWord[i][0])){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否是非通过关键字
	 * @param selpath
	 * @return
	 */
	public static synchronized boolean isNoPassTranKeyWord(String selpath){
		if(selpath!=null && selpath.length()>0){
			for(int i=0;i<TranKeyWord.length;i++){
//				if(TranKeyWord[i][1].equalsIgnoreCase("1") && selpath.indexOf(TranKeyWord[i][0])!=-1){
//					return true;
//				}
				if(TranKeyWord[i][1].equalsIgnoreCase("1") && selpath.startsWith(TranKeyWord[i][0])){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否是非通过关键字
	 * @param selpath
	 * @return
	 */
	public static synchronized boolean isNoPassTranKeyWord(String[] selpath){
		if(selpath!=null && selpath.length>0){
			for(int j=0;j<selpath.length;j++){
				for(int i=0;i<TranKeyWord.length;i++){
	//				if(TranKeyWord[i][1].equalsIgnoreCase("1") && selpath.indexOf(TranKeyWord[i][0])!=-1){
	//					return true;
	//				}
					if(TranKeyWord[i][1].equalsIgnoreCase("1") && selpath[j].startsWith(TranKeyWord[i][0])){
						return true;
					}
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否是通过结束结点
	 * @param selpath
	 * @return
	 */
	public static synchronized boolean isPassEndNodeKeyWord(String key){
		if(key!=null && key.length()>0){
			for(int i=0;i<TranKeyWord.length;i++){
				if(TranKeyWord[i][1].equalsIgnoreCase("2") && key.indexOf(TranKeyWord[i][0])!=-1 && TranKeyWord[i][2].equalsIgnoreCase("1")){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否是非通过结束结点
	 * @param selpath
	 * @return
	 */
	public static synchronized boolean isNoPassEndNodeKeyWord(String key){
		if(key!=null && key.length()>0){
			for(int i=0;i<TranKeyWord.length;i++){
				if(TranKeyWord[i][1].equalsIgnoreCase("2") && key.indexOf(TranKeyWord[i][0])!=-1 && TranKeyWord[i][2].equalsIgnoreCase("0")){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}

}
