<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<html:form action="/customerVisitPlanAction.do?method=toUpdateRecord">
	<html:hidden property="isreturn" />
	<html:hidden property="id" value="${customerVisitPlanDetailBean.jnlno}" />
	<table width="100%" class="tb">
		<tr>
			<td class="wordtd"><bean:message
					key="customerVisitPlan.customerlevel" /></td>
			   
			    <input name="companyId" type="hidden" value="null"/>
			<td><bean:write name="cvpm" property="custLevel"></bean:write><html:hidden
					name="customerVisitPlanDetailBean" property="jnlno" /></td>

			<td class="wordtd"><bean:message key="customerlevel.companyName" /></td>
			<td><bean:write name="cvpm" property="companyName"></bean:write>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message
					key="customerlevel.principalName" /></td>
			<td><bean:write name="cvpm" property="principalName"></bean:write></td>
			<td class="wordtd"><bean:message
					key="customerlevel.principalPhone" /></td>
			<td><bean:write name="cvpm" property="principalPhone"></bean:write></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message
					key="customerlevel.maintDivision" /></td>
			<td>${comName}
			<html:hidden  name="cvpm" property="maintDivision" styleId="maintDivision"/></td>
			<td class="wordtd"><bean:message key="customerlevel.mainStation" /></td>
			<td>${storageName}
			<html:hidden  name="cvpm" property="maintStation" styleId="assignedMainStation"/></td>
		</tr>
		</table>
		<br/>
		<table width="100%" class="tb" id="jobHistory" >
          <tr id="titleRow_0"  class="wordtd" >
              <td style="text-align: center;">拜访日期<font color="red">*</font></td>
              <td style="text-align: center;">拜访人员<font color="red">*</font></td>
              <td style="text-align: center;">拜访人员<font color="red">*</font></td>
              <td style="text-align: center;">备注</td>
            </tr>
            <tr id="tr_0">                
              <td align="center">
              <input type="hidden" name="cb1" id="cb1"/>
                <input type="text" name="visitDate" id="visitDate" value='<bean:write name="customerVisitPlanDetailBean" property="visitDate"/>' size="20" class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
              </td>
               <td align="center">
              <html:select property="visitPosition" onchange="change(this,'visitStaff_0')">
              <html:option value="">请选择</html:option>
              <html:options collection="class1List" property="class1id" labelProperty="class1name"  />
              </html:select></td>
              <td align="center">
              <html:select property="visitStaff" styleId="visitStaff_0">
              <html:option value="%">请选择</html:option>
              </html:select></td>
              <td align="center"><html:text name="customerVisitPlanDetailBean" property="rem" size="50" ></html:text></td>
            </tr>   
	</table>
</html:form>