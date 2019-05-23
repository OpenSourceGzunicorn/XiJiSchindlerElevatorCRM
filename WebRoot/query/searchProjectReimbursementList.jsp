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
<html:hidden property="property(reimbursPeople)" />
<html:hidden property="property(billnostr)" />
	<table>
		<tr>
			<td>
				流水号
				:
			</td>
			<td>
				<html:text property="property(billno)" styleClass="default_input" />
			</td>
			<td>
			&nbsp;&nbsp;
				合同号
				:
			</td>
			<td>
				<html:text property="property(maintContractNo)" styleClass="default_input" />
			</td>
			
	<td>
	&nbsp;&nbsp;
		合同类型
		:
	</td>
	<td>
		<html:select property="property(busType)">
			<html:option value="">--请选择--</html:option>
			<html:option value="B">保养</html:option>
			<html:option value="W">维修</html:option>
			<html:option value="G">改造</html:option>
		</html:select>
	</td>
			</tr>
			<tr>
			    <td>
        维保分部
        :
      </td>
      <td>
        <html:select property="property(comid)">
        <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>
   <td>
&nbsp;&nbsp;
        维保站
        :
      </td>
      <td>
        <html:text property="property(storage)" styleClass="default_input" />
      </td>
      
      <td>
&nbsp;&nbsp;
       是否参与合同
        :
      </td>
      <td>
        <html:select property="property(isjoin)">
        <html:option value="%">全部</html:option>
        <html:option value="Y">是</html:option>
        <html:option value="N">否</html:option>
        </html:select>
      </td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
		<tr>
		<td>     
       	 销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>  
      <td>  
        &nbsp;&nbsp;     
       	 项目名称
        :
      </td>
      <td colspan="3">
        <html:text property="property(projectName)" styleClass="default_input" size="50"/>
      </td>  
		</tr>
	</table>
	<br>
	<table:table id="guiProjectReimbursement" name="searchProjectReimbursementList">
		<logic:iterate id="element" name="searchProjectReimbursementList">
			<table:define id="c_cb">
			<bean:define id="billno" name="element" property="billno" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=billno .toString()%>" />
				<html:hidden name="element" property="billno" styleId="billno" />
				<html:hidden name="element" property="maintContractNo" styleId="maintContractNo"/>
				<html:hidden name="element" property="busType" styleId="busType"/> 
				<html:hidden name="element" property="num" styleId="num"/>
				<html:hidden name="element" property="maintDivision" styleId="maintDivision"/>
				<html:hidden name="element" property="comName" styleId="comNameprb"/>
				<html:hidden name="element" property="maintStation" styleId="maintStation"/>
				<html:hidden name="element" property="storageName" styleId="storageNameprb"/>
			</table:define>
			<table:define id="c_billno">
				<bean:write name="element" property="billno" />
			</table:define>
			<table:define id="c_maintContractNo">
				<bean:write name="element" property="maintContractNo" />
			</table:define>
			<table:define id="c_busType">
				<logic:match name="element" property="busType" value="B">保养</logic:match>
				<logic:match name="element" property="busType" value="W">维修</logic:match>
				<logic:match name="element" property="busType" value="G">改造</logic:match>
			</table:define>
			<table:define id="c_num">
				<bean:write name="element" property="num" />
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="comName" />
			</table:define>				
			<table:define id="c_maintStation">
				<bean:write name="element" property="storageName" />
			</table:define>			
			<table:define id="c_contractStatus">
				<bean:write name="element" property="contractStatus" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>