<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<html:base/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
<!--
body {
	background:#F7F7F7;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<title></title>
<script language=javascript>
function changeRight()
{
if(parent.Index.cols != "*,12,60%")
{
parent.Index.cols = "*,12,60%";
document.all.menuSwitch.innerHTML = "<img src='../images/common/icon_left.gif' align='absmiddle'>";
}
else
{
parent.Index.cols = "*,12,99%";
document.all.menuSwitch.innerHTML = "<img src='../images/common/icon_right.gif' align='absmiddle'>";
}
}
</script>

</head>
<body>
<table width="12" height="100%" border=0 align="center" cellpadding=0 cellspacing=0 style="cursor:pointer; border:1px #E1C788 solid; border-bottom:0px; border-top:0px; background:#FDF1D2;">
<tr><td height="100%" align="center" id=menuSwitch onclick=changeRight()><img src="../images/common/icon_left.gif" align="absmiddle"></td>
  </tr>
</table>
</body>
</html>