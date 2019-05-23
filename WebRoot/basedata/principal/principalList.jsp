<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
  <table>
    <tr>
      <td>
        <bean:message key="principal.principalId" />
        :
      </td>
      <td>
        <html:text property="property(principalId)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        <bean:message key="principal.principalName" />
        :
      </td>
      <td>
        <html:text property="property(principalName)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        <bean:message key="principal.enabledFlag" />
        :
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
      <html:hidden property="property(genReport)" styleId="genReport" />
    </tr>
  </table>
  <br>
  <table:table id="guiPrincipal" name="principalList">
    <logic:iterate id="element" name="principalList">
      <table:define id="c_cb">
        <bean:define id="principalId" name="element" property="principalId" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=principalId.toString()%>" />
      </table:define>
      <table:define id="c_principalId">
        <a href="<html:rewrite page='/principalAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="principalId"/>">
        <bean:write name="element" property="principalId" />
        </a>
      </table:define>
      <table:define id="c_principalName">
        <bean:write name="element" property="principalName" />
      </table:define>
      <table:define id="c_phone">
        <bean:write name="element" property="phone" />
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