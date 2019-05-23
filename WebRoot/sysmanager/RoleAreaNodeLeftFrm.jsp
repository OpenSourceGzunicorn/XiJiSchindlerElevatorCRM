<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/RoleShow.tld" prefix="RoleShow" %>

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

<RoleShow:RoleAreaShowTag />

var ifExpandTree = 1;
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

//window.attachEvent("onload",window_onload);

</SCRIPT>
<style>
.tree_body{border-right:black 1px solid;}
.tree_header{cursor:hand;width:100%;height:18;padding-top:2px;background :#b8c4f4;BORDER: #646c94 1px solid;}
.node{background-color:white;}
.tree_div{padding-left:8px;}
</style>
</head>
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
<script>
document.write(d);
</script>
</body>
</html:html>