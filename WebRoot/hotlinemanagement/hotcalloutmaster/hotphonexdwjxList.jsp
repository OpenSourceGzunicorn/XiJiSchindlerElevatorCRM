<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
  <table width="100%" >
    <tr>
      <td>
        销售合同号:<html:text property="property(salesContractNo)" />
      </td>
      <td>
        电梯编号:<html:text property="property(elevatorNo)"  />
      </td>
       <td>
        项目名称:<html:text property="property(salesContractName)"   />
      </td>
    </tr>
    <tr>
    <td colspan="3">
        &nbsp;&nbsp;项目地址:<html:text property="property(projectAddress)" size="35"/>
    </td>
    </tr>
  </table>
  <br>
  <table:table id="guihotphone" name="MaintContractList">
    <logic:iterate id="element" name="MaintContractList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="123" />
        <input type="hidden" name="companyId" id="companyId" value="${element.companyId }"/>    
        <input type="hidden" name="companyName" id="companyName" value="${element.companyName }"/>  
        <input type="hidden" name="address" id="address" value="${element.address }"/>  
        <input type="hidden" name="salesContractNo" id="salesContractNo" value="${element.salesContractNo }"/>  
        <input type="hidden" name="elevatorNo" id="elevatorNo" value="${element.elevatorNo }"/>  
        <input type="hidden" name="elevatorParam" id="elevatorParam" value="${element.elevatorParam }"/>  
        <input type="hidden" name="storageId" id="storageId" value="${element.storageId }"/>  
        <input type="hidden" name="storageName" id="storageName" value="${element.storageName }"/>  
        <input type="hidden" name="maintPersonnel" id="maintPersonnel" value="${element.maintPersonnel }"/>  
        <input type="hidden" name="username" id="username" value="${element.username }"/>  
        <input type="hidden" name="phone" id="phone" value="${element.phone }"/>
        <input type="hidden" name="projectName" id="projectName" value="${element.salesContractName}"/>
      </table:define>
      <table:define id="c_salesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
      <table:define id="c_elevatorParam">
        <bean:write name="element" property="elevatorParam" />
      </table:define>
      <table:define id="c_elevatorNo">
        <bean:write name="element" property="elevatorNo" />
      </table:define>
      <table:define id="c_salesContractName">
        <bean:write name="element" property="salesContractName" />
      </table:define>
      <table:define id="c_companyId">
        <bean:write name="element" property="companyName" />
      </table:define>
      <table:define id="c_address">
        <bean:write name="element" property="address" />
      </table:define>
      <table:define id="c_storageId">
        <bean:write name="element" property="storageName" />
      </table:define>
      <table:define id="c_userid">
        <bean:write name="element" property="username" />
      </table:define>
      <table:define id="c_phone">
        <bean:write name="element" property="phone" />
      </table:define>
      <table:define id="c_isOutsideFootball">
      	<logic:present name="element" property="isOutsideFootball">
      	<logic:match name="element" property="isOutsideFootball" value="Y">是</logic:match>
      	<logic:match name="element" property="isOutsideFootball" value="N">否</logic:match>
      	</logic:present>
        <%-- <bean:write name="element" property="isOutsideFootball" /> --%>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>