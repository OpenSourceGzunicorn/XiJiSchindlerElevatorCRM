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
<html:hidden property="property(genReport)" styleId="genReport" />
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
        所属分部
        :
      </td>
      <td>
        <html:select property="property(maintDivision)">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>      
    </tr>
  </table>
  <br>
  <table:table id="guiSalaryBonusManage" name="salaryBonusManageList">
    <logic:iterate id="element" name="salaryBonusManageList">
      <table:define id="c_cb">
        <bean:define id="billno" name="element" property="billno" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billno}" />
      </table:define>
      <table:define id="c_billno">
        <a href="<html:rewrite page='/salaryBonusManageAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
          <bean:write name="element" property="billno" />
        </a>
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_maintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:define id="c_costsDate">
        <bean:write name="element" property="costsDate" />
      </table:define>
      <table:define id="c_totalAmount">
        <bean:write name="element" property="totalAmount" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>