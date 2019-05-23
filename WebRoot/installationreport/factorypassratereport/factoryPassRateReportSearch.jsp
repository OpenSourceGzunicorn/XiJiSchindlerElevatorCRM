
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/common/javascript/highcharts/highcharts.js"/>"></script>
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<br>
<html:form action="/factoryPassRateReportAction.do?method=toSearchResults">

    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
     <tr>
	    <td width="20%" class="wordtd">提交厂检时间:</td>
	    <td width="80%" class="inputtd">
	    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true});"/>
		- 
		<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})"/>
	    <html:hidden property="genReport" styleId="genReport" />
	    </td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">所属部门:<br/>
    	全选<input type="checkbox" name="departmentarr" onclick="isChangeVal(this);">
    	</td>
    	<td width="80%" class="inputtd">
    		<%-- 
    		<html:select property="department" styleId="department">
		    	<html:options collection="departmentList" property="comid" labelProperty="comfullname"/>
    		</html:select><font color="red">*</font>
    		--%>
    		<table id="searchCompany" style="border: 0;margin: 0;" class="tb">
       		<tr>
             <%int specialno=1; %>
       		<logic:present name="departmentList">
				  <logic:iterate id="element" name="departmentList">
						<td nowrap="nowrap"  style="border: none;" width="5%">
						&nbsp;
                         <input type="checkbox" name="department" value="${element.comid}">${element.comfullname }
                         </td>
					
						<% if(specialno%5==0){ %></tr><tr><%} %>
						<%specialno++; %>
				</logic:iterate>
				</tr>
			</logic:present>
       	</table>
    	</td>
    </tr>
    <tr>
      <td width="20%" class="wordtd">项目省份:</td>
	    <td width="80%" class="inputtd">
        <input type="text" name="projectprovince" id="projectprovince" class="default_input"/>
      </td>
    </tr>
	<tr>
		<td width="20%" class="wordtd">是否汇总:</td>
		<td width="80%" class="inputtd">
			<select name="ishuizong">
				<option value="Y">是</option>
				<option value="N">否</option>
			</select>
		</td>
	</tr>
    </table>
</html:form>



