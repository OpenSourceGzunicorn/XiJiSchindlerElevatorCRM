<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<br>
<html:form action="/ServeTable.do">
  <table>
    <tr>
    <td>
                       员工代码：
      </td>
      <td>
        <html:text property="property(ygid)" styleClass="default_input" />
      </td>
      <td>
      &nbsp;&nbsp;员工名称：
      </td>
      <td>
        <html:text property="property(name)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;<bean:message key="personnelManage.contractNo" />：
      </td>
      <td>
        <html:text property="property(contractNo)" styleClass="default_input" />
      </td>
      <%-- td>
        &nbsp;&nbsp;<bean:message key="personnelManage.startDate" />：
        <html:text property="property(startDates)" size="15" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
        	到
        <html:text property="property(startDatee)" size="15" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
      </td--%>  
      </tr>
      <tr>
    	<td>所属分部：</td>
    	<td>
    		<html:select property="property(maintdivision)" styleId="maintdivision" onchange="Evenmore(this,'maintstation')">
		    	<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
    		</html:select>
    	</td>
    	<td>&nbsp;&nbsp;所属部门：</td>
    	<td>
    		<html:select property="property(maintstation)" styleId="maintstation">
    			<%-- html:option value="">全部</html:option--%>
		    	<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
    		</html:select>
    	</td>
      <td>
        &nbsp;&nbsp;<bean:message key="principal.enabledFlag" />：
      </td>
      <td>
        <html:select property="property(enabledFlag)">
          <html:option value="">
            <bean:message key="pageword.all" />
          </html:option>
          <html:option value="Y">
            <bean:message key="pageword.yes" />
          </html:option>
          <html:option value="N">
            <bean:message key="pageword.no" />
          </html:option>
        </html:select>
      </td>
    </tr>
      <html:hidden property="property(genReport)" styleId="genReport" />
      <html:hidden property="property(exceltype)" styleId="exceltype" />
    </tr>
  </table>
  <br>
  <table:table id="guiPersonnelManage" name="personnelManageList">
    <logic:iterate id="element" name="personnelManageList">
      <table:define id="c_cb">
        <bean:define id="billno" name="element" property="billno" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno.toString()%>" />
      </table:define>
      <table:define id="c_ygid">
        <bean:write name="element" property="ygid" />
      </table:define>
      <table:define id="c_name">
        <a href="<html:rewrite page='/personnelManageAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
        <bean:write name="element" property="name" />
        </a>
      </table:define>
      <table:define id="c_sex">
        <bean:write name="element" property="sex" />
      </table:define>
      <table:define id="c_contractNo">
        <bean:write name="element" property="contractNo" />
      </table:define>
      <table:define id="c_education">
        <bean:write name="element" property="education" />
      </table:define>
      <table:define id="c_idCardNo">
        <bean:write name="element" property="idCardNo" />
      </table:define>
      <table:define id="c_familyAddress">
        <bean:write name="element" property="familyAddress" />
      </table:define>
      <table:define id="c_phone">
        <bean:write name="element" property="phone" />
      </table:define>
      <table:define id="c_startDate">
        <bean:write name="element" property="startDate" />
      </table:define>
      <table:define id="c_phone">
        <bean:write name="element" property="phone" />
      </table:define>
      <table:define id="c_startDate">
        <bean:write name="element" property="startDate" />
      </table:define>
      <table:define id="c_sector">
        <bean:write name="element" property="sector" />
      </table:define>
      <table:define id="c_maintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:define id="c_r1">
        <bean:write name="element" property="r1" />
      </table:define>
      <table:define id="c_r2">
        <bean:write name="element" property="r2" />
      </table:define>
      <table:define id="c_r3">
        <bean:write name="element" property="r3" />
      </table:define>
      <table:define id="c_enabledFlag">
        <logic:match name="element" property="enabledFlag" value="Y">
          <bean:message key="pageword.yes" />
        </logic:match>
        <logic:match name="element" property="enabledFlag" value="N">
          <bean:message key="pageword.no" />
        </logic:match>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>