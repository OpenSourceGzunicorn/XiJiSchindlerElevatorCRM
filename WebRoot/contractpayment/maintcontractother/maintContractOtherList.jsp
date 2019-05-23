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
<html:hidden property="property(hiddatestr)" styleId="hiddatestr" />
<html:hidden property="property(genReport)" styleId="genReport" />
  <table>
    <tr>
      <td>&nbsp;&nbsp;<bean:message key="maintContract.billNo" />:</td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>                 
      <td>&nbsp;&nbsp;<bean:message key="maintContract.maintContractNo" />:</td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td> 
      <td>&nbsp;&nbsp;销售合同号:</td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>           
    </tr>
    <tr>
    	<td>&nbsp;&nbsp;维保分部:</td>
    	<td>
    		<html:select property="property(maintdivision)" styleId="maintdivision" onchange="Evenmore(this,'maintstation')">
		    	<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
    		</html:select>
    	</td>
    	<td>&nbsp;&nbsp;维保站:</td>
    	<td>
    		<html:select property="property(maintstation)" styleId="maintstation">
    			<%-- html:option value="">全部</html:option--%>
		    	<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
    		</html:select>
    	</td>
      <td>&nbsp;&nbsp;开票名称:</td>
      <td>
        <html:text property="property(invoiceName)" size="30" styleClass="default_input" />
      </td>  
    </tr>
  </table>
  <br>
  <table:table id="guiMaintContractOther" name="maintContractOtherList">
    <logic:iterate id="element" name="maintContractOtherList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.billno}" />
        <html:hidden name="element" property="nototherfee"/>
      </table:define>
      <table:define id="c_r9">
        <bean:write name="element" property="r9" />
      </table:define>
      <table:define id="c_billNo">
        <a href="<html:rewrite page='/maintContractOtherAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
          <bean:write name="element" property="billno" />
        </a>
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintdivision" />
      </table:define>
      <table:define id="c_maintStation">
        <bean:write name="element" property="maintstation" />
      </table:define>
      <table:define id="c_maintContractNo">
      	<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="element"  property="billno"/>" target="_blnk">
        	<bean:write name="element" property="maintcontractno" />
        </a>
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="invoicename" />
      </table:define>
       <table:define id="c_otherfee">
        <bean:write name="element" property="otherfee" />
      </table:define>
       <table:define id="c_nototherfee">
        <bean:write name="element" property="nototherfee" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>