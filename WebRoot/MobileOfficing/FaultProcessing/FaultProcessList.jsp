<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<html:form action="/FaultProcessSearchAction.do">
<html:hidden property="property(genReport)" styleId="genReport"/>
	<table>
		<tr>
			<td>报修人:</td>
			<td><html:text property="property(repairName)" styleClass="default_input"/></td>
			<td>&nbsp;&nbsp;报修人电话:</td>
			<td><html:text property="property(repairPhone)" styleClass="default_input"/></td>
			<td>&nbsp;&nbsp;项目名称:</td>
			<td><html:text property="property(projectName)" styleClass="default_input" size="40"/></td>
		</tr>
		<tr>
			<td>是否困人:</td>
			<td>
				<html:select property="property(isTiring)">
					<html:option value="%">请选择</html:option>
					<html:option value="Y">是</html:option>
					<html:option value="N">否</html:option>
				</html:select>
			</td>
			<td>&nbsp;&nbsp;是否处理:</td>
			<td> 
				<html:select property="property(isProcess)" >
					<html:option value="%">请选择</html:option>
					<html:option value="Y">是</html:option>
					<html:option value="N">否</html:option>
				</html:select>
			</td>
			<td>&nbsp;&nbsp;项目地址:</td>
			<td><html:text property="property(projectAddress)" styleClass="default_input" size="40"/></td>
		</tr>
	</table>
	<br>
 	<table:table id="guiFaultProcess" name="faultList">
 		<logic:iterate id="ele" name="faultList">
 			<table:define id="c_cb">
 				<html:radio property="checkBoxList(ids)" styleId="ids" value="${ele.appNo }" />       		
 			</table:define>
 			
 			<table:define id="c_repairName">
 				<a href="<html:rewrite page='/FaultProcessAction.do'/>?method=toPrepareUpdateRecord&id=<bean:write name="ele"  property="appNo"/>"> ${ele.repairName }</a>
 			</table:define>
 			
 			<table:define id="c_repairPhone">
 				<logic:equal name="ele" property="isTiring2" value="Y">
 					<font color="red">${ele.repairPhone }</font>
 				</logic:equal>
 				<logic:notEqual name="ele" property="isTiring2" value="Y">
 					${ele.repairPhone }
 				</logic:notEqual>
 			</table:define>
 			
 			<table:define id="c_projectName">
  				<logic:equal name="ele" property="isTiring2" value="Y">
 					<font color="red"> ${ele.projectName }</font>
 				</logic:equal>
 				<logic:notEqual name="ele" property="isTiring2" value="Y">
 					${ele.projectName }
 				</logic:notEqual>
 			</table:define>
 			
 			<table:define id="c_projectAddress">
  				<logic:equal name="ele" property="isTiring2" value="Y">
 					<font color="red"> ${ele.projectAddress }</font>
 				</logic:equal>
 				<logic:notEqual name="ele" property="isTiring2" value="Y">
  					${ele.projectAddress }
 				</logic:notEqual>
 			</table:define>
 			
 			<table:define id="c_isTiring">
				<logic:equal name="ele" property="isTiring" value="Y">
 					<logic:equal name="ele" property="isTiring2" value="Y">
 						<font color="red"> 是</font>
 					</logic:equal>
 					<logic:notEqual name="ele" property="isTiring2" value="Y">
 						是
 					</logic:notEqual>
				</logic:equal> 
				<logic:equal name="ele" property="isTiring" value="N">
 					<logic:equal name="ele" property="isTiring2" value="Y">
 						<font color="red"> 否</font>
 					</logic:equal>
 					<logic:notEqual name="ele" property="isTiring2" value="Y">
 						否
 					</logic:notEqual>
				</logic:equal>
 			</table:define>
 			
			<table:define id="c_operId">
				<logic:equal name="ele" property="isTiring2" value="Y">
 					<font color="red"> ${ele.operId }</font>
				</logic:equal>
				<logic:notEqual name="ele" property="isTiring2" value="Y">
  					${ele.operId }
 				</logic:notEqual>
 			</table:define>
 			
 			<table:define id="c_operDate">
 				<logic:equal name="ele" property="isTiring2" value="Y">
 					<font color="red">  ${ele.operDate }</font>
 				</logic:equal>
 				<logic:notEqual name="ele" property="isTiring2" value="Y">
   					${ele.operDate }
 				</logic:notEqual>
 			</table:define>
 			
 			<table:define id="c_isProcess">
				<logic:equal name="ele" property="isProcess" value="Y">
					<logic:equal name="ele" property="isTiring2" value="Y">
						<font color="red">  是</font>
	 				</logic:equal>
					<logic:notEqual name="ele" property="isTiring2" value="Y">
						是
					</logic:notEqual>
				</logic:equal> 
				<logic:notEqual name="ele" property="isProcess" value="Y">
					<logic:equal name="ele" property="isTiring2" value="Y">
	 					<font color="red"> 否</font>
						</logic:equal>
					<logic:notEqual name="ele" property="isTiring2" value="Y">
	  					否
					</logic:notEqual>
				</logic:notEqual>
			</table:define>			
			<table:tr/>			
		</logic:iterate>
 	</table:table>
</html:form>