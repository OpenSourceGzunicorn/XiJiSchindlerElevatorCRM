<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<html:form action="/maintContractAction.do?method=toExpireRecord">
	<html:hidden property="isreturn" />
	<html:hidden property="id" value="${id}"/>
	<table width="100%" class="tb">
		<tr>
			<td class="wordtd"><bean:message key="customerVisitPlan.customerlevel" /></td>
			<td>
				<bean:write name="cvpm" property="custLevel"></bean:write>
				<html:hidden name="cvpm" property="custLevel"/>
			</td>
			<td class="wordtd"><bean:message key="customerlevel.companyName" /></td>
			<td>
				<bean:write name="cvpm" property="companyName"></bean:write>
				<html:hidden name="cvpm" property="companyName"/>
				<html:hidden name="cvpm" property="companyId"/>
				<html:hidden name="cvpm" property="maintContractNo"/>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="customerlevel.principalName" /></td>
			<td>
				<bean:write name="cvpm" property="principalName"></bean:write>
				<html:hidden name="cvpm" property="principalName"/>
			</td>
			<td class="wordtd"><bean:message key="customerlevel.principalPhone" /></td>
			<td>
				<bean:write name="cvpm" property="principalPhone"></bean:write>
				<html:hidden name="cvpm" property="principalPhone"/>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="customerlevel.maintDivision" /></td>
			<td>
				<bean:write name="maintDivisionName"/>
				<html:hidden name="cvpm" property="maintDivision"/>
			</td>
			<td class="wordtd"><bean:message key="customerlevel.mainStation" /></td>
			<td>
				<bean:write name="maintStationName"/>
				<html:hidden name="cvpm" property="maintStation"/>
			</td>
		</tr>
		</table>
		<br/>
		<table width="100%" class="tb" id="jobHistory" >
          <tr id="titleRow_0"  class="wordtd" >
              <td style="text-align: center;">拜访日期<font color="red">*</font></td>
              <td style="text-align: center;">拜访职位<font color="red">*</font></td>
              <td style="text-align: center;">拜访人员<font color="red">*</font></td>
              <td style="text-align: center;">备注</td>
            </tr>
            <tr id="tr_0">                
              <td align="center">
              <input type="hidden" name="cb1" id="cb1"/>
                <html:text name="customerVisitPlanDetailBean" property="visitDate" styleId="visitDate" size="20" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
              </td>
              <td align="center">
             	 <html:select property="visitPosition" styleId="visitPosition" onchange="isshow(this)">
			          <html:option value="20">维保经理</html:option>
			          <html:option value="100">服务销售专员</html:option>
		        </html:select>
              </td>
              <td align="center">
              	<html:select property="visitStaff" styleId="visitStaff">
			         <html:option value=""></html:option>
		        </html:select>
              </td>
              <td><html:textarea name="customerVisitPlanDetailBean" property="rem" cols="80"></html:textarea></td>
            </tr>   
	</table>
</html:form>
<script language="javascript">

function isshow(obj){
	var objval=obj.value;
	var visitStaff=document.getElementById("visitStaff");
	visitStaff.length=0;
	
	<logic:present name="userlist">
		<logic:iterate name="userlist" id="ele2">
			var class1id='<bean:write name="ele2" property="class1id"/>';
			if(objval==class1id){
				visitStaff.add(new Option('<bean:write name="ele2" property="username"/>','<bean:write name="ele2" property="userid"/>'));
			}
		</logic:iterate>
    </logic:present>
}

var visitPosition=document.getElementById("visitPosition");
isshow(visitPosition);

</script>
