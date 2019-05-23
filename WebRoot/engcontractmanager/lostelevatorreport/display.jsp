<%@ page contentType="text/html;charset=GBK" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>

<logic:present name="lostElevatorReportBean">
<html:hidden name="lostElevatorReportBean" property="jnlno"/>
<html:hidden name="lostElevatorReportBean" property="billNo"/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
    <td nowrap="nowrap" height="23" colspan="4">&nbsp;<b>丢梯报告基础信息</td>
  </tr>
  <tr>
    <td class="wordtd">所属分部:</td>
    <td width=30%" nowrap="nowrap">
    	${lostElevatorReportBean.maintDivision}
    </td>
    <td class="wordtd">所属维保站:</td>
    <td nowrap="nowrap" width="30%">
    	${lostElevatorReportBean.maintStation}
    </td>
  </tr>
  <tr>    
    <td class="wordtd">维保合同号:</td>
    <td>
    	
    	<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="lostElevatorReportBean"  property="billNo"/>" target="_blnk">${lostElevatorReportBean.maintContractNo}</a>
	 </td>
    <td nowrap="nowrap" class="wordtd">项目名称:</td>
    <td>
    	${lostElevatorReportBean.projectName}
    </td>   
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">台量:</td>
    <td>
    	${lostElevatorReportBean.eleNum}
    </td>   
    <td nowrap="nowrap" class="wordtd">丢梯日期:</td>
    <td>
    	${lostElevatorReportBean.lostElevatorDate}
    </td>          
  </tr>
  <tr>      
    <td nowrap="nowrap" class="wordtd">合同类别:</td>
    <td>
    	${lostElevatorReportBean.contractNatureOf}
    </td>          
    <td nowrap="nowrap" class="wordtd">原因分析:</td>
    <td>
    	${lostElevatorReportBean.causeAnalysis}
    </td>   
  </tr>
  <tr>      
    <td class="wordtd">使用单位联系人:</td>
    <td>
    	${lostElevatorReportBean.contacts}
    </td>          
    <td nowrap="nowrap" class="wordtd">联系电话:</td>
    <td>
    	${lostElevatorReportBean.contactPhone}
    </td>   
  </tr>
  <tr>      
    <td class="wordtd">备注详细原因:</td>
    <td colspan="3">
    	${lostElevatorReportBean.detailedRem}
    </td>          
  </tr>
  <tr>      
    <td nowrap="nowrap" class="wordtd">录入人:</td>
    <td>
    	${lostElevatorReportBean.operId}
    </td>         
    <td nowrap="nowrap" class="wordtd">录入日期:</td>
    <td>
    	${lostElevatorReportBean.operDate}
    </td>   
  </tr>
</table>

<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" height="23" colspan="2">&nbsp;<b>其它需增加信息</b></td>        
  </tr>
  <tr>
    <td width="20%" class="wordtd">竞争单位名称:</td>
    <td nowrap="nowrap" width="75%" class="isred">
    	${lostElevatorReportBean.competeCompany}
    </td> 
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd">恢复计划:</td>
    <td nowrap="nowrap" class="isred">
    	${lostElevatorReportBean.recoveryPlan}
    </td>                  
  </tr>
  </table>
  
<!-- 已上传的附件 -->
<%@ include file="UpLoadFileDisplay.jsp" %>

  <script language="javascript">
  	/* function isRed2(cause,company,plan){
  		if(cause!="客户自行维保" && cause!="销售停梯" && cause!="主动放弃" && company=="" && plan==""){
  			$(".isred").css("background-color","red");
  		}
  	}
  	$(document).ready(function() {
  		isRed2('${lostElevatorReportBean.causeAnalysis}','${lostElevatorReportBean.competeCompany}','${lostElevatorReportBean.recoveryPlan}')
	}) */
  </script>
</logic:present>