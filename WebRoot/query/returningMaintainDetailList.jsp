<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<table>
		<tr>
	      <td>       
	        流水号
	        :
	      </td>
	      <td>
	        <html:text property="property(billNo)" styleClass="default_input" />
	      </td> 
	       <td> 
	       &nbsp;&nbsp;      
	        合同号
	        :
	      </td>
	      <td>
	        <html:text property="property(maintContractNo)" styleClass="default_input" />
	      </td>                   
	    
	      <td>
	      &nbsp;&nbsp;       
	        经办人
	        :
	      </td>
	      <td>
	        <html:text property="property(attn)" size="35" styleClass="default_input" />
	      </td>
	      </tr>
	    <tr>
	       <td>      
        销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>
	      <td> 
	        &nbsp;&nbsp;        
	        <bean:message key="maintContractQuote.maintDivision" />
	        :
	      </td>
	      <td>
	        <html:select property="property(maintDivision)">
			  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
	        </html:select>
	      </td>        
	      <td> 
	        &nbsp;&nbsp;        
	        合同类型
	        :
	      </td>
	      <td>
	        <html:select property="property(contracttype)">
			  <html:option value="">全部</html:option>
			  <html:option value="委托">委托</html:option>
			  <html:option value="维保">维保</html:option>
	        </html:select>
	      </td>        
	    </tr>	
		<html:hidden property="property(genReport)" styleId="genReport" />
	</table>
<br>
	<table:table id="guiReturningMaintainDetail" name="searchReturningMaintainDetailList">
		<logic:iterate id="element" name="searchReturningMaintainDetailList">
			<table:define id="c_cb">
			 
			 	<bean:define id="billNo" name="element" property="billno" />  
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=billNo.toString()%>" />
			    <html:hidden name="element" property="wtbillno" styleId="wtBillno"/>
			    <html:hidden name="element" property="wbbillno" styleId="wbBillno1"/>
			    <html:hidden name="element" property="billno" styleId="wbBillno"/>
			    <html:hidden name="element" property="contracttype" styleId="r4"/>
			    <html:hidden name="element" property="maintcontractno" styleId="maintContractNo"/>
			    <html:hidden name="element" property="contractsdate" styleId="contractSdate"/>
			    <html:hidden name="element" property="contractedate" styleId="contractEdate"/>
			</table:define>
			<table:define id="c_contracttype">
          		<bean:write name="element" property="contracttype" />
	      	</table:define>
			<table:define id="c_billNo">
          		<bean:write name="element" property="billno" />
	      	</table:define>
	       	<table:define id="c_maintContractNo">
	          	<bean:write name="element" property="maintcontractno" />
	      	</table:define>
	      	<table:define id="c_attn">
	        	<bean:write name="element" property="attn" />
	      	</table:define>
	      	<table:define id="c_contractSdate">
	        	<bean:write name="element" property="contractsdate" />
	      	</table:define>
	      	<table:define id="c_contractEdate">
	        	<bean:write name="element" property="contractedate" />
	      	</table:define>
	      	<table:define id="c_maintDivision">
	        	<bean:write name="element" property="maintdivision" />
	      	</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>