<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<br>
<html:form action="/ServeTable.do">
	<p align="center"><b><bean:message key="haduser.haduserlise"/></b></p>
	<table>
	  <tr>
		<td><bean:message key="haduser.hadusercount"/>:</td><td><bean:write name="hadusecount"/></td>
	  </tr>
	</table>
	<input type="hidden" name="property(genReport)" id="genReport"/>
    <table:table id="guiShopUseReport" name="shopList">
      <logic:iterate id="element" name="shopList">
		<table:define id="c_shopid">
        <bean:write name="element"  property="shopid"/>
		</table:define>
		<table:define id="c_shopname">
        <bean:write name="element" property="shopname"/>
		</table:define>
		<table:define id="c_areaid">
        <bean:write name="element" property="areaname"/>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
</html:form>