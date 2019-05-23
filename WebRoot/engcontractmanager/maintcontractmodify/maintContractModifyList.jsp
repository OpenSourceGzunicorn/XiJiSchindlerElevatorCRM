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
<html:hidden property="property(hiddatestr)" styleId="hiddatestr"  />
<html:hidden property="property(genReport)" styleId="genReport" />
  <table>
    <tr>
      <td>      
        <bean:message key="maintContract.billNo" />
        :
      </td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>                 
      <td>
        &nbsp;&nbsp;               
        <bean:message key="maintContract.maintContractNo" />
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td> 
      <td>  
        &nbsp;&nbsp;     
        <bean:message key="maintContract.attn" />
        :
      </td>
      <td>
        <html:text property="property(attn)" styleClass="default_input" />
      </td>
      <td>  
        &nbsp;&nbsp;     
        销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td> 
    </tr>   
    <tr>
      <td> 
        电梯编号
        :
      </td>
      <td>
        <html:text property="property(elevatorNo)" styleClass="default_input" />
      </td>
      <td>           
        &nbsp;&nbsp;      
        <bean:message key="maintContract.maintDivision" />
        :
      </td>                         
      <td>
        <html:select property="property(maintDivision)">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td> 
      <td> 
        &nbsp;&nbsp;      
                    甲方单位
        :
      </td>
      <td colspan="3">
        <html:text property="property(companyName)" size="65" styleClass="default_input" />
      </td>
    </tr>   
  </table>
  <br>
  <table:table id="guiMaintContractModify" name="maintContractModifyList">
    <logic:iterate id="element" name="maintContractModifyList">
      <table:define id="c_cb">
      	<html:radio property="checkBoxList(ids)" styleId="ids" value="${element.billNo}" />
        <html:hidden name="element" property="maintContractNo" />  
        <html:hidden name="element" property="contractStatus"/>  
      </table:define>
      <table:define id="c_billNo">
         <bean:write name="element" property="billNo" />
      </table:define>
      <table:define id="c_maintContractNo">
        <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="companyId" />
      </table:define>
      <table:define id="c_contractSdate">
        <bean:write name="element" property="contractSdate" />
      </table:define>
      <table:define id="c_contractEdate">
        <bean:write name="element" property="contractEdate" />
      </table:define>
      <table:define id="c_attn">
        <bean:write name="element" property="attn" />
      </table:define>
      <table:define id="c_contractStatus">
        <bean:write name="element" property="contractStatusName" />
      </table:define>
      <table:define id="c_auditStatus">
        <logic:match name="element" property="auditStatus" value="Y">已审核</logic:match>
        <logic:match name="element" property="auditStatus" value="N">未审核</logic:match>
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_maintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>