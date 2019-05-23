<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
	<html:form action="/ServeTable.do">
	<table border="0" cellpadding="5" cellspacing="0" nowrap="nowrap">
	  <tr>
		<td>用户代码：</td>
<td align="left">
		 <html:text styleClass="default_input" property="property(userid)"/></td>
<td>用户名称：</td>
<td align="left">
		 <html:text styleClass="default_input" property="property(username)"/></td>
<td>所属分部：</td>
<td align="left"><html:select
				property="property(grcid)">
				<html:options collection="grcidlist" property="grcid"
					labelProperty="grcname"></html:options>
			</html:select></td>
		</tr>
    <table:table id="guiMainShr" name="mainshrlist">
      <logic:iterate id="element" name="mainshrlist">
		<table:define id="c_cb">
        	<html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="grcid"/>
        	</html:multibox>
		</table:define>
		<table:define id="c_userid">
       <bean:write name="element" property="userid"/>
		</table:define>
		<table:define id="c_username">
        <bean:write name="element" property="username"/>
         <input type="hidden" name="grcid" id="grcid" value="${element.grcid }"/>
         		<input type="hidden" name="userid" id="userid" value="${element.userid }"/>
         		<input type="hidden" name="username" id="username" value="${element.username }"/>
         		<input type="hidden" name="grcname" id="grcname" value="${element.grcname }"/>
		</table:define>
		<table:define id="c_grcname">
        <bean:write name="element" property="grcname"/>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>