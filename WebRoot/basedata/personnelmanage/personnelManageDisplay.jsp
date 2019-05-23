<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<br>
<html:errors/>
<logic:present name="personnelManageBean">
  <table width="100%" height="24" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr class="td_2">
      <td width="2" background="/images/line.gif"></td>
      <td width="100" class="tab-on" id="navcell" onclick="switchCell('0')" >员工档案</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="1" height="25"></td>
        
      <td width="100" class="tab-off" id="navcell" onclick="switchCell('1')">岗位历史</td>  
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="1" height="25"></td>

      <td width="100" class="tab-off" id="navcell" onclick="switchCell('2')">培训档案</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="1" height="25"></td>
      
      <td width="100" class="tab-off" id="navcell" onclick="switchCell('3')">证书档案</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="1" height="25"></td>
      
      <td width="100" class="tab-off" id="navcell" onclick="switchCell('4')">工具领取档案</td>
      <td width="60%" background="/images/line_2.gif">&nbsp;</td>

    </tr>
  </table>
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">  
    <tr id="tb">
      <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
      <td bgcolor="#ffffff">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
          <br>
          <tr>
            <td class="wordtd">员工代码:</td>
            <td width="35%"><bean:write name="personnelManageBean" property="ygid"/></td>
            <td class="wordtd">员工名称:</td>
            <td><bean:write name="personnelManageBean" property="name"/></td>
          </tr>
          <tr>
            <td class="wordtd"><bean:message key="personnelManage.sex"/>:</td>
            <td><bean:write name="personnelManageBean" property="sex"/></td>
            <td class="wordtd"><bean:message key="personnelManage.idCardNo"/>:</td>
            <td><bean:write name="personnelManageBean" property="idCardNo"/></td>
          </tr>  
          <tr>    
              <td class="wordtd"><bean:message key="personnelManage.education"/>:</td>
            <td><bean:write name="personnelManageBean" property="education"/></td>
          	<td class="wordtd"><bean:message key="personnelManage.phone"/>:</td>
            <td><bean:write name="personnelManageBean" property="phone"/></td>
          </tr>  
          <tr> 
          	<td class="wordtd"><bean:message key="personnelManage.familyAddress"/>:</td>
            <td ><bean:write name="personnelManageBean" property="familyAddress"/></td>
          	<td class="wordtd">出生日期:</td>
            <td><bean:write name="personnelManageBean" property="birthDate" /></td>
          </tr>  
          <tr>
            <td class="wordtd">备用电话:</td>
            <td><bean:write name="personnelManageBean" property="phone2"/></td>
            <td class="wordtd"><bean:message key="personnelManage.contractNo"/>:</td>
            <td><bean:write name="personnelManageBean" property="contractNo"/></td>
          </tr>  
          <tr>    
            <td class="wordtd"><bean:message key="personnelManage.startDate"/>:</td>
            <td><bean:write name="personnelManageBean" property="startDate" /></td>
          <td class="wordtd"><bean:message key="personnelManage.years"/>:</td>
            <td><bean:write name="personnelManageBean" property="years"/></td>
          </tr>  
          <tr>    
            <td class="wordtd"><bean:message key="personnelManage.endDate"/>:</td>
            <td><bean:write name="personnelManageBean" property="endDate" /></td>
            <td class="wordtd"><bean:message key="personnelManage.workAddress"/>:</td>
            <td><bean:write name="personnelManageBean" property="workAddress"/></td>
          </tr>  
          <tr>
            <td class="wordtd">所属分部:</td>
            <td><bean:write name="personnelManageBean" property="sector"/></td>
            <td class="wordtd">所属部门:</td>
            <td><bean:write name="personnelManageBean" property="maintStation"/></td>
          </tr>  
          <tr>
            <td class="wordtd">户别:</td>
            <td><bean:write name="personnelManageBean" property="hubie"/></td>
             <td class="wordtd">是否驻外:</td>
            <td><bean:write name="personnelManageBean" property="iszhuwai"/></td>
         </tr>
          
          <tr>
           	<td class="wordtd">岗位级别:</td>
            <td><bean:write name="personnelManageBean" property="jobLeval"/></td>
         	<td class="wordtd">带位级别:</td>
            <td><bean:write name="personnelManageBean" property="level"/></td>
          </tr> 
          <tr>
            <td class="wordtd">试用期:</td>
            <td><bean:write name="personnelManageBean" property="probation"/></td>
            <td class="wordtd">试用期工资:</td>
            <td><bean:write name="personnelManageBean" property="probationgz" /></td>
          </tr>
          
          <tr>
          	<td class="wordtd">试用结束日期:</td>
            <td><bean:write name="personnelManageBean" property="probationEdate"/></td>
          	
          	<td class="wordtd">领取安全帽日期:</td>
            <td><bean:write name="personnelManageBean" property="r3"/></td>
	            
          </tr> 
          
          <tr>
            <td class="wordtd">劳保鞋:</td>
            <td><bean:write name="personnelManageBean" property="r1"/></td>
            <td class="wordtd">工装号:</td>
            <td><bean:write name="personnelManageBean" property="r2"/></td>
          </tr>
          
          <tr>
            <td class="wordtd"><bean:message key="personnelManage.rem"/>:</td>
            <td ><bean:write name="personnelManageBean" property="rem"/></td>
            <td class="wordtd"><bean:message key="personnelManage.enabledFlag"/>:</td>
            <td>  
              	<logic:equal name="personnelManageBean" property="enabledFlag" value="Y"><bean:message key="pageword.yes"/></logic:equal>
            	<logic:equal name="personnelManageBean" property="enabledFlag" value="N"><bean:message key="pageword.no"/></logic:equal>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    
      <!-- 岗位历史 -->
      <tr id="tb" style="display:none;" >
        <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;" >&nbsp;</td>
        <td bgcolor="#ffffff" >
          <br> 
          <table id="jobHistory" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">          
            <tr id="titleRow_1" class="td_3_1">
              <td>在岗时间段</td>
              <td>工作地址</td>
              <td>岗位名称</td>
              <td>级别</td>
              <td>调整日期</td>
            </tr>
            <logic:iterate id="element" name="jobHistoryList" >
	            <tr id="tr_0">
	              <td align="center">
	               	<bean:write name="element" property="sdate"/>
	                -
	                <bean:write name="element" property="edate"/>
	              </td>
	              <td align="center"><bean:write name="element"  property="workAddress" /></td>
	              <td align="center"><bean:write name="element"  property="jobTitle" /></td>
	              <td align="center"><bean:write name="element"  property="level" /></td>
	              <td align="center"><bean:write name="element"  property="revisionDate" /></td>
	            </tr>
            </logic:iterate> 
          </table>               
        </td>       
      </tr>
      
      <!-- 培训档案 -->
      <tr id="tb" style="display:none;">
        <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;" >&nbsp;</td>
        <td bgcolor="#ffffff">
          <br>     
          <table id="trainingHistory" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">          
            <tr id="titleRow_2" class="td_3_1">
              <td>培训日期</td>
              <td>培训课程</td>
              <td>课时</td>
              <td>理论成绩</td>
              <td>实操成绩</td>
              <td>表现成绩</td>
              <td>培训评估/备注</td>
              <td>总分</td>
              <td>等级</td>
            </tr>
            <logic:iterate id="element2" name="trainingHistoryList" >
            <tr id="tr_1">
              <td align="center">
              	<bean:write name="element2" property="straDate"/>
                -
                <bean:write name="element2" property="etraDate"/>
              </td>
              <td align="center">
              	<bean:write name="element2" property="traCourse"/>
              </td>
              <td align="center"><bean:write name="element2" property="lesson"/></td>
              <td align="center"><bean:write name="element2" property="theoreticalResults"/></td>
              <td align="center"><bean:write name="element2" property="practicalResults"/></td>
              <td align="center"><bean:write name="element2" property="perforResults"/></td>            
              <td align="center"><bean:write name="element2" property="traAssess"/></td>
              <td align="center"><bean:write name="element2" property="totalScore"/></td>
              <td align="center"><bean:write name="element2" property="rating" /></td>            
            </tr>
            </logic:iterate> 
          </table>     
        </td>
      </tr>
      
      <!-- 证书档案 -->
      <tr id="tb" style="display:none;">
        <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;" >&nbsp;</td>
        <td bgcolor="#ffffff">
          <br>         
         <table id="certificateExam" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">          
            <tr id="titleRow_3" class="td_3_1">
              <td>证书号</td>
              <td>证书名称</td>
              <td>证书性质</td>
              <td>开始日期</td>
              <td>失效日期</td>
              <td>发证机关</td>
              <td nowrap="nowrap">是否报销</td>
              <td nowrap="nowrap">是否扣费</td>
              <td>证书去向</td>
              <td>备注</td>
            </tr>
            <logic:iterate id="element3" name="certificateExamList" >
            <tr id="tr_2">
              <td align="center"><bean:write name="element3" property="certificateNo" /></td>
              <td align="center"><bean:write name="element3" property="certificateName" /></td>
              <td align="center"><bean:write name="element3" property="certificateProperty" /></td>
              <td align="center"><bean:write name="element3" property="startDate" /></td>
              <td align="center"><bean:write name="element3" property="endDate" /></td>                    
             
              <td align="center"><bean:write name="element3" property="issuingAuthority"/></td>
              <td align="center">
              		<bean:write name="element3" property="isExpense"/>
              	    <!--<logic:equal name="element3" property="isExpense" value="Y"><bean:message key="pageword.yes"/></logic:equal>
           			<logic:equal name="element3" property="isExpense" value="N"><bean:message key="pageword.no"/></logic:equal>-->
              </td>
              <td align="center">
              		<bean:write name="element3" property="isCharging"/>
              		<!--<logic:equal name="element3" property="isCharging" value="Y"><bean:message key="pageword.yes"/></logic:equal>
            		<logic:equal name="element3" property="isCharging" value="N"><bean:message key="pageword.no"/></logic:equal>-->
              <td align="center">
              		<bean:write name="element3" property="certificateExt"/>
              </td>
              <td align="center">
					<bean:write name="element3" property="rem" /> 
			  </td>                    
            </tr>
            </logic:iterate>
          </table> 
        </td>
      </tr>
      
       <!-- 工具领取档案 -->
       <tr id="tb" style="display:none;">
        <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;" >&nbsp;</td>
        <td bgcolor="#ffffff">
          <br>
          <div>
	      	<font color="red">按申请人查询</font>&nbsp;
	      	<input type="text" id="searchval" name="searchval" value="" class="default_input"/>
	      	<input type="button"  value="搜索" onclick="toSearchOper()" class="default_input">
	      	&nbsp;&nbsp;<input type="button"  value="显示全部" onclick="showAll()" class="default_input">
	      </div>           
          <table id="toolReceive" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">
            <tr id="titleRow_4" class="td_3_1">
              <td nowrap="nowrap">工具编号</td>
              <td nowrap="nowrap">工具名称</td>
              <td nowrap="nowrap">工具型号</td>
              <td nowrap="nowrap">数量</td>
              <td nowrap="nowrap">申请人</td>
              <td nowrap="nowrap">领取日期</td>
              <td nowrap="nowrap">是否收费</td>
              <td nowrap="nowrap">是否清算</td>
              <td nowrap="nowrap">备注</td>
              <td nowrap="nowrap">附件</td>
            </tr>
            <logic:iterate id="element4" name="ToolReceiveList" >
            <tr id="tr_3">
              <td align="center"><bean:write name="element4" property="toolId"/></td>
              <td align="center" width="300px" style="text-align: center;word-wrap:break-word;"><bean:write name="element4" property="toolName" /></td>
              <td align="center"><bean:write name="element4" property="toolParam" /></td>
              <td align="center"><bean:write name="element4" property="toolnum" /></td>
              <td align="center"><bean:write name="element4" property="operName" /></td>
              <td align="center"><bean:write name="element4" property="operDate"/></td>                    
              <td align="center">
	              	<logic:equal name="element4" property="isCharge" value="Y">已收费</logic:equal>
            		<logic:equal name="element4" property="isCharge" value="N">未收费</logic:equal>
            		<logic:equal name="element4" property="isCharge" value="M">公司发放</logic:equal>
            		<logic:equal name="element4" property="isCharge" value="X">现场购买</logic:equal>
            	</td>
              <td align="center">
	              	<logic:equal name="element4" property="isLiquidation" value="Y"><bean:message key="pageword.yes"/></logic:equal>
            		<logic:equal name="element4" property="isLiquidation" value="N"><bean:message key="pageword.no"/></logic:equal>
              </td>
              <td align="center" width="200px" style="text-align: center;word-wrap:break-word;"><bean:write name="element4" property="rem" />              
            	<td align="center"><a href="javascript:downloadFile(${element4.numNo });">${element4.r2 }</a></td>
            </tr>
            </logic:iterate>
          </table> 
        </td>
      </tr>
	  
	</table>
</logic:present>

<script>
	//搜索方法
	function toSearchOper(){
		var searchval = document.getElementById("searchval").value;//要搜索的内容
		var tableid = document.getElementById("toolReceive");
		var rowlen = tableid.rows.length;
		
		for(var i = 1;i<rowlen;i++){
			var opername = tableid.rows[i].cells[4].innerHTML+"";
			if(opername.indexOf(searchval) == -1){
				tableid.rows[i].style.display="none";
			}else{
				tableid.rows[i].style.display="inline";
			}
		}
		
	}
	
	//显示全部
	function showAll(){
		var tableid = document.getElementById("toolReceive");
		var rowlen = tableid.rows.length;
		
		for(var i = 1;i<rowlen;i++){
			tableid.rows[i].style.display="inline";
		}
	}

</script>