<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/TreeShow.tld" prefix="TreeShow" %>

<html:html locale="true">
<html:base/>
<head>
<title><bean:message key="app.title"/>
</title>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='dTreeCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='publicCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='queryCSS'/>">
<SCRIPT LANGUAGE=javascript src="<html:rewrite forward='dTreeJS'/>"></SCRIPT>

<SCRIPT LANGUAGE=javascript>
<TreeShow:TreeShowTag />

ifExpandTree = 1;
function window_onload()
{
  ShowTree();
}

function ShowTree()
{
  ifExpandTree = ( ifExpandTree == 1 ) ? 0 : 1;
  if(ifExpandTree == 1)
	{
		d.openAll();
	}
	else
	{
		d.closeAll();
	}  	
}

function hidden(){
  window.parent.main.cols="15,0,*";
}
</script>
<body leftmargin="0" topmargin="0" class="tree_body">
   <div class="tree_header" align="center">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td width="80%">
     <div width="100%" onclick="javascript:ShowTree();" align="center">
		   <bean:message key="catalog.title"/>
     </div>
     </td>
     <td width="20%">
     <div title="<bean:message key='catalog.mini'/>" onclick="javascript:hidden();" align="right" width="100%">
       |¡û
     </div>
     </td>
    </tr>
    </table>
   </div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td></td></tr>
  <tr><td>
    <div id=divDebug></div>
    <div class="tree_div" id=divRender></div>
  </td>
  </tr>
</table>
</body>
</html:html>