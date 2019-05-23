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
        报修单位名称:<html:text property="property(companyName)"   />
      </td>
    </tr>
  </table>
  <br>
  <table:table id="guihotphone2" name="MaintContractList2">
    <logic:iterate id="element" name="MaintContractList2">
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
      <table:define id="c_useUnit">
        <bean:write name="element" property="companyName" />
      </table:define>
      <table:define id="c_deliveryAddress">
        <bean:write name="element" property="address" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>