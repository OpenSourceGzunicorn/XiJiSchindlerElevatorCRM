<%@page import="com.gzunicorn.common.util.DateUtil"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<html:form action="/customerVisitPlanAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
<tr>
<td class="wordtd" ><bean:message key="customerVisitPlan.customerlevel"/></td>
<td><html:text property="custLevel" maxlength="255" styleId="orderby" styleClass="default_input" readonly="true"/>
<input type="button" value=".." onclick="openNewWindow('searchCustomerlevelAction','toSearchRecord')"/><font color="red">*</font>
<html:hidden property="companyId"  styleId="companyId" />
</td>

<td class="wordtd" ><bean:message key="customerlevel.companyName"/></td>
<td><html:text property="companyName" size="40" maxlength="255" styleId="companyName" styleClass="default_input_noborder" readonly="true"/></td>
</tr>

<tr>
<td class="wordtd" ><bean:message key="customerlevel.principalName"/></td>
<td><html:text property="principalName" maxlength="255" styleId="principalName" styleClass="default_input_noborder" readonly="true"/></td>
<td class="wordtd" ><bean:message key="customerlevel.principalPhone"/></td>
<td><html:text property="principalPhone" maxlength="255" styleId="principalPhone" styleClass="default_input_noborder" readonly="true"/></td>
</tr>

<tr>
<td class="wordtd" ><bean:message key="customerlevel.maintDivision"/></td>
<td><input id="maintDivisionName" class="default_input_noborder" readonly="true">
<html:hidden property="maintDivision" styleId="maintDivision"/></td>
<td class="wordtd" ><bean:message key="customerlevel.mainStation"/></td>
<td><input id="assignedMainStationName" class="default_input_noborder" readonly="true">
<html:hidden property="maintStation"  styleId="assignedMainStation" /></td>
</tr>

</table>

<br> 
          <input name="BtnAdd" type="Button" value="增 加" class="default_input" onclick="addonerows(jobHistory,0)"/> 
          <input name="BtnDel" type="button" value="删 除" class="default_input" onclick="deleteRow(jobHistory,0)"/> 
          <table id="jobHistory" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="margin-top:5">          
            <tr id="titleRow_0" class="wordtd" style="text-align: center;">
              <td><input type="checkbox" name="ckAll"  onclick="op(this,jobHistory)"/></td>
              <td>拜访日期<font color="red">*</font></td>
              <td>拜访职位<font color="red">*</font></td>
              <td>拜访人员<font color="red">*</font></td>
              <td>备注</td>
            </tr>
            <tr id="tr_0" >
              <td align="center">
                <input type="checkbox" name="cb1" id="cb1" onclick="opleft(this,jobHistory)"/>
              </td>
              <td align="center">
                <input type="text" name="visitDate" id="visitDate" value='<bean:write name="toToday"/>' size="20" class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
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
              <td align="center"><input type="text" name="rem" id="rem" size="50" class="default_input"/></td>
            </tr>
          </table>  
          
       <script type="text/javascript">
// 把所有id="tr_?"的列保存为模版
  var defaultrowobjs = new Array();
  var n = 0;
  while(document.getElementById("tr_"+n)!=null){
    n++;
  }
  for(var i=0;i<n;i++){
    defaultrowobjs[i]=new Array();  
    var row = document.getElementById("tr_"+i); 
    if(row != null){
      for(var j=0;j<row.cells.length;j++){      
        defaultrowobjs[i][j] = row.cells[j].innerHTML;  
      }
    
  //  row.parentNode.deleteRow(row.rowIndex); 
    }
  }
</script>   
          
</html:form>