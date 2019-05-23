<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="authority" value="elevatortransfercaseregisteraudit"/>
<input type="hidden" name="flowname" id="flowname" value="${flowname}"/>
<a href="" id="avf" target="_blank"></a>
   <table border="0">
    <tr>
      <td>    
      &nbsp;&nbsp;  
        流水号
        :
      </td>
      <td>
        <html:text property="property(billno)" styleClass="default_input" />
      </td>                 
      <td> 
      &nbsp;&nbsp;    
        销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input"/>
      </td> 
    <td>       
     &nbsp;&nbsp;
     电梯编号
        :
      </td>
      <td>
         <html:text property="property(elevatorNo)" styleClass="default_input"/>
      </td> 
      
      <td>      
      &nbsp;&nbsp;
        厂检人员名称
        :
      </td>
      <td>
        <html:text property="property(staffName)" styleClass="default_input" />
      </td>
                
    </tr>
    <tr>   
      <td> 
        &nbsp;&nbsp;      
        所属部门
        :
      </td>
      <td>
        <html:select property="property(department)">
        	<html:option value="">全部</html:option>
		    <html:options collection="departmentList" property="comid" labelProperty="comfullname"/>
        </html:select>
      </td>     
      <td>
        &nbsp;&nbsp;               
        项目名称
        :
      </td>
      <td colspan="3">
        <html:text property="property(projectName)" styleClass="default_input" size="56" />
      </td> 
      <td>&nbsp;&nbsp;
       	厂检结果：</td>
        <td>
        	<html:select property="property(factoryCheckResult)">
        		<html:option value="">全部</html:option>
        		<html:option value="合格">合格</html:option>
        		<html:option value="不合格">不合格</html:option>
        	</html:select>
        </td>
    </tr>
    <tr>     
      <td>    
      &nbsp;&nbsp;   
                    提交标志
        :
      </td>
      <td>
        <html:select property="property(submitType)">
          <html:option value="">全部</html:option>
		  <html:option value="Y">已提交</html:option>
		  <html:option value="N">未提交</html:option>
		  <html:option value="Z">转派</html:option>
		  <html:option value="R">驳回</html:option>
        </html:select>
      </td> 
   <td>       
     &nbsp;&nbsp;
     安装公司名称
        :
      </td>
      <td colspan="3">
         <html:text property="property(insCompanyName)" styleClass="default_input" size="56" />
      </td>
      <td>&nbsp;&nbsp;
      	厂检版本:</td>
       <td>
       	<html:select property="property(checkVersion)">
       		<html:option value="">全部</html:option>
       		<html:option value="Y">最新版本</html:option>
       		<html:option value="N">历史版本</html:option>
       	</html:select>
       </td>
    </tr>
    <tr>
    	<td>
    		&nbsp;&nbsp;处理状态:
    	</td>
    	<td>
    		<html:select property="property(processStatus)">
	       		<html:option value="">全部</html:option>
	       		<html:option value="0">未接收</html:option>
	       		<html:option value="1">已登记未提交</html:option>
	       		<html:option value="2">已登记已提交</html:option>
	       		<html:option value="3">已审核</html:option>
	       		<html:option value="4">已接收</html:option>
       		</html:select>
    	</td>                   
      <td>                 
     	  &nbsp;&nbsp;流程状态:
      </td>
      <td>
        <html:select property="property(status)">
          <html:option value="">全部</html:option>
		  <html:options collection="processStatusList" property="typeid" labelProperty="typename"/>
        </html:select>
      </td>   
    </tr>
  </table>  
	
	<br>
	<table:table id="guiElevatorTransferCaseRegister" name="elevatorTransferCaseRegisterAuditList">
    <logic:iterate id="element" name="elevatorTransferCaseRegisterAuditList">
      <table:define id="c_cb">
        <bean:define id="billno" name="element" property="billno" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billno}" />
        <html:hidden name="element" property="submitType" />
        <html:hidden name="element" property="isClose" />
      </table:define>
      <table:define id="c_billno">
        <a href="<html:rewrite page='/elevatorTransferCaseRegisterAuditAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
          <bean:write name="element" property="billno" />
        </a>
      </table:define>
      <table:define id="c_checkTime">
        <bean:write name="element" property="checkTime" />
        <html:hidden name="element" property="tokenId"/>       
      </table:define>
      <table:define id="c_checkNum">
        <bean:write name="element" property="checkNum" />
      </table:define>
      <table:define id="c_projectName">
        <bean:write name="element" property="projectName" />
      </table:define>
      <table:define id="c_insCompanyName">
        <bean:write name="element" property="insCompanyName" />
      </table:define>
      <table:define id="c_salesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
       <table:define id="c_elevatorNo">
        <bean:write name="element" property="elevatorNo" />
      </table:define>   
      <table:define id="c_staffName">
        <bean:write name="element" property="staffName" />
      </table:define>
      <table:define id="c_department">
        <bean:write name="element" property="department" />
      </table:define>
      <table:define id="c_factoryCheckResult">
        <bean:write name="element" property="factoryCheckResult" />
      </table:define>
      <table:define id="c_status">
        <bean:write name="element" property="r2" />    
        <html:hidden name="element" property="status"/>
      </table:define>
        <table:define id="c_processName">
        <bean:write name="element" property="processName" />    
      </table:define>  
      <table:define id="c_submitType">
        <logic:match name="element" property="submitType" value="Y">已提交</logic:match>
        <logic:match name="element" property="submitType" value="N">未提交</logic:match>
        <logic:match name="element" property="submitType" value="R">驳回</logic:match>
        <logic:match name="element" property="submitType" value="Z">转派</logic:match>
      </table:define>
      <table:define id="c_processStatus">
        <html:hidden name="element" property="processStatus"/>
        <logic:match name="element" property="processStatus" value="0">未接收</logic:match>
        <logic:match name="element" property="processStatus" value="4">已接收</logic:match>
        <logic:match name="element" property="processStatus" value="1">已登记未提交</logic:match>
        <logic:match name="element" property="processStatus" value="2">已登记已提交</logic:match>
        <logic:match name="element" property="processStatus" value="3">已审核</logic:match>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>