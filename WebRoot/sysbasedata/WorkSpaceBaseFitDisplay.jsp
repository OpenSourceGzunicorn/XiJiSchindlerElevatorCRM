<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="basedatabean">
<table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
  <tr>
    <td width="15%" class="wordtd"><bean:message key="Track.jnlno"/>£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="wsid"/></td>
  </tr>
  <tr>  
    <td width="15%" class="wordtd"><bean:message key="workspacebasefit.wskey" />£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="wskey"/></td>
  </tr>
  <tr>  
    <td width="15%" class="wordtd"><bean:message key="randommaintaskmaster.title"/>£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="title"/></td>
  </tr>
  <tr>  
    <td width="15%" class="wordtd"><bean:message key="workspacebasefit.link"/>£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="link"/></td>
  </tr>
  <tr>  
    <td width="15%" class="wordtd"><bean:message key="workspacebasefit.tip" />£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="tip"/></td>
  </tr>
  <tr>  
    <td width="15%" class="wordtd"><bean:message key="DivId" />£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="divid"/></td>
  </tr>
  <tr>  
    <td width="15%" class="wordtd"><bean:message key="JSfuName" />£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="jsfuname"/></td>
  </tr>
  <tr>
    <td width="15%" class="wordtd">≈≈–Ú∫≈£∫</td>
    <td width="35%"><bean:write name="basedatabean" property="numno"/></td>
  </tr>
  <tr>     
    <td width="15%" class="wordtd"><bean:message key="workspacebasefit.enabledflag" />£∫</td>
    <td width="35%" class="inputtd"><logic:equal name="basedatabean" property="enabledflag" value="Y">
      <bean:message key="enabledflag.yes"/>
    </logic:equal>
        <logic:equal name="basedatabean" property="enabledflag" value="N">
          <bean:message key="enabledflag.no"/>
        </logic:equal></td>
  </tr>
  </table>
</logic:present>