<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="property(srt)" />
  <table>
    <tr>
      	<td><bean:message key="company.comid"/>:</td>
      	<td><html:text property="property(comid)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="company.comfullname"/>:</td>
		<td><html:text property="property(comfullname)" styleClass="default_input"/></td>
		<html:hidden property="property(genReport)" styleId="genReport" />
    </tr>
  </table>
  <br>
  
  
<logic:present name="srt">
  <table:table id="guiSearchCompany_kk" name="searchCompanyList">
      <logic:iterate id="element" name="searchCompanyList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="comid"/>
        	</html:multibox--%>
			<%-- <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=comid.toString()%>" /> --%>
        	<bean:define id="comid" name="element" property="comid" />
			<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=comid.toString()%>" />
			<html:hidden name="element" property="comfullname" styleId="comfullname"/>
			<html:hidden name="element" property="comid" styleId="comid"/>
			<html:hidden name="element" property="comfullname" styleId="comNameMsr"/>
			<html:hidden name="element" property="comid" styleId="maintDivisionBx"/>
		</table:define>
		<table:define id="c_comid">
       <bean:write name="element"  property="comid"/>
		</table:define>
		<table:define id="c_comfullname">
        <bean:write name="element" property="comfullname"/>
		</table:define>
		<table:define id="c_comtype">
        <bean:write name="element"  property="comtype"/>
		</table:define>
		<table:define id="c_linkman">
        <bean:write name="element" property="linkman"/>
		</table:define>
		<table:define id="c_linkmantel">
        <bean:write name="element" property="linkmantel"/>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
  </logic:present>
  
<logic:notPresent name="srt">  
  <table:table id="guiSearchCompany_kk2" name="searchCompanyList">
      <logic:iterate id="element" name="searchCompanyList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="comid"/>
        	</html:multibox--%>
			<%-- <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=comid.toString()%>" /> --%>
        	<bean:define id="comid" name="element" property="comid" />
        	<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=comid.toString()%>" />
			<html:hidden name="element" property="comfullname" styleId="comfullname"/>
			<html:hidden name="element" property="comid" styleId="comid"/>
			<html:hidden name="element" property="comfullname" styleId="comNameMsr"/>
			<html:hidden name="element" property="comid" styleId="maintDivisionBx"/>
		</table:define>
		<table:define id="c_comid">
       <bean:write name="element"  property="comid"/>
		</table:define>
		<table:define id="c_comfullname">
        <bean:write name="element" property="comfullname"/>
		</table:define>
		<table:define id="c_comtype">
        <bean:write name="element"  property="comtype"/>
		</table:define>
		<table:define id="c_linkman">
        <bean:write name="element" property="linkman"/>
		</table:define>
		<table:define id="c_linkmantel">
        <bean:write name="element" property="linkmantel"/>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
  </logic:notPresent>
  
  
</html:form>